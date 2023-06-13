/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto.custom;

import java.sql.Timestamp;

public class DmaccDEventoPerNotificatore {
	private Long id;
	private String codiceEvento;
	private String descrizioneEvento;
	private String msg_mail;
	private String msg_sms;
	private String msg_push;
	private String msg_mex;
	private Timestamp dataInserimento;
	private Timestamp dataAggiornamento;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodiceEvento() {
		return codiceEvento;
	}
	public void setCodiceEvento(String codiceEvento) {
		this.codiceEvento = codiceEvento;
	}
	public String getDescrizioneEvento() {
		return descrizioneEvento;
	}
	public void setDescrizioneEvento(String descrizioneEvento) {
		this.descrizioneEvento = descrizioneEvento;
	}
	public String getMsg_mail() {
		return msg_mail;
	}
	public void setMsg_mail(String msg_mail) {
		this.msg_mail = msg_mail;
	}
	public String getMsg_sms() {
		return msg_sms;
	}
	public void setMsg_sms(String msg_sms) {
		this.msg_sms = msg_sms;
	}
	public String getMsg_push() {
		return msg_push;
	}
	public void setMsg_push(String msg_push) {
		this.msg_push = msg_push;
	}
	public String getMsg_mex() {
		return msg_mex;
	}
	public void setMsg_mex(String msg_mex) {
		this.msg_mex = msg_mex;
	}
	public Timestamp getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(Timestamp dataInserimento) {
		this.dataInserimento = dataInserimento;
	}
	public Timestamp getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setDataAggiornamento(Timestamp dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public DmaccDEventoPerNotificatore(Long id, String codiceEvento, String descrizioneEvento, String msg_mail,
			String msg_sms, String msg_push, String msg_mex, Timestamp dataInserimento, Timestamp dataAggiornamento) {
		super();
		this.id = id;
		this.codiceEvento = codiceEvento;
		this.descrizioneEvento = descrizioneEvento;
		this.msg_mail = msg_mail;
		this.msg_sms = msg_sms;
		this.msg_push = msg_push;
		this.msg_mex = msg_mex;
		this.dataInserimento = dataInserimento;
		this.dataAggiornamento = dataAggiornamento;
	}
	public DmaccDEventoPerNotificatore() {
		super();
	}
	
}
