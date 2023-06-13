/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.notificatore.dto.medico;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificaMedicoCustom{
    public String uuid;
    public PayloadMedicoCustom payload;
	@JsonProperty("to_be_retried")
    public boolean toBeRetried;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public PayloadMedicoCustom getPayload() {
		return payload;
	}
	public void setPayload(PayloadMedicoCustom payload) {
		this.payload = payload;
	}
	public boolean isToBeRetried() {
		return toBeRetried;
	}
	public void setToBeRetried(boolean toBeRetried) {
		this.toBeRetried = toBeRetried;
	}
    
}