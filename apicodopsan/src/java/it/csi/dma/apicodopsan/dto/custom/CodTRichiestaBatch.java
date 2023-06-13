/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto.custom;

import java.sql.Timestamp;

public class CodTRichiestaBatch {
	private Long batchricId;
	private String batchricRichiedente;
	private Integer batchId;
	private Integer batchparamId;
	private String batchparamValue;
	private Timestamp validitaInizio;
	private Timestamp validitaFine;
	private Timestamp dataCreazione;
	private Timestamp dataCancellazione;
	private String utenteCreazione;
	private String utenteCancellazione;
	private String batchricMotivazione;
	public Long getBatchricId() {
		return batchricId;
	}
	public void setBatchricId(Long batchricId) {
		this.batchricId = batchricId;
	}
	public String getBatchricRichiedente() {
		return batchricRichiedente;
	}
	public void setBatchricRichiedente(String batchricRichiedente) {
		this.batchricRichiedente = batchricRichiedente;
	}
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	public Integer getBatchparamId() {
		return batchparamId;
	}
	public void setBatchparamId(Integer batchparamId) {
		this.batchparamId = batchparamId;
	}
	public String getBatchparamValue() {
		return batchparamValue;
	}
	public void setBatchparamValue(String batchparamValue) {
		this.batchparamValue = batchparamValue;
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
	public Timestamp getDataCancellazione() {
		return dataCancellazione;
	}
	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}
	public String getUtenteCreazione() {
		return utenteCreazione;
	}
	public void setUtenteCreazione(String utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}
	public String getUtenteCancellazione() {
		return utenteCancellazione;
	}
	public void setUtenteCancellazione(String utenteCancellazione) {
		this.utenteCancellazione = utenteCancellazione;
	}
	public String getBatchricMotivazione() {
		return batchricMotivazione;
	}
	public void setBatchricMotivazione(String batchricMotivazione) {
		this.batchricMotivazione = batchricMotivazione;
	}
	
	
}