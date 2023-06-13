/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.PayloadAdesione;
import it.csi.dma.apicodopsan.dto.PayloadNotificaLettura;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.verificaservices.dmacc.Errore;
import it.csi.dma.apicodopsan.verificaservices.dmacc.RisultatoCodice;
import it.csi.dma.apicodopsan.verificaservices.dmacc.VerificaUtenteAbilitatoRequest;
import it.csi.dma.apicodopsan.verificaservices.dmacc.VerificaUtenteAbilitatoResponse;

@Service
public class ValidateGenericMeritWhitMedicoImpl extends ValidateGenericMeritImpl {

	/*
	 * TERZO LIVELLO DI VERIFICA : VERIFICA FUNZIONALE
	 */
	@Override // VALIDATE: Adesione/Revoca
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadAdesione payloadAdesione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {

		List<ErroreDettaglio> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, payloadAdesione,
				securityContext, httpHeaders, httpRequest);

		if (!result.isEmpty()) {
			return result;
		}

		if (functionalCheckAdesione(shibIdentitaCodiceFiscale, payloadAdesione.isAdesione())) {
			if (payloadAdesione.isAdesione()) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_060.getCode(), null));
			}

			if (!payloadAdesione.isAdesione()) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_061.getCode(), null));
			}
		}

		/*
		 * Richiamo servizio verifica utente abilitato
		 */
		 
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

	@Override // VALIDATE: NotificaLetturaMessaggi
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadNotificaLettura payloadNotificaLettura,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws DatabaseException {

		List<ErroreDettaglio> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione,
				payloadNotificaLettura, securityContext, httpHeaders, httpRequest);

		if (!result.isEmpty()) {
			return result;
		}

		/*
		 * Richiamo servizio verifica utente abilitato
		 */
		 
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

	@Override // VALIDATE: LetturaMessaggio
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String idConversazione, String idMessaggio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {

		List<ErroreDettaglio> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, idConversazione,
				idMessaggio, securityContext, httpHeaders, httpRequest);

		if (!result.isEmpty()) {
			return result;
		}

		/*
		 * Richiamo servizio verifica utente abilitato
		*/
		 
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

	@Override
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws DatabaseException {

		List<ErroreDettaglio> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, securityContext,
				httpHeaders, httpRequest);

		if (!result.isEmpty()) {
			return result;
		}

		/*
		 * Richiamo servizio verifica utente abilitato
		 */
		 
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

	@Override // VALIDATE: ListaAssisititi
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer limit, Integer offset, String stato, String cognome, String nome,
			String codiceFiscale, Integer etaMin, Integer etaMax, String sesso, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = super.validate(shibIdentitaCodiceFiscale, xRequestId,
				xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
				collocazioneDescrizione, limit, offset, stato, cognome, nome, codiceFiscale, etaMin, etaMax, sesso,
				securityContext, httpHeaders, httpRequest);

		// --- errori del merito ---
		if (!result.isEmpty()) {
			return result;
		}
		
		/*
		 * Richiamo servizio verifica utente abilitato
		*/
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
	
		 //Richiamo servizio verifica utente abilitato
		return result;
	}

	@Override //VALIDATE: ListaConversazioni
	public List<ErroreDettaglio>  validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer limit, Integer offset, String solaLettura, String cognome,
			String codiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException{
		
		List<ErroreDettaglio> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, limit, offset,
				solaLettura, cognome, codiceFiscale, securityContext, httpHeaders, httpRequest);

		// --- errori del merito ---
		if (!result.isEmpty()) {
			return result;
		}
		
		/*
		 * Richiamo servizio verifica utente abilitato
		*/
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
