/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apiopsanaura.dto.custom;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseElencoEsenzioniCustom{

	@JsonProperty("elenco_esenzioni")
	protected List<UtRecEsenzioniVOCustom> elencoEsenzioni;
	@JsonProperty("error_code")
    protected String errorCode;
	@JsonProperty("error_message")
    protected String errorMessage;
	@JsonProperty("ret_val")
    protected BigDecimal retVal;
	
	public List<UtRecEsenzioniVOCustom> getElencoEsenzioni() {
		return elencoEsenzioni;
	}
	public void setElencoEsenzioni(List<UtRecEsenzioniVOCustom> elencoEsenzioni) {
		this.elencoEsenzioni = elencoEsenzioni;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public BigDecimal getRetVal() {
		return retVal;
	}
	public void setRetVal(BigDecimal retVal) {
		this.retVal = retVal;
	}

}
