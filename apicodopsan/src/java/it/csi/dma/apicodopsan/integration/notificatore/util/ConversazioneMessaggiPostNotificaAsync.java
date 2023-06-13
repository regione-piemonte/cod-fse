/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.notificatore.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import it.csi.dma.apicodopsan.dto.custom.Soggetto;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.apicodopsan.integration.notificatore.NotificatoreService;
import it.csi.dma.apicodopsan.integration.service.DelegatiService;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Component
public class ConversazioneMessaggiPostNotificaAsync  extends LoggerUtil{

	@Autowired
	NotificatoreService notificatoreService;
	@Autowired
	CodTSoggettoDao codTSoggettoDao;
	@Autowired
	DelegatiService delegatiService;
	
	@Async
	public void notifyAsync(String shibIdentitaCodiceFiscale,Integer soggettoAutore, String xRequestId,String xCodiceServizio)   {
			StringBuffer sb = new StringBuffer(" NOTIFICA INVIATA PER "+xRequestId+"- STATO NOTIFICA INVIO= ");
		try {
			Soggetto soggetto = codTSoggettoDao.selectSoggettoById(soggettoAutore);
			notificatoreService.notificaEventoMessaggioPost(shibIdentitaCodiceFiscale, soggetto,xRequestId);
			List<Soggetto> listaDelegati = delegatiService.cercaDelegati(xRequestId,  xCodiceServizio,  shibIdentitaCodiceFiscale,
			soggetto.getSoggettoCf());
			if(listaDelegati!=null && listaDelegati.size()>0) {
				for(Soggetto delegato:listaDelegati) {
					notificatoreService.notificaEventoMessaggioPostPerDelegato(shibIdentitaCodiceFiscale, delegato, soggetto.getSoggettoCf(),xRequestId);
				}
			}
			sb.append("OK");
		
		} catch (DatabaseException e1) {
			logError("notifyAsync", "Errore riguardante database:"+ e1.getMessage());
			sb.append("Errore riguardante database:"+e1.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logError("notifyAsync","Exception invio notifica:"+e.getCause() +" - "+e.getMessage());
			sb.append("Exception invio notifica:"+e.getCause() +" - "+e.getMessage());
		}
			logInfo("execute-notifyAsync", sb.toString());
	}
	
}
