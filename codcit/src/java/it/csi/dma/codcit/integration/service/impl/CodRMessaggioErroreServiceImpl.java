/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.dto.custom.MessaggioErrore;
import it.csi.dma.codcit.integration.dao.custom.CodRMessaggioErroreDao;
import it.csi.dma.codcit.integration.service.CodRMessaggioErroreService;
import it.csi.dma.codcit.util.Constants;
import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.LoggerUtil;
import it.csi.dma.codcit.util.enumerator.CodeErrorEnum;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CodRMessaggioErroreServiceImpl extends LoggerUtil implements CodRMessaggioErroreService {

	private static final int DEFAULT_COD_R_MESSAGGIO_ERRORE_ERRORE_ID = 1;
	@Autowired
	CodRMessaggioErroreDao codRMessaggioErroreDao;

	@Override
	public ErrorBuilder saveError(ErrorBuilder error, HttpServletRequest httpRequest) {
		var methodName = "saveError";
		try {
			List<MessaggioErrore> messaggioErroreList = new ArrayList<>();
			if (error.getDetal() != null && !error.getDetal().isEmpty()) {
				
				for (ErroreDettaglioExt erroreDettaglio : error.getDetal()) {
					var messaggioErrrore = new MessaggioErrore();
					messaggioErrrore.setInformazioniAggiuntive(erroreDettaglio.getValore());
					messaggioErrrore.setErroreId(erroreDettaglio.getErroreId()!=null?erroreDettaglio.getErroreId():DEFAULT_COD_R_MESSAGGIO_ERRORE_ERRORE_ID);
					Object contextChiaveId = httpRequest.getAttribute(Constants.CONTEXT_CHIAVE_ID);
					if(contextChiaveId instanceof Long) {
						messaggioErrrore.setMessaggioId((Long) contextChiaveId);
					} else {
						logError(methodName, "non trovato messaggio errore id inserito default 0 ");
						messaggioErrrore.setMessaggioId(null);
					}
					
					messaggioErroreList.add(messaggioErrrore);
				}
				codRMessaggioErroreDao.insert(messaggioErroreList);

			}
		} catch (Exception e) {
			logError(methodName, "errore in salvataggio errore dettaglio",e.getMessage());
			var erroreDettaglio = new ErroreDettaglioExt();
			erroreDettaglio.setValore(e.getMessage());
			erroreDettaglio.setChiave(CodeErrorEnum.FSE_COD_FATAL.getCode());
			error.detail(erroreDettaglio);
		}
		return error;

	}
}
