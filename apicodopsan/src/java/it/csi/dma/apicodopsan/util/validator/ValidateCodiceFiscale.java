/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator;

import java.util.List;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.exception.DatabaseException;

public interface ValidateCodiceFiscale {

	public List<ErroreDettaglio> validate(String xCodiceVerticale,
			String xCodiceServizio, String codiceFiscale, String xForwardedFor, String xRequestId) throws DatabaseException;
}
