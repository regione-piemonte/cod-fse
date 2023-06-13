/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.custom.Soggetto;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.ErrorParamEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateCodiceFiscale;
/**
 * Verifica che il codice fiscale utilizzato sia disponibile in anagrafica
 * @author 2132
 *
 */
@Service
public class ValidateCodiceFiscaleImpl extends BaseValidate implements ValidateCodiceFiscale {

	@Autowired
	CodTSoggettoDao codTSoggettoDao;

	@Override
	public List<ErroreDettaglio> validate(String xCodiceVerticale, String xCodiceServizio, String codiceFiscale,
			String xForwardedFor, String xRequestId) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");
		List<ErroreDettaglio> result = new ArrayList<>();
		Soggetto soggetto=codTSoggettoDao.selectSoggettoByCF(codiceFiscale);
		if(soggetto==null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_073.getCode(),
					ErrorParamEnum.CODICE_FISCALE.getCode()));
		}
		return result;
	}
	
}
