/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.dto.custom.LMessaggi;
import it.csi.dma.apicodopsan.integration.dao.custom.CodLMessaggiDao;
import it.csi.dma.apicodopsan.integration.service.CodLMessaggiService;



@Service
public class CodLMessaggiServiceImpl implements CodLMessaggiService {
	
	@Autowired
	private CodLMessaggiDao codLMessaggiDao;

	@Override
	public long insertMessaggi(LMessaggi lMessaggi) {
		return codLMessaggiDao.insertMessaggi(lMessaggi);
	}

	@Override
	public int updateMessaggi(LMessaggi lMessaggi, Long id) {
		
		return codLMessaggiDao.updateMessaggi(lMessaggi, id);
	}
}
