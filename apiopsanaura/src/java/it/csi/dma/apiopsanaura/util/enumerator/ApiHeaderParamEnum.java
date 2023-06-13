/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.util.enumerator;


public enum ApiHeaderParamEnum {

	SHIB_IRIDE_IDENTITADIGITALE("Shib-Iride-IdentitaDigitale"),
	X_AUTHENTICATION("x-authentication");
	
	private final String code;

	private ApiHeaderParamEnum(String inCode) {
		this.code = inCode;
	}

	public String getCode() {
		return code;
	}

	
}
