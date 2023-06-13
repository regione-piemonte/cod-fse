/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.PayloadAdesione;
import it.csi.dma.apicodopsan.dto.PayloadNotificaLettura;
import it.csi.dma.apicodopsan.exception.DatabaseException;

public interface ValidateGeneric {

	//VALIDATE: Adesione/Revoca
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadAdesione payloadAdesione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException;

	//VALIDATE: NotificaLetturaMessaggi
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadNotificaLettura payloadNotificaLettura,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws DatabaseException;
	
	//VALIDATE: LetturaMessaggio
		public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
				String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
				String collocazioneDescrizione, String idConversazione, String idMessaggio,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
				throws DatabaseException;

	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws DatabaseException;
	
	//VALIDATE: ListaAssistiti
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer limit, Integer offset, String stato, String cognome, String nome,
			String codiceFiscale, Integer etaMin, Integer etaMax, String sesso, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException;
	
	//VALIDATE: ListaConversazioni
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer limit, Integer offset, String solaLettura, String cognome,
			String codiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException;
}
