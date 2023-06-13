/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.validator.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.util.enumerator.ErrorParamEnum;
import it.csi.dma.codcit.util.validator.ValidateCfMedicoGeneric;

@Service("validateCfMedicoGeneric")
public class ValidateGenericMeritWhitCfMedicoImpl extends ValidateGenericMeritImpl implements ValidateCfMedicoGeneric {

	@Override
	public List<ErroreDettaglioExt> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String citId, String cfMedico) throws DatabaseException {
		List<ErroreDettaglioExt> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				citId);
		
		checkIfEmptyCod050(result, cfMedico, ErrorParamEnum.CF_MEDICO);

		if (result.isEmpty()) {
			formalCheckCfCod051(result, cfMedico, ErrorParamEnum.CF_MEDICO);
		}
		
		return result;
	}

}
