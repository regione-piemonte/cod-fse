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
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.ErrorParamEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateConversazioneMessaggiGet;

@Service
public class ValidateConversazioneMessaggiGetImpl extends BaseValidate implements ValidateConversazioneMessaggiGet {

	@Override
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String idConversazione, Integer limit, Integer offset,
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
		result = notNullCommonCheckForList(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, limit, offset);
		checkIdConversazione(result, idConversazione);
		if (result.size() > 0) {
			return result;
		} else {
			// 2b controllo conformita'
			complianceCommonCheckForList(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio,
					xCodiceVerticale, ruolo, limit, offset);

		}
		return result;
	}

	private void checkIdConversazione(List<ErroreDettaglio> result, String idConversazione) throws DatabaseException {
		if (StringUtils.isBlank(idConversazione)) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.ID_CONVERSAZIONE.getCode()));
		}
	}

}
