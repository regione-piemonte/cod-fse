/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto.custom;

import java.sql.Date;

public class SoggettoAbilitato {

	Integer abilitazioneId;
	Integer soggettoIdAbilitato;
	Integer soggettoIdAbilitante;
	Date abilitazioneInizio;
	Date dataCreazione;
	Date dataModifica;
	String utenteCreazione;
	String utenteModifica;
	
	public Integer getAbilitazioneId() {
		return abilitazioneId;
	}
	public void setAbilitazioneId(Integer abilitazioneId) {
		this.abilitazioneId = abilitazioneId;
	}
	public Integer getSoggettoIdAbilitato() {
		return soggettoIdAbilitato;
	}
	public void setSoggettoIdAbilitato(Integer soggettoIdAbilitato) {
		this.soggettoIdAbilitato = soggettoIdAbilitato;
	}
	public Integer getSoggettoIdAbilitante() {
		return soggettoIdAbilitante;
	}
	public void setSoggettoIdAbilitante(Integer soggettoIdAbilitante) {
		this.soggettoIdAbilitante = soggettoIdAbilitante;
	}
	public Date getAbilitazioneInizio() {
		return abilitazioneInizio;
	}
	public void setAbilitazioneInizio(Date abilitazioneInizio) {
		this.abilitazioneInizio = abilitazioneInizio;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public Date getDataModifica() {
		return dataModifica;
	}
	public void setDataModifica(Date dataModifica) {
		this.dataModifica = dataModifica;
	}
	public String getUtenteCreazione() {
		return utenteCreazione;
	}
	public void setUtenteCreazione(String utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}
	public String getUtenteModifica() {
		return utenteModifica;
	}
	public void setUtenteModifica(String utenteModifica) {
		this.utenteModifica = utenteModifica;
	}
}
