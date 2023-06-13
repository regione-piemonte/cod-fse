/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.validator.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.util.enumerator.ErrorParamEnum;
import it.csi.dma.codcit.util.validator.ValidateGenericMessagges;

@Service("validateGenericMessagges")
public class ValidateGenericMessaggeImpl extends ValidateGenericMeritImpl implements ValidateGenericMessagges {

	@Override
	public List<ErroreDettaglioExt> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String citId, String idConversazione) throws DatabaseException {
		List<ErroreDettaglioExt> errori = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, citId);
		checkIfEmptyCod050(errori, idConversazione, ErrorParamEnum.ID_CONVERSAZIONE);
		return errori;
	}

}
