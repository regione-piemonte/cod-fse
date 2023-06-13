/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;

import it.csi.dma.apiopsanaura.dto.ErroreDettaglio;
import it.csi.dma.apiopsanaura.exception.DatabaseException;
import it.csi.dma.apiopsanaura.util.enumerator.CodeErrorEnum;
import it.csi.dma.apiopsanaura.util.enumerator.ErrorParamEnum;
import it.csi.dma.apiopsanaura.util.validator.ValidateAssistitiGet;

@Service
public class ValidateAssistitiGetImpl extends BaseValidate implements ValidateAssistitiGet {


	@Override
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer nPagina, String codFiscAss, String cognome, String nome,
			Integer etaMin, Integer etaMax, String sesso, String esenzionePatologia, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();
//		2a
		result = notNullCommonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);
		if (nPagina==null ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.NUMERO_PAGINA.getCode()));
		}
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
