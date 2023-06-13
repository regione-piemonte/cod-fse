/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.filter;

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
public class ApiOpsanauraHeaderDecorator implements ContainerRequestFilter {
	private static final String X_FORWARDED_FOR = "X-Forwarded-For";
	private static final Logger logger = Logger.getLogger(ApiOpsanauraHeaderDecorator.class);

	@Context
	HttpServletRequest webRequest;

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {
		logger.info("[ApiOpsanauraHeaderDecorator::filter] BEGIN");
		try {
			if (!ctx.getHeaders().containsKey(X_FORWARDED_FOR)) {
				ctx.getHeaders().add(X_FORWARDED_FOR, webRequest.getRemoteAddr());
				logger.info("[ApiOpsanauraHeaderDecorator::filter] Viene aggiunto X_FORWARDED_FOR  :: "
						+ webRequest.getRemoteAddr());
			} else {
				String xff = ctx.getHeaders().getFirst(X_FORWARDED_FOR);
				if (!xff.contains(webRequest.getRemoteAddr())) {
					xff += ", " + webRequest.getRemoteAddr();
					ctx.getHeaders().put(X_FORWARDED_FOR, Arrays.asList(xff));
				}
				logger.info("[ApiOpsanauraHeaderDecorator::filter] X_FORWARDED_FOR presente :: " + xff);

			}

		} catch (Exception e) {
			logger.warn("[ApiOpsanauraHeaderDecorator::filter] ", e);
		}

		logger.info("[ApiOpsanauraHeaderDecorator::filter] END");

	}
}