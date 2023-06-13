/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto.custom;

import java.sql.Date;

public class Ruolo {
	
	Integer id;
	String sol;
	String codiceRuoloPua;
	String descrizioneRuoloPua;
	String codiceRuoloFse;
	String descrizioneRuoloFse;
	Date dataInserimento;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSol() {
		return sol;
	}
	public void setSol(String sol) {
		this.sol = sol;
	}
	public String getCodiceRuoloPua() {
		return codiceRuoloPua;
	}
	public void setCodiceRuoloPua(String codiceRuoloPua) {
		this.codiceRuoloPua = codiceRuoloPua;
	}
	public String getDescrizioneRuoloPua() {
		return descrizioneRuoloPua;
	}
	public void setDescrizioneRuoloPua(String descrizioneRuoloPua) {
		this.descrizioneRuoloPua = descrizioneRuoloPua;
	}
	public String getCodiceRuoloFse() {
		return codiceRuoloFse;
	}
	public void setCodiceRuoloFse(String codiceRuoloFse) {
		this.codiceRuoloFse = codiceRuoloFse;
	}
	public String getDescrizioneRuoloFse() {
		return descrizioneRuoloFse;
	}
	public void setDescrizioneRuoloFse(String descrizioneRuoloFse) {
		this.descrizioneRuoloFse = descrizioneRuoloFse;
	}
	public Date getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}
}
