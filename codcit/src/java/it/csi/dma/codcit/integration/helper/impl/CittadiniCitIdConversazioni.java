/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.helper.impl;

import java.sql.Date;
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
import it.csi.dma.codcit.dto.ModelConversazioneNuova;
import it.csi.dma.codcit.dto.PayloadConversazione;
import it.csi.dma.codcit.dto.custom.Conversazione;
import it.csi.dma.codcit.dto.custom.DmaccDCatalogoLogAudit;
import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.exception.DelegheFallimentoException;
import it.csi.dma.codcit.exception.ResponseErrorException;
import it.csi.dma.codcit.integration.auditlog.ApiAuditEnum;
import it.csi.dma.codcit.integration.auditlog.AuditLogService;
import it.csi.dma.codcit.integration.dao.custom.CodDDisabilitazioneMotivoDao;
import it.csi.dma.codcit.integration.dao.custom.CodTSoggettoAbilitatoDao;
import it.csi.dma.codcit.integration.dao.custom.CodTSoggettoDisabilitatoDao;
import it.csi.dma.codcit.integration.dao.custom.DmaccTCatalogoLogAuditDao;
import it.csi.dma.codcit.integration.dao.impl.CodTConversazioneDao;
import it.csi.dma.codcit.integration.service.CodDErroreService;
import it.csi.dma.codcit.integration.service.CodRMessaggioErroreService;
import it.csi.dma.codcit.integration.service.DelegheService;
import it.csi.dma.codcit.integration.service.StatoConsensiService;
import it.csi.dma.codcit.util.Constants;
import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.enumerator.CodeErrorEnum;
import it.csi.dma.codcit.util.enumerator.StatusEnum;
import it.csi.dma.codcit.util.validator.ValidateMedicoArgomentoAutoreGeneric;
import it.csi.statoconsensi.dma.StatoConsensiOUT;

@Service
public class CittadiniCitIdConversazioni extends ExecuteUtil {

	@Autowired
	@Qualifier("validateMedicoArgomentoAutoreGeneric")
	ValidateMedicoArgomentoAutoreGeneric validateGeneric;

	@Autowired
	CodTSoggettoAbilitatoDao codTSoggettoAbilitatoDao;

	@Autowired
	CodTSoggettoDisabilitatoDao codTSoggettoDisabilitatoDao;

	@Autowired
	DelegheService delegheService;

	@Autowired
	CodDErroreService codDErroreService;

	@Autowired
	CodDDisabilitazioneMotivoDao codDDisabilitazioneMotivoDao;

	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	@Autowired
	CodTConversazioneDao codTConversazioneDao;

	@Autowired
	StatoConsensiService statoConsensiSerivce;
	
	@Autowired
	AuditLogService auditLogService;
	
	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;
	
	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String citId, PayloadConversazione payloadConversazione,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		var methodName = "execute";
		ErrorBuilder error = null;

		try {
			// 2.a 2.b validate
			List<ErroreDettaglioExt> listError = validateGeneric.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, citId, payloadConversazione.getMedico(), payloadConversazione.getAutore(), payloadConversazione.getArgomento());
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),"errore in validate");
			}

			// deleghe verifica codici fiscali
			if (!delegheService.checkUtenteAutorizzatoOrDelegato(xRequestId, xCodiceServizio, shibIdentitaCodiceFiscale,
					citId)) {
				logError(methodName, "delegato non autorizzato");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.CC_ER_185.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.FORBIDDEN, listError),"delegato non autorizzato");
			}

			Integer idCittadino = codTSoggettoAbilitatoDao.selectIdSoggettoCittadino(citId);
			Integer idMedico = codTSoggettoAbilitatoDao.selectIdSoggettoMedico(payloadConversazione.getMedico().getCodiceFiscale());
			//2c.1
			if( idCittadino == null) {
				logError(methodName, "Cittadino non trovato");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_073.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listError),"Cittadino non trovato");
			}

			//2c.2
			if(idMedico == null) {
				logError(methodName, "Medico non trovato");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_074.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listError),"Medico non trovato");
			}

			//2c.2(bis)
			if(codTSoggettoAbilitatoDao.selectSoggettoAbilitatoWhereCittadinoAbilitatoDaMedico(payloadConversazione.getMedico().getCodiceFiscale(), citId) == 0) {
				logError(methodName, "Cittadino non abilitato");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_075.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.FORBIDDEN, listError),"Cittadino non abilitato al servizio");
			}

			//TODO eseguire la chiamata a statoConsensi
			StatoConsensiOUT consensiOut = statoConsensiSerivce.getStatoConsensi(citId, shibIdentitaCodiceFiscale, xCodiceServizio, xRequestId);

			//verifiche alla risposta a statoconsensi
			if(isError078(consensiOut)) {
				logError(methodName, "Cittadino non assistito FSE_COD_078");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_078.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.FORBIDDEN, listError),"Cittadino non assistito della regione");
			}

			if(isError077(consensiOut)) {
				logError(methodName, "Cittadino non assistito FSE_COD_077");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_077.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.FORBIDDEN, listError),"Cittadino non assistito della regione");
			}

			Conversazione conversazione = new Conversazione();
			conversazione.setConversazioneOggetto(payloadConversazione.getArgomento());
			conversazione.setSoggettoIdAutore(idCittadino);
			conversazione.setSoggettoIdPartecipante(idMedico);
			conversazione.setUtente_creazione(shibIdentitaCodiceFiscale);
			conversazione.setUtente_modifica(shibIdentitaCodiceFiscale);

			Long idConversazione = codTConversazioneDao.insertTConversazione(conversazione);

			Conversazione conversazioneRet = codTConversazioneDao.selectConversazioneFromId(Long.valueOf(idConversazione));

			ModelConversazioneNuova result = new ModelConversazioneNuova();
			result.setArgomento(conversazioneRet.getConversazioneOggetto());
			result.setId(conversazioneRet.getConversazioneCod());
			result.setAutore(newModelAutore(citId, shibIdentitaCodiceFiscale));
			result.setDataCreazione(new Date(conversazioneRet.getDataCreazione().getTime()));
			result.setMedico(payloadConversazione.getMedico());
			
			//AUDIT LOG
			String codiceCatalogo = "COD_CIT_NCONV";
			if(!shibIdentitaCodiceFiscale.equals(citId)) {
				codiceCatalogo ="COD_CIT_NCONV_DEL";
			}
			String descrizioneCatalogoLogAudit = null;
			Long idCatalogoLogAudit = null;
			DmaccDCatalogoLogAudit catalogoLogAudit = dmaccTCatalogoLogAuditDao
					.selectCatalogoDescrizioneByCodice(codiceCatalogo);
			if (catalogoLogAudit != null) {
				idCatalogoLogAudit = catalogoLogAudit.getId();
				descrizioneCatalogoLogAudit = catalogoLogAudit.getDescrizione().replace("{0}",
						citId);
				descrizioneCatalogoLogAudit = descrizioneCatalogoLogAudit.replace("{2}",
						payloadConversazione.getMedico().getCodiceFiscale());
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
					ApiAuditEnum.POST_CITTADINI_CONVERSAZIONI,descrizioneCatalogoLogAudit,idCatalogoLogAudit);
			
			return Response.ok(result).build();

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

	private boolean isError078(StatoConsensiOUT consensiOut) {

		return (StringUtils.isBlank(consensiOut.getIdentificativoInformativaCorrente()) || (!consensiOut.getIdentificativoInformativaCorrente().startsWith("010")) );

	}

	private boolean isError077(StatoConsensiOUT consensiOut) {
		return ("false".equalsIgnoreCase(consensiOut.getConsensoAlimentazione()) || "false".equalsIgnoreCase(consensiOut.getConsensoConsultazione()));
	}



	private ModelAutore newModelAutore(String citId, String shibIdentitaCf) {
		ModelAutore autore = new ModelAutore();
		autore.setCodiceFiscale(citId);
		if( citId.equals(shibIdentitaCf)) {
			autore.setTipo(TipoEnum.ASSISTITO);
		}else {
			autore.setTipo(TipoEnum.DELEGATO);
		}
		return autore;
	}
}
