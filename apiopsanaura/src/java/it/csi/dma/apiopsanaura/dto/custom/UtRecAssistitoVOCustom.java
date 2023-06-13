/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apiopsanaura.dto.custom;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UtRecAssistitoVOCustom
{
	@JsonProperty("cod_fisc_ass")
    protected String codFiscAss;
    protected String cognome;
    protected String nome;
    protected String sesso;
    @JsonProperty("data_nascita")
    protected String dataNascita;
    @JsonProperty("cod_comune_nascita")
    protected String codComuneNascita;
    @JsonProperty("descr_comune_nascita")
    protected String descrComuneNascita;
    @JsonProperty("id_aura")
    protected BigDecimal idAura;
	public String getCodFiscAss() {
		return codFiscAss;
	}
	public void setCodFiscAss(String codFiscAss) {
		this.codFiscAss = codFiscAss;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSesso() {
		return sesso;
	}
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getCodComuneNascita() {
		return codComuneNascita;
	}
	public void setCodComuneNascita(String codComuneNascita) {
		this.codComuneNascita = codComuneNascita;
	}
	public String getDescrComuneNascita() {
		return descrComuneNascita;
	}
	public void setDescrComuneNascita(String descrComuneNascita) {
		this.descrComuneNascita = descrComuneNascita;
	}
	public BigDecimal getIdAura() {
		return idAura;
	}
	public void setIdAura(BigDecimal idAura) {
		this.idAura = idAura;
	}
	public UtRecAssistitoVOCustom(String codFiscAss, String cognome, String nome, String sesso, String dataNascita,
			String codComuneNascita, String descrComuneNascita, BigDecimal idAura) {
		super();
		this.codFiscAss = codFiscAss;
		this.cognome = cognome;
		this.nome = nome;
		this.sesso = sesso;
		this.dataNascita = dataNascita;
		this.codComuneNascita = codComuneNascita;
		this.descrComuneNascita = descrComuneNascita;
		this.idAura = idAura;
	}
	public UtRecAssistitoVOCustom() {
		super();
	}


}
