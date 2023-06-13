/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.validator.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.util.enumerator.CodeErrorEnum;
import it.csi.dma.codcit.util.enumerator.ErrorParamEnum;
import it.csi.dma.codcit.util.enumerator.SolaLetturaValueEnum;
import it.csi.dma.codcit.util.validator.ValidateGenericMeritWhitCfMedicoPaginazione;

@Service("ValidateGenericMeritWhitCfMedicoPaginazione")
public class ValidateGenericMeritWhitCfMedicoPaginazioneImpl extends ValidateGenericMeritImpl
		implements ValidateGenericMeritWhitCfMedicoPaginazione {

	@Override
	public List<ErroreDettaglioExt> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String citId, String cfMedico, String solaLettura, Integer limit, Integer offset)
			throws DatabaseException {
		List<ErroreDettaglioExt> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, citId);
		// --- errori del merito ---
		checkIfEmptyCod050(result, solaLettura, ErrorParamEnum.SOLA_LETTURA);
		checkIfEmptyCod050(result, limit, ErrorParamEnum.LIMIT);
		checkIfEmptyCod050(result, offset, ErrorParamEnum.OFFSET);

		if (result.isEmpty()) {

			if (cfMedico != null) {
				formalCheckCfCod051(result, cfMedico, ErrorParamEnum.CF_MEDICO);
			}
			checkifLessThanValue(result, limit, CodeErrorEnum.FSE_COD_062, 1);
			checkifLessThanValue(result, offset, CodeErrorEnum.FSE_COD_063, 0);
			checkifInsideArrayOfValue(result, solaLettura, CodeErrorEnum.FSE_COD_064,
					Arrays.asList(SolaLetturaValueEnum.SOLA_LETTURA_A.getCode(),
							SolaLetturaValueEnum.SOLA_LETTURA_B.getCode(),
							SolaLetturaValueEnum.SOLA_LETTURA_T.getCode()),
					ErrorParamEnum.SOLA_LETTURA);

		}

		return result;
	}
}
