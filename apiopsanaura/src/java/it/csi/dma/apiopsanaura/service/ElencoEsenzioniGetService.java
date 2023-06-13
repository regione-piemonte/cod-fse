/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.elencoesenzioni.ElencoEsenzioniSoap;
import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.elencoesenzioni.RequestElencoEsenzioni;
import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.elencoesenzioni.ResponseElencoEsenzioni;
import it.csi.dma.apiopsanaura.util.LoggerUtil;

@Service
public class ElencoEsenzioniGetService extends LoggerUtil {

	@Autowired
	@Qualifier("elencoEsenzioniService")
	ElencoEsenzioniSoap elencoEsenzioniService;


	public ResponseElencoEsenzioni getElencoEsenzioniAura(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String tipoEsenzione, String listaDiagnosi, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
		String methodName = "getElencoEsenzioniAura";
		try {
			
			RequestElencoEsenzioni request = buildRequestElencoEsenzioni(tipoEsenzione,listaDiagnosi);

			ResponseElencoEsenzioni response = elencoEsenzioniService.elencoEsenzioni(request);
			return response;
		} catch (Exception e) {
			logError(methodName, "EXCEPTION: " + e.getCause() + " - " + e.getMessage());
			throw e;
		}
	}

	private RequestElencoEsenzioni buildRequestElencoEsenzioni(String tipoEsenzione, String listaDiagnosi) {
		RequestElencoEsenzioni result = new RequestElencoEsenzioni();
		result.setListaDiagnosi(listaDiagnosi);
		result.setTipoEsen(tipoEsenzione);
		return result;
	}


}