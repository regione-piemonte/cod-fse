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

import it.csi.dma.apicodopsan.business.be.AssistitiApi;
import it.csi.dma.apicodopsan.dto.PayloadAbilitazione;
import it.csi.dma.apicodopsan.dto.PayloadAbilitazioneTotale;
import it.csi.dma.apicodopsan.integration.helper.impl.Assistiti;
import it.csi.dma.apicodopsan.integration.helper.impl.AssistitiPost;
import it.csi.dma.apicodopsan.integration.helper.impl.AssistitiTuttiPost;

@Component
public class AssistitiApiServiceImpl implements AssistitiApi {

	@Autowired
	private Assistiti assistiti;
	@Autowired
	private AssistitiPost assistitiPost;
	
	@Autowired
	private AssistitiTuttiPost assistitiTuttiPost;
	
	public Response assistitiGet(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer limit, Integer offset, String stato, String cognome, String nome,
			String codiceFiscale, Integer etaMin, Integer etaMax, String sesso, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		return assistiti.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, limit, offset, stato, cognome,
				nome, codiceFiscale, etaMin, etaMax, sesso, securityContext, httpHeaders, httpRequest);
	}

	public Response assistitiPost(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadAbilitazione payloadAbilitazione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		// do some magic!
		return assistitiPost.execute( shibIdentitaCodiceFiscale,  xRequestId,  xForwardedFor,
				 xCodiceServizio,  xCodiceVerticale,  ruolo,  collocazioneCodice,
				 collocazioneDescrizione,  payloadAbilitazione,  securityContext,
				 httpHeaders,  httpRequest);
	}
	public Response assistitiTuttiPost(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadAbilitazioneTotale payloadAbilitazioneTotale,
			SecurityContext securityContext, HttpHeaders httpHeaders,  HttpServletRequest httpRequest) {
		// do some magic!
		return assistitiTuttiPost.execute( shibIdentitaCodiceFiscale,  xRequestId,  xForwardedFor,
				 xCodiceServizio,  xCodiceVerticale,  ruolo,  collocazioneCodice,
				 collocazioneDescrizione,  payloadAbilitazioneTotale,
				 securityContext,  httpHeaders,   httpRequest);
	}
}
