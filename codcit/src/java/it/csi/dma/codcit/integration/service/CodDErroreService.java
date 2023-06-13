/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.service;

import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;

public interface CodDErroreService {

	ErroreDettaglioExt getValueGenericError(String key, String... param) throws DatabaseException;

	ErroreDettaglioExt getValueGenericErrorFromExternalService(String externalService, String errorCode, String errorDesc) throws DatabaseException;

}