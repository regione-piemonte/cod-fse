/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.entity;

import java.io.Serializable;

public class RispostaFunzione implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5716392479079405492L;
	
	
	public RispostaFunzione() {
		super();
	}
	public RispostaFunzione(Integer codice, String descrizione) {
		super();
		this.codice = codice;
		this.descrizione = descrizione;
	}
	
	
	@Override
	public String toString() {
		return "RispostaFunzione [codice=" + codice + ", descrizione=" + descrizione + "]";
	}


	private Integer codice;
	private String descrizione;
	public Integer getCodice() {
		return codice;
	}
	public void setCodice(Integer codice) {
		this.codice = codice;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
