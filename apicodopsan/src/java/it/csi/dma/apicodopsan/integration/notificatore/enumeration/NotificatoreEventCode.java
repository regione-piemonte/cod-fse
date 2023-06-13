/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.notificatore.enumeration;


public enum NotificatoreEventCode {

	EVENTO_POST_MESSAGGIO("EVCOD10"),
	EVENTO_POST_MESSAGGIO_DELEGATO("EVCOD11"),
	EVENTO_PUT_MESSAGGIO("EVCOD12"),
	EVENTO_PUT_MESSAGGIO_DELEGATO("EVCOD13"),
	EVENTO_ABILITA_CITTADINO("EVCOD20"),
	EVENTO_ABILITA_CITTADINO_DELEGATO("EVCOD21"),
	EVENTO_DISABILITA_CITTADINO("EVCOD25"),
	EVENTO_DISABILITA_CITTADINO_DELEGATO("EVCOD26"),
	EVENTO_MEDICO_ABILITA("EVCOD22"),
	EVENTO_MEDICO_ABILITA_GIA_ABILITATI("EVCOD23"),
	EVENTO_MEDICO_ABILITA_ERRORE("EVCOD24"),
	EVENTO_MEDICO_DISABILITA("EVCOD27"),
	EVENTO_MEDICO_DISABILITA_GIA_DISABILITATI("EVCOD28"),
	EVENTO_MEDICO_DISABILITA_ERRORE("EVCOD29");
	private final String code;

	private NotificatoreEventCode(String inCode) {
		this.code = inCode;
	}

	public String getCode() {
		return code;
	}

	
}
