/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto.custom;

import java.sql.Timestamp;

public class CParametro {

	private String parametroId;
	private String parametroNome;
	private String parametroValore;
	private String parametroDesc;
	private Timestamp validitaInizio;
	private Timestamp validitaFine;
	private Timestamp dataCreazione;
	private Timestamp dataModifica;
	private String utenteCreazione;
	private String utenteModifica;
	
	public String getParametroId() {
		return parametroId;
	}
	public void setParametroId(String parametroId) {
		this.parametroId = parametroId;
	}
	public String getParametroNome() {
		return parametroNome;
	}
	public void setParametroNome(String parametroNome) {
		this.parametroNome = parametroNome;
	}
	public String getParametroValore() {
		return parametroValore;
	}
	public void setParametroValore(String parametroValore) {
		this.parametroValore = parametroValore;
	}
	public String getParametroDesc() {
		return parametroDesc;
	}
	public void setParametroDesc(String parametroDesc) {
		this.parametroDesc = parametroDesc;
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
