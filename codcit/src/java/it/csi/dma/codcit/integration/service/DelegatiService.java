/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.csi.deleghe.deleghebe.ws.DelegheCittadiniService;
import it.csi.deleghe.deleghebe.ws.model.Cittadino;
import it.csi.deleghe.deleghebe.ws.model.DelegaCittadino;
import it.csi.deleghe.deleghebe.ws.model.DelegaServizio;
import it.csi.deleghe.deleghebe.ws.msg.GetDelegati;
import it.csi.deleghe.deleghebe.ws.msg.GetDelegatiResponse;
import it.csi.dma.codcit.dto.custom.Soggetto;
import it.csi.dma.codcit.integration.service.util.GradoDelega;
import it.csi.dma.codcit.integration.service.util.StatoDelega;
import it.csi.dma.codcit.util.LoggerUtil;

@Component
public class DelegatiService extends LoggerUtil {

	public static final String DELEGHE_COD_PREFIX = "DELEGHE-COD-";

	@Autowired
	@Qualifier("delegheCittadiniService")
	DelegheCittadiniService delegheCittadiniService;

	@Value("${delegheCodiceServizio}")
	private String codiceServizio;

	public List<Soggetto> cercaDelegati(String xRequestID, String xCodiceServizio, String shibIdentitaCodiceFiscale,
			String cf) {
		final String METHOD_NAME = "cercaDelegati";
		logInfo(METHOD_NAME, "CF SOG"+cf);
		List<Soggetto> result=null;
		try {
			result = new ArrayList<Soggetto>();
			GetDelegati req = new GetDelegati();
			it.csi.deleghe.deleghebe.ws.model.Richiedente richiedente = new it.csi.deleghe.deleghebe.ws.model.Richiedente(
					xRequestID, xCodiceServizio, shibIdentitaCodiceFiscale);
			req.setRichiedente(richiedente);
			Cittadino cittDelegante = new Cittadino();
			cittDelegante.setCodiceFiscale(cf);
			req.setCittadinoDelegante(cittDelegante);
			req.setCodiciServizio(Arrays.asList(codiceServizio));
			req.setStatiDelega(Arrays.asList(StatoDelega.ATTIVA.toString()));
//			Cittadino cittDelegato = new Cittadino();
////			cittDelegato.setCodiceFiscale(cf);
//			req.setCittadinoDelegato(cittDelegato);
//
			GetDelegatiResponse gdResp=null;
			gdResp = delegheCittadiniService.getDelegatiService(req);
			Soggetto tmp;
			if (it.csi.deleghe.deleghebe.ws.model.RisultatoCodice.SUCCESSO.equals(gdResp.getEsito())) {
				for (DelegaCittadino delega : gdResp.getDelegati()) {
					if (checkStatoDelegaIsCompliance(delega.getDeleghe())) {
						tmp =   new Soggetto();
						tmp.setSoggettoCf(delega.getCodiceFiscale());
						tmp.setSoggettoNome(delega.getNome());
						tmp.setSoggettoCognome(delega.getCognome());
						result.add(tmp);
					}
				}

			}
		}catch(Exception e) {
			logError(METHOD_NAME, "EXCEPTION impossibile recuperare i delegati:"+e.getCause() +" - "+e.getMessage());
		}
	
		
		return result;
	}

	private boolean checkStatoDelegaIsCompliance(List<DelegaServizio> deleghe) {
		for (DelegaServizio delegaServizio : deleghe) {
			if (codiceServizio.equals(delegaServizio.getCodiceServizio())
					&& StatoDelega.ATTIVA.toString().equals(delegaServizio.getStatoDelega())
					&& GradoDelega.FORTE.toString().equalsIgnoreCase(delegaServizio.getGradoDelega())
					&& delegaServizio.getDataInizioDelega().compareTo(new Date()) <= 0
					&& (
					delegaServizio.getDataFineDelega()==null ||
					delegaServizio.getDataFineDelega().compareTo(new Date()) >= 0)
					) {
				return true;
			}
		}
		return false;
	}

}
