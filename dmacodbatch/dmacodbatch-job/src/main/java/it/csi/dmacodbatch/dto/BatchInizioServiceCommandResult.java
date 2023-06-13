/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.dto;

import java.util.Date;

import it.csi.dmacodbatch.util.CommandResult;

public class BatchInizioServiceCommandResult implements CommandResult {

	private Integer esecuzione;
	private Date ultimaEsecuzione;
	private Integer batchId;
	private Integer requestId;

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public BatchInizioServiceCommandResult() {
	}
	
	public BatchInizioServiceCommandResult(Integer esecuzione, Date ultimaEsecuzione, Integer batchId, Integer requestId) {
		super();
		this.esecuzione = esecuzione;
		this.ultimaEsecuzione = ultimaEsecuzione;
		this.batchId = batchId;
		this.requestId = requestId;
	}

	public Integer getEsecuzione() {
		return esecuzione;
	}

	public void setEsecuzione(Integer esecuzione) {
		this.esecuzione = esecuzione;
	}

	public Date getUltimaEsecuzione() {
		return ultimaEsecuzione;
	}

	public void setUltimaEsecuzione(Date ultimaEsecuzione) {
		this.ultimaEsecuzione = ultimaEsecuzione;
	}



	public Integer getBatchId() {
		return batchId;
	}



	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
}
