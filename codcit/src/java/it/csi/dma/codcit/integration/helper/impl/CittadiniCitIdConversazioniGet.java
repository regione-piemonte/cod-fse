/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.helper.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.csi.dma.codcit.dto.ModelAutore;
import it.csi.dma.codcit.dto.ModelAutore.TipoEnum;
import it.csi.dma.codcit.dto.ModelConversazione;
import it.csi.dma.codcit.dto.ModelMedico;
import it.csi.dma.codcit.dto.ModelMotivoBlocco;
import it.csi.dma.codcit.dto.ModelUltimoMessaggio;
import it.csi.dma.codcit.dto.custom.ConversazioneCustom;
import it.csi.dma.codcit.dto.custom.DmaccDCatalogoLogAudit;
import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.exception.DelegheFallimentoException;
import it.csi.dma.codcit.exception.ResponseErrorException;
import it.csi.dma.codcit.integration.auditlog.ApiAuditEnum;
import it.csi.dma.codcit.integration.auditlog.AuditLogService;
import it.csi.dma.codcit.integration.dao.custom.CodTConversazioneCustomDao;
import it.csi.dma.codcit.integration.dao.custom.CodTSoggettoDisabilitatoDao;
import it.csi.dma.codcit.integration.dao.custom.DmaccTCatalogoLogAuditDao;
import it.csi.dma.codcit.integration.service.CodCParametroService;
import it.csi.dma.codcit.integration.service.CodCParametroService.ConfigParam;
import it.csi.dma.codcit.integration.service.CodDErroreService;
import it.csi.dma.codcit.integration.service.CodRMessaggioErroreService;
import it.csi.dma.codcit.integration.service.DelegheService;
import it.csi.dma.codcit.integration.service.StatoConsensiService;
import it.csi.dma.codcit.util.Constants;
import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.enumerator.CodeErrorEnum;
import it.csi.dma.codcit.util.enumerator.HeaderEnum;
import it.csi.dma.codcit.util.enumerator.StatusEnum;
import it.csi.dma.codcit.util.validator.ValidateGenericMeritWhitCfMedicoPaginazione;
import it.csi.dma.codcit.util.validator.impl.ValidateLimitOffsetImpl;

@Service
public class CittadiniCitIdConversazioniGet extends ExecuteUtil {

	@Autowired
	@Qualifier("ValidateGenericMeritWhitCfMedicoPaginazione")
	ValidateGenericMeritWhitCfMedicoPaginazione validateGeneric;

	@Autowired
	DelegheService delegheService;

	@Autowired
	CodDErroreService codDErroreService;

	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	@Autowired
	StatoConsensiService statoConsensiSerivce;

	@Autowired
	CodTConversazioneCustomDao codTConversazioneCustomDao;

	@Autowired
	CodTSoggettoDisabilitatoDao codTSoggettoDisabilitatoDao;

	@Autowired
	CodCParametroService codCParametroService;

	@Autowired
	@Qualifier("validateLimitOffset")
	ValidateLimitOffsetImpl validateLimitOffset;

	@Autowired
	AuditLogService auditLogService;

	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;

	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String citId, String solaLettura, Integer limit, Integer offset, String cfMedico,
			String argomento, String idConversazione, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		var methodName = "execute";
		ErrorBuilder error = null;

		try {
			// 2.a 2.b validate
			List<ErroreDettaglioExt> listError = validateGeneric.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, citId, cfMedico, solaLettura, limit, offset);
			listError=validateLimitOffset.validate(listError, limit, offset);
			// jira dma-3852 campo argomento almeno di 2 caratteri
			if(StringUtils.isNoneEmpty(argomento)&&argomento.length()<2) {
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_051.getCode(), "argomento", argomento));
			}
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}


			// deleghe verifica codici fiscali
			if (!delegheService.checkUtenteAutorizzatoOrDelegato(xRequestId, xCodiceServizio, shibIdentitaCodiceFiscale,
					citId)) {
				logError(methodName, "delegato non autorizzato");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.CC_ER_185.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.FORBIDDEN, listError),
						"delegato non autorizzato");
			}

			List<ModelConversazione> result = new ArrayList<>();

			List<ConversazioneCustom> conversazioniList = codTConversazioneCustomDao
					.selectConversazioniExtByCittadinoAndMedicoAndArgomentoAndIdConversazioneAndSolaLettura(cfMedico,
							citId, argomento, idConversazione, solaLettura, offset, limit);
			Integer totalElements = codTConversazioneCustomDao.
					countConversazioniExtByCittadinoAndMedicoAndArgomentoAndIdConversazioneAndSolaLettura(cfMedico,
							citId, argomento, idConversazione, solaLettura);
			// jira DMA-3864: se viene effettuata una ricerca puntuale per id conversazione e totalElements Ã¨ 0 restituisce codice errore FSE-CODD-067
			if(StringUtils.isNoneEmpty(idConversazione)&&totalElements==0) {
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_067.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listError),"Messaggio non trovato");
			}
			// TODO: GESTIONE AUDIT DA FARE
			for (ConversazioneCustom conversazione : conversazioniList) {
				var modelConversazione = new ModelConversazione();
				modelConversazione.setId(conversazione.getConversazioneCod());
				var modelMedico = new ModelMedico();
				modelMedico.setNome(conversazione.getMedicoSoggettoNome());
				modelMedico.setCognome(conversazione.getMedicoSoggettoCognome());
				modelMedico.setCodiceFiscale(conversazione.getMedicoSoggettoCf());
				modelConversazione.setMedico(modelMedico);
				modelConversazione.setArgomento(getStringFromByteArray(conversazione.getConversazioneOggetto()));
				modelConversazione.setNMessaggiNonLetti(conversazione.getMessaggiNonLetti());
				modelConversazione.setSolaLettura(conversazione.getConversazioneDataBlocco() != null ? true : null);
				if (conversazione.getMessaggioId() != null) {
					var modelUltimoMessaggio = new ModelUltimoMessaggio();
					modelUltimoMessaggio.setId(Long.toString(conversazione.getMessaggioId()));
					modelUltimoMessaggio.setDataCreazione(conversazione.getMessaggioDataCreazione());
					modelUltimoMessaggio.setLetto(conversazione.getMessaggioLetturaData() != null);
					modelUltimoMessaggio.setTesto(getStringFromByteArray(conversazione.getMessaggioTesto()));
					modelUltimoMessaggio
							.setModificato(
									(conversazione.getMessaggioDataModifica() != null
											&& conversazione.getMessaggioDataCreazione() != null
											&& conversazione.getMessaggioDataCreazione()
													.compareTo(conversazione.getMessaggioDataModifica()) != 0) ? true
															: null);
					if (Boolean.TRUE.equals(modelUltimoMessaggio.isModificato())) {
						modelUltimoMessaggio.setAutoreModifica(conversazione.getMessaggioUtenteModifica());
						modelUltimoMessaggio.setDataModifica(conversazione.getMessaggioDataModifica());
					}
					modelConversazione.setUltimoMessaggio(modelUltimoMessaggio);
				}
				var modelAutore = new ModelAutore();
				modelAutore.setCodiceFiscale(conversazione.getUtenteCreazione());
				if (conversazione.getMessaggioUtenteModifica() != null
						&& conversazione.getSoggettoCf().equals(conversazione.getMessaggioUtenteModifica())) {
					modelAutore.setTipo(TipoEnum.ASSISTITO);
				} else if (conversazione.getMessaggioUtenteModifica() != null
						&& !conversazione.getSoggettoCf().equals(conversazione.getMessaggioUtenteModifica())) {
					modelAutore.setTipo(TipoEnum.DELEGATO);
				} else if (conversazione.getSoggettoIdDa().equals(conversazione.getMedicoSoggettoId())) {
					modelAutore.setTipo(TipoEnum.MEDICO);
				}
				modelConversazione.setAutore(modelAutore);
				modelConversazione.setDataCreazione(conversazione.getDataCreazione());

				//gestisco la scadenza della conversazione
				Integer giorniEliminazione = codCParametroService.get(ConfigParam.NUM_GIORNI_ELIMINAZIONE_CONVERSAZIONE);
				modelConversazione.setDataEliminazione(addDays((conversazione.getMessaggioDataCreazione()!=null?conversazione.getMessaggioDataCreazione():conversazione.getDataCreazione()),giorniEliminazione));

				if (conversazione.getDisabilitazioneMotivoId() != null) {
					var modelMotivoBlocco = new ModelMotivoBlocco();
					modelMotivoBlocco.setCodice(conversazione.getDisabilitazioneMotivoCod());
					modelMotivoBlocco.setDescrizione(conversazione.getDisabilitazioneMotivoDesc());
					modelConversazione.setMotivoBlocco(modelMotivoBlocco);
					modelConversazione.setMotivazioneMedico(codTSoggettoDisabilitatoDao.selectMotivazioneBlocco(citId,conversazione.getMedicoSoggettoId()));
				}
				//Escludo dalla lista le conversazioni con messaggi scaduti
				if(modelConversazione.getDataEliminazione().after(new java.util.Date())) {
					result.add(modelConversazione);
				}
			}
			//AUDIT LOG
			String codiceCatalogo = "COD_CIT_CONV";
			if(!shibIdentitaCodiceFiscale.equals(citId)) {
				codiceCatalogo ="COD_CIT_CONV_DEL";
			}
			String descrizioneCatalogoLogAudit = null;
			Long idCatalogoLogAudit = null;
			DmaccDCatalogoLogAudit catalogoLogAudit = dmaccTCatalogoLogAuditDao
					.selectCatalogoDescrizioneByCodice(codiceCatalogo);
			if (catalogoLogAudit != null) {
				idCatalogoLogAudit = catalogoLogAudit.getId();
				descrizioneCatalogoLogAudit = catalogoLogAudit.getDescrizione().replace("{0}",
						citId);
				if(!shibIdentitaCodiceFiscale.equals(citId)) {
					descrizioneCatalogoLogAudit = descrizioneCatalogoLogAudit.replace("{1}",
							shibIdentitaCodiceFiscale);
				}

			}
			auditLogService.insertAuditLogCittadino(xRequestId,
					xForwardedFor,
					shibIdentitaCodiceFiscale,
					citId,
					xCodiceServizio,
					Constants.CODICE_VERTICALE,
					citId,
					ApiAuditEnum.GET_CITTADINI_CONVERSAZIONI,descrizioneCatalogoLogAudit,idCatalogoLogAudit);
			return Response.ok(result).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId)
					.header(HeaderEnum.X_TOTAL_ELEMENTS.getCode(), totalElements).build();

		} catch (DatabaseException e) {
			logError(methodName, "Errore riguardante database:", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		} catch (DelegheFallimentoException e) {
			logError(methodName, "Errore riguardante deleghe:", e.getMessage());
			error = e.getResponseError();
		} catch (ResponseErrorException e) {
			logError(methodName, "Errore generico response:", e.getMessage());
			error = e.getResponseError();
		} catch (Exception e) {
			logError(methodName, "Errore generico:", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		}

		error = codRMessaggioErroreService.saveError(error, httpRequest);
		return error.generateResponseError();
	}

	protected static java.sql.Timestamp addDays(java.sql.Timestamp date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return new java.sql.Timestamp(cal.getTime().getTime());

    }
}
