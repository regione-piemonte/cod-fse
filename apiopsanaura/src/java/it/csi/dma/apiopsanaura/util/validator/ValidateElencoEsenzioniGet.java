/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.util.validator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import it.csi.dma.apiopsanaura.dto.ErroreDettaglio;
import it.csi.dma.apiopsanaura.exception.DatabaseException;

public interface ValidateElencoEsenzioniGet {



	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String tipoEsenzione, String listaDiagnosi, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest)throws DatabaseException;
}
