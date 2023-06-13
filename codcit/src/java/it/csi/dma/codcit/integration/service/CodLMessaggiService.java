/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.service;

import it.csi.dma.codcit.dto.custom.LMessaggi;

public interface CodLMessaggiService {

	long insertMessaggi(LMessaggi lMessaggi);

	int updateMessaggi(LMessaggi lMessaggi, Long id);

}