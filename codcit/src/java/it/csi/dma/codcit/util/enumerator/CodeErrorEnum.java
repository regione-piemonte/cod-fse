/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.enumerator;

public enum CodeErrorEnum {
	FSE_COD_050("FSE-COD-050"),
	FSE_COD_051("FSE-COD-051"),
	FSE_COD_062("FSE-COD-062"),
	FSE_COD_063("FSE-COD-063"),
	FSE_COD_064("FSE-COD-064"),
	FSE_COD_067("FSE-COD-067"),
	FSE_COD_073("FSE-COD-073"),
	FSE_COD_074("FSE-COD-074"),
	FSE_COD_075("FSE-COD-075"),
	FSE_COD_077("FSE-COD-077"),
	FSE_COD_078("FSE-COD-078"),
	FSE_COD_079("FSE-COD-079"),
	FSE_COD_080("FSE-COD-080"),
	FSE_COD_081("FSE-COD-081"),
	FSE_COD_082("FSE-COD-082"),
	FSE_COD_085("FSE-COD-085"),
	FSE_COD_086("FSE-COD-086"),
	FSE_COD_092("FSE-COD-092"),
	FSE_COD_093("FSE-COD-093"),
	FSE_COD_095("FSE-COD-095"),
	FSE_COD_FATAL("FSE-COD-FATAL"),
	FSE_COD_091("FSE-COD-091"),
	CC_ER_185("CC_ER_185");

	private final String code;

	CodeErrorEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}


}
