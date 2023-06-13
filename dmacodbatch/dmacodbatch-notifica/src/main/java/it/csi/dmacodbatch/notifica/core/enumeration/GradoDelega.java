/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.notifica.core.enumeration;

public enum GradoDelega {
	
	FORTE("FORTE"),
	DEBOLE("DEBOLE");
	
	
	private String stato;
	
	GradoDelega(String stato){
		this.stato = stato;
	}
	
	public String toString() {
		return stato;
	}
}
