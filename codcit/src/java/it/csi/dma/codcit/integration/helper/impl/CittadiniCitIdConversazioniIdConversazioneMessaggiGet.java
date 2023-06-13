/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.helper.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.csi.dma.codcit.dto.ModelMessaggio;
import it.csi.dma.codcit.dto.custom.ConversazioneSoggettoCustom;
import it.csi.dma.codcit.dto.custom.DmaccDCatalogoLogAudit;
import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.dto.custom.Soggetto;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.exception.DelegheFallimentoException;
import it.csi.dma.codcit.exception.ResponseErrorException;
import it.csi.dma.codcit.integration.auditlog.ApiAuditEnum;
import it.csi.dma.codcit.integration.auditlog.AuditLogService;
import it.csi.dma.codcit.integration.dao.custom.CodTConversazioneCustomDao;
import it.csi.dma.codcit.integration.dao.custom.CodTMessaggioAllegatoCustomDao;
import it.csi.dma.codcit.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.codcit.integration.dao.custom.DmaccTCatalogoLogAuditDao;
import it.csi.dma.codcit.integration.service.CodDErroreService;
import it.csi.dma.codcit.integration.service.CodRMessaggioErroreService;
import it.csi.dma.codcit.integration.service.DelegheService;
import it.csi.dma.codcit.util.Constants;
import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.enumerator.CodeErrorEnum;
import it.csi.dma.codcit.util.enumerator.HeaderEnum;
import it.csi.dma.codcit.util.enumerator.StatusEnum;
import it.csi.dma.codcit.util.validator.ValidateGenericMessagges;
import it.csi.dma.codcit.util.validator.impl.ValidateLimitOffsetImpl;

@Service
public class CittadiniCitIdConversazioniIdConversazioneMessaggiGet extends ExecuteUtil {

	@Autowired
	@Qualifier("validateGenericMessagges")
	ValidateGenericMessagges validateGeneric;

	@Autowired
	DelegheService delegheService;

	@Autowired
	CodDErroreService codDErroreService;

	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	@Autowired
	CodTConversazioneCustomDao codTConversazioneCustomDao;

	@Autowired
	CodTMessaggioAllegatoCustomDao codTMessaggioAllegatoCustomDao;

	@Autowired
	@Qualifier("validateLimitOffset")
	ValidateLimitOffsetImpl validateLimitOffset;
	
	@Autowired
	AuditLogService auditLogService;
	
	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;
	@Autowired
	CodTSoggettoDao codTSoggettoDao;

	public Response execute(String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String citId, String idConversazione,
			Integer limit, Integer offset, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		var methodName = "execute";
		ErrorBuilder error = null;


		try {
			// 2.a 2.b validate
			//TODO creare una validate per il controllo della paginazione
			List<ErroreDettaglioExt> listError = validateGeneric.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, citId, idConversazione);
			listError=validateLimitOffset.validate(listError, limit, offset);
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			//2.c Altri controlli
			List<ConversazioneSoggettoCustom> listConversazioneSoggettoCustom=codTConversazioneCustomDao.selectConversazioniByconversazioneCod(idConversazione);
			if(listConversazioneSoggettoCustom==null || listConversazioneSoggettoCustom.size()==0) {
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_081.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listError),"Nessuna conversazione trovata");
			} else {
				//ricerco se almeno un soggettoCf Ã¨ uguale a citId altrimenti errore FSE_COD-082
				if(listConversazioneSoggettoCustom.stream().filter(c->c.getSoggettoCf().equalsIgnoreCase(citId)).count()<1) {
					listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_082.getCode(),citId));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listError),"Nessuna conversazione trovata per il cf:"+citId);
				}
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

			//List<ModelMessaggio> result = new ArrayList<>();
			//5. ricerca messaggi
			Soggetto soggettoMedico = codTSoggettoDao.selectSoggettoById(  listConversazioneSoggettoCustom.get(0).getSoggettoIdPartecipante());
			List<ModelMessaggio> result=codTMessaggioAllegatoCustomDao.selectListaMessaggiFromIdConversazioneOffsetLimit(listConversazioneSoggettoCustom.get(0).getConversazioneId(),soggettoMedico,offset, limit);
			Integer totalElements = codTMessaggioAllegatoCustomDao.countListaMessaggiFromIdConversazione(listConversazioneSoggettoCustom.get(0).getConversazioneId());
			
			//AUDIT LOG
			String codiceCatalogo = "COD_CIT_ELEMSG";
			if(!shibIdentitaCodiceFiscale.equals(citId)) {
				codiceCatalogo ="COD_CIT_ELEMSD_DEL";
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
						idConversazione);
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
					ApiAuditEnum.GET_CITTADINI_CONVERSAZIONI_MESSAGGI,descrizioneCatalogoLogAudit,idCatalogoLogAudit);
			
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
}
