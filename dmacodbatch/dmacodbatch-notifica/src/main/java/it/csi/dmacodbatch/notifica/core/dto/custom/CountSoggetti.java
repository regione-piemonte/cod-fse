/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.notifica.core.dto.custom;

public class CountSoggetti {
	
	Integer soggettiAbilitati;
	Integer soggettiDisabilitati;
	
	
	public Integer getSoggettiAbilitati() {
		return soggettiAbilitati;
	}
	public void setSoggettiAbilitati(Integer soggettiAbilitati) {
		this.soggettiAbilitati = soggettiAbilitati;
	}
	public Integer getSoggettiDisabilitati() {
		return soggettiDisabilitati;
	}
	public void setSoggettiDisabilitati(Integer soggettiDisabilitati) {
		this.soggettiDisabilitati = soggettiDisabilitati;
	}
	
	
}
