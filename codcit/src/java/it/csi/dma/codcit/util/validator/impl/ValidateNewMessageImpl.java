/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.validator.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import it.csi.dma.codcit.dto.PayloadMessaggio;
import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.util.enumerator.ErrorParamEnum;
import it.csi.dma.codcit.util.validator.ValidateNewMessage;

@Service("validateNewMessage")
public class ValidateNewMessageImpl extends ValidateGenericMessaggeImpl implements ValidateNewMessage {

	@Override
	public List<ErroreDettaglioExt> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String citId, String idConversazione, PayloadMessaggio payloadMessaggio)
			throws DatabaseException {
		List<ErroreDettaglioExt> errori = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, citId, idConversazione);
		
		boolean emptyMessaggio = checkIfEmptyCod050(errori, payloadMessaggio, ErrorParamEnum.MESSAGGIO);
		
		return errori;
	}

}
