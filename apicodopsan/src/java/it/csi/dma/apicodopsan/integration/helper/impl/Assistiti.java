/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.helper.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.ModelAssistito;
import it.csi.dma.apicodopsan.dto.custom.DmaccDCatalogoLogAudit;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.exception.ResponseErrorException;
import it.csi.dma.apicodopsan.integration.auditlog.ApiAuditEnum;
import it.csi.dma.apicodopsan.integration.auditlog.AuditLogService;
import it.csi.dma.apicodopsan.integration.dao.custom.CodDErroreDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.apicodopsan.integration.dao.custom.DmaccTCatalogoLogAuditDao;
import it.csi.dma.apicodopsan.integration.service.CodRMessaggioErroreService;
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.HeaderEnum;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;
import it.csi.dma.apicodopsan.util.validator.impl.ValidateGenericMeritWhitMedicoImpl;

@Service
public class Assistiti extends LoggerUtil {

	@Autowired
	ValidateGenericMeritWhitMedicoImpl validateMedicoRequest;

	@Autowired
	CodTSoggettoDao codTSoggettoDao;

	@Autowired
	CodDErroreDao codDErroreDao;

	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	@Autowired
	DataSource dataSource;

	@Autowired
	AuditLogService auditLogService;

	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer limit, Integer offset, String stato, String cognome, String nome,
			String codiceFiscale, Integer etaMin, Integer etaMax, String sesso, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		var methodName = "execute";
		ErrorBuilder error = null;
		try {
			// validate
			List<ErroreDettaglio> listError = validateMedicoRequest.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, limit, offset, stato, cognome, nome, codiceFiscale, etaMin, etaMax, sesso,
					securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateMedicoRequest");
			}

			/*
			 * INIZIO LOGICA APPLICATIVA SUPERATI TUTTI I CONTROLLI
			 *
			 * REGISTRA NOTIFICA ADESIONE
			 */

			List<ModelAssistito> resultLst = new ArrayList<>();
			Integer totalElements = 0;
			if (stato.equals("A")) {
				resultLst = codTSoggettoDao.selectLstAssistitiAbilitatiPag(shibIdentitaCodiceFiscale, cognome, nome,
						codiceFiscale, etaMin, etaMax, sesso, stato, limit, offset);
				totalElements = codTSoggettoDao.countTotalAssistitiAbilitatiPag(shibIdentitaCodiceFiscale, cognome,
						nome, codiceFiscale, etaMin, etaMax, sesso, stato);

			} else {
				resultLst = codTSoggettoDao.selectLstAssistitiiDisabilitatiPag(shibIdentitaCodiceFiscale, cognome, nome,
						codiceFiscale, etaMin, etaMax, sesso, stato, limit, offset);
				totalElements = codTSoggettoDao.countTotalAssistitiiDisabilitatiPag(shibIdentitaCodiceFiscale, cognome,
						nome, codiceFiscale, etaMin, etaMax, sesso, stato);
			}
			// AUDIT LOG
			String codiceCatalogo = "COD_MED_LISTASS";
			String descrizioneCatalogoLogAudit = null;
			Long idCatalogoLogAudit = null;
			DmaccDCatalogoLogAudit catalogoLogAudit = dmaccTCatalogoLogAuditDao
					.selectCatalogoDescrizioneByCodice(codiceCatalogo);
			if (catalogoLogAudit != null) {
				idCatalogoLogAudit = catalogoLogAudit.getId();
				descrizioneCatalogoLogAudit = catalogoLogAudit.getDescrizione().replace("{0}",
						shibIdentitaCodiceFiscale);
			}
			auditLogService.insertAuditLog(xRequestId, xForwardedFor, shibIdentitaCodiceFiscale, xCodiceServizio,
					xCodiceVerticale, ruolo, collocazioneDescrizione, null, ApiAuditEnum.GET_ASSISTITI,
					descrizioneCatalogoLogAudit, idCatalogoLogAudit);

			return Response.ok(resultLst).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId)
					.header(HeaderEnum.X_TOTAL_ELEMENTS.getCode(), totalElements).build();
//		} catch (DatabaseException e) {
//			logError(methodName, "Errore riguardante database:", e.getMessage());
//			e.printStackTrace();
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
//		} catch (Exception e) {
//			logError(methodName, "Errore non gestito :", e.getMessage());
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
//		}
		} catch (DatabaseException e) {
//			codRMessaggioErroreService.saveError(null, httpRequest);
			logError(methodName, "Errore riguardante database:", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		} catch (ResponseErrorException e) {
			logError(methodName, "Errore generico response:", e.getMessage());
			error = e.getResponseError();
		} catch (Exception e) {
//			codRMessaggioErroreService.saveError(null, httpRequest);
			logError(methodName, "Errore non gestito :", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		}
		error = codRMessaggioErroreService.saveError(error, httpRequest);
		return error.generateResponseError();
	}
}
