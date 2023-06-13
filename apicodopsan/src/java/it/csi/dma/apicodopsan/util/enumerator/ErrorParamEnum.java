/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.enumerator;

public enum ErrorParamEnum {
	
	X_REQUEST_ID("x-Request-Id"),
	SHIB_IDENTITA_CODICEFISCALE("Shib-Identita-CodiceFiscale"),
	X_FORWARDED_FOR("x-Forwarded-for"),
	X_CODICE_SERVIZIO("x-Codice-Servizio"),
	X_CODICE_VERTICALE("x-Codice-Verticale"),
	RUOLO("Ruolo"),
	COLLOCAZIONE_CODICE("Collocazione_codice"),
	COLLOCAZIONE_DESCRIZIONE("Collocazione_descrizione"),
	ID_CONVERSAZIONE("Id_Conversazione"),
	ID_MESSAGGIO("Id_Messaggio"),
	ADESIONE("Adesione"),
	NOME_MEDICO("Nome_medico"),
	COGNOME_MEDICO("Cognome_medico"),
	LIMIT("Limit"),
	OFFSET("Offset"),
	STATO("Stato"),
	COGNOME("Cognome"),
	CODICE_FISCALE("Codice_Fiscale"),
	NOME("Nome"),
	SESSO("Sesso"),
	ETA_MINIMA("Eta_Minima"),
	ETA_MASSIMA("Eta_Massima"),
	SOLA_LETTURA("Sola_Lettura"),
	FATAL_ERROR("Fatal_Error"),
	PAYLOAD_ABILITAZIONE("Payload_Abilitazione"),
	ABILITAZIONE("Abilitazione"),
	LISTA_ASSISTITI("Lista_Assistiti"),
	MOTIVAZIONE_MEDICO("Motivazione_Medico"),
	ADESIONE_MEDICO("Adesione_Medico"), 
	TESTO_MESSAGGIO("Testo_messaggio"), 
	NOME_MEDICO_MESSAGGIO("Messaggio_Nome_Medico "),
	COGNOME_MEDICO_MESSAGGIO("Messaggio_Cognome_Medico"),
	MESSAGGIO_NON_PRESENTE("Messaggio non presente"),
	MESSAGGIO_NON_PROPRIETARIO("Messaggio non proprietario"),
	MESSAGGIO_NON_MODIFICABILE("Messaggio non modificabile"),
	ID_DOCUMENTO("id documento"),
	COD_CL("Cod cl");
	
	private final String code;
	
	ErrorParamEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
