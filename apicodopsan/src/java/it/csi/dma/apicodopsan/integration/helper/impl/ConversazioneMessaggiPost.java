/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.helper.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.ModelMessaggioNuovo;
import it.csi.dma.apicodopsan.dto.PayloadMessaggio_;
import it.csi.dma.apicodopsan.dto.custom.CodTMessaggio;
import it.csi.dma.apicodopsan.dto.custom.DmaccDCatalogoLogAudit;
import it.csi.dma.apicodopsan.dto.custom.Soggetto;
import it.csi.dma.apicodopsan.dto.custom.TConversazione;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.exception.ResponseErrorException;
import it.csi.dma.apicodopsan.integration.auditlog.ApiAuditEnum;
import it.csi.dma.apicodopsan.integration.auditlog.AuditLogService;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTConversazioneDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTMessaggioAllegatoCustomDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoAbilitatoDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.apicodopsan.integration.dao.custom.DmaccTCatalogoLogAuditDao;
import it.csi.dma.apicodopsan.integration.notificatore.util.ConversazioneMessaggiPostNotificaAsync;
import it.csi.dma.apicodopsan.integration.service.CodRMessaggioErroreService;
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.HeaderEnum;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateCittadinoAbilitatoDalMedico;
import it.csi.dma.apicodopsan.util.validator.ValidateConversazioneMedico;
import it.csi.dma.apicodopsan.util.validator.ValidateConversazioneMessaggiPost;
import it.csi.dma.apicodopsan.util.validator.ValidateMedicoAdesione;
import it.csi.dma.apicodopsan.util.validator.ValidateUtenteAbilitatoServizio;

@Service
public class ConversazioneMessaggiPost extends LoggerUtil {

	@Autowired
	ValidateConversazioneMessaggiPost validateConversazioneMessaggiPost;
	@Autowired
	ValidateMedicoAdesione validateMedicoAdesione;
	@Autowired
	ValidateUtenteAbilitatoServizio validateUtenteAbilitato;
	@Autowired
	ValidateConversazioneMedico validateConversazioneMedico;
	@Autowired
	ValidateCittadinoAbilitatoDalMedico validateCittadinoAbilitatoDalMedico;
	@Autowired
	CodTConversazioneDao codTconversazioneDao;
	@Autowired
	CodTMessaggioAllegatoCustomDao codTMessaggioAllegatoCustomDao;
	@Autowired
	CodTSoggettoAbilitatoDao codTSoggettoAbilitatoDao;
	@Autowired
	CodTConversazioneDao codTConversazioneDao;
	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;
	@Autowired
	ConversazioneMessaggiPostNotificaAsync conversazioneMessaggiPostNotificaAsync;
	@Autowired
	AuditLogService auditLogService;
	@Autowired
	CodTSoggettoDao codTSoggettoDao;
	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;

	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String idConversazione, PayloadMessaggio_ payloadMessaggio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = "execute";
		ErrorBuilder error = null;
		try {

			// 2a,2b
			List<ErroreDettaglio> listError = validateConversazioneMessaggiPost.validate(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, idConversazione, payloadMessaggio, securityContext, httpHeaders,
					httpRequest);
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateConversazioneMessaggiPost");
			}
//			Altri controlli
//			2c adesione utente
			listError = validateMedicoAdesione.validate(shibIdentitaCodiceFiscale);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateMedicoAdesione");
			}
			// 2c2 ESISTE UNA CONVERSAZIONE PER QUELL'ID e non e' bloccata
			boolean controlloBloccoConversazione = true;
			listError = validateConversazioneMedico.validate(idConversazione, shibIdentitaCodiceFiscale,
					controlloBloccoConversazione);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateConversazioneMedico");
			}
			// 3) utente abilitato al servizio
			listError = validateUtenteAbilitato.validate(xCodiceVerticale, xCodiceServizio, shibIdentitaCodiceFiscale,
					xForwardedFor, xRequestId, ruolo);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateUtenteAbilitato");
			}
			List<TConversazione> listConv = codTconversazioneDao
					.selectListConversazioneByCodAndSoggettoCf(idConversazione, shibIdentitaCodiceFiscale);
			TConversazione conversazione = listConv.get(0);
			Integer soggettoAutore = conversazione.getSoggettoIdAutore();
			Integer soggettoMedico = conversazione.getSoggettoIdPartecipante();
			// Verifica se il cittadino abilitato dal medico.
			listError = validateCittadinoAbilitatoDalMedico.validate(soggettoAutore, soggettoMedico);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateCittadinoAbilitatoDalMedico");
			}
			// INSERIRE IL MESSAGGIO SU DB
			CodTMessaggio messaggio = new CodTMessaggio();
			messaggio.setConversazioneId(conversazione.getConversazioneId());
			messaggio.setMessaggioTestoCifrato(payloadMessaggio.getTesto());
			messaggio.setSoggettoIdDa(soggettoMedico);
			messaggio.setSoggettoIdA(soggettoAutore);
			messaggio.setUtenteCreazione(shibIdentitaCodiceFiscale);
			messaggio.setUtenteModifica(shibIdentitaCodiceFiscale);

			long idMessaggio = codTMessaggioAllegatoCustomDao.insertTMessaggio(messaggio);

			ModelMessaggioNuovo messaggioRet = codTMessaggioAllegatoCustomDao
					.selectMessaggioFromIdSelectSingola(idMessaggio, conversazione.getConversazioneId());

			

			// NOTIFICATORE
			conversazioneMessaggiPostNotificaAsync.notifyAsync(shibIdentitaCodiceFiscale, soggettoAutore, xRequestId,
					xCodiceServizio);
//			);
			// AUDIT LOG
			String codiceCatalogo = "COD_MED_NMSG";
			Soggetto autoreConversazione = codTSoggettoDao.selectSoggettoById(soggettoAutore);
			String descrizioneCatalogoLogAudit = null;
			Long idCatalogoLogAudit = null;
			DmaccDCatalogoLogAudit catalogoLogAudit = dmaccTCatalogoLogAuditDao
					.selectCatalogoDescrizioneByCodice(codiceCatalogo);
			if (catalogoLogAudit != null) {
				idCatalogoLogAudit = catalogoLogAudit.getId();
				descrizioneCatalogoLogAudit = catalogoLogAudit.getDescrizione().replace("{0}",
						shibIdentitaCodiceFiscale);
				descrizioneCatalogoLogAudit = descrizioneCatalogoLogAudit.replace("{1}", idConversazione);
			}
			auditLogService.insertAuditLog(xRequestId, xForwardedFor, shibIdentitaCodiceFiscale, xCodiceServizio,
					xCodiceVerticale, ruolo, collocazioneDescrizione, autoreConversazione.getSoggettoCf(),
					ApiAuditEnum.POST_CONVERSAZIONE_MESSAGGI, descrizioneCatalogoLogAudit, idCatalogoLogAudit);
			return Response.ok().header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId).entity(messaggioRet).build();

//		} catch (DatabaseException e) {
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			logError(methodName, "Errore riguardante database:", e.getMessage());
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
//		} catch (Exception e) {
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			logError(methodName, "Errore non gestito :", e.getMessage());
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
//		}
		} catch (DatabaseException e) {
			logError(methodName, "Errore riguardante database:", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		} catch (ResponseErrorException e) {
			logError(methodName, "Errore generico response:", e.getMessage());
			error = e.getResponseError();
		} catch (Exception e) {
			logError(methodName, "Errore non gestito :", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		}
		error = codRMessaggioErroreService.saveError(error, httpRequest);
		return error.generateResponseError();
	}

}