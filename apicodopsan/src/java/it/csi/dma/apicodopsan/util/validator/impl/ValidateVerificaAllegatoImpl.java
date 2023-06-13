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
import it.csi.dma.apicodopsan.util.validator.ValidateVerificaAllegato;

@Service
public class ValidateVerificaAllegatoImpl extends BaseValidate implements ValidateVerificaAllegato {

	@Override
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String idDocumento,
			 String ruolo,  String collocazioneCodice, String collocazioneDescrizione,
			String codiceFiscale, String codCl, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws DatabaseException{
		// TODO Auto-generated method stub
		var methodName = "validate";
		logInfo(methodName, "BEGIN");
		/**
		 * 2a
		 */
		
		List<ErroreDettaglio> result = new ArrayList<>();
		notNullCommonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);
		checkQueryParam(result, idDocumento, codiceFiscale, codCl);
		if (result.size() > 0) {
			return result;
		} else {
			// 2b controllo conformita'
			complianceCommonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo);
			try {
				Integer.parseInt(idDocumento);
			}catch(NumberFormatException e) {
				result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(),
						ErrorParamEnum.ID_DOCUMENTO.getCode(), idDocumento));
			}

		}
		return result;
		
	}
	
	
	private List<ErroreDettaglio> checkQueryParam(List<ErroreDettaglio> result, String idDocumento, String codiceFiscale, String codCl) throws DatabaseException{
		
		if (StringUtils.isBlank(idDocumento)) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.ID_DOCUMENTO.getCode()));
		}
		
		if (StringUtils.isBlank(codiceFiscale)) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.CODICE_FISCALE.getCode()));
		}		
		
		if (StringUtils.isBlank(codCl)) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.COD_CL.getCode()));
		}
		return result;
	}






	
	
}
