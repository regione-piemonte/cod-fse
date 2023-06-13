/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.notifica.core.dto.custom;

import java.sql.Timestamp;

public class Soggetto {

	Long soggettoId;
	String soggettoCf;
	String soggettoNome;
	String soggettoCognome;
	Timestamp soggettoDataDiNascita;
	String soggettoSesso;
	Long idPaziente;
	Boolean soggettoIsMedico;
	Timestamp dataCreazione;
	Timestamp dataModifca;
	String utenteCreazione;
	String utenteModifica;
	
	public Long getSoggettoId() {
		return soggettoId;
	}
	public void setSoggettoId(Long soggettoId) {
		this.soggettoId = soggettoId;
	}
	public String getSoggettoCf() {
		return soggettoCf;
	}
	public void setSoggettoCf(String soggettoCf) {
		this.soggettoCf = soggettoCf;
	}
	public String getSoggettoNome() {
		return soggettoNome;
	}
	public void setSoggettoNome(String soggettoNome) {
		this.soggettoNome = soggettoNome;
	}
	public String getSoggettoCognome() {
		return soggettoCognome;
	}
	public void setSoggettoCognome(String soggettoCognome) {
		this.soggettoCognome = soggettoCognome;
	}
	public Timestamp getSoggettoDataDiNascita() {
		return soggettoDataDiNascita;
	}
	public void setSoggettoDataDiNascita(Timestamp soggettoDataDiNascita) {
		this.soggettoDataDiNascita = soggettoDataDiNascita;
	}
	public String getSoggettoSesso() {
		return soggettoSesso;
	}
	public void setSoggettoSesso(String soggettoSesso) {
		this.soggettoSesso = soggettoSesso;
	}
	public Long getIdPaziente() {
		return idPaziente;
	}
	public void setIdPaziente(Long idPaziente) {
		this.idPaziente = idPaziente;
	}
	public Boolean getSoggettoIsMedico() {
		return soggettoIsMedico;
	}
	public void setSoggettoIsMedico(Boolean soggettoIsMedico) {
		this.soggettoIsMedico = soggettoIsMedico;
	}
	public Timestamp getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public Timestamp getDataModifca() {
		return dataModifca;
	}
	public void setDataModifca(Timestamp dataModifca) {
		this.dataModifca = dataModifca;
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
