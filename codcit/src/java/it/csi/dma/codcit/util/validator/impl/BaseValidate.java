/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.validator.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.dma.codcit.dto.ErroreDettaglio;
import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.service.CodDErroreService;
import it.csi.dma.codcit.util.Constants;
import it.csi.dma.codcit.util.LoggerUtil;
import it.csi.dma.codcit.util.enumerator.CodeErrorEnum;
import it.csi.dma.codcit.util.enumerator.ErrorParamEnum;

public abstract class BaseValidate extends LoggerUtil {

	@Autowired
	CodDErroreService codDErroreService;

	public ErroreDettaglioExt getValueGenericError(String key, String... param) throws DatabaseException {

		return codDErroreService.getValueGenericError(key, param);
	}

	public ErroreDettaglioExt getValueGenericError(CodeErrorEnum codeError, ErrorParamEnum... errorParam)
			throws DatabaseException {
		if (errorParam == null || errorParam.length == 0) {
			return getValueGenericError(codeError.getCode());
		} else {
			String[] errorParamString = Arrays.stream(errorParam).map(ErrorParamEnum::getCode).toArray(String[]::new);
			return getValueGenericError(codeError.getCode(), errorParamString);

		}
	}

	protected boolean formalCheckCF(String cf) {
		return cf.length() < 11 || cf.length() > 16;
	}

	protected boolean checkIfEmptyCod050(List<ErroreDettaglioExt> result, Object objectToBeChecked,
			ErrorParamEnum errorParam) throws DatabaseException {
		boolean resultRet = false;
		if (objectToBeChecked == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), errorParam.getCode()));
			resultRet = true;
		}
		return resultRet;
	}

	protected void checkIfEmptyCod050(List<ErroreDettaglioExt> result, String valueToBeChecked,
			ErrorParamEnum errorParam) throws DatabaseException {
		if (StringUtils.isEmpty(valueToBeChecked)) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), errorParam.getCode()));
		}
	}

	protected void formalCheckCfCod051(List<ErroreDettaglioExt> result, String valueToBeChecked,
			ErrorParamEnum errorParam) throws DatabaseException {
		if (formalCheckCF(valueToBeChecked)) {
			result.add(
					getValueGenericError(CodeErrorEnum.FSE_COD_051.getCode(), errorParam.getCode(), valueToBeChecked));
		}
	}

	protected void checkIfEqualToFinalValueCod051(List<ErroreDettaglioExt> result, String xCodiceServizio,
			ErrorParamEnum errorParam, String finalValue) throws DatabaseException {
		if (!xCodiceServizio.equalsIgnoreCase(finalValue)) {
			result.add(
					getValueGenericError(CodeErrorEnum.FSE_COD_051.getCode(), errorParam.getCode(), xCodiceServizio));
		}
	}

	protected void checkifLessThanValue(List<ErroreDettaglioExt> result, Integer valueToBeChecked, CodeErrorEnum codeError,
			Integer lessThanThis, ErrorParamEnum... errorParam) throws DatabaseException {
		if (valueToBeChecked < lessThanThis) {
			result.add(getValueGenericError(codeError, errorParam));
		}
	}

	protected void checkifInsideArrayOfValue(List<ErroreDettaglioExt> result, String valueToBeChecked,
			CodeErrorEnum codeError, List<String> strings, ErrorParamEnum... errorParam) throws DatabaseException {
		if (!strings.contains(valueToBeChecked)) {
			result.add(getValueGenericError(codeError, errorParam));
		}
	}

	protected boolean formalCheckLimit(int limit) {
		return !(limit > 0);
	}

	protected boolean formalCheckOffset(int offset) {
		return !(offset >= 0);
	}

	protected boolean formalCheckCodiceServizio(String xCodiceServizio)  {

		if(xCodiceServizio.equals(Constants.CODICE_SERVIZIO_SANSOL)) {
			return false;
		} else {
			return true;
		}

	}

	// VALIDATROI COMMON
	// VERIFICA CHE NON SIANO NULLI I CAMPI COMUNI
		protected List<ErroreDettaglio> notNullCommonCheck(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
				String xRequestId, String xForwardedFor, String xCodiceServizio) throws DatabaseException {
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



			return result;
		}
		// NOT NULL CON ELEMENTI DI PAGINAZIONE
		protected List<ErroreDettaglioExt> validateLimitOffset(List<ErroreDettaglioExt> result, Integer limit, Integer offset) throws DatabaseException {
			int numErr=result.size();
			if (limit==null ) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
						ErrorParamEnum.LIMIT.getCode()));
			}

			if (offset==null) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
						ErrorParamEnum.OFFSET.getCode()));
			}
			//se sono stati creati altri errori interrompo controlli
			if (numErr<result.size()) {
				return result;
			}
			//continuo con i controlli
			if(formalCheckLimit(limit)) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_062.getCode(),
						ErrorParamEnum.LIMIT.getCode(), limit.toString()));
			}
			if(formalCheckOffset(offset)) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_063.getCode(),
						ErrorParamEnum.OFFSET.getCode(),offset.toString()));
			}
			return result;
		}
		// VERIFICA LA CONFORMITA' DEI CAMPI COMUNI
		protected List<ErroreDettaglio> complianceCommonCheck(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
				String xForwardedFor, String xCodiceServizio)
				throws DatabaseException {

			if (formalCheckCF(shibIdentitaCodiceFiscale)) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_051.getCode(),
						ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode(), shibIdentitaCodiceFiscale));
			}


			if (formalCheckCodiceServizio(xCodiceServizio)) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_051.getCode(),
						ErrorParamEnum.X_CODICE_SERVIZIO.getCode(), xCodiceServizio));
			}


			return result;

		}

//		protected List<ErroreDettaglio> complianceCommonCheckForList(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
//				String xForwardedFor, String xCodiceServizio,Integer limit, Integer offset)
//				throws DatabaseException {
//			complianceCommonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio);
//			if(formalCheckLimit(limit)) {
//				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_062.getCode(),
//						ErrorParamEnum.LIMIT.getCode(), limit.toString()));
//			}
//			if(formalCheckOffset(offset)) {
//				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_063.getCode(),
//						ErrorParamEnum.OFFSET.getCode(),offset.toString()));
//			}
//			return result;
//		}




}
