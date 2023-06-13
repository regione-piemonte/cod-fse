/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.helper.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.PayloadNotificaLettura;
import it.csi.dma.apicodopsan.dto.custom.Soggetto;
import it.csi.dma.apicodopsan.dto.custom.TAdesione;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.exception.ResponseErrorException;
import it.csi.dma.apicodopsan.integration.dao.custom.CodDErroreDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodSAdesioneDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTAdesioneDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.apicodopsan.integration.service.CodRMessaggioErroreService;
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;
import it.csi.dma.apicodopsan.util.validator.impl.ValidateGenericMeritWhitMedicoImpl;

@Service
public class NotificaLettura extends LoggerUtil {

	@Autowired
	ValidateGenericMeritWhitMedicoImpl validateMedicoRequest;

	@Autowired
	CodTSoggettoDao codTSoggettoDao;

	@Autowired
	CodTAdesioneDao codTAdesioneDao;

	@Autowired
	CodSAdesioneDao codSAdesioneDao;

	@Autowired
	CodDErroreDao codDErroreDao;
	
	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	@Autowired
	DataSource dataSource;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadNotificaLettura payloadNotificaLettura,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		var methodName = "execute";
		ErrorBuilder error = null;
		try {
			// validate
			List<ErroreDettaglio> listError = validateMedicoRequest.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, payloadNotificaLettura, securityContext, httpHeaders, httpRequest);

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
			Soggetto soggetto = codTSoggettoDao.selectSoggetto(shibIdentitaCodiceFiscale);
			if (soggetto == null) {
				 commonError(httpRequest);
			}

			TAdesione adesione = codTAdesioneDao.selectAdesione(soggetto.getSoggettoCf());
			if (adesione == null) {
				 commonError(httpRequest);
			}

			if(payloadNotificaLettura.isNotificaLettura().booleanValue() == adesione.isMostraLetturaMessaggiAAssistiti().booleanValue()) {
				ErroreDettaglio erroreDettaglio = new ErroreDettaglio();
				erroreDettaglio.setChiave(CodeErrorEnum.FSE_COD_089.getCode());
				erroreDettaglio.setValore(codDErroreDao.selectErroreDescFromErroreCod(CodeErrorEnum.FSE_COD_089.getCode()));
				List<ErroreDettaglio> result = new ArrayList<>();

				result.add(erroreDettaglio);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, result),
						erroreDettaglio.getValore());
//				codRMessaggioErroreService.saveError(result, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, result);

			}
			codSAdesioneDao.insertSAdesione(adesione);
			codTAdesioneDao.updateAdesioneNotifica(adesione, soggetto.getSoggettoCf(),
					payloadNotificaLettura.isNotificaLettura());

			return Response.status(200).entity(xRequestId).build();
			
//		} catch (DatabaseException e) {
//			logError(methodName, "Errore riguardante database:", e.getMessage());
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
//		} catch (Exception e) {
//			logError(methodName, "Errore non gestito :", e.getMessage());
//			codRMessaggioErroreService.saveError(null, httpRequest);
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

	private void commonError(HttpServletRequest httpRequest) throws DatabaseException, ResponseErrorException {

		ErroreDettaglio erroreDettaglio = new ErroreDettaglio();
		erroreDettaglio.setChiave(CodeErrorEnum.FSE_COD_066.getCode());
		erroreDettaglio.setValore(codDErroreDao.selectErroreDescFromErroreCod(CodeErrorEnum.FSE_COD_066.getCode()));
		List<ErroreDettaglio> result = new ArrayList<>();

		result.add(erroreDettaglio);
		throw new ResponseErrorException(
				ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, result),
				"errore in commonError: "+erroreDettaglio.getValore());
//		codRMessaggioErroreService.saveError(result, httpRequest);
//		return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, result);

	}

}
