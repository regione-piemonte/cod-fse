/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.exception;

import it.csi.dma.apiopsanaura.util.ErrorBuilder;

public class ResponseErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2134345368882934388L;

	private final transient ErrorBuilder responseError;
	
	public ResponseErrorException(ErrorBuilder errorBuilder, Throwable message) {
		super(message);
		this.responseError = errorBuilder;
	}

	public ResponseErrorException(ErrorBuilder errorBuilder, String message) {
		super(message);
		this.responseError = errorBuilder;
	}

	@SuppressWarnings("unused")
	private ResponseErrorException() {
		this.responseError = null;
	}

	public ErrorBuilder getResponseError() {
		return responseError;
	}
}
