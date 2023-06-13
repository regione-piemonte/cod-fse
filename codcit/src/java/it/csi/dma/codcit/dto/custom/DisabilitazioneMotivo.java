/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.dto.custom;

import java.sql.Timestamp;

public class DisabilitazioneMotivo {

	Integer disabilitazioneMotivoId;
	String disabilitazioneMotivoCod;
	String disabilitazioneMotivoDesc;
	Timestamp validitaInizio;
	Timestamp validitaFine;
	Timestamp dataCreazione;
	Timestamp dataModifica;
	String utenteCreazione;
	String utenteModifica;

	public Integer getDisabilitazioneMotivoId() {
		return disabilitazioneMotivoId;
	}

	public void setDisabilitazioneMotivoId(Integer disabilitazioneMotivoId) {
		this.disabilitazioneMotivoId = disabilitazioneMotivoId;
	}

	public String getDisabilitazioneMotivoCod() {
		return disabilitazioneMotivoCod;
	}

	public void setDisabilitazioneMotivoCod(String disabilitazioneMotivoCod) {
		this.disabilitazioneMotivoCod = disabilitazioneMotivoCod;
	}

	public String getDisabilitazioneMotivoDesc() {
		return disabilitazioneMotivoDesc;
	}

	public void setDisabilitazioneMotivoDesc(String disabilitazioneMotivoDesc) {
		this.disabilitazioneMotivoDesc = disabilitazioneMotivoDesc;
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
