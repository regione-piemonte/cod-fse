/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.notificatore.enumeration;


public enum NotificatoreEventCode {

	EVENTO_POST_MESSAGGIO_MEDICO("EVCOD01"),
	EVENTO_POST_MESSAGGIO_CITTADINO_DELEGATO("EVCOD02"),
	EVENTO_PUT_MESSAGGIO_MEDICO("EVCOD03"),
	EVENTO_PUT_MESSAGGIO_CITTADINO_DELEGATO("EVCOD04");
	
	private final String code;

	private NotificatoreEventCode(String inCode) {
		this.code = inCode;
	}

	public String getCode() {
		return code;
	}

	
}
