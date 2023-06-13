/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.PayloadMessaggio1;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.ErrorParamEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateConversazioneMessaggiPut;

@Service
public class ValidateConversazioneMessaggiPutImpl extends BaseValidate implements ValidateConversazioneMessaggiPut {


	private void checkIdConversazione(List<ErroreDettaglio> result, String idConversazione) throws DatabaseException {
		if (StringUtils.isBlank(idConversazione)) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.ID_CONVERSAZIONE.getCode()));
		}
	}


	private void checKPayloadMessaggio(List<ErroreDettaglio> result,PayloadMessaggio1 payloadMessaggio) throws DatabaseException {
		if (StringUtils.isBlank(payloadMessaggio.getTesto())) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.TESTO_MESSAGGIO.getCode()));
		}
		
	}

	@Override
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String idConversazione, PayloadMessaggio1 payloadMessaggio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");
		/*
		 * 2. Verifica parametri in input (Criteri di validazione della richiesta) 2a)
		 * Obbligatorieta'
		 * 
		 * Check errori comuni
		 */

		List<ErroreDettaglio> result = new ArrayList<>();
//		2a
		result = notNullCommonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);
		checkIdConversazione(result, idConversazione);
		checKPayloadMessaggio(result,payloadMessaggio);
		if (result.size() > 0) {
			return result;
		} else {
			// 2b controllo conformita'
			complianceCommonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio,
					xCodiceVerticale, ruolo);

		}
		return result;
	}

}
