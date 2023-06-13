/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator;

import java.util.List;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.exception.DatabaseException;

public interface ValidateInfoMedico {
		 
	
	public List<ErroreDettaglio> validate(	String shibIdentitaCodiceFiscale,String xRequestId,String xForwardedFor,
			String xCodiceServizio,String xCodiceVerticale,
			String ruolo,String collocazioneCodice,String collocazioneDescrizione)throws DatabaseException ;
}
