/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.auditlog;

public enum RuoloCittEnum {
	CITTADINO("CIT"),
	DELLEGATO("DEL");
	
	private final String code;

	private RuoloCittEnum(String inCode) {
		this.code = inCode;
	}

	public String getCode() {
		return code;
	}
	
}
