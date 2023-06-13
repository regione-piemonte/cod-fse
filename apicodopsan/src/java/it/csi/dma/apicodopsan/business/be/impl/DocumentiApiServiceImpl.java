/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.dma.apicodopsan.business.be.DocumentiApi;
import it.csi.dma.apicodopsan.integration.helper.impl.DocumentiVerificaAllegatoGet;

@Component
public class DocumentiApiServiceImpl implements DocumentiApi {
	
	@Autowired
	DocumentiVerificaAllegatoGet documentiVerificaAllegatoGet;
	
	

	@Override
	public Response documentiIdDocumentoVerificaAllegatoGet(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String idDocumento,
			@NotNull String ruolo, @NotNull String collocazioneCodice, @NotNull String collocazioneDescrizione,
			String codiceFiscale, String codCl, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return documentiVerificaAllegatoGet.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, xCodiceVerticale, idDocumento, ruolo, collocazioneCodice, collocazioneDescrizione, codiceFiscale, codCl, securityContext, httpHeaders, httpRequest);
	}

}
