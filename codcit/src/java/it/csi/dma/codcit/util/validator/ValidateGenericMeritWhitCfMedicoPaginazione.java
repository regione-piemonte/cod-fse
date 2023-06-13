/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.validator;

import java.util.List;

import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;

public interface ValidateGenericMeritWhitCfMedicoPaginazione {

	List<ErroreDettaglioExt> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String citId, String cfMedico, String solaLettura, Integer limit, Integer offset)
			throws DatabaseException;

}