/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.dto.custom;

import java.sql.Timestamp;

public class AdesioneSoggetto {
	private long adesioneId;
	private long soggettoId;
	private Timestamp adesioneInizio;
	private Timestamp adesioneFine;
	private boolean mostraLetturaMessaggiAAssistiti;
	private Timestamp dataCreazione;
	private Timestamp data_modifica;
	private String utenteCreazione;
	private String utenteModifica;

	public AdesioneSoggetto(long adesioneId, long soggettoId, Timestamp adesioneInizio, Timestamp adesioneFine,
			boolean mostraLetturaMessaggiAAssistiti, Timestamp dataCreazione, Timestamp data_modifica,
			String utenteCreazione, String utenteModifica) {
		super();
		this.adesioneId = adesioneId;
		this.soggettoId = soggettoId;
		this.adesioneInizio = adesioneInizio;
		this.adesioneFine = adesioneFine;
		this.mostraLetturaMessaggiAAssistiti = mostraLetturaMessaggiAAssistiti;
		this.dataCreazione = dataCreazione;
		this.data_modifica = data_modifica;
		this.utenteCreazione = utenteCreazione;
		this.utenteModifica = utenteModifica;
	}

	public long getAdesioneId() {
		return adesioneId;
	}
	public void setAdesioneId(long adesioneId) {
		this.adesioneId = adesioneId;
	}
	public long getSoggettoId() {
		return soggettoId;
	}
	public void setSoggettoId(long soggettoId) {
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
	public boolean isMostraLetturaMessaggiAAssistiti() {
		return mostraLetturaMessaggiAAssistiti;
	}
	public void setMostraLetturaMessaggiAAssistiti(boolean mostraLetturaMessaggiAAssistiti) {
		this.mostraLetturaMessaggiAAssistiti = mostraLetturaMessaggiAAssistiti;
	}
	public Timestamp getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public Timestamp getData_modifica() {
		return data_modifica;
	}
	public void setData_modifica(Timestamp data_modifica) {
		this.data_modifica = data_modifica;
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
