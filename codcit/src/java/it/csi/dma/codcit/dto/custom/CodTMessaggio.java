/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.dto.custom;

import java.sql.Timestamp;

public class CodTMessaggio {
	private Long messaggioId;
	private String messaggioTestoCifrato;
	private Integer soggettoIdDa;
	private Integer soggettoIdA;
	private Timestamp messaggioDataInvio;
	private Timestamp messaggioLetturaData;
	private String messaggioLetturaDaCf;
	private Long conversazioneId;
	private Timestamp dataCreazione;
	private Timestamp dataModifica;
	private String utenteCreazione;
	private String utenteModifica;
	public Long getMessaggioId() {
		return messaggioId;
	}
	public void setMessaggioId(Long messaggioId) {
		this.messaggioId = messaggioId;
	}
	public String getMessaggioTestoCifrato() {
		return messaggioTestoCifrato;
	}
	public void setMessaggioTestoCifrato(String messaggioTestoCifrato) {
		this.messaggioTestoCifrato = messaggioTestoCifrato;
	}
	public Integer getSoggettoIdDa() {
		return soggettoIdDa;
	}
	public void setSoggettoIdDa(Integer soggettoIdDa) {
		this.soggettoIdDa = soggettoIdDa;
	}
	public Integer getSoggettoIdA() {
		return soggettoIdA;
	}
	public void setSoggettoIdA(Integer soggettoIdA) {
		this.soggettoIdA = soggettoIdA;
	}
	public Timestamp getMessaggioDataInvio() {
		return messaggioDataInvio;
	}
	public void setMessaggioDataInvio(Timestamp messaggioDataInvio) {
		this.messaggioDataInvio = messaggioDataInvio;
	}
	public Timestamp getMessaggioLetturaData() {
		return messaggioLetturaData;
	}
	public void setMessaggioLetturaData(Timestamp messaggioLetturaData) {
		this.messaggioLetturaData = messaggioLetturaData;
	}
	public String getMessaggioLetturaDaCf() {
		return messaggioLetturaDaCf;
	}
	public void setMessaggioLetturaDaCf(String messaggioLetturaDaCf) {
		this.messaggioLetturaDaCf = messaggioLetturaDaCf;
	}
	public Long getConversazioneId() {
		return conversazioneId;
	}
	public void setConversazioneId(Long conversazioneId) {
		this.conversazioneId = conversazioneId;
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
