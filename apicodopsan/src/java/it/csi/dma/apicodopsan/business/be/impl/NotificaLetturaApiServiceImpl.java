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

import it.csi.dma.apicodopsan.business.be.NotificaLetturaApi;
import it.csi.dma.apicodopsan.dto.PayloadNotificaLettura;
import it.csi.dma.apicodopsan.integration.helper.impl.NotificaLettura;

@Component
public class NotificaLetturaApiServiceImpl implements NotificaLetturaApi {
	
	@Autowired
	NotificaLettura notificaLetturaRequest;
	
	public Response notificaLetturaPut(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadNotificaLettura payloadNotificaLettura,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		// do some magic!
		return notificaLetturaRequest.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione,
				payloadNotificaLettura, securityContext, httpHeaders, httpRequest);
	}
}
