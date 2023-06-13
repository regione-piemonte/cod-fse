/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.validator.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.util.Constants;
import it.csi.dma.codcit.util.enumerator.ErrorParamEnum;
import it.csi.dma.codcit.util.validator.ValidateGeneric;

@Service("validateGeneric")
public class ValidateGenericMeritImpl extends ValidateGenericImpl implements ValidateGeneric {

	@Override
	public List<ErroreDettaglioExt> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String citId) throws DatabaseException {
		List<ErroreDettaglioExt> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, citId);
		// --- errori del merito ---
		if (result.isEmpty()) {
			formalCheckCfCod051(result, shibIdentitaCodiceFiscale, ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE);
			formalCheckCfCod051(result, citId, ErrorParamEnum.CIT_ID);
			checkIfEqualToFinalValueCod051(result, xCodiceServizio, ErrorParamEnum.APPLICAZIONE,
					Constants.CODICE_SERVIZIO_SANSOL);
		}

		return result;
	}

}
