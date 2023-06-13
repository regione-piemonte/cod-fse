/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.dma.apicodopsan.business.be.AdesioneApi;
import it.csi.dma.apicodopsan.dto.PayloadAdesione;
import it.csi.dma.apicodopsan.integration.helper.impl.AdesioneRevocaMedico;

@Component
public class AdesioneApiServiceImpl implements AdesioneApi {

	@Autowired
	AdesioneRevocaMedico adesioneRevocaMedico;
	public Response adesionePost(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadAdesione payloadAdesione, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
	
		return adesioneRevocaMedico.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, payloadAdesione, securityContext, httpHeaders, httpRequest);
	}
}
