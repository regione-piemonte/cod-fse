/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util;

public class Constants {
	public static final String COMPONENT_NAME = "codcit";
	public static final String COMPONENT_NAME_CAMMEL_CASE = "codCit";
	public static final Boolean COMPONENT_ACTIVE = true;
	public static final String COMPONENT_DESCRIPTION = "contatto digitale per il cittadino v 1.0.0";

	public static final String CODICE_SERVIZIO_SANSOL = "SANSOL";
	public static final String CODICE_VERTICALE ="CITCOD";
	
	public static final String ERRORE_NON_CODIFICATO = "Errore non codificato";
	public static final String ERRORE_NON_CODIFICATO_INTERNO = "Errore non codificato per errore in rimappatura";

	public static final String UTENTE_APPLICATIVO = "CODCIT_APP";
	

	public static final String CONTEXT_CHIAVE_ID = "chiaveid";
	public static final String CONTEXT_TEMPO_PARTENZA = "tempopartenza";

	private Constants() {
		throw new IllegalStateException("Utility class");
	}
}
