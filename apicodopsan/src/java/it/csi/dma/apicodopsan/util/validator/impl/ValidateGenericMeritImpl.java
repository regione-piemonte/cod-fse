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
import it.csi.dma.apicodopsan.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericMeritImpl extends ValidateGenericImpl {

	/*
	 * SECONDO LIVELLO DI VERIFICA : VERIFICHE FORMALI
	 */

	@Override // VALIDATE: Adesione/Revoca
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadAdesione payloadAdesione, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {

		// CONTROLLI GENERICI
		List<ErroreDettaglio> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, payloadAdesione,
				securityContext, httpHeaders, httpRequest);

		// --- errori del merito ---
		if (!result.isEmpty()) {
			return result;
		}

		return commonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo);
	}

	@Override // VALIDATE: NotificaLetturaMessaggi
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadNotificaLettura payloadNotificaLettura,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws DatabaseException {

		// CONTROLLI GENERICI
		List<ErroreDettaglio> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione,
				payloadNotificaLettura, securityContext, httpHeaders, httpRequest);

		// --- errori del merito ---
		if (!result.isEmpty()) {
			return result;
		}

		return commonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo);
	}

	@Override
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String idConversazione, String idMessaggio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {

		// CONTROLLI GENERICI
		List<ErroreDettaglio> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, idConversazione,
				idMessaggio, securityContext, httpHeaders, httpRequest);

		// --- errori del merito ---
		if (!result.isEmpty()) {
			return result;
		}

		return commonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo);
	}

	@Override // VALIDATE: LetturaMessaggio
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {

		// CONTROLLI GENERICI
		List<ErroreDettaglio> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, securityContext,
				httpHeaders, httpRequest);

		// --- errori del merito ---
		if (!result.isEmpty()) {
			return result;
		}

		return commonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo);
	}

	@Override // VALIDATE: ListaAssisititi
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer limit, Integer offset, String stato, String cognome, String nome,
			String codiceFiscale, Integer etaMin, Integer etaMax, String sesso, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {

		List<ErroreDettaglio> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, limit, offset,
				stato, cognome, nome, codiceFiscale, etaMin, etaMax, sesso, securityContext, httpHeaders, httpRequest);

		// --- errori del merito ---
		if (!result.isEmpty()) {
			return result;
		}

		result = commonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio, xCodiceVerticale,
				ruolo);

		// Check errori specifici
		if (formalCheckLimit(limit)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_062.getCode(), ErrorParamEnum.LIMIT.getCode(),
					limit+""));
		}

		if (formalCheckOffset(offset)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_063.getCode(), ErrorParamEnum.OFFSET.getCode(),
					offset+""));
		}

		if (formalCheckStato(stato)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_068.getCode(), ErrorParamEnum.STATO.getCode(),
					stato));
		}

		if (codiceFiscale != null && formalCheckCFFilter(codiceFiscale)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_090.getCode(), ErrorParamEnum.CODICE_FISCALE.getCode(),
					codiceFiscale));
		}

		if (formalCheckCognome(cognome)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_065.getCode(), ErrorParamEnum.COGNOME.getCode(),
					cognome));
		}

		if (formalCheckNome(nome)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_069.getCode(), ErrorParamEnum.NOME.getCode(),
					nome));
		}

		if (formalCheckSesso(sesso)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_070.getCode(), ErrorParamEnum.SESSO.getCode(),
					sesso));
		}

//		if (formalCheckEtaMin(etaMin)) {
//			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_071.getCode(), ErrorParamEnum.ETA_MINIMA.getCode(),
//					etaMin+""));
//		}
//
//		if (formalCheckEtaMax(etaMin, etaMax)) {
//			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_072.getCode(), ErrorParamEnum.ETA_MASSIMA.getCode(),
//					etaMax+""));
//		}

		return result;
	}

	@Override // VALIDATE: ListaConversazioni
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer limit, Integer offset, String solaLettura, String cognome,
			String codiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {

		List<ErroreDettaglio> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione, limit, offset,
				solaLettura, cognome, codiceFiscale, securityContext, httpHeaders, httpRequest);

		// --- errori del merito ---
		if (!result.isEmpty()) {
			return result;
		}

		result = commonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio, xCodiceVerticale,
				ruolo);

		// Check errori specifici
		if (formalCheckLimit(limit)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_062.getCode(), ErrorParamEnum.LIMIT.getCode(),
					limit+""));
		}

		if (formalCheckOffset(offset)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_063.getCode(), ErrorParamEnum.OFFSET.getCode(),
					offset+""));
		}
		
		if (formalCheckSolaLettura(solaLettura)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_064.getCode(), ErrorParamEnum.SOLA_LETTURA.getCode(),
					solaLettura));
		}
		
		if (codiceFiscale != null && !codiceFiscale.trim().isEmpty() && formalCheckCFFilter(codiceFiscale)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_090.getCode(), ErrorParamEnum.CODICE_FISCALE.getCode(),
					codiceFiscale));
		}

		if (cognome != null && !cognome.trim().isEmpty() && formalCheckCognome(cognome)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_065.getCode(), ErrorParamEnum.COGNOME.getCode(),
					cognome));
		}
		
		return result;
	}

	// CHECK ERRORI COMUNI
	private List<ErroreDettaglio> commonCheck(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo)
			throws DatabaseException {

		if (formalCheckCF(shibIdentitaCodiceFiscale)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(),
					ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode(), shibIdentitaCodiceFiscale));
		}

		if (formalCheckCodiceVerticale(xCodiceVerticale)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(),
					ErrorParamEnum.X_CODICE_VERTICALE.getCode(), xCodiceVerticale));
		}

		if (formalCheckCodiceServizio(xCodiceServizio)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(),
					ErrorParamEnum.X_CODICE_SERVIZIO.getCode(), xCodiceServizio));
		}

		if (formalCheckPresenzaRuolo(ruolo, xCodiceServizio)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(), ErrorParamEnum.RUOLO.getCode(), ruolo));
		}

		return result;

	}

}
