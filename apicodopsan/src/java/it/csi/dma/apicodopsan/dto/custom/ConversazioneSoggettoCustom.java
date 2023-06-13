/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto.custom;

import java.sql.Timestamp;

public class ConversazioneSoggettoCustom {
	Long conversazioneId;
	String conversazioneCod;
	String conversazioneOggetto;
	Integer soggettoIdAutore;
	Integer soggettoIdPartecipante;
	Timestamp conversazioneDataBlocco;
	Integer disabilitazioneMotivoId;
	Timestamp dataCreazione;
	Timestamp dataModifica;
	String utente_creazione;
	String utente_modifica;
	String soggettoCf;
	public ConversazioneSoggettoCustom(Long conversazioneId, String conversazioneCod, String conversazioneOggetto,
			Integer soggettoIdAutore, Integer soggettoIdPartecipante, Timestamp conversazioneDataBlocco,
			Integer disabilitazioneMotivoId, Timestamp dataCreazione, Timestamp dataModifica, String utente_creazione,
			String utente_modifica, String soggettoCf) {
		super();
		this.conversazioneId = conversazioneId;
		this.conversazioneCod = conversazioneCod;
		this.conversazioneOggetto = conversazioneOggetto;
		this.soggettoIdAutore = soggettoIdAutore;
		this.soggettoIdPartecipante = soggettoIdPartecipante;
		this.conversazioneDataBlocco = conversazioneDataBlocco;
		this.disabilitazioneMotivoId = disabilitazioneMotivoId;
		this.dataCreazione = dataCreazione;
		this.dataModifica = dataModifica;
		this.utente_creazione = utente_creazione;
		this.utente_modifica = utente_modifica;
		this.soggettoCf = soggettoCf;
	}
	public Long getConversazioneId() {
		return conversazioneId;
	}
	public void setConversazioneId(Long conversazioneId) {
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
	public Timestamp getConversazioneDataBlocco() {
		return conversazioneDataBlocco;
	}
	public void setConversazioneDataBlocco(Timestamp conversazioneDataBlocco) {
		this.conversazioneDataBlocco = conversazioneDataBlocco;
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
	public String getUtente_creazione() {
		return utente_creazione;
	}
	public void setUtente_creazione(String utente_creazione) {
		this.utente_creazione = utente_creazione;
	}
	public String getUtente_modifica() {
		return utente_modifica;
	}
	public void setUtente_modifica(String utente_modifica) {
		this.utente_modifica = utente_modifica;
	}
	public String getSoggettoCf() {
		return soggettoCf;
	}
	public void setSoggettoCf(String soggettoCf) {
		this.soggettoCf = soggettoCf;
	}

}
