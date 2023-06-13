/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.codcit.dto.custom.CodDErrore;
import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.dao.custom.CodDErroreDao;
import it.csi.dma.codcit.integration.service.CodDErroreService;
import it.csi.dma.codcit.util.Constants;
import it.csi.dma.codcit.util.CreateTemplateMessage;
import it.csi.dma.codcit.util.LoggerUtil;

@Service
public class CodDErroreServiceImpl extends LoggerUtil implements CodDErroreService {
	@Autowired
	CodDErroreDao codDErroreDao;

	@Override
	public ErroreDettaglioExt getValueGenericError(String key, String... param) throws DatabaseException {
		var value = Constants.ERRORE_NON_CODIFICATO;
		Integer id = null;

		var codDErrore = codDErroreDao.selectErroreDescFromErroreCod(key);
		if (codDErrore != null) {
			if (!StringUtils.isEmpty(codDErrore.getErroreDesc())) {
				value = codDErrore.getErroreDesc();
			}
			id = codDErrore.getErroreId();
		}
		value = addParameterToTemplateIfExist(value, param);

		return setError(key, value, id);
	}

	private String addParameterToTemplateIfExist(String value, String... param) {
		if (param != null) {
			Map<String, Object> parameter = generateParamList(param);
			value = CreateTemplateMessage.generateTextByTemplateAndMap(value, parameter);
		}
		return value;
	}

	private Map<String, Object> generateParamList(String... params) {
		Map<String, Object> parameter = new HashMap<>();
		for (var i = 0; i < params.length; i++) {
			parameter.put(String.valueOf(i), params[i]);
		}
		return parameter;
	}

	private ErroreDettaglioExt setError(String key, String value, Integer erroreId) {
		var erroreDettaglio = new ErroreDettaglioExt();
		erroreDettaglio.setChiave(key);
		erroreDettaglio.setValore(value);
		erroreDettaglio.setErroreId(erroreId);
		return erroreDettaglio;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public ErroreDettaglioExt getValueGenericErrorFromExternalService(String externalService, String errorCode,
			String errorDesc) throws DatabaseException {
		var methodName = "getValueGenericErrorFromExternalService";
		var key = externalService.concat(errorCode);
		// Non e' permessa la parametrizzazione dell'errorDesc per servizi esterni
		// inquanto non sapremmo cosa inserire nel parametro.
		var value = Constants.ERRORE_NON_CODIFICATO_INTERNO;
		Integer id = null;

		var codDErrore = codDErroreDao.selectErroreDescFromErroreCod(key);

		if (codDErrore == null) {
			try {
				var codDErroreNew = new CodDErrore();
				codDErroreNew.setUtenteModifica(Constants.UTENTE_APPLICATIVO);
				codDErroreNew.setUtenteCreazione(Constants.UTENTE_APPLICATIVO);
				codDErroreNew.setErroreCod(key);
				codDErroreNew.setErroreDesc(errorDesc);
				var res = codDErroreDao.insert(codDErroreNew);
				if (res == 0) {
					logError(methodName, "non e' stato possibile inserire erroreCod");
				} else {
					value = codDErroreNew.getErroreDesc();
					id = res;
				}
			} catch (Exception e) {
				logError(methodName, "errore in inserimento nuovo codice errore, ritorniamo errore non codificato",
						e.getMessage());
			}
		} else {
			if (!StringUtils.isEmpty(codDErrore.getErroreDesc())) {
				value = codDErrore.getErroreDesc();
			}
			id = codDErrore.getErroreId();
		}

		return setError(key, value, id);
	}

}
