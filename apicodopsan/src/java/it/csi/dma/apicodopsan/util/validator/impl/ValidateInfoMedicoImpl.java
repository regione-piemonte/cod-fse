/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.validator.ValidateInfoMedico;
import it.csi.dma.apicodopsan.verificaservices.dmacc.VerificaUtenteAbilitatoRequest;
import it.csi.dma.apicodopsan.verificaservices.dmacc.VerificaUtenteAbilitatoResponse;

@Service
public class ValidateInfoMedicoImpl extends BaseValidate implements ValidateInfoMedico {

	@Override
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");
		/**
		 * DMA-COD-SER-21-V01-Medico-GET Info medico
		 */
		
		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();
		//2a 
		result = notNullCommonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);
		//2b
		result = complianceCommonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo);
		//3 Call Verifica Utente Abilitato
		VerificaUtenteAbilitatoRequest verificaUtenteAbilitato = createVerificaUtenteAbilitatoRequest(
				xCodiceVerticale, xCodiceServizio, shibIdentitaCodiceFiscale, xForwardedFor, xRequestId, ruolo);

		VerificaUtenteAbilitatoResponse  verificaUtenteAbilitatoResp = functionalCheckUtenteAbilitato(verificaUtenteAbilitato);
		
		result = checkVerificaUtenteAbilitatoResponse(result, verificaUtenteAbilitatoResp);
		
		return result;
	}

}
