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

import it.csi.dma.apicodopsan.business.be.ConversazioniApi;
import it.csi.dma.apicodopsan.dto.PayloadMessaggio1;
import it.csi.dma.apicodopsan.dto.PayloadMessaggio_;
import it.csi.dma.apicodopsan.integration.helper.impl.ConversazioneMessaggiGet;
import it.csi.dma.apicodopsan.integration.helper.impl.ConversazioneMessaggiPost;
import it.csi.dma.apicodopsan.integration.helper.impl.ConversazioneMessaggiPut;
import it.csi.dma.apicodopsan.integration.helper.impl.Conversazioni;

@Component
public class ConversazioniApiServiceImpl implements ConversazioniApi {

	@Autowired
	Conversazioni conversazioni;
	
	@Autowired
	ConversazioneMessaggiGet conversazioneMessaggiGet;
	
	@Autowired
	ConversazioneMessaggiPost conversazioneMessaggiPost;
	
	@Autowired
	ConversazioneMessaggiPut conversazioneMessaggiPut;

	public Response conversazioniGet(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer limit, Integer offset, String solaLettura, String cognome,
			String codiceFiscale, String idConversazione, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		return conversazioni.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, limit, offset, solaLettura,
				cognome, codiceFiscale, securityContext, httpHeaders, httpRequest,idConversazione);
	}

	public Response conversazioniIdConversazioneMessaggiGet(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo,
			String collocazioneCodice, String collocazioneDescrizione, String idConversazione, Integer limit,
			Integer offset, SecurityContext securityContext, HttpHeaders httpHeaders,
			 HttpServletRequest httpRequest) {

		// do some magic!
		return conversazioneMessaggiGet.execute( shibIdentitaCodiceFiscale,  xRequestId,
				 xForwardedFor,  xCodiceServizio,  xCodiceVerticale,  ruolo,
				 collocazioneCodice,  collocazioneDescrizione,  idConversazione,  limit,
				 offset,  securityContext,  httpHeaders,
				  httpRequest);

	}

	public Response conversazioniIdConversazioneMessaggiIdMessaggioLettoPut(String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo,
			String collocazioneCodice, String collocazioneDescrizione, String idConversazione, String idMessaggio,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		return conversazioni.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, idConversazione, idMessaggio,
				securityContext, httpHeaders, httpRequest);
	}


	@Override
	public Response conversazioniIdConversazioneMessaggiIdMessaggioPut(String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String xCodiceVerticale,
			 String ruolo,  String collocazioneCodice,  String collocazioneDescrizione,
			String idConversazione, String idMessaggio, PayloadMessaggio1 payloadMessaggio_,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return conversazioneMessaggiPut.execute( shibIdentitaCodiceFiscale,
			 xRequestId,  xForwardedFor,  xCodiceServizio,  xCodiceVerticale,
			  ruolo,   collocazioneCodice,   collocazioneDescrizione,
			 idConversazione,  idMessaggio,  payloadMessaggio_,
			 securityContext,  httpHeaders,  httpRequest);
	}

	@Override
	public Response conversazioniIdConversazioneMessaggiPost(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String xCodiceVerticale,  String ruolo,
			 String collocazioneCodice,  String collocazioneDescrizione, String idConversazione,
			PayloadMessaggio_ payloadMessaggio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return conversazioneMessaggiPost.execute( shibIdentitaCodiceFiscale,  xRequestId,
				 xForwardedFor,  xCodiceServizio,  xCodiceVerticale,   ruolo,
				  collocazioneCodice,   collocazioneDescrizione,  idConversazione,
				 payloadMessaggio,  securityContext,  httpHeaders,
				 httpRequest);
	}
}
