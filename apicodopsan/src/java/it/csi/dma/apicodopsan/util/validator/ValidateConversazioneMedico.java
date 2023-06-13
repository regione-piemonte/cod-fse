/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator;

import java.util.List;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.exception.DatabaseException;

public interface ValidateConversazioneMedico {

	public List<ErroreDettaglio> validate(  String idConversazione,String shibIdentitaCodiceFiscale,boolean checkBloccoConversazione)throws DatabaseException ;
}
