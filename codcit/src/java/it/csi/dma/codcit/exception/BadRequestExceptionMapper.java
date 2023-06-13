/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.BadRequestException;

import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.enumerator.StatusEnum;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

	@Override
	public Response toResponse(BadRequestException arg0) {
		return ErrorBuilder.generateResponseErrorProvider(StatusEnum.BAD_REQUEST, null );
	}
	

}
