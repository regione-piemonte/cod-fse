/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.validator.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import it.csi.dma.codcit.dto.ModelAutorePayload;
import it.csi.dma.codcit.dto.ModelMedico;
import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.util.enumerator.ErrorParamEnum;
import it.csi.dma.codcit.util.validator.ValidateMedicoArgomentoAutoreGeneric;

@Service("validateMedicoArgomentoAutoreGeneric")
public class ValidateGenericMeritWhitMedicoArgomentoAutoreImpl extends ValidateGenericMeritImpl
		implements ValidateMedicoArgomentoAutoreGeneric {

	@Override
	public List<ErroreDettaglioExt> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String citId, ModelMedico medico, ModelAutorePayload autore, String argomento)
			throws DatabaseException {
		List<ErroreDettaglioExt> result = super.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
				xCodiceServizio, citId);
		  
		
		if(!checkIfEmptyCod050(result, medico, ErrorParamEnum.MEDICO)) {
			checkIfEmptyCod050(result, medico.getCodiceFiscale(), ErrorParamEnum.MEDICO_CF_MEDICO);
			checkIfEmptyCod050(result, medico.getNome(), ErrorParamEnum.MEDICO_NOME);
			checkIfEmptyCod050(result, medico.getCognome(), ErrorParamEnum.MEDICO_COGNOME);

		}
		checkIfEmptyCod050(result, argomento, ErrorParamEnum.ARGOMENTO);
		
		if (!checkIfEmptyCod050(result, autore, ErrorParamEnum.AUTORE)) {
			checkIfEmptyCod050(result, autore.getCodiceFiscale(), ErrorParamEnum.AUTORE_CODICE_FISCALE);
			/*checkIfEmptyCod050(result, autore.getNome(), ErrorParamEnum.AUTORE_NOME);
			checkIfEmptyCod050(result, autore.getCognnome(), ErrorParamEnum.AUTORE_COGNOME);*/
		}
		if (result.isEmpty()) {
			formalCheckCfCod051(result, medico.getCodiceFiscale(), ErrorParamEnum.MEDICO_CF_MEDICO);
			formalCheckCfCod051(result, autore.getCodiceFiscale(), ErrorParamEnum.AUTORE_CODICE_FISCALE);
		}

		return result;
	}
}
