/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util.validator.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;

@Service("validateLimitOffset")
public class ValidateLimitOffsetImpl extends BaseValidate {

	public List<ErroreDettaglioExt> validate(List<ErroreDettaglioExt> result, Integer limit, Integer offset) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");

		validateLimitOffset(result, limit, offset);
		return result;
	}

}
