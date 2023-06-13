/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto.custom;

import java.sql.Timestamp;

public class SConversazione {

	Integer sConversazioneId;
	Integer conversazioneId;
	String conversazioneCod;
	String conversazioneOggetto;
	Integer soggettoIdAutore;
	Integer soggettoIdPartecipante;
	Timestamp conversazioneDataBlocco;
	Integer disabilitazioneMotivoId;
	Timestamp dataCreazione;
	Timestamp dataModifica;
	String utenteCreazione;
	String utenteModifica	;
	Timestamp validitaInizio;
	Timestamp validitaFine;
	
	public Integer getConversazioneId() {
		return conversazioneId;
	}
	public void setConversazioneId(Integer conversazioneId) {
		this.conversazioneId = conversazioneId;
	}
	public String getConversazioneCod() {
		return conversazioneCod;
	}
	public void setConversazioneCod(String conversazioneCod) {
		this.conversazioneCod = conversazioneCod;
	}
	public String getConversazioneOggetto() {
		return conversazioneOggetto;
	}
	public void setConversazioneOggetto(String conversazioneOggetto) {
		this.conversazioneOggetto = conversazioneOggetto;
	}
	public Integer getSoggettoIdAutore() {
		return soggettoIdAutore;
	}
	public void setSoggettoIdAutore(Integer soggettoIdAutore) {
		this.soggettoIdAutore = soggettoIdAutore;
	}
	public Integer getSoggettoIdPartecipante() {
		return soggettoIdPartecipante;
	}
	public void setSoggettoIdPartecipante(Integer soggettoIdPartecipante) {
		this.soggettoIdPartecipante = soggettoIdPartecipante;
	}

	public Integer getDisabilitazioneMotivoId() {
		return disabilitazioneMotivoId;
	}
	public void setDisabilitazioneMotivoId(Integer disabilitazioneMotivoId) {
		this.disabilitazioneMotivoId = disabilitazioneMotivoId;
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
	public Integer getsConversazioneId() {
		return sConversazioneId;
	}
	public void setsConversazioneId(Integer sConversazioneId) {
		this.sConversazioneId = sConversazioneId;
	}
	public Timestamp getValiditaInizio() {
		return validitaInizio;
	}
	public void setValiditaInizio(Timestamp validitaInizio) {
		this.validitaInizio = validitaInizio;
	}
	public Timestamp getValiditaFine() {
		return validitaFine;
	}
	public void setValiditaFine(Timestamp validitaFine) {
		this.validitaFine = validitaFine;
	}
	public Timestamp getConversazioneDataBlocco() {
		return conversazioneDataBlocco;
	}
	public void setConversazioneDataBlocco(Timestamp conversazioneDataBlocco) {
		this.conversazioneDataBlocco = conversazioneDataBlocco;
	}
}
