/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.entity;

import java.io.Serializable;

public class ModelAssistito implements Serializable{

	private String cf;
	private String stato;
	
	@Override
	public String toString() {
		return "ModelAssistito [cf=" + cf + ", stato=" + stato + "]";
	}
	public ModelAssistito() {
		super();
	}
	public ModelAssistito(String cf, String stato) {
		super();
		this.cf = cf;
		this.stato = stato;
	}
	public String getCf() {
		return cf;
	}
	public void setCf(String cf) {
		this.cf = cf;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	
}
