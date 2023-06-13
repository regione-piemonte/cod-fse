/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.exception.DatabaseException;

public interface ValidateVerificaAllegato {

	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String idDocumento, String ruolo,
			String collocazioneCodice, String collocazioneDescrizione, String codiceFiscale, String codCl,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws DatabaseException;

}
