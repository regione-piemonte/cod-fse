/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.service;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.contattodigitale.ContattoDigitaleSoap;
import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.contattodigitale.RequestContattoDigitale;
import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.contattodigitale.ResponseContattoDigitale;
import it.csi.dma.apiopsanaura.util.LoggerUtil;

@Service
public class AssistitiGetService extends LoggerUtil {

	
	@Autowired
	@Qualifier("contattoDigitaleService")
	ContattoDigitaleSoap contattoDigitaleService;


	public ResponseContattoDigitale getAssistitiAura(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo,
			String collocazioneCodice, String collocazioneDescrizione, Integer nPagina, String codFiscAss,
			String cognome, String nome, Integer etaMin, Integer etaMax, String sesso, String esenzionePatologia,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
		String methodName = "getAssistitiAura";
		try {

			RequestContattoDigitale request = buildRequestContattoDigitale(shibIdentitaCodiceFiscale, nPagina,
					codFiscAss, cognome, nome, etaMin, etaMax, sesso, esenzionePatologia);

			ResponseContattoDigitale response = contattoDigitaleService.contattoDigitale(request);
			return response;
		} catch (Exception e) {
			logError(methodName, "EXCEPTION: " + e.getCause() + " - " + e.getMessage());
			throw e;
		}
	}

	private RequestContattoDigitale buildRequestContattoDigitale(String shibIdentitaCodiceFiscale, Integer nPagina,
			String codFiscAss, String cognome, String nome, Integer etaMin, Integer etaMax, String sesso,
			String esenzionePatologia) {
		RequestContattoDigitale request = new RequestContattoDigitale();
		request.setCodFiscMed(shibIdentitaCodiceFiscale);
		request.setCodFiscAss(codFiscAss);
		request.setCognome(cognome);
		request.setNome(nome);
		if (etaMax != null && etaMax >= 0) {
			request.setEtaMax(BigDecimal.valueOf(etaMax));
		}
		if (etaMin != null && etaMin >=0) {
			request.setEtaMin(BigDecimal.valueOf(etaMin));
		}
		request.setPageNumber(BigDecimal.valueOf(nPagina));
		request.setPatologia(esenzionePatologia);
		request.setSesso(sesso);
		return request;
	}

}