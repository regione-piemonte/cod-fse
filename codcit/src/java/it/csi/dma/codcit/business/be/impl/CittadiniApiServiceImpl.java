/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.codcit.business.be.CittadiniApi;
import it.csi.dma.codcit.dto.PayloadConversazione;
import it.csi.dma.codcit.dto.PayloadMessaggio;
import it.csi.dma.codcit.integration.helper.impl.CittadiniCitIdAbilitazione;
import it.csi.dma.codcit.integration.helper.impl.CittadiniCitIdConversazioni;
import it.csi.dma.codcit.integration.helper.impl.CittadiniCitIdConversazioniGet;
import it.csi.dma.codcit.integration.helper.impl.CittadiniCitIdConversazioniIdConversazioneMessaggiGet;
import it.csi.dma.codcit.integration.helper.impl.CittadiniCitIdConversazioniIdConversazioneMessaggiIdMessaggioPatch;
import it.csi.dma.codcit.integration.helper.impl.CittadiniCitIdConversazioniIdConversazioneMessaggiIdMessaggioPut;
import it.csi.dma.codcit.integration.helper.impl.CittadiniCitIdConversazioniIdConversazioneMessaggiPost;
import it.csi.dma.codcit.integration.helper.impl.CittadiniCitIdInfoMedico;

@Service
public class CittadiniApiServiceImpl implements CittadiniApi {

	@Autowired
	CittadiniCitIdAbilitazione cittadiniCitIdAbilitazione;

	@Autowired
	CittadiniCitIdInfoMedico cittadiniCitIdInfoMedico;

	@Autowired
	CittadiniCitIdConversazioni cittadiniCitIdConversazioni;

	@Autowired
	CittadiniCitIdConversazioniIdConversazioneMessaggiPost cittadiniCitIdConversazioniIdConversazioneMessaggiPost;

	@Autowired
	CittadiniCitIdConversazioniGet cittadiniCitIdConversazioniGet;

	@Autowired
	CittadiniCitIdConversazioniIdConversazioneMessaggiGet cittadiniCitIdConversazioniIdConversazioneMessaggiGet;

	@Autowired
	CittadiniCitIdConversazioniIdConversazioneMessaggiIdMessaggioPatch cittadiniCitIdConversazioniIdConversazioneMessaggiIdMessaggioPatch;

	@Autowired
	CittadiniCitIdConversazioniIdConversazioneMessaggiIdMessaggioPut cittadiniCitIdConversazioniIdConversazioneMessaggiIdMessaggioPut;

	public Response cittadiniCitIdAbilitazioneGet(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String citId, String cfMedico,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		return cittadiniCitIdAbilitazione.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				citId, cfMedico, securityContext, httpHeaders, httpRequest);
	}

	public Response cittadiniCitIdConversazioniGet(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String citId, String solaLettura, Integer limit,
			Integer offset, String cfMedico, String argomento, String idConversazione, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		// do some magic!
		return cittadiniCitIdConversazioniGet.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, citId, solaLettura, limit, offset, cfMedico, argomento, idConversazione,
				securityContext, httpHeaders, httpRequest);
	}

	public Response cittadiniCitIdConversazioniIdConversazioneMessaggiGet(String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String citId, String idConversazione,
			Integer limit, Integer offset, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		return cittadiniCitIdConversazioniIdConversazioneMessaggiGet.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, citId, idConversazione, limit, offset, securityContext, httpHeaders, httpRequest);
	}

	public Response cittadiniCitIdConversazioniIdConversazioneMessaggiIdMessaggioLettoPut(String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String citId, String idConversazione,
			String idMessaggio, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		// do some magic!
		return cittadiniCitIdConversazioniIdConversazioneMessaggiIdMessaggioPatch.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, citId, idConversazione, idMessaggio, securityContext, httpHeaders, httpRequest);
	}

	public Response cittadiniCitIdConversazioniIdConversazioneMessaggiIdMessaggioPut(String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String citId, String idConversazione,
			String idMessaggio, PayloadMessaggio payloadMessaggio, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		return cittadiniCitIdConversazioniIdConversazioneMessaggiIdMessaggioPut.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, citId, idConversazione, idMessaggio, payloadMessaggio, securityContext, httpHeaders, httpRequest);
	}

	public Response cittadiniCitIdConversazioniIdConversazioneMessaggiPost(String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String citId, String idConversazione,
			PayloadMessaggio payloadMessaggio, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		// do some magic!
		return cittadiniCitIdConversazioniIdConversazioneMessaggiPost.execute(shibIdentitaCodiceFiscale,
			xRequestId,xForwardedFor,xCodiceServizio,citId,idConversazione,
			payloadMessaggio,securityContext,httpHeaders,
			httpRequest);
	}

	public Response cittadiniCitIdConversazioniPost(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String citId, PayloadConversazione payloadConversazione,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		// do some magic!
		return cittadiniCitIdConversazioni.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, citId, payloadConversazione, securityContext, httpHeaders, httpRequest);
	}

	public Response cittadiniCitIdInfoMedicoGet(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String citId, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		return cittadiniCitIdInfoMedico.execute(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				citId, securityContext, httpHeaders, httpRequest);

	}
}
