/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.helper.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.csi.dma.codcit.dto.ModelDocumento;
import it.csi.dma.codcit.dto.ModelMessaggio;
import it.csi.dma.codcit.dto.PayloadMessaggio;
import it.csi.dma.codcit.dto.custom.CodTMessaggio;
import it.csi.dma.codcit.dto.custom.DmaccDCatalogoLogAudit;
import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.exception.DelegheFallimentoException;
import it.csi.dma.codcit.exception.ResponseErrorException;
import it.csi.dma.codcit.integration.auditlog.ApiAuditEnum;
import it.csi.dma.codcit.integration.auditlog.AuditLogService;
import it.csi.dma.codcit.integration.dao.custom.CodTMessaggioAllegatoCustomDao;
import it.csi.dma.codcit.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.codcit.integration.dao.custom.DmaccTCatalogoLogAuditDao;
import it.csi.dma.codcit.integration.notificatore.util.ConversazioneMessaggiPutNotificaAsync;
import it.csi.dma.codcit.integration.service.CodDErroreService;
import it.csi.dma.codcit.integration.service.CodRMessaggioErroreService;
import it.csi.dma.codcit.integration.service.DelegheService;
import it.csi.dma.codcit.util.Constants;
import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.enumerator.CodeErrorEnum;
import it.csi.dma.codcit.util.enumerator.StatusEnum;
import it.csi.dma.codcit.util.validator.ValidatePatchMessage;

@Service
public class CittadiniCitIdConversazioniIdConversazioneMessaggiIdMessaggioPut extends ExecuteUtil {

	@Autowired
	@Qualifier("ValidatePatchMessage")
	ValidatePatchMessage validatePatchMessage;

	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	@Autowired
	DelegheService delegheService;

	@Autowired
	CodDErroreService codDErroreService;

	@Autowired
	CodTMessaggioAllegatoCustomDao codTMessaggioAllegatoCustomDao;

	@Autowired
	CodTSoggettoDao codTSoggettoDao;
	@Autowired
	ConversazioneMessaggiPutNotificaAsync conversazioneMessaggiPutNotificaAsync;
	@Autowired
	AuditLogService auditLogService;

	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;


	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String citId, String idConversazione, String idMessaggio,
			PayloadMessaggio payloadMessaggio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

		var methodName = "execute";
		ErrorBuilder error = null;
		try {
			// 2.a 2.b validate
			List<ErroreDettaglioExt> listError = validatePatchMessage.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, citId, idConversazione, idMessaggio);
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			// 4 Call deleghe verifica codici fiscali
			if (!delegheService.checkUtenteAutorizzatoOrDelegato(xRequestId, xCodiceServizio, shibIdentitaCodiceFiscale,
					citId)) {
				logError(methodName, "delegato non autorizzato");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.CC_ER_185.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.FORBIDDEN, listError),
						"delegato non autorizzato");
			}

			CodTMessaggio messaggio = codTMessaggioAllegatoCustomDao
					.selectMessaggioFromIdAndCodConversazioneAndSoggetto(citId, idConversazione,
							Long.parseLong(idMessaggio));
			if (messaggio == null) {
				logError(methodName, "Messaggio non trovato");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_067.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listError),
						"Messaggio non trovato");
			}
			// verifico che non esistano messaggi succesivi al messaggio che si intende
			// modificare
			if (codTMessaggioAllegatoCustomDao.countMessaggiSuccessivi(messaggio) > 0) {
				logError(methodName, "Messaggio non modificabile");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_091.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.FORBIDDEN, listError),
						"Messaggio non modificabile");
			}

			codTMessaggioAllegatoCustomDao.insertSMessaggio(messaggio);

			messaggio.setMessaggioTestoCifrato(payloadMessaggio.getTesto());
			messaggio.setUtenteModifica(shibIdentitaCodiceFiscale);
			codTMessaggioAllegatoCustomDao.updatePutTMessaggio(messaggio, shibIdentitaCodiceFiscale);
			// gestione allegati
			// cancello vecchi allegati
			codTMessaggioAllegatoCustomDao.deleteTAllegato(messaggio.getMessaggioId());
			// reinserisco allegati
			if (payloadMessaggio.getAllegati() != null && payloadMessaggio.getAllegati().size() > 0) {
				for (ModelDocumento documento : payloadMessaggio.getAllegati()) {
					codTMessaggioAllegatoCustomDao.insertTAllegato(documento, messaggio.getMessaggioId(),
							shibIdentitaCodiceFiscale);
				}
			}

			ModelMessaggio messaggioRet = codTMessaggioAllegatoCustomDao
					.selectMessaggioFromIdSelectSingola(messaggio.getMessaggioId());
//			come da analisi lo forzo a true DMA-3869
			if (messaggioRet != null) {
				messaggioRet.setModificabile(true);
				messaggioRet.setModificato(true);
			}
			//NOTIFICATORE
			conversazioneMessaggiPutNotificaAsync.notifyAsync(citId, messaggio.getSoggettoIdA(), xRequestId, xCodiceServizio,
					xForwardedFor,shibIdentitaCodiceFiscale);
			//AUDIT LOG
			String codiceCatalogo = "COD_CIT_MMSG";
			if(!shibIdentitaCodiceFiscale.equals(citId)) {
				codiceCatalogo ="COD_CIT_MMSG_DEL";
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
					ApiAuditEnum.PUT_CITTADINI_CONVERSAZIONI_MESSAGGI,descrizioneCatalogoLogAudit,idCatalogoLogAudit);
			
			
			return Response.ok().entity(messaggioRet).build();
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
