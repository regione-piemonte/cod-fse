/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.notificatore.dto.medico;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadMedicoCustom {
	private String id;
	@JsonProperty("bulk_id")
	private String bulkId;
	private String applicazione;
	@JsonProperty("user_id")
	private String userId;
	private Email email;
	private Push push;
	private Mex mex;
	private Memo memo;
	private Sms sms;
	private String tag;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBulkId() {
		return bulkId;
	}
	public void setBulkId(String bulkId) {
		this.bulkId = bulkId;
	}
	public String getApplicazione() {
		return applicazione;
	}
	public void setApplicazione(String applicazione) {
		this.applicazione = applicazione;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Email getEmail() {
		return email;
	}
	public void setEmail(Email email) {
		this.email = email;
	}
	public Push getPush() {
		return push;
	}
	public void setPush(Push push) {
		this.push = push;
	}
	public Mex getMex() {
		return mex;
	}
	public void setMex(Mex mex) {
		this.mex = mex;
	}
	public Memo getMemo() {
		return memo;
	}
	public void setMemo(Memo memo) {
		this.memo = memo;
	}
	public Sms getSms() {
		return sms;
	}
	public void setSms(Sms sms) {
		this.sms = sms;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	    
	    
}
