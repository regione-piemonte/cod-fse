/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.enumerator;


public enum BatchCodEnum {
	ABILITAZIONE_SINGOLI("ABI_SING_ASS"),
	DISABILITAZIONE_SINGOLI("DIS_SIGN_ASS"),
	ABILITAZIONE_TUTTI ("ABI_TUTTI_ASS"),
	DISABILITAZIONE_TUTTI("DIS_TUTTI_ASS");
	
	private final String code;

	private BatchCodEnum(String inCode) {
		this.code = inCode;
	}

	public String getCode() {
		return code;
	}

	
}
