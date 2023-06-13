/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.PayloadAbilitazione;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.ErrorParamEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateMedicoAssistitiPost;

@Service
@Qualifier("validateMedicoAssistitiPost")
public class ValidateMedicoAssistitiPostImpl extends BaseValidate implements ValidateMedicoAssistitiPost {

	@Override
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadAbilitazione payloadAbilitazione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");
		/*
		 * 2. Verifica parametri in input (Criteri di validazione della richiesta) 2a)
		 * Obbligatorieta'
		 *
		 * Check errori comuni
		 */

		List<ErroreDettaglio> result = new ArrayList<>();
//		2a
		result = notNullCommonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
				xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);
		checkPayloadAbilitazione(payloadAbilitazione, result);
		if(result.size()>0) {
			return result;
		}else {
			//2b controllo conformita'
			complianceCommonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo);

		}
		return result;
	}

	private void checkPayloadAbilitazione(PayloadAbilitazione payloadAbilitazione, List<ErroreDettaglio> result)
			throws DatabaseException {
		if(payloadAbilitazione == null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.PAYLOAD_ABILITAZIONE.getCode()));
		}else {
//			if ((payloadAbilitazione.isAbilitazione()!=null&&!payloadAbilitazione.isAbilitazione())&&StringUtils.isBlank(payloadAbilitazione.getMotivazioneMedico()) ) {
//				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
//						ErrorParamEnum.MOTIVAZIONE_MEDICO.getCode()));
//			}

			if (payloadAbilitazione.getAssistiti()==null || payloadAbilitazione.getAssistiti().isEmpty()) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), ErrorParamEnum.LISTA_ASSISTITI.getCode()));
			}

			if (payloadAbilitazione.isAbilitazione()==null) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
						ErrorParamEnum.ABILITAZIONE.getCode()));
			}
		}
	}


}
