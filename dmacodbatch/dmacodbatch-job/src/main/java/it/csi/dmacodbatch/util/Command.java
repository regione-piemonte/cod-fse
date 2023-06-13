/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.util;

import it.csi.dmacodbatch.exception.BusinessException;

public interface Command {
	public CommandResult execute(CommandParameter... parameters) throws BusinessException;
}
