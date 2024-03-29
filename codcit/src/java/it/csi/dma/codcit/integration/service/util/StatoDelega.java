/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.service.util;

public enum StatoDelega {
	ATTIVA("ATTIVA"),
	INSCADENZA("IN SCADENZA"),
	REVOCATA("REVOCATA"),
	RIFIUTATA("RIFIUTATA"),
	SCADUTA("SCADUTA");
	
	
	private String stato;
	
	StatoDelega(String stato){
		this.stato = stato;
	}
	
	public String toString() {
		return stato;
	}
}
