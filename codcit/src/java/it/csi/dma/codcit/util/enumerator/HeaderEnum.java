/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.enumerator;

public enum HeaderEnum {
	X_TOTAL_ELEMENTS("X-Total-Elements"), 
	X_REQUEST_ID("X-Request-ID"),
	X_Page_Size("X-Page-Size"),
	X_Page("X-Page"),
	X_Total_Pages("X-Total-Pages");

	private final String code;

	HeaderEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
