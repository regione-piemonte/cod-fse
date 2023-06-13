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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.codcit.dto.ModelDocumento;
import it.csi.dma.codcit.dto.ModelMessaggio;
import it.csi.dma.codcit.dto.PayloadMessaggio;
import it.csi.dma.codcit.dto.custom.CodTMessaggio;
import it.csi.dma.codcit.dto.custom.Conversazione;
import it.csi.dma.codcit.dto.custom.DmaccDCatalogoLogAudit;
import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.exception.DelegheFallimentoException;
import it.csi.dma.codcit.exception.ResponseErrorException;
import it.csi.dma.codcit.integration.auditlog.ApiAuditEnum;
import it.csi.dma.codcit.integration.auditlog.AuditLogService;
import it.csi.dma.codcit.integration.dao.custom.CodTMessaggioAllegatoCustomDao;
import it.csi.dma.codcit.integration.dao.custom.CodTSoggettoAbilitatoDao;
import it.csi.dma.codcit.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.codcit.integration.dao.custom.DmaccTCatalogoLogAuditDao;
import it.csi.dma.codcit.integration.dao.impl.CodTConversazioneDao;
import it.csi.dma.codcit.integration.notificatore.util.ConversazioneMessaggiPostNotificaAsync;
import it.csi.dma.codcit.integration.service.CodDErroreService;
import it.csi.dma.codcit.integration.service.CodRMessaggioErroreService;
import it.csi.dma.codcit.integration.service.DelegheService;
import it.csi.dma.codcit.util.Constants;
import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.enumerator.CodeErrorEnum;
import it.csi.dma.codcit.util.enumerator.StatusEnum;
import it.csi.dma.codcit.util.validator.ValidateNewMessage;

@Service
public class CittadiniCitIdConversazioniIdConversazioneMessaggiPost extends ExecuteUtil {
	@Autowired
	@Qualifier("validateNewMessage")
	ValidateNewMessage validateNewMessage;

	@Autowired
	DelegheService delegheService;

	@Autowired
	CodDErroreService codDErroreService;

	@Autowired
	CodTSoggettoAbilitatoDao codTSoggettoAbilitatoDao;

	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	@Autowired
	CodTConversazioneDao codTConversazioneDao;

	@Autowired
	CodTMessaggioAllegatoCustomDao codTMessaggioAllegatoCustomDao;

	@Autowired
	CodTSoggettoDao codTSoggettoDao;
	
	@Autowired
	ConversazioneMessaggiPostNotificaAsync conversazioneMessaggiPostNotificaAsync;
	
	@Autowired
	AuditLogService auditLogService;

	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String citId, String idConversazione, PayloadMessaggio payloadMessaggio,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		var methodName = "execute";
		ErrorBuilder error = null;

		try {
			// 2.a 2.b validate
			List<ErroreDettaglioExt> listError = validateNewMessage.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, citId, idConversazione, payloadMessaggio);
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

			Integer idCittadino = codTSoggettoAbilitatoDao.selectIdSoggettoCittadino(citId);

			// 2c.1
			if (idCittadino == null) {
				logError(methodName, "Cittadino non trovato");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_073.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listError),
						"Cittadino non trovato");
			}

			// 2c.2 uso il dao cod_t_conversazione e al posto di where verifico da codice
			Conversazione conversazione = codTConversazioneDao.selectConversazioneFromUuid(idConversazione);
			if (conversazione == null || conversazione.getSoggettoIdAutore().intValue() != idCittadino.intValue()) {
				logError(methodName, "Conversazione non trovata");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_082.getCode(),citId));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listError),
						"Conversazione non trovata");
			}

			if (conversazione.getConversazioneDataBlocco() != null) {
				logError(methodName, "Conversazione non trovata");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_085.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listError),
						"Conversazione non trovata");
			}

			// 2c2
			if (codTSoggettoAbilitatoDao.selectSoggettoAbilitatoWhereCittadinoAbilitatoDaMedicoFromId(
					conversazione.getSoggettoIdPartecipante(), idCittadino) == 0) {
				logError(methodName, "Cittadino non abilitato");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_075.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.FORBIDDEN, listError),
						"Cittadino non abilitato al servizio");
			}

			CodTMessaggio messaggio = new CodTMessaggio();
			messaggio.setConversazioneId(conversazione.getConversazioneId());
			messaggio.setMessaggioTestoCifrato(payloadMessaggio.getTesto());
			messaggio.setSoggettoIdDa(idCittadino);
			messaggio.setSoggettoIdA(conversazione.getSoggettoIdPartecipante());
			messaggio.setUtenteCreazione(shibIdentitaCodiceFiscale);
			messaggio.setUtenteModifica(shibIdentitaCodiceFiscale);

			long idMessaggio = codTMessaggioAllegatoCustomDao.insertTMessaggio(messaggio);

			if (payloadMessaggio.getAllegati() != null && payloadMessaggio.getAllegati().size() > 0) {
				for (ModelDocumento documento : payloadMessaggio.getAllegati()) {
					codTMessaggioAllegatoCustomDao.insertTAllegato(documento, idMessaggio, shibIdentitaCodiceFiscale);
				}
			}

			ModelMessaggio messaggioRet = codTMessaggioAllegatoCustomDao
					.selectMessaggioFromIdSelectSingola(idMessaggio);
			// come da analisi lo forzo a false DMA-3869
			if (messaggioRet != null) {
				messaggioRet.setModificabile(true);
				messaggioRet.setModificato(false);
			}
			// NOTIFICATORE

			conversazioneMessaggiPostNotificaAsync.notifyAsync(citId, conversazione.getSoggettoIdPartecipante(),
									xRequestId, xCodiceServizio, xForwardedFor,shibIdentitaCodiceFiscale);
			//AUDIT LOG
			String codiceCatalogo = "COD_CIT_NMSG";
			if(!shibIdentitaCodiceFiscale.equals(citId)) {
				codiceCatalogo ="COD_CIT_NMSG_DEL";
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
					ApiAuditEnum.POST_CITTADINI_CONVERSAZIONI_MESSAGGI,descrizioneCatalogoLogAudit,idCatalogoLogAudit);
			
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
