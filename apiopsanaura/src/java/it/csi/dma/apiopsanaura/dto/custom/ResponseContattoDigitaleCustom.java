/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apiopsanaura.dto.custom;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
public class ResponseContattoDigitaleCustom{
	@JsonProperty("elenco_assistiti")
    protected List<UtRecAssistitoVOCustom> elencoAssistiti;
	@JsonProperty("error_code")
    protected String errorCode;
	@JsonProperty("error_message")
    protected String errorMessage;
	@JsonProperty("total_page")
    protected BigDecimal totalPage;
	@JsonProperty("ret_val")
	protected BigDecimal retVal;
	
    public List<UtRecAssistitoVOCustom> getElencoAssistiti() {
		return elencoAssistiti;
	}
	public void setElencoAssistiti(List<UtRecAssistitoVOCustom> elencoAssistiti) {
		this.elencoAssistiti = elencoAssistiti;
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
	public BigDecimal getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(BigDecimal totalPage) {
		this.totalPage = totalPage;
	}
	public BigDecimal getRetVal() {
		return retVal;
	}
	public void setRetVal(BigDecimal retVal) {
		this.retVal = retVal;
	}


}
