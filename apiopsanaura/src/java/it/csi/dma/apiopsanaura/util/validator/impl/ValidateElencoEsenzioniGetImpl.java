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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import it.csi.dma.apiopsanaura.dto.ErroreDettaglio;
import it.csi.dma.apiopsanaura.exception.DatabaseException;
import it.csi.dma.apiopsanaura.util.enumerator.CodeErrorEnum;
import it.csi.dma.apiopsanaura.util.enumerator.ErrorParamEnum;
import it.csi.dma.apiopsanaura.util.validator.ValidateElencoEsenzioniGet;

@Service
public class ValidateElencoEsenzioniGetImpl extends BaseValidate implements ValidateElencoEsenzioniGet {



	@Override
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String tipoEsenzione, String listaDiagnosi, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {
		// TODO Auto-generated method stub
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();
//		2a
		result = notNullCommonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);
		if (StringUtils.isBlank(tipoEsenzione) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.TIPO_ESENZIONE.getCode()));
		}
		if (StringUtils.isBlank(listaDiagnosi) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.LISTA_DIAGNOSI.getCode()));
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
