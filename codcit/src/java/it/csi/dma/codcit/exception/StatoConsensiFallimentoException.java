/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.exception;

import it.csi.dma.codcit.util.ErrorBuilder;

public class StatoConsensiFallimentoException extends ResponseErrorException {

	public StatoConsensiFallimentoException(ErrorBuilder response, Throwable messagge) {
		super(response, messagge);
	}
	
	public StatoConsensiFallimentoException(ErrorBuilder errorBuilder, String message) {
		super(errorBuilder, message);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
