/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.dto.custom;

import java.sql.Timestamp;

public class MessaggioErrore {

	private Long messaggioErroreId;
	private Long messaggioId;
	private Integer erroreId;
	private String informazioniAggiuntive;
	private Timestamp dataCreazione;
	private Timestamp dataModifica;
	
	public Long getMessaggioErroreId() {
		return messaggioErroreId;
	}
	public void setMessaggioErroreId(Long messaggioErroreId) {
		this.messaggioErroreId = messaggioErroreId;
	}
	public Long getMessaggioId() {
		return messaggioId;
	}
	public void setMessaggioId(Long messaggioId) {
		this.messaggioId = messaggioId;
	}
	public Integer getErroreId() {
		return erroreId;
	}
	public void setErroreId(Integer erroreId) {
		this.erroreId = erroreId;
	}
	public String getInformazioniAggiuntive() {
		return informazioniAggiuntive;
	}
	public void setInformazioniAggiuntive(String informazioniAggiuntive) {
		this.informazioniAggiuntive = informazioniAggiuntive;
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
}
