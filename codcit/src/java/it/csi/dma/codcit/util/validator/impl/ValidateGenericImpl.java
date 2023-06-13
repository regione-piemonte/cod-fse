/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.util.enumerator.ErrorParamEnum;

public class ValidateGenericImpl extends BaseValidate {

	public List<ErroreDettaglioExt> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String citId) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta
		List<ErroreDettaglioExt> result = new ArrayList<>();

		checkIfEmptyCod050(result, xRequestId, ErrorParamEnum.X_REQUEST_ID);
		checkIfEmptyCod050(result, shibIdentitaCodiceFiscale, ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE);
		checkIfEmptyCod050(result, xForwardedFor, ErrorParamEnum.X_FORWARDED_FOR);
		checkIfEmptyCod050(result, xCodiceServizio, ErrorParamEnum.X_CODICE_SERVIZIO);
		checkIfEmptyCod050(result, citId, ErrorParamEnum.CIT_ID);

		return result;
	}

}
