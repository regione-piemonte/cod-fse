/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.dto.custom;

import java.sql.Timestamp;

public class ConversazioneCustom {
	private Long conversazioneId;
	private Integer disabilitazioneMotivoId;
	private String conversazioneCod;
	private String medicoSoggettoNome;
	private String medicoSoggettoCognome;
	private String medicoSoggettoCf;
	private byte[] conversazioneOggetto;
	private Timestamp conversazioneDataBlocco;
	private Timestamp dataCreazione;
	private String utenteCreazione;
	private Integer messaggiNonLetti;
	private Timestamp messaggioDataCreazione;
	private Long messaggioId;
	private Timestamp messaggioLetturaData;
	private byte[] messaggioTesto;
	private String messaggioUtenteCreazione;
	private String disabilitazioneMotivoCod;
	private String disabilitazioneMotivoDesc;
	private String messaggioUtenteModifica;
	private Timestamp messaggioDataModifica;
	private String soggettoCf;
	private Integer soggettoIdDa;
	private Integer MedicoSoggettoId;
	
	
	public Integer getSoggettoIdDa() {
		return soggettoIdDa;
	}
	public void setSoggettoIdDa(Integer soggettoIdDa) {
		this.soggettoIdDa = soggettoIdDa;
	}
	public Integer getMedicoSoggettoId() {
		return MedicoSoggettoId;
	}
	public void setMedicoSoggettoId(Integer medicoSoggettoId) {
		MedicoSoggettoId = medicoSoggettoId;
	}
	public String getSoggettoCf() {
		return soggettoCf;
	}
	public void setSoggettoCf(String soggettoCf) {
		this.soggettoCf = soggettoCf;
	}
	public Long getConversazioneId() {
		return conversazioneId;
	}
	public void setConversazioneId(Long conversazioneId) {
		this.conversazioneId = conversazioneId;
	}
	public Integer getDisabilitazioneMotivoId() {
		return disabilitazioneMotivoId;
	}
	public void setDisabilitazioneMotivoId(Integer disabilitazioneMotivoId) {
		this.disabilitazioneMotivoId = disabilitazioneMotivoId;
	}
	public String getConversazioneCod() {
		return conversazioneCod;
	}
	public void setConversazioneCod(String conversazioneCod) {
		this.conversazioneCod = conversazioneCod;
	}
	public String getMedicoSoggettoNome() {
		return medicoSoggettoNome;
	}
	public void setMedicoSoggettoNome(String medicoSoggettoNome) {
		this.medicoSoggettoNome = medicoSoggettoNome;
	}
	public String getMedicoSoggettoCognome() {
		return medicoSoggettoCognome;
	}
	public void setMedicoSoggettoCognome(String medicoSoggettoCognome) {
		this.medicoSoggettoCognome = medicoSoggettoCognome;
	}
	public String getMedicoSoggettoCf() {
		return medicoSoggettoCf;
	}
	public void setMedicoSoggettoCf(String medicoSoggettoCf) {
		this.medicoSoggettoCf = medicoSoggettoCf;
	}
	public byte[] getConversazioneOggetto() {
		return conversazioneOggetto;
	}
	public void setConversazioneOggetto(byte[] conversazioneOggetto) {
		this.conversazioneOggetto = conversazioneOggetto;
	}
	public Timestamp getConversazioneDataBlocco() {
		return conversazioneDataBlocco;
	}
	public void setConversazioneDataBlocco(Timestamp conversazioneDataBlocco) {
		this.conversazioneDataBlocco = conversazioneDataBlocco;
	}
	public Timestamp getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public String getUtenteCreazione() {
		return utenteCreazione;
	}
	public void setUtenteCreazione(String utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}
	public Integer getMessaggiNonLetti() {
		return messaggiNonLetti;
	}
	public void setMessaggiNonLetti(Integer messaggiNonLetti) {
		this.messaggiNonLetti = messaggiNonLetti;
	}
	public Timestamp getMessaggioDataCreazione() {
		return messaggioDataCreazione;
	}
	public void setMessaggioDataCreazione(Timestamp messaggioDataCreazione) {
		this.messaggioDataCreazione = messaggioDataCreazione;
	}
	public Long getMessaggioId() {
		return messaggioId;
	}
	public void setMessaggioId(Long messaggioId) {
		this.messaggioId = messaggioId;
	}
	public Timestamp getMessaggioLetturaData() {
		return messaggioLetturaData;
	}
	public void setMessaggioLetturaData(Timestamp messaggioLetturaData) {
		this.messaggioLetturaData = messaggioLetturaData;
	}
	public byte[] getMessaggioTesto() {
		return messaggioTesto;
	}
	public void setMessaggioTesto(byte[] messaggioTesto) {
		this.messaggioTesto = messaggioTesto;
	}
	public String getMessaggioUtenteCreazione() {
		return messaggioUtenteCreazione;
	}
	public void setMessaggioUtenteCreazione(String messaggioUtenteCreazione) {
		this.messaggioUtenteCreazione = messaggioUtenteCreazione;
	}
	public String getDisabilitazioneMotivoCod() {
		return disabilitazioneMotivoCod;
	}
	public void setDisabilitazioneMotivoCod(String disabilitazioneMotivoCod) {
		this.disabilitazioneMotivoCod = disabilitazioneMotivoCod;
	}
	public String getDisabilitazioneMotivoDesc() {
		return disabilitazioneMotivoDesc;
	}
	public void setDisabilitazioneMotivoDesc(String disabilitazioneMotivoDesc) {
		this.disabilitazioneMotivoDesc = disabilitazioneMotivoDesc;
	}
	public Timestamp getMessaggioDataModifica() {
		return messaggioDataModifica;
	}
	public void setMessaggioDataModifica(Timestamp messaggioDataModifica) {
		this.messaggioDataModifica = messaggioDataModifica;
	}
	public String getMessaggioUtenteModifica() {
		return messaggioUtenteModifica;
	}
	public void setMessaggioUtenteModifica(String messaggioUtenteModifica) {
		this.messaggioUtenteModifica = messaggioUtenteModifica;
	}
	
	
}
