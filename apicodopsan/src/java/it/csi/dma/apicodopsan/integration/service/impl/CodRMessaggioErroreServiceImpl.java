/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.custom.ErroreDettaglioExt;
import it.csi.dma.apicodopsan.dto.custom.MessaggioErrore;
import it.csi.dma.apicodopsan.integration.dao.custom.CodDErroreDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodRMessaggioErroreDao;
import it.csi.dma.apicodopsan.integration.service.CodRMessaggioErroreService;
import it.csi.dma.apicodopsan.util.Constants;
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CodRMessaggioErroreServiceImpl extends LoggerUtil implements CodRMessaggioErroreService {
	
	@Autowired
	private CodRMessaggioErroreDao codRMessaggioErroreDao;
	
	@Autowired
	private CodDErroreDao codDErroreDao;
 
	private static final int DEFAULT_COD_R_MESSAGGIO_ERRORE_ERRORE_ID = 1;
	
//	@Override
//	public void saveError(List<ErroreDettaglio> messaggioErroreList, HttpServletRequest httpRequest) {
//		var methodName = "saveError";
//		
//		try {
//			MessaggioErrore messaggioErroreDB = new MessaggioErrore();
//			if (messaggioErroreList != null && !messaggioErroreList.isEmpty()) {
//				
//				for (ErroreDettaglio erroreDettaglio : messaggioErroreList) {
//					
//					Integer errorId = codDErroreDao.selectErrorIdFromErrorCode(erroreDettaglio.getChiave());
//					messaggioErroreDB.setErroreId(errorId != null ? errorId : DEFAULT_COD_R_MESSAGGIO_ERRORE_ERRORE_ID);
//					messaggioErroreDB.setInformazioniAggiuntive(erroreDettaglio.getValore());
//					Object contextChiaveId = httpRequest.getAttribute(Constants.CONTEXT_CHIAVE_ID);
//					messaggioErroreDB.setMessaggioId(contextChiaveId instanceof Long ? (Long) contextChiaveId : null);
//					
//					if(messaggioErroreDB.getMessaggioId() == null)
//						logError(methodName, "non trovato messaggio errore id inserito default 0 ");
//
//					codRMessaggioErroreDao.insert(messaggioErroreDB);
//				}
//			}else {
//				messaggioErroreDB.setErroreId(DEFAULT_COD_R_MESSAGGIO_ERRORE_ERRORE_ID);
//				messaggioErroreDB.setInformazioniAggiuntive(codDErroreDao.selectErroreDescFromErroreCod(CodeErrorEnum.FSE_COD_FATAL.getCode()));
//				Object contextChiaveId = httpRequest.getAttribute(Constants.CONTEXT_CHIAVE_ID);
//				messaggioErroreDB.setMessaggioId(contextChiaveId instanceof Long ? (Long) contextChiaveId : null);
//
//				if(messaggioErroreDB.getMessaggioId() == null)
//					logError(methodName, "non trovato messaggio errore id inserito default 0 ");
//				
//				codRMessaggioErroreDao.insert(messaggioErroreDB);
//				
//			}
//		} catch (Exception e) {
//			logError(methodName, "errore in salvataggio errore dettaglio",e.getMessage());
//			var erroreDettaglio = new ErroreDettaglioExt();
//			erroreDettaglio.setValore(e.getMessage());
//			erroreDettaglio.setChiave(CodeErrorEnum.FSE_COD_FATAL.getCode());			
//		}
//
//
//	}
	
	@Override
	public ErrorBuilder saveError(ErrorBuilder error, HttpServletRequest httpRequest) {
		var methodName = "saveError";
		try {
			MessaggioErrore messaggioErroreDB = new MessaggioErrore();
			List<MessaggioErrore> messaggioErroreList = new ArrayList<>();
			if (error.getDetal() != null && !error.getDetal().isEmpty()) {
				
				for (ErroreDettaglio erroreDettaglio : error.getDetal()) {
					Integer errorId = codDErroreDao.selectErrorIdFromErrorCode(erroreDettaglio.getChiave());
					messaggioErroreDB.setErroreId(errorId != null ? errorId : DEFAULT_COD_R_MESSAGGIO_ERRORE_ERRORE_ID);
					messaggioErroreDB.setInformazioniAggiuntive(erroreDettaglio.getValore());
					Object contextChiaveId = httpRequest.getAttribute(Constants.CONTEXT_CHIAVE_ID);
					messaggioErroreDB.setMessaggioId(contextChiaveId instanceof Long ? (Long) contextChiaveId : null);
					
					if(messaggioErroreDB.getMessaggioId() == null)
						logError(methodName, "non trovato messaggio errore id inserito default 0 ");

					codRMessaggioErroreDao.insert(messaggioErroreDB);
				}
			}else {
				messaggioErroreDB.setErroreId(DEFAULT_COD_R_MESSAGGIO_ERRORE_ERRORE_ID);
				messaggioErroreDB.setInformazioniAggiuntive(codDErroreDao.selectErroreDescFromErroreCod(CodeErrorEnum.FSE_COD_FATAL.getCode()));
				Object contextChiaveId = httpRequest.getAttribute(Constants.CONTEXT_CHIAVE_ID);
				messaggioErroreDB.setMessaggioId(contextChiaveId instanceof Long ? (Long) contextChiaveId : null);

				if(messaggioErroreDB.getMessaggioId() == null)
					logError(methodName, "non trovato messaggio errore id inserito default 0 ");
				
				codRMessaggioErroreDao.insert(messaggioErroreDB);
				
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
