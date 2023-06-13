/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

@Provider
@PreMatching
public class ApiCodopsanHeaderDecorator implements ContainerRequestFilter {
	private static final String X_FORWARDED_FOR = "X-Forwarded-For";
	private static final Logger logger = Logger.getLogger(ApiCodopsanHeaderDecorator.class);

	@Context
	HttpServletRequest webRequest;

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {
		logger.info("[ApiCodopsanHeaderDecorator::filter] BEGIN");
		try {
			if (!ctx.getHeaders().containsKey(X_FORWARDED_FOR)) {
				ctx.getHeaders().add(X_FORWARDED_FOR, webRequest.getRemoteAddr());
				logger.info("[ApiCodopsanHeaderDecorator::filter] Viene aggiunto X_FORWARDED_FOR  :: "
						+ webRequest.getRemoteAddr());
			} else {
				String xff = ctx.getHeaders().getFirst(X_FORWARDED_FOR);
				if (!xff.contains(webRequest.getRemoteAddr())) {
					xff += ", " + webRequest.getRemoteAddr();
					ctx.getHeaders().put(X_FORWARDED_FOR, Arrays.asList(xff));
				}
				logger.info("[ApiCodopsanHeaderDecorator::filter] X_FORWARDED_FOR presente :: " + xff);

			}

		} catch (Exception e) {
			logger.warn("[ApiCodopsanHeaderDecorator::filter] ", e);
		}

		logger.info("[ApiCodopsanHeaderDecorator::filter] END");

	}
}