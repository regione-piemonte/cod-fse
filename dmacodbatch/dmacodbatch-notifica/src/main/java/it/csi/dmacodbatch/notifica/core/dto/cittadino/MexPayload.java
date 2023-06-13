/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.notifica.core.dto.cittadino;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MexPayload {
	@JsonProperty("title")
	private String title;
	@JsonProperty("body")
	private String body;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
}
