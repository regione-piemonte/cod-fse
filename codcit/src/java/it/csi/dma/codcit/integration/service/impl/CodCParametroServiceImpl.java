/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.codcit.integration.dao.custom.CodCParametroDao;
import it.csi.dma.codcit.integration.service.CodCParametroService;
import it.csi.dma.codcit.util.LoggerUtil;

@Service
public class CodCParametroServiceImpl extends LoggerUtil implements CodCParametroService {

	@Autowired
	CodCParametroDao codCParametroDao;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(ConfigParam cp) {
		return (T) get(cp, cp.getClazz());

	}

	@SuppressWarnings("unchecked")
	private <T> T get(ConfigParam cp, Class<T> c) {
		final var methodName = "get";

		if (c == null || !c.isAssignableFrom(cp.getClazz())) {
			logError(methodName, "eccezione in inizializzazione parametri in", this.getClass().getSimpleName(),
					" il value non e' assegnabile a T");

			return null;
		}
		String configParamEntity;
		try {
			configParamEntity = codCParametroDao.selectValoreParametroFromParametroNome(cp.getValue());

			String s;
			if (configParamEntity != null && !configParamEntity.isEmpty()) {
				s = configParamEntity;
				if (String.class.isAssignableFrom(cp.getClazz())) {

					return (T) s;
				} else if (Long.class.isAssignableFrom(cp.getClazz())) {

					return (T) Long.valueOf(s);
				} else if (Integer.class.isAssignableFrom(cp.getClazz())) {

					return (T) Integer.valueOf(s);
				} else if (Boolean.class.isAssignableFrom(cp.getClazz())) {

					return (T) checkBoolean(s);
				}
			}

		} catch (ClassCastException e) {
			logError(methodName, "eccezione in inizializzazione parametri in", this.getClass().getSimpleName(),
					"valore su db non castabile uso il valore di default se presente per parametro:", cp.getValue());
			logError(methodName, "Error:", e.getMessage());
		} catch (NumberFormatException e) {
			logError(methodName, "eccezione in inizializzazione parametri in", this.getClass().getSimpleName(),
					"il value non e' formattabile alla classe voluta per il parametro:", cp.getValue());
			logError(methodName, "Error:", e.getMessage());
		} catch (Exception e) {
			logError(methodName, "eccezione in inizializzazione parametri in", this.getClass().getSimpleName(),
					"errore generico per parametro: ", cp.getValue());
		}

		return getDefault(cp);
	}

	/**
	 * @param s
	 * @return
	 */
	private Boolean checkBoolean(String s) {
		s = s.trim();
		return Boolean.valueOf("s".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s)
				|| "y".equalsIgnoreCase(s) || "si".equalsIgnoreCase(s));
	}

	@SuppressWarnings("unchecked")
	private <T> T getDefault(ConfigParam cp) {
		if (cp.getDefaultValue() != null) {
			return (T) cp.getDefaultValue();
		} else {
			return null;
		}
	}
}
