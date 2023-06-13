/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto.custom;

import java.sql.Timestamp;

public class SAdesione {

	Integer sAdesioneId;
	Integer adesioneId;
	Integer soggettoId;
	Timestamp adesioneInizio;
	Timestamp adesioneFine;
	Boolean mostraLetturaMessaggiAAssistiti;
	Timestamp dataCreazione;
	Timestamp dataModifica;
	String utenteCreazione;
	String utenteModifica;
	Timestamp validitaInizio;
	Timestamp validitaFine ;
	
	public Integer getAdesioneId() {
		return adesioneId;
	}
	public void setAdesioneId(Integer adesioneId) {
		this.adesioneId = adesioneId;
	}
	public Integer getSoggettoId() {
		return soggettoId;
	}
	public void setSoggettoId(Integer soggettoId) {
		this.soggettoId = soggettoId;
	}
	public Timestamp getAdesioneInizio() {
		return adesioneInizio;
	}
	public void setAdesioneInizio(Timestamp adesioneInizio) {
		this.adesioneInizio = adesioneInizio;
	}
	public Timestamp getAdesioneFine() {
		return adesioneFine;
	}
	public void setAdesioneFine(Timestamp adesioneFine) {
		this.adesioneFine = adesioneFine;
	}
	public Boolean isMostraLetturaMessaggiAAssistiti() {
		return mostraLetturaMessaggiAAssistiti;
	}
	public void setMostraLetturaMessaggiAAssistiti(Boolean mostraLetturaMessaggiAAssistiti) {
		this.mostraLetturaMessaggiAAssistiti = mostraLetturaMessaggiAAssistiti;
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
	public Integer getsAdesioneId() {
		return sAdesioneId;
	}
	public void setsAdesioneId(Integer sAdesioneId) {
		this.sAdesioneId = sAdesioneId;
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
}
