/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util;

import org.apache.log4j.Logger;

public abstract class LoggerUtil {

	private static final String GENERIC_SEPARATOR = " ";
	private static final String SEPARATOR = ": ";
	protected final Logger log = Logger.getLogger(getClass());

	protected void logInfo(String methodName, String string) {
		log.info(methodName + SEPARATOR + string);

	}

	protected void logError(String methodName, String string) {
		log.error(methodName + SEPARATOR + string);

	}

	protected void logError(String methodName, String... extra) {

		var string = new StringBuilder();
		var first = true;
		for (String s : extra) {
			if (first) {
				first = false;
			} else {
				string.append(GENERIC_SEPARATOR);
			}

			string.append(s);
		}
		log.error(methodName + SEPARATOR + string.toString());

	}

}
