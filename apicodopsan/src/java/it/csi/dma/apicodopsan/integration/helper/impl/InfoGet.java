/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.helper.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.ModelMedicoInfo;
import it.csi.dma.apicodopsan.dto.custom.CountSoggetti;
import it.csi.dma.apicodopsan.dto.custom.TAdesione;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.exception.ResponseErrorException;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTAdesioneDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTRichiestaBatchDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.apicodopsan.integration.helper.impl.base.BaseService;
import it.csi.dma.apicodopsan.integration.service.CodRMessaggioErroreService;
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.HeaderEnum;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateInfoMedico;

@Service
public class InfoGet extends BaseService {

	@Autowired
	ValidateInfoMedico validateInfoMedico;

	@Autowired
	CodTAdesioneDao codTAdesioneDao;

	@Autowired
	CodTSoggettoDao codTSoggettoDao;
	
	@Autowired
	CodTRichiestaBatchDao codTRichiestaBatchDao;
	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		String methodName = "execute";
		ErrorBuilder error = null;
		try {

			List<ErroreDettaglio> listError = validateInfoMedico.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione);

			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateInfoMedico");
			
			}

			TAdesione adesione = codTAdesioneDao.selectUltimaAdesione(shibIdentitaCodiceFiscale);
			if (adesione == null) {
				commonError(httpRequest, CodeErrorEnum.FSE_COD_066, StatusEnum.NOT_FOUND);
			}

			CountSoggetti countSoggetti = codTSoggettoDao
					.selectCountSoggettiAbilitatiDisabilitati(shibIdentitaCodiceFiscale);
			Integer richiesteAttive = codTRichiestaBatchDao.countRichiesteAttiveMedico(shibIdentitaCodiceFiscale);
			ModelMedicoInfo modelMedicoInfo = newModelMedicoInfo(adesione, countSoggetti,richiesteAttive);

			return Response.ok(modelMedicoInfo).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId).build();

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

	private ModelMedicoInfo newModelMedicoInfo(TAdesione adesione, CountSoggetti countSoggetti, Integer richiesteAttive) {
		var modelMedicoInfo = new ModelMedicoInfo();
		boolean abilitazione =richiesteAttive != null? richiesteAttive>0: false; 
		modelMedicoInfo.setAbilitazioneAssistiti(abilitazione);
		modelMedicoInfo.setDataFineAdesione(adesione.getAdesioneFine());
		modelMedicoInfo.setDataInizioAdesione(adesione.getAdesioneInizio());
		modelMedicoInfo.setNotificaLetturaMessaggi(adesione.isMostraLetturaMessaggiAAssistiti());
		modelMedicoInfo.setNPazientiAbilitati(countSoggetti.getSoggettiAbilitati());
		modelMedicoInfo.setNPazientiBloccati(countSoggetti.getSoggettiDisabilitati());

		return modelMedicoInfo;
	}
}
