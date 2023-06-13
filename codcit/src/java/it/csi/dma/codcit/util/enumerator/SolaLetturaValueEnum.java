/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.enumerator;

public enum SolaLetturaValueEnum {
	SOLA_LETTURA_A("A"),
	SOLA_LETTURA_B("B"),
	SOLA_LETTURA_T("T");

	private final String code;
	
	SolaLetturaValueEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
