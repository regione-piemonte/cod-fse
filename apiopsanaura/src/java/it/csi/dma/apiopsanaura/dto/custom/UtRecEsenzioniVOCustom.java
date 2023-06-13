/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apiopsanaura.dto.custom;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UtRecEsenzioniVOCustom{
	@JsonProperty("cod_diagnosi")
    protected String codDiagnosi;
	@JsonProperty("cod_esenzione")
    protected String codEsenzione;
	@JsonProperty("data_fine_validita")
    protected XMLGregorianCalendar dataFineValidita;
	@JsonProperty("data_inizio_validita")
    protected XMLGregorianCalendar dataInizioValidita;
	@JsonProperty("desc_diagnosi")
    protected String descDiagnosi;
	@JsonProperty("desc_esenzione")
    protected String descEsenzione;
	@JsonProperty("tipo_esenzione")
    protected String tipoEsenzione;
	public String getCodDiagnosi() {
		return codDiagnosi;
	}
	public void setCodDiagnosi(String codDiagnosi) {
		this.codDiagnosi = codDiagnosi;
	}
	public String getCodEsenzione() {
		return codEsenzione;
	}
	public void setCodEsenzione(String codEsenzione) {
		this.codEsenzione = codEsenzione;
	}
	public XMLGregorianCalendar getDataFineValidita() {
		return dataFineValidita;
	}
	public void setDataFineValidita(XMLGregorianCalendar dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
	public XMLGregorianCalendar getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(XMLGregorianCalendar dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public String getDescDiagnosi() {
		return descDiagnosi;
	}
	public void setDescDiagnosi(String descDiagnosi) {
		this.descDiagnosi = descDiagnosi;
	}
	public String getDescEsenzione() {
		return descEsenzione;
	}
	public void setDescEsenzione(String descEsenzione) {
		this.descEsenzione = descEsenzione;
	}
	public String getTipoEsenzione() {
		return tipoEsenzione;
	}
	public void setTipoEsenzione(String tipoEsenzione) {
		this.tipoEsenzione = tipoEsenzione;
	}

}
