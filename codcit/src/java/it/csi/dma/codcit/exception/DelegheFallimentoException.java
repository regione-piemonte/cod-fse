/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.exception;

import it.csi.dma.codcit.util.ErrorBuilder;

public class DelegheFallimentoException extends ResponseErrorException {

	public DelegheFallimentoException(ErrorBuilder response, Throwable messagge) {
		super(response, messagge);
	}

	public DelegheFallimentoException(ErrorBuilder errorBuilder, String message) {
		super(errorBuilder, message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2134345368882934388L;
	
	

}
