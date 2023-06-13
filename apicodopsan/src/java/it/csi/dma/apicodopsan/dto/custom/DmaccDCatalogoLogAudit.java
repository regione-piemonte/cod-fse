/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto.custom;

import java.sql.Timestamp;

public class DmaccDCatalogoLogAudit {

	Long id;
	String codice;
	String descrizione;
	Timestamp dataInserimento;
	String descrizioneCodice;
	String visualizzaWebapp;
	String flagNotificaCit;
	String descrizionebck;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Timestamp getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(Timestamp dataInserimento) {
		this.dataInserimento = dataInserimento;
	}
	public String getDescrizioneCodice() {
		return descrizioneCodice;
	}
	public void setDescrizioneCodice(String descrizioneCodice) {
		this.descrizioneCodice = descrizioneCodice;
	}
	public String getVisualizzaWebapp() {
		return visualizzaWebapp;
	}
	public void setVisualizzaWebapp(String visualizzaWebapp) {
		this.visualizzaWebapp = visualizzaWebapp;
	}
	public String getFlagNotificaCit() {
		return flagNotificaCit;
	}
	public void setFlagNotificaCit(String flagNotificaCit) {
		this.flagNotificaCit = flagNotificaCit;
	}
	public String getDescrizionebck() {
		return descrizionebck;
	}
	public void setDescrizionebck(String descrizionebck) {
		this.descrizionebck = descrizionebck;
	}
	@Override
	public String toString() {
		return "DmaccDCatalogoLogAudit [id=" + id + ", codice=" + codice + ", descrizione=" + descrizione
				+ ", dataInserimento=" + dataInserimento + ", descrizioneCodice=" + descrizioneCodice
				+ ", visualizzaWebapp=" + visualizzaWebapp + ", flagNotificaCit=" + flagNotificaCit
				+ ", descrizionebck=" + descrizionebck + "]";
	}
	
}
