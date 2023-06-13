/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.apiopsanaura.business.be.AssistitiApi;
import it.csi.dma.apiopsanaura.integration.helper.imp.AssistitiMedicoAuraGet;
@Service
public class AssistitiApiServiceImpl implements AssistitiApi {

	@Autowired
	AssistitiMedicoAuraGet assistitiMedicoAuraGet;

	public Response getAssistiti(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer nPagina, String codFiscAss, String cognome, String nome,
			Integer etaMin, Integer etaMax, String sesso, String esenzionePatologia, SecurityContext securityContext,
			HttpHeaders httpHeaders,  HttpServletRequest httpRequest) {
		// do some magic!
		return assistitiMedicoAuraGet.execute( shibIdentitaCodiceFiscale,  xRequestId,  xForwardedFor,
				 xCodiceServizio,  xCodiceVerticale,  ruolo,  collocazioneCodice,
				 collocazioneDescrizione,  nPagina,  codFiscAss,  cognome,  nome,
				 etaMin,  etaMax,  sesso,  esenzionePatologia,  securityContext,
				 httpHeaders,   httpRequest);
	}
}
