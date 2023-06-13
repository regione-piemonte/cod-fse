/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.interceptor.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.common.util.StringUtils;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.dma.apicodopsan.dto.custom.LMessaggi;
import it.csi.dma.apicodopsan.integration.service.CodLMessaggiService;
import it.csi.dma.apicodopsan.util.Constants;
import it.csi.dma.apicodopsan.util.LoggerUtil;




@Provider
@ServerInterceptor
public class TraceRequestInterceptor extends LoggerUtil implements ContainerRequestFilter, ContainerResponseFilter {
	private @Context HttpServletRequest httpRequest;
	private @Context HttpServletResponse response;
	private @Context SecurityContext secContext;

	/**
	 * Verificare se corretto innestare il dao qui o se meglio mettere un BEAN che si occupi di 'slegare' la tracciatura su database
	 */

	CodLMessaggiService codLMessaggiDao = null;

	private void getCodLMessaggiDao() {
		if(codLMessaggiDao == null) {
			codLMessaggiDao = WebApplicationContextUtils.getRequiredWebApplicationContext( httpRequest .getServletContext()).getBean(CodLMessaggiService.class);
		}
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		getCodLMessaggiDao();
		final String methodName =  "filter";
		LMessaggi lMessaggi = new LMessaggi();

		try {
			lMessaggi.setCfRichiedente(httpRequest.getHeader("Shib-Identita-CodiceFiscale"));
			lMessaggi.setxRequestId(httpRequest.getHeader("X-Request-ID"));
			lMessaggi.setComponente(Constants.COMPONENT_NAME);
			lMessaggi.setIpChiamante(httpRequest.getHeader("X-Forwarded-For"));
			lMessaggi.setxCodiceServizio(httpRequest.getHeader("X-Codice-Servizio"));
			lMessaggi.setMetodo(httpRequest.getMethod());
			lMessaggi.setRequestUri(httpRequest.getRequestURI() + (StringUtils.isEmpty(httpRequest.getQueryString())?"":"?"+httpRequest.getQueryString()));
			lMessaggi.setSoggettoCf(getRichiedenteFromURL(httpRequest.getRequestURI()));
			if(secContext.getUserPrincipal() != null) {
				lMessaggi.setCfChiamante(secContext.getUserPrincipal().getName());
			}

			if (isJson(requestContext)) {
	            try {
	                String json = IOUtils.toString(requestContext.getEntityStream(), StandardCharsets.UTF_8);
	                lMessaggi.setRequestPayload(json.getBytes());
	                InputStream in = IOUtils.toInputStream(json, StandardCharsets.UTF_8);
	                requestContext.setEntityStream(in);

	            } catch (IOException ex) {
	            	logError(methodName, "IOUtils exception:", ex.getMessage());
	            }
	        }

			long id = codLMessaggiDao.insertMessaggi(lMessaggi);
			httpRequest.setAttribute(Constants.CONTEXT_CHIAVE_ID, Long.valueOf(id));
			httpRequest.setAttribute(Constants.CONTEXT_TEMPO_PARTENZA, Long.valueOf(System.currentTimeMillis()));
		} catch (Exception e) {
			logError(methodName, "generic exception in request:", e.getMessage());
		}


	}


   private boolean isJson(ContainerRequestContext request) {
        return request.getMediaType() != null && request.getMediaType().toString().contains("application/json");
    }




	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		getCodLMessaggiDao();
		final String methodName =  "filter";
		try {

			Long chiave = (Long) httpRequest.getAttribute(Constants.CONTEXT_CHIAVE_ID);
			Long tempoPartenza = (Long) httpRequest.getAttribute(Constants.CONTEXT_TEMPO_PARTENZA);


			if(chiave != null) {
				LMessaggi lMessaggi = new LMessaggi();
				lMessaggi.setEsitoChiamata(responseContext.getStatus());
				lMessaggi.setMsResponse(System.currentTimeMillis() - tempoPartenza);
				Object entity = responseContext.getEntity();
				try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
					/**
					 * TODO verificare se corretto prenderlo sempre o se fare un isJson come per la request
					 */
					ObjectMapper mapper = new ObjectMapper();
					mapper.writeValue(bos, entity);
					lMessaggi.setResponsePayload(bos.toByteArray());
				} catch (Exception e) {
					logError(methodName, "errore in ByteArrayOutputStream:", e.getMessage());
				}
				codLMessaggiDao.updateMessaggi(lMessaggi, chiave);
			}

		} catch (Exception e) {
			logError(methodName, "generic exception in response:", e.getMessage());
		}

	}


	private static String getRichiedenteFromURL(String url) {
		String[] regexs = new String[] {".*\\/cittadini\\/(.*?)\\/.*", ".*\\/utenti\\/(.*?)\\/.*"};

		for (String regex : regexs) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(url);
			if(matcher.matches()) {
				return matcher.group(1);
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private String getHeaderParam(HttpHeaders httpHeaders, String headerParam) {
		List<String> values = httpHeaders.getRequestHeader(headerParam);
		if (values == null || values.isEmpty()) {
			return null;
		}
		return values.get(0);
	}
}
