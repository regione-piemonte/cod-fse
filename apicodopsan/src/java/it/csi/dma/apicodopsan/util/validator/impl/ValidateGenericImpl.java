/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.util.StringUtils;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.PayloadAdesione;
import it.csi.dma.apicodopsan.dto.PayloadNotificaLettura;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.ErrorParamEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateGeneric;

public class ValidateGenericImpl extends BaseValidate implements ValidateGeneric {
	/*
	 * PRIMO LIVELLO DI VERIFICA : CAMPI OBBLIGATORI
	 */

	@Override // VALIDATE: Adesione/Revoca
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadAdesione payloadAdesione, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");
		/*
		 * 2. Verifica parametri in input (Criteri di validazione della richiesta) 2a)
		 * ObbligatorietÃ 
		 * 
		 * Check errori comuni
		 */
		List<ErroreDettaglio> result = new ArrayList<>();
		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);

		// Check errori specifici
		if (StringUtils.isEmpty(payloadAdesione.getCognomeMedico())|| payloadAdesione.getCognomeMedico() == null) {
			result.add(
					getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), ErrorParamEnum.COGNOME_MEDICO.getCode()));
		}

		if (StringUtils.isEmpty(payloadAdesione.getNomeMedico()) || payloadAdesione.getNomeMedico() == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), ErrorParamEnum.NOME_MEDICO.getCode()));
		}

		if (payloadAdesione.isAdesione() == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), ErrorParamEnum.ADESIONE.getCode()));
		}
		return result;
	}

	@Override // VALIDATE: NotificaLetturaMessaggi
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadNotificaLettura payloadNotificaLettura,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");
		/*
		 * 2. Verifica parametri in input (Criteri di validazione della richiesta) 2a)
		 * ObbligatorietÃ 
		 * 
		 * Check errori comuni
		 */
		List<ErroreDettaglio> result = new ArrayList<>();
		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);

		// Check errori specifici

		if (payloadNotificaLettura.isNotificaLettura() == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), ErrorParamEnum.ADESIONE.getCode()));
		}

		return result;
	}

	@Override // VALIDATE: LetturaMessaggio
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String idConversazione, String idMessaggio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");

		/*
		 * 2. Verifica parametri in input (Criteri di validazione della richiesta) 2a)
		 * ObbligatorietÃ 
		 * 
		 * Check errori comuni
		 */

		List<ErroreDettaglio> result = new ArrayList<>();
		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);

		// Check errori specifici
		if (StringUtils.isEmpty(idConversazione) || idConversazione == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.ID_CONVERSAZIONE.getCode()));
		}

		if (StringUtils.isEmpty(idMessaggio) || idMessaggio == null) {
			result.add(
					getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), ErrorParamEnum.ID_MESSAGGIO.getCode()));
		}

		return result;
	}

	@Override
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");

		/*
		 * 2. Verifica parametri in input (Criteri di validazione della richiesta) 2a)
		 * ObbligatorietÃ 
		 * 
		 * Check errori comuni
		 */

		List<ErroreDettaglio> result = new ArrayList<>();
		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);

		return result;
	}

	@Override //VALIDATE: ListaAssisititi
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer limit, Integer offset, String stato, String cognome, String nome,
			String codiceFiscale, Integer etaMin, Integer etaMax, String sesso, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");

		/*
		 * 2. Verifica parametri in input (Criteri di validazione della richiesta) 2a)
		 * ObbligatorietÃ 
		 * 
		 * Check errori comuni
		 */

		List<ErroreDettaglio> result = new ArrayList<>();
		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);

		// Check errori specifici
		if (limit == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.LIMIT.getCode()));
		}
		
		if (offset == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.OFFSET.getCode()));
		}
		
		if (StringUtils.isEmpty(stato) || stato.equals(null)) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.STATO.getCode()));
		}

		return result;
	}
	
	@Override //VALIDATE: ListaConversazioni
	public List<ErroreDettaglio>  validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer limit, Integer offset, String solaLettura, String cognome,
			String codiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		
		var methodName = "validate";
		logInfo(methodName, "BEGIN");

		/*
		 * 2. Verifica parametri in input (Criteri di validazione della richiesta) 2a)
		 * ObbligatorietÃ 
		 * 
		 * Check errori comuni
		 */

		List<ErroreDettaglio> result = new ArrayList<>();
		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);

		// Check errori specifici
		if (limit == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.LIMIT.getCode()));
		}
		
		if (offset == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.OFFSET.getCode()));
		}
		
		if (StringUtils.isEmpty(solaLettura) || solaLettura.equals(null)) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.SOLA_LETTURA.getCode()));
		}
		
		return result;
		
	}

	// METODI PRIVATI
	private List<ErroreDettaglio> commonCheck(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo,
			String collocazioneCodice, String collocazioneDescrizione) throws DatabaseException {

		if (StringUtils.isEmpty(xRequestId) || xRequestId == null) {
			result.add(
					getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), ErrorParamEnum.X_REQUEST_ID.getCode()));
		}

		if (StringUtils.isEmpty(shibIdentitaCodiceFiscale) || shibIdentitaCodiceFiscale == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode()));
		}

		if (StringUtils.isEmpty(xForwardedFor) || xForwardedFor == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.X_FORWARDED_FOR.getCode()));
		}

		if (StringUtils.isEmpty(xCodiceServizio) || xCodiceServizio == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.X_CODICE_SERVIZIO.getCode()));
		}

		if (StringUtils.isEmpty(collocazioneDescrizione) || collocazioneDescrizione == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.COLLOCAZIONE_DESCRIZIONE.getCode()));
		}

		if (StringUtils.isEmpty(collocazioneCodice) || collocazioneCodice == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.COLLOCAZIONE_CODICE.getCode()));
		}

		if (StringUtils.isEmpty(ruolo) || ruolo == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), ErrorParamEnum.RUOLO.getCode()));
		}

		if (StringUtils.isEmpty(xCodiceVerticale) || xCodiceVerticale == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.X_CODICE_VERTICALE.getCode()));
		}

		return result;
	}
}
