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

import it.csi.dma.apiopsanaura.business.be.EsenzioniPatologiaApi;
import it.csi.dma.apiopsanaura.integration.helper.imp.ElencoEsenzioniMedicoGet;
@Service
public class EsenzioniPatologiaApiServiceImpl implements EsenzioniPatologiaApi {

	@Autowired
	ElencoEsenzioniMedicoGet elencoEsenzioniMedicoGet;
	
	public Response getListaEsenzioniPatologia(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo,
			String collocazioneCodice, String collocazioneDescrizione, String tipoEsenzione, String listaDiagnosi,
			SecurityContext securityContext, HttpHeaders httpHeaders,  HttpServletRequest httpRequest) {
		
		return elencoEsenzioniMedicoGet.execute( shibIdentitaCodiceFiscale,  xRequestId,
			 xForwardedFor,  xCodiceServizio,  xCodiceVerticale,  ruolo,
			 collocazioneCodice,  collocazioneDescrizione,  tipoEsenzione,  listaDiagnosi,
			 securityContext,  httpHeaders,   httpRequest);
	}
}
