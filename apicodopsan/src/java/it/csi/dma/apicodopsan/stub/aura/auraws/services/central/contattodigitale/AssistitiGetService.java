/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.stub.aura.auraws.services.central.contattodigitale;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.util.LoggerUtil;

@Service
public class AssistitiGetService extends LoggerUtil {

	@Autowired
	@Qualifier("contattoDigitaleService")
	ContattoDigitaleSoap contattoDigitaleService;

	public ResponseContattoDigitale getAssistitiAura(String shibIdentitaCodiceFiscale, Integer nPagina)
			throws Exception {
		String methodName = "getAssistitiAura";
		try {

			RequestContattoDigitale request = new RequestContattoDigitale();

			request.setCodFiscMed(shibIdentitaCodiceFiscale);
			// only for test massivo cf da usere per avere + di una pagina BNGFNC54E03A859D
			// request.setCodFiscMed("BNGFNC54E03A859D");
			// test per errore gestito
			// request.setEtaMax(new BigDecimal(5));

			request.setPageNumber(new BigDecimal(1));
			ResponseContattoDigitale response = contattoDigitaleService.contattoDigitale(request);
			// TODO ottimizzare la richiesta con futureCall
			if (response != null && response.totalPage != null && response.getTotalPage().intValue() > 1) {
				for (int i = 2; i <= response.getTotalPage().intValue(); i++) {
					request.setPageNumber(new BigDecimal(i));
					ResponseContattoDigitale response_i = contattoDigitaleService.contattoDigitale(request);
					if (response != null && StringUtils.isNotEmpty(response.errorCode)) {// verifico che non vada in
																							// errore nessuna chiamata
																							// interna
						log.error("Errore nel richiamare AURA");
						throw new Exception("Errore nel loop per richiamare AURA indice:" + i);
					} else {
						response.elencoAssistiti.addAll(response_i.getElencoAssistiti());
					}
				}
			} else if (response != null && StringUtils.isNotEmpty(response.errorCode)) {// verifico che non sia andato
																						// in errore
				log.error("Errore nel richiamare AURA");
				throw new Exception("Errore nella primma chiamata ad AURA");
			} // TODO bisogna gestire altri errori?

			return response;
		} catch (Exception e) {
			logError(methodName, "EXCEPTION: " + e.getCause() + " - " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

}
