/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import it.csi.dma.apiopsanaura.dto.ErroreDettaglio;
import it.csi.dma.apiopsanaura.dto.custom.TAdesione;
import it.csi.dma.apiopsanaura.exception.DatabaseException;
import it.csi.dma.apiopsanaura.util.enumerator.CodeErrorEnum;
import it.csi.dma.apiopsanaura.util.enumerator.ErrorParamEnum;
import it.csi.dma.apiopsanaura.util.validator.ValidateMedicoAdesione;

@Service
public class ValidateMedicoAdesioneImpl extends BaseValidate implements ValidateMedicoAdesione {



	@Override
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();
		
		TAdesione adesione = codTAdesioneDao.selectAdesione(shibIdentitaCodiceFiscale);
		if(adesione==null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_084.getCode(),
					ErrorParamEnum.ADESIONE_MEDICO.getCode()));
		}
		return result;
	}

	
	
}
