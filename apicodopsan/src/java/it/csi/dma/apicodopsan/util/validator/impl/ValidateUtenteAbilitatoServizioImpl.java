/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.validator.ValidateUtenteAbilitatoServizio;
import it.csi.dma.apicodopsan.verificaservices.dmacc.Errore;
import it.csi.dma.apicodopsan.verificaservices.dmacc.RisultatoCodice;
import it.csi.dma.apicodopsan.verificaservices.dmacc.VerificaUtenteAbilitatoRequest;
import it.csi.dma.apicodopsan.verificaservices.dmacc.VerificaUtenteAbilitatoResponse;
/**
 * Verifica utente abilitato estratto dalla classe di validationeGerericMerit
 * @author 2132
 *
 */
@Service
public class ValidateUtenteAbilitatoServizioImpl extends BaseValidate implements ValidateUtenteAbilitatoServizio {


	@Override
	public List<ErroreDettaglio> validate(String xCodiceVerticale, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, String xForwardedFor, String xRequestId, String ruolo)
			throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();
		VerificaUtenteAbilitatoRequest verificaUtenteAbilitato = createVerificaUtenteAbilitatoRequest(
				xCodiceVerticale, xCodiceServizio, shibIdentitaCodiceFiscale, xForwardedFor, xRequestId, ruolo);

		VerificaUtenteAbilitatoResponse  verificaUtenteAbilitatoResp = functionalCheckUtenteAbilitato(verificaUtenteAbilitato);
		
        if(verificaUtenteAbilitatoResp.getEsito().equals(RisultatoCodice.FALLIMENTO)) {
        	List<Errore> wsErrors  =verificaUtenteAbilitatoResp.getErrori();
        
        	for(Errore wsError : wsErrors) {
        		ErroreDettaglio erroreDettaglio = new ErroreDettaglio();
        		erroreDettaglio.setChiave(wsError.getCodice());
        		erroreDettaglio.setValore(wsError.getDescrizione());
        		result.add(erroreDettaglio);
        	}	
        }
        

		return result;
	}
	
/*	private VerificaUtenteAbilitatoRequest createVerificaUtenteAbilitatoRequest(String xCodiceVerticale,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, String xForwardedFor, String xRequestId,
			String ruolo) {
		// â¢ numero di transazione= x-Request-Id ***********
		// â¢ CF richiedente= Shib-Identita-CodiceFiscale ***********
		// â¢ Codice ruolo=ruolo ??????
		// â¢ Codice Applicazione richiedente= x-Codice-Servizio ***********
		// â¢ Codice Regime di operativitÃ =AMB ????????
		// â¢ Applicativo Verticale.codice= x-Codice-Verticale ***********
		// â¢ Ip=primo ip di x-Forwarded-for ***********
		// â¢ cfUtente= Shib-Identita-CodiceFiscale ????????
		// â¢ ruoloUtente=ruolo ***********

		VerificaUtenteAbilitatoRequest verificaUtenteAbilitatoRequest = new VerificaUtenteAbilitatoRequest();
		RichiedenteInfo richiedenteInfo = new RichiedenteInfo();
		ApplicativoVerticale applicativoVerticale = new ApplicativoVerticale();
		ApplicazioneRichiedente applicazioneRichiedente = new ApplicazioneRichiedente();
		Ruolo ruoloWs = new Ruolo();

		applicativoVerticale.setCodice(xCodiceVerticale);
		richiedenteInfo.setApplicativoVerticale(applicativoVerticale);

		applicazioneRichiedente.setCodice(xCodiceServizio);
		richiedenteInfo.setApplicazione(applicazioneRichiedente);

		ruoloWs.setCodice(ruolo);
		richiedenteInfo.setRuolo(ruoloWs);

		richiedenteInfo.setCodiceFiscale(shibIdentitaCodiceFiscale);
		richiedenteInfo.setIp(xForwardedFor);
		richiedenteInfo.setNumeroTransazione(xRequestId);
		verificaUtenteAbilitatoRequest.setRichiedente(richiedenteInfo);

		return verificaUtenteAbilitatoRequest;
	}*/
	
}
