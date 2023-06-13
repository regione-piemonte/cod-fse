/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.csi.dma.apicodopsan.dto.Errore;
import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.custom.ErroreDettaglioExt;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;



public class ErrorBuilder {
	private Errore errore;
	private List<ErroreDettaglio> detail;

	
	private ErrorBuilder() {
		this.errore = new Errore();
		this.detail = new ArrayList<>();
	}
	
	public static Response generateResponseError(StatusEnum request, List<ErroreDettaglio> error) {
		
		Errore e = new Errore();
		e.setStatus(request.getStatus());
		e.setCode(request.getCode());
		e.setTitle(request.getTitle());
		e.setDetail(error);
		return Response.status(request.getStatus().intValue()).entity(e).build();
	}

	
	private Errore build() {
		var collect = detail.stream().collect(Collectors.toList());
		errore.setDetail(collect);
		return this.errore;
	}

	public ErrorBuilder link(String link) {
		if (errore.getLinks() == null) {
			errore.setLinks(new ArrayList<>());
		}
		errore.getLinks().add(link);
		return this;
	}

	public ErrorBuilder detail(ErroreDettaglio erroreDettaglio) {
		detail.add(erroreDettaglio);
		return this;
	}

	public ErrorBuilder detail(List<ErroreDettaglio> erroreDettaglio) {
		if (erroreDettaglio != null) {
									 
		  
					
			detail = Stream.concat(detail.stream(), erroreDettaglio.stream()).collect(Collectors.toList());

		}
		return this;
	}

	public ErrorBuilder status(Integer status) {
		errore.setStatus(status);
		return this;
	}

	public ErrorBuilder code(String code) {
		errore.setCode(code);
		return this;
	}

	public ErrorBuilder title(String title) {
		errore.setTitle(title);
		return this;
	}

	public List<ErroreDettaglio> getDetal() {
		return detail;
	}

	public static ErrorBuilder from(StatusEnum request) {
		return new ErrorBuilder().status(request.getStatus()).code(request.getCode()).title(request.getTitle());
	}

	public static Response generateResponseErrorProvider(StatusEnum request, List<ErroreDettaglio> error) {
		var returnError = new ErrorBuilder().status(request.getStatus()).code(request.getCode())
				.title(request.getTitle()).detail(error);
		return Response.status(request.getStatus().intValue()).entity(returnError.build())
				.type(MediaType.APPLICATION_JSON).build();

	}

	public static ErrorBuilder generateErrorBuilderError(StatusEnum request, List<ErroreDettaglio> error) {
		return new ErrorBuilder().status(request.getStatus()).code(request.getCode()).title(request.getTitle())
				.detail(error);
	}

	public Response generateResponseError() {
		return Response.status(errore.getStatus() != null ? errore.getStatus() : StatusEnum.SERVER_ERROR.getStatus())
				.entity(build()).build();
	}

}
