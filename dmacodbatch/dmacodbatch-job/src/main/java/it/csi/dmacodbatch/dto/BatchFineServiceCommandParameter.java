/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.dto;

import it.csi.dmacodbatch.util.CommandParameter;

public class BatchFineServiceCommandParameter implements CommandParameter{

	private String statoBatch;
	private Integer batchId;
	private String note;
	private Integer esecuzione;
	private Integer requestId;
	

	
	
	public BatchFineServiceCommandParameter(String statoBatch, Integer batchId, String note, Integer esecuzione, Integer requestId) {
		super();
		this.statoBatch = statoBatch;
		this.batchId = batchId;
		this.note = note;
		this.esecuzione = esecuzione;
		this.requestId = requestId;
	}


	public Integer getEsecuzione() {
		return esecuzione;
	}


	public void setEsecuzione(Integer esecuzione) {
		this.esecuzione = esecuzione;
	}


	public Integer getRequestId() {
		return requestId;
	}


	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}


	public BatchFineServiceCommandParameter() {
		super();
	}


	public String getStatoBatch() {
		return statoBatch;
	}
	public void setStatoBatch(String statoBatch) {
		this.statoBatch = statoBatch;
	}
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
