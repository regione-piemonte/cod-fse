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
import it.csi.dma.apicodopsan.dto.PayloadMessaggio1;
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
import it.csi.dma.apicodopsan.integration.notificatore.util.ConversazioneMessaggiPutNotificaAsync;
import it.csi.dma.apicodopsan.integration.service.CodRMessaggioErroreService;
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.HeaderEnum;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateCittadinoAbilitatoDalMedico;
import it.csi.dma.apicodopsan.util.validator.ValidateConversazioneMedico;
import it.csi.dma.apicodopsan.util.validator.ValidateConversazioneMessaggiPut;
import it.csi.dma.apicodopsan.util.validator.ValidateMedicoAdesione;
import it.csi.dma.apicodopsan.util.validator.ValidateUtenteAbilitatoServizio;
import it.csi.dma.apicodopsan.util.validator.impl.ValidateConversazioneMessaggioMedicoImpl;

@Service
public class ConversazioneMessaggiPut extends LoggerUtil {

	@Autowired
	ValidateConversazioneMessaggiPut validateConversazioneMessaggiPut;
	@Autowired
	ValidateMedicoAdesione validateMedicoAdesione;
	@Autowired
	ValidateUtenteAbilitatoServizio validateUtenteAbilitato;
	@Autowired
	ValidateConversazioneMedico validateConversazioneMedico;
	@Autowired
	ValidateCittadinoAbilitatoDalMedico validateCittadinoAbilitatoDalMedico;
	@Autowired
	ValidateConversazioneMessaggioMedicoImpl validateConversazioneMessaggioMedicoImpl;
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
	ConversazioneMessaggiPutNotificaAsync conversazioneMessaggiPutNotificaAsync;
	@Autowired
	AuditLogService auditLogService;
	@Autowired
	CodTSoggettoDao codTSoggettoDao;
	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;

	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String conversazioneUUID, String idMessaggio,
			PayloadMessaggio1 payloadMessaggio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

		String methodName = "execute";
		ErrorBuilder error = null;
		try {
			// 2a,2b
			List<ErroreDettaglio> listError = validateConversazioneMessaggiPut.validate(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, conversazioneUUID, payloadMessaggio, securityContext, httpHeaders,
					httpRequest);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateConversazioneMessaggiPut");
			}
			// Altri controlli
			// 2c adesione utente
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
			listError = validateConversazioneMedico.validate(conversazioneUUID, shibIdentitaCodiceFiscale,
					controlloBloccoConversazione);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateConversazioneMedico");
			}
			List<TConversazione> listConv = codTconversazioneDao
					.selectListConversazioneByCodAndSoggettoCf(conversazioneUUID, shibIdentitaCodiceFiscale);
			TConversazione conversazione = listConv.get(0);
			Integer soggettoAutore = conversazione.getSoggettoIdAutore();
			Integer soggettoMedico = conversazione.getSoggettoIdPartecipante();
			Long idConversazione = conversazione.getConversazioneId();

			// Verifica se il cittadino abilitato dal medico.
			listError = validateCittadinoAbilitatoDalMedico.validate(soggettoAutore, soggettoMedico);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateCittadinoAbilitatoDalMedico");
			}
			// 3) utente abilitato
			listError = validateUtenteAbilitato.validate(xCodiceVerticale, xCodiceServizio, shibIdentitaCodiceFiscale,
					xForwardedFor, xRequestId, ruolo);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateUtenteAbilitato");
			}

			// 4 controlli sul messaggio esistente proprietario modificabile
			listError = validateConversazioneMessaggioMedicoImpl.validate(idConversazione, shibIdentitaCodiceFiscale,
					idMessaggio);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateConversazioneMessaggioMedicoImpl");
			}

			// CERCA MESSAGGIO E AGGIORNA
			// INSERIRE IL MESSAGGIO SU DB
			Long messaggioL = Long.parseLong(idMessaggio);
			Integer result = codTMessaggioAllegatoCustomDao.updateTMessaggioMedico(messaggioL,
					shibIdentitaCodiceFiscale, payloadMessaggio.getTesto());

			ModelMessaggioNuovo messaggioRet = codTMessaggioAllegatoCustomDao
					.selectMessaggioFromIdSelectSingola(messaggioL, idConversazione);

			// AUDIT LOG
			String codiceCatalogo = "COD_MED_MMSG";
			String descrizioneCatalogoLogAudit = null;
			Long idCatalogoLogAudit = null;
			Soggetto autoreConversazione = codTSoggettoDao.selectSoggettoById(soggettoAutore);
			DmaccDCatalogoLogAudit catalogoLogAudit = dmaccTCatalogoLogAuditDao
					.selectCatalogoDescrizioneByCodice(codiceCatalogo);
			if (catalogoLogAudit != null) {
				idCatalogoLogAudit = catalogoLogAudit.getId();
				descrizioneCatalogoLogAudit = catalogoLogAudit.getDescrizione().replace("{0}",
						shibIdentitaCodiceFiscale);
				descrizioneCatalogoLogAudit = descrizioneCatalogoLogAudit.replace("{1}", conversazioneUUID);
			}
			auditLogService.insertAuditLog(xRequestId, xForwardedFor, shibIdentitaCodiceFiscale, xCodiceServizio,
					xCodiceVerticale, ruolo, collocazioneDescrizione, autoreConversazione.getSoggettoCf(),
					ApiAuditEnum.PUT_CONVERSAZIONE_MESSAGGI, descrizioneCatalogoLogAudit, idCatalogoLogAudit);
			// NOTIFICATORE
			conversazioneMessaggiPutNotificaAsync.notifyAsync(shibIdentitaCodiceFiscale, soggettoAutore, xRequestId,
					xCodiceServizio);

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
