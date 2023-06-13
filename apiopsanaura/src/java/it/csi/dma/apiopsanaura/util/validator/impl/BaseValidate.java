/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.util.validator.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.dma.apiopsanaura.dto.ErroreDettaglio;
import it.csi.dma.apiopsanaura.dto.custom.Ruolo;
import it.csi.dma.apiopsanaura.dto.custom.TAdesione;
import it.csi.dma.apiopsanaura.exception.DatabaseException;
import it.csi.dma.apiopsanaura.integration.dao.custom.CodDErroreDao;
import it.csi.dma.apiopsanaura.integration.dao.custom.CodTAdesioneDao;
import it.csi.dma.apiopsanaura.integration.dao.custom.DmaccTDecodificaRuoliPuaDao;
import it.csi.dma.apiopsanaura.util.Constants;
import it.csi.dma.apiopsanaura.util.CreateTemplateMessage;
import it.csi.dma.apiopsanaura.util.LoggerUtil;
import it.csi.dma.apiopsanaura.util.enumerator.CodeErrorEnum;
import it.csi.dma.apiopsanaura.util.enumerator.ErrorParamEnum;

public abstract class BaseValidate extends LoggerUtil {


	@Autowired
	CodDErroreDao codDErroreDao;
	
	@Autowired
	DmaccTDecodificaRuoliPuaDao dmaccTDecodificaRuoliPuaDao;

	@Autowired
	CodTAdesioneDao codTAdesioneDao;
	
	protected ErroreDettaglio getValueGenericError(String key, String param) throws DatabaseException {
		var value = codDErroreDao.selectErroreDescFromErroreCod(key);

		if(param!=null) {
			Map<String, Object> parameter = generateParamList("0", param);
			value = CreateTemplateMessage.generateTextByTemplateAndMap(value, parameter);
		}
		
		var result1 = setError(key, value);
		return result1;
	}

	protected ErroreDettaglio getValueFormalError(String key, String param,String fieldValue) throws DatabaseException {
		var value = codDErroreDao.selectErroreDescFromErroreCod(key);

		if(param!=null) {
			Map<String, Object> parameter = generateParamList("0", param);
			parameter.put("1", fieldValue);
			value = CreateTemplateMessage.generateTextByTemplateAndMap(value, parameter);
		}
		
		var result1 = setError(key, value);
		return result1;
	}
	
	private Map<String, Object> generateParamList(String paramKey, String paramValue) {
		Map<String, Object> parameter = new HashMap<>();
		parameter.put(paramKey, paramValue);
		return parameter;
	}

	private ErroreDettaglio setError(String key, String value) {
		var erroreDettaglio = new ErroreDettaglio();
		erroreDettaglio.setChiave(key);
		erroreDettaglio.setValore(value);
		return erroreDettaglio;
	}

	protected boolean formalCheckCF(String cf) {
		
		if(cf.length() != 11 && cf.length() != 16)
			return true;

		String regexCF 	 = "[a-zA-Z]{6}\\d\\d[a-zA-Z]\\d\\d[a-zA-Z]\\d\\d\\d[a-zA-Z]";
		String regexPIVA = "[0-9]{11}";
		
		if(cf.length() == 16 &&  !Pattern.matches(regexCF, cf)) {
			return true;
		}

		if(cf.length() == 11 &&  !Pattern.matches(regexPIVA, cf)) {
			return true;
		}
		
	    return false;
	}					

	protected boolean formalCheckPresenzaRuolo(String ruolo, String xCodiceServizio) throws DatabaseException {
		
		Ruolo existingRole = dmaccTDecodificaRuoliPuaDao.selectRuolo(ruolo,xCodiceServizio);
		if(existingRole == null)
			return true ;
		else
			return false;
		
	}

	protected boolean functionalCheckAdesione(String shibIdentitaCodiceFiscale,boolean adesione) throws DatabaseException {
		
		TAdesione existingAdesione = codTAdesioneDao.selectAdesione(shibIdentitaCodiceFiscale);
		if(existingAdesione != null && adesione)
			return true ;
		
		if(existingAdesione == null && !adesione)
			return true;
		
		return false;
	}

	
	
	
	protected boolean formalCheckCodiceVerticale(String xCodiceVerticale) {
		
		if(xCodiceVerticale.equals(Constants.CODICE_VERTICALE)) {
			return false;
		} else {
			return true;
		}
		
	}
	
	protected boolean formalCheckCodiceServizio(String xCodiceServizio)  {
		
		if(xCodiceServizio.equals(Constants.CODICE_SERVIZIO)) {
			return false;
		} else {
			return true;
		}
		
	}
	
	protected boolean formalCheckLimit(int limit) {
		return !(limit > 0);
	}
	
	protected boolean formalCheckOffset(int offset) {
		return !(offset >= 0);
	}
	
	protected boolean formalCheckStato(String stato) {
		return !(stato.equals("A") || stato.equals("B"));
	}
	
	protected boolean formalCheckSolaLettura(String solaLettura) {
		
		return !(solaLettura.equals("A") || solaLettura.equals("B") || solaLettura.equals("T")); 
	}
	
	protected boolean formalCheckCognome(String cognome) {	
		return cognome != null ? !(cognome.length() >= 2) : false;
	}
	
	protected boolean formalCheckNome(String nome) {	
		return nome != null ? !(nome.length() >= 2) : false;
	}

	protected boolean formalCheckCFFilter(String codiceFiscale) {	
		return codiceFiscale != null ? !(codiceFiscale.length() >= 2) : false;
	}
	
	protected boolean formalCheckSesso(String sesso) {
		return sesso != null ? !(sesso.equals("M") || sesso.equals("F")) : false;
	}
	
	protected boolean formalCheckEtaMin(Integer etaMin) {
		return etaMin != null ? !(etaMin >= 0) : false;
	}
	
	protected boolean formalCheckEtaMax(Integer etaMin, Integer etaMax) {
		return etaMin != null && etaMax != null ? !(etaMax > etaMin) : false;
	}
	// VERIFICA CHE NON SIANO NULLI I CAMPI COMUNI
	protected List<ErroreDettaglio> notNullCommonCheck(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo,
			String collocazioneCodice, String collocazioneDescrizione) throws DatabaseException {
		if (StringUtils.isBlank(xRequestId)) {
			result.add(
					getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), ErrorParamEnum.X_REQUEST_ID.getCode()));
		}

		if (StringUtils.isEmpty(shibIdentitaCodiceFiscale) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode()));
		}

		if (StringUtils.isBlank(xForwardedFor) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.X_FORWARDED_FOR.getCode()));
		}

		if (StringUtils.isBlank(xCodiceServizio)) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.X_CODICE_SERVIZIO.getCode()));
		}

		if (StringUtils.isBlank(collocazioneDescrizione) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.COLLOCAZIONE_DESCRIZIONE.getCode()));
		}

		if (StringUtils.isBlank(collocazioneCodice) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.COLLOCAZIONE_CODICE.getCode()));
		}

		if (StringUtils.isBlank(ruolo) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), ErrorParamEnum.RUOLO.getCode()));
		}

		if (StringUtils.isBlank(xCodiceVerticale) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.X_CODICE_VERTICALE.getCode()));
		}

		return result;
	}
	// NOT NULL CON ELEMENTI DI PAGINAZIONE
	protected List<ErroreDettaglio> notNullCommonCheckForList(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo,
			String collocazioneCodice, String collocazioneDescrizione,Integer limit, Integer offset) throws DatabaseException {
		notNullCommonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);
		if (limit==null ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.LIMIT.getCode()));
		}

		if (offset==null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.OFFSET.getCode()));
		}
		return result;
	}
	// VERIFICA LA CONFORMITA' DEI CAMPI COMUNI
	protected List<ErroreDettaglio> complianceCommonCheck(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo)
			throws DatabaseException {

		if (formalCheckCF(shibIdentitaCodiceFiscale)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(),
					ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode(), shibIdentitaCodiceFiscale));
		}

		if (formalCheckCodiceVerticale(xCodiceVerticale)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(),
					ErrorParamEnum.X_CODICE_VERTICALE.getCode(), xCodiceVerticale));
		}

		if (formalCheckCodiceServizio(xCodiceServizio)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(),
					ErrorParamEnum.X_CODICE_SERVIZIO.getCode(), xCodiceServizio));
		}

		if (formalCheckPresenzaRuolo(ruolo, xCodiceServizio)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(), ErrorParamEnum.RUOLO.getCode(), ruolo));
		}

		return result;

	}
	
	protected List<ErroreDettaglio> complianceCommonCheckForList(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo,Integer limit, Integer offset)
			throws DatabaseException {
		complianceCommonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo);
		if(formalCheckLimit(limit)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_062.getCode(),
					ErrorParamEnum.LIMIT.getCode(), limit.toString()));
		}
		if(formalCheckOffset(offset)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_063.getCode(),
					ErrorParamEnum.OFFSET.getCode(), offset.toString()));
		}
		return result;
	}
	
	
	

}
