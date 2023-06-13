/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.helper.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.ModelMessaggio;
import it.csi.dma.apicodopsan.dto.custom.DmaccDCatalogoLogAudit;
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
import it.csi.dma.apicodopsan.integration.service.CodRMessaggioErroreService;
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.HeaderEnum;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateConversazioneMedico;
import it.csi.dma.apicodopsan.util.validator.ValidateConversazioneMessaggiGet;
import it.csi.dma.apicodopsan.util.validator.ValidateUtenteAbilitatoServizio;

@Service
public class ConversazioneMessaggiGet extends LoggerUtil {

	@Autowired
	ValidateConversazioneMessaggiGet validateConversazioneMessaggiGet;
	@Autowired
	ValidateUtenteAbilitatoServizio validateUtenteAbilitato;
	@Autowired
	ValidateConversazioneMedico validateConversazioneMedico;
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
	AuditLogService auditLogService;
	@Autowired
	CodTSoggettoDao codTSoggettoDao;
	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;

	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String idConversazione, Integer limit, Integer offset,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = "execute";
		ErrorBuilder error = null;
		try {

			List<ErroreDettaglio> listError = validateConversazioneMessaggiGet.validate(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, idConversazione, limit, offset, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateConversazioneMessaggiGet");
			}
//			2c ESISTE UNA CONVERSAZIONE PER QUELL'ID
			boolean controlloBloccoConversazione = false;
			listError = validateConversazioneMedico.validate(idConversazione, shibIdentitaCodiceFiscale,
					controlloBloccoConversazione);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateConversazioneMedico");
			}
//			3) utente abilitato al servizio
			listError = validateUtenteAbilitato.validate(xCodiceVerticale, xCodiceServizio, shibIdentitaCodiceFiscale,
					xForwardedFor, xRequestId, ruolo);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateUtenteAbilitato");
			}
			//// RICERCA MESSAGGI
			List<TConversazione> listConversazioneSoggettoCustom = codTconversazioneDao
					.selectListConversazioneByCodAndSoggettoCf(idConversazione, shibIdentitaCodiceFiscale);
			List<ModelMessaggio> result = new ArrayList<ModelMessaggio>();
			Integer totalElements = 0;
			String autoreCf = null;
			if (listConversazioneSoggettoCustom != null && listConversazioneSoggettoCustom.size() > 0) {
				result = codTMessaggioAllegatoCustomDao.selectListaMessaggiFromIdConversazioneOffsetLimit(
						listConversazioneSoggettoCustom.get(0).getConversazioneId(), offset, limit);
				totalElements = codTMessaggioAllegatoCustomDao.countListaMessaggiFromIdConversazione(
						listConversazioneSoggettoCustom.get(0).getConversazioneId());
				autoreCf = codTSoggettoDao
						.selectSoggettoById(listConversazioneSoggettoCustom.get(0).getSoggettoIdAutore())
						.getSoggettoCf();
			}
			// AUDIT LOG
			String codiceCatalogo = "COD_MED_ELEMSG";
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
					xCodiceVerticale, ruolo, collocazioneDescrizione, autoreCf, ApiAuditEnum.GET_CONVERSAZIONE_MESSAGGI,
					descrizioneCatalogoLogAudit, idCatalogoLogAudit);

			return Response.ok(result).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId)
					.header(HeaderEnum.X_TOTAL_ELEMENTS.getCode(), totalElements).build();
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
