/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.exception;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.enumerator.StatusEnum;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

	@Override
	public Response toResponse(NotFoundException arg0) {
		return ErrorBuilder.generateResponseErrorProvider(StatusEnum.NOT_FOUND, null );
	}

}
