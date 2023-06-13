/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.dto.custom;

import java.sql.Timestamp;

public class SoggettoDisabilitato {

	Integer abilitazioneId;
	Integer soggettoIdAbilitato;
	Integer soggettoIdAbilitante;
	Integer disabilitazioneMotivoId;
	String disabilitazioneMotivazione;
	Timestamp abilitazioneInizio;
	Timestamp abilitazioneFine;
	Timestamp dataCreazione;
	Timestamp dataModifica;
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

	public Integer getDisabilitazioneMotivoId() {
		return disabilitazioneMotivoId;
	}

	public void setDisabilitazioneMotivoId(Integer disabilitazioneMotivoId) {
		this.disabilitazioneMotivoId = disabilitazioneMotivoId;
	}

	public String getDisabilitazioneMotivazione() {
		return disabilitazioneMotivazione;
	}

	public void setDisabilitazioneMotivazione(String disabilitazioneMotivazione) {
		this.disabilitazioneMotivazione = disabilitazioneMotivazione;
	}

	public Timestamp getAbilitazioneInizio() {
		return abilitazioneInizio;
	}

	public void setAbilitazioneInizio(Timestamp abilitazioneInizio) {
		this.abilitazioneInizio = abilitazioneInizio;
	}

	public Timestamp getAbilitazioneFine() {
		return abilitazioneFine;
	}

	public void setAbilitazioneFine(Timestamp abilitazioneFine) {
		this.abilitazioneFine = abilitazioneFine;
	}

	public Timestamp getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Timestamp getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(Timestamp dataModifica) {
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
