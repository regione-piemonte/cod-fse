/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.service.util;

public enum StatoConsensi {
	CODICE("PERSONAL"),
	TIPO_ATTIVITA("READ"),
	STRUTTURA_UTENTE("------"),
	IDENTIFICATIVO_ORGANIZZAZIONE("010"),
	REGIME("AMB"),
	RUOLO("CIT");
	
	private String stato;
	
	StatoConsensi(String stato){
		this.stato = stato;
	}
	
	public String toString() {
		return stato;
	}
}
