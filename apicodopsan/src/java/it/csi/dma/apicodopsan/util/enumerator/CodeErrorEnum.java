/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.enumerator;

public enum CodeErrorEnum {
	FSE_COD_050("FSE-COD-050"),
	FSE_COD_051("FSE-COD-051"),
	FSE_COD_060("FSE-COD-060"),
	FSE_COD_061("FSE-COD-061"),
	FSE_COD_062("FSE-COD-062"),
	FSE_COD_063("FSE-COD-063"),
	FSE_COD_064("FSE-COD-064"),
	FSE_COD_065("FSE-COD-065"),
	FSE_COD_066("FSE-COD-066"),
	FSE_COD_067("FSE-COD-067"),
	FSE_COD_068("FSE-COD-068"),
	FSE_COD_069("FSE-COD-069"),
	FSE_COD_070("FSE-COD-070"),
	FSE_COD_071("FSE-COD-071"),
	FSE_COD_072("FSE-COD-072"),
	FSE_COD_088("FSE-COD-088"),
	FSE_COD_089("FSE-COD-089"),
	FSE_COD_090("FSE-COD-090"),
	FSE_COD_FATAL("FSE-COD-FATAL"),
	FSE_COD_084("FSE-COD-084"),
	FSE_COD_081("FSE-COD-081"),
	FSE_COD_085("FSE-COD-085"),
	FSE_COD_075("FSE-COD-075"),
	FSE_COD_091("FSE-COD-091"),
	FSE_COD_092("FSE-COD-092"),
	FSE_COD_093("FSE-COD-093"),
	FSE_COD_094("FSE-COD-094"),
	FSE_COD_080("FSE-COD-080"),
	FSE_COD_073("FSE-COD-073");
	private final String code;

	CodeErrorEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}


}
