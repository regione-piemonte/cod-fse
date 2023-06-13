/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.service;

public interface CodCParametroService {


	<T> T get(ConfigParam cp);


	/**
	 * Enumerazione di tutti i parametri possibili
	 *
	 */
	public enum ConfigParam {
		NUM_MAX_TENTATIVI_INI_STATOCONSENSO("NUM_MAX_TENTATIVI_INI_STATOCONSENSO", Integer.class),
		NUM_GIORNI_ELIMINAZIONE_CONVERSAZIONE("NUM_GIORNI_ELIMINAZIONE_CONVERSAZIONE", Integer.class)
		;

		String value;
		Class<?> clazz;
		Object defaultValue;
		ConfigParam(String s, Class<?> c) {
			this.value = s;
			this.clazz = c;
			this.defaultValue = null;
		}

		ConfigParam(String s, Object d) {
			this.value = s;
			this.clazz = d.getClass();
			this.defaultValue = d;
		}

		public String getValue() {
			return value;
		}

		public Class<?> getClazz() {
			return clazz;
		}

		public Object getDefaultValue() {
			return defaultValue;
		}

	}


}
