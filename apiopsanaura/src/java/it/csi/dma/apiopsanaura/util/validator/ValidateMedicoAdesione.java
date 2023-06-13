/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.util.validator;

import java.util.List;

import it.csi.dma.apiopsanaura.dto.ErroreDettaglio;
import it.csi.dma.apiopsanaura.exception.DatabaseException;

public interface ValidateMedicoAdesione {

	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale) throws DatabaseException;
}
