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
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.exception.ResponseErrorException;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTMessaggioAllegatoCustomDao;
import it.csi.dma.apicodopsan.integration.service.CodRMessaggioErroreService;
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateMedicoAdesione;
import it.csi.dma.apicodopsan.util.validator.ValidateUtenteAbilitatoServizio;
import it.csi.dma.apicodopsan.util.validator.ValidateVerificaAllegato;

@Service
public class DocumentiVerificaAllegatoGet extends LoggerUtil {

	@Autowired
	ValidateVerificaAllegato validateVerificaAllegato;
	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;
	@Autowired
	ValidateMedicoAdesione validateMedicoAdesione;
	@Autowired
	CodTMessaggioAllegatoCustomDao codTMessaggioAllegatoCustomDao;
	
	@Autowired
	ValidateUtenteAbilitatoServizio validateUtenteAbilitatoServizio;	

	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String idDocumento, String ruolo,
			String collocazioneCodice, String collocazioneDescrizione, String codiceFiscale, String codCl,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		final String methodName = "execute";
		ErrorBuilder error = null;
		try {
			
			//2a e 2b controlli obbligatori e formali
			List<ErroreDettaglio> listError = validateVerificaAllegato.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, xCodiceVerticale, idDocumento, ruolo, collocazioneCodice,
					collocazioneDescrizione, codiceFiscale, codCl, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
				/* sarebbe meglio mettere in throw e fare il salvataggio in fondo
				throw new ResponseErrorException(ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError), "Errore in ");
				*/ 
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateVerificaAllegato");
			}
			
			//2c verifica che il medico abbia aderito al servizio
			listError = validateMedicoAdesione.validate(shibIdentitaCodiceFiscale);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateMedicoAdesione");
			}
			
			//3 verifica utente abilitato
			listError = validateUtenteAbilitatoServizio.validate(xCodiceVerticale, xCodiceServizio, shibIdentitaCodiceFiscale, xForwardedFor, xRequestId, ruolo);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateUtenteAbilitatoServizio");
			}
			
			Integer count = codTMessaggioAllegatoCustomDao.checkVerificaAllegato(shibIdentitaCodiceFiscale, codiceFiscale, Integer.parseInt(idDocumento), codCl);
			if(count > 0) {
				return Response.ok().build();
			}else {
				return Response.status(403).build();
			}
			
			
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
