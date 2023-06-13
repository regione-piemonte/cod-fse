/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto.custom;

import java.sql.Timestamp;

public class CodDErrore {
	
	private Integer erroreId;
	private String erroreCod;
	private String erroreDesc;
	private Timestamp validitaInizio;
	private Timestamp validitaFine;
	private Timestamp dataCreazione;
	private Timestamp dataModifica;
	private String utenteCreazione;
	private String utenteModifica;
	public Integer getErroreId() {
		return erroreId;
	}
	public void setErroreId(Integer erroreId) {
		this.erroreId = erroreId;
	}
	public String getErroreCod() {
		return erroreCod;
	}
	public void setErroreCod(String erroreCod) {
		this.erroreCod = erroreCod;
	}
	public String getErroreDesc() {
		return erroreDesc;
	}
	public void setErroreDesc(String erroreDesc) {
		this.erroreDesc = erroreDesc;
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
