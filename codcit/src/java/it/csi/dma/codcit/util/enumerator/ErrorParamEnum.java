/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.enumerator;

public enum ErrorParamEnum {
	
	X_REQUEST_ID("x-Request-Id"),
	SHIB_IDENTITA_CODICEFISCALE("Shib-Identita-CodiceFiscale"),
	X_FORWARDED_FOR("x-Forwarded-for"),
	X_CODICE_SERVIZIO("x-Codice-Servizio"),
	APPLICAZIONE("applicazione"),
	CF_MEDICO("cf_medico"),
	CIT_ID("cit_id"),
	MEDICO("Medico"),
	MEDICO_CF_MEDICO("Medico.codice_fiscale"),
	MEDICO_COGNOME("Medico.cognome"),
	MEDICO_NOME("Medico.nome"),
	ARGOMENTO("argomento"),
	AUTORE("Autore"),
	AUTORE_CODICE_FISCALE("Autore.codice_fiscale"),
	AUTORE_NOME("Autore.nome"),
	AUTORE_COGNOME("Autore.cognome"),
	SOLA_LETTURA("sola_lettura"),
	LIMIT("limit"),
	OFFSET("offset"),
	EXAMPLE("ESEMPIO"),
	ID_CONVERSAZIONE("Id_conversazione"),
	MESSAGGIO("Messaggio");

	private final String code;
	
	ErrorParamEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
