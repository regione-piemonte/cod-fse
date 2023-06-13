/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.enumerator;

public enum StatusEnum {

	BAD_REQUEST(400, "BAD_REQUEST", "BAD_REQUEST TITLE"),
	NOT_FOUND(404, "NOT FOUND", "NOT FOUND TITLE"),
	SERVER_ERROR(500, "SERVER_ERROR", "SERVER_ERROR TITLE"),
	SERVER_ERROR_DELEGHE(500, "SERVER_ERROR DELEGHE", "SERVER_ERROR DELEGHE TITLE"),
	SERVER_ERROR_AURA(500, "SERVER_ERROR AURA", "SERVER_ERROR AURA TITLE"),
	SERVER_ERROR_FATAL(500, "SERVER_ERROR_FATAL", "SERVER_ERROR FATAL"),
	FORBIDDEN(403, "FORBIDDEN", "FORBIDDEN");


	  private final Integer status;
	  private final String code;
	  private final String title;

	StatusEnum(Integer status, String code, String title) {
		this.status = status;
		this.code = code;
		this.title = title;

	}

	public String getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

	public Integer getStatus() {
		return status;
	}
}
