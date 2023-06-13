/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto.custom;

import java.sql.Timestamp;

public class TMessaggio {
	
	private int messaggioId;
	private byte[] messaggioTestoCifrato;
	private int soggettoIdDa;
	private int soggettoIdA;
	private Timestamp messaggioDataInvio;
	private Timestamp messaggioLetturaData;
	private String messaggioLetturaDaCf;
	private int conversazioneId;
	private Timestamp dataCreazione;
	private Timestamp dataModifica;
	private String utenteCreazione;
	private String utenteModifica;
	public int getMessaggioId() {
		return messaggioId;
	}
	public void setMessaggioId(int messaggioId) {
		this.messaggioId = messaggioId;
	}
	
	public byte[] getMessaggioTestoCifrato() {
		return messaggioTestoCifrato;
	}
	public void setMessaggioTestoCifrato(byte[] messaggioTestoCifrato) {
		this.messaggioTestoCifrato = messaggioTestoCifrato;
	}
	public int getSoggettoIdDa() {
		return soggettoIdDa;
	}
	public void setSoggettoIdDa(int soggettoIdDa) {
		this.soggettoIdDa = soggettoIdDa;
	}
	public int getSoggettoIdA() {
		return soggettoIdA;
	}
	public void setSoggettoIdA(int soggettoIdA) {
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
	public int getConversazioneId() {
		return conversazioneId;
	}
	public void setConversazioneId(int conversazioneId) {
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
	
