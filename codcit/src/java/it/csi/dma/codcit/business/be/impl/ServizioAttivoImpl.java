/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.business.be.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.dma.codcit.business.be.ServizioAttivoApi;
import it.csi.dma.codcit.dto.ModelInfoServizio;
import it.csi.dma.codcit.util.Constants;

public class ServizioAttivoImpl implements ServizioAttivoApi {

	@Override
	public Response servizioAttivoGet(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		var result = new ModelInfoServizio();
		result.setData(new Date());
		result.setDescrizione(Constants.COMPONENT_DESCRIPTION);
		result.setNome(Constants.COMPONENT_NAME_CAMMEL_CASE);
		result.setServizioAttivo(Constants.COMPONENT_ACTIVE);
		
		return Response.ok(result).build();
	}

}
