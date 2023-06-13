/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.notificatore.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import it.csi.dma.codcit.dto.custom.Soggetto;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.codcit.integration.notificatore.NotificatoreMedicoService;
import it.csi.dma.codcit.integration.notificatore.NotificatoreService;
import it.csi.dma.codcit.integration.service.DelegatiService;
import it.csi.dma.codcit.util.LoggerUtil;

@Component
public class ConversazioneMessaggiPutNotificaAsync  extends LoggerUtil{

	@Autowired
	NotificatoreService notificatoreService;
	@Autowired
	CodTSoggettoDao codTSoggettoDao;
	@Autowired
	DelegatiService delegatiService;
	@Autowired
	NotificatoreMedicoService notificatoreMedicoService;
	
	@Async
	public void notifyAsync(String citId, Integer medicoId, String xRequestId,
			String xCodiceServizio, String xForwardedFor,String shibIdentitaCodiceFiscale) {
		StringBuffer sb = new StringBuffer(" NOTIFICA INVIATA PER "+xRequestId+"- STATO NOTIFICA INVIO= ");
		try {
			boolean isDelegato=false;
			Soggetto soggettoMedico = codTSoggettoDao.selectSoggettoById(medicoId);
			notificatoreMedicoService.notificaEventoMessaggioPut(citId, soggettoMedico, xRequestId,
					xForwardedFor);
			if(!shibIdentitaCodiceFiscale.equals(citId)) {
				Soggetto soggettoCittadino = codTSoggettoDao.selectSoggettoByCF(citId);
				notificatoreService.notificaEventoMessaggioPut(shibIdentitaCodiceFiscale, soggettoCittadino,xRequestId);
				isDelegato=true;
			}
			
			List<Soggetto> listaDelegati = delegatiService.cercaDelegati(xRequestId, xCodiceServizio,
					citId, citId);
			if (listaDelegati != null && listaDelegati.size() > 0) {
				for (Soggetto delegato : listaDelegati) {
					if(!(isDelegato && delegato.getSoggettoCf().equals(shibIdentitaCodiceFiscale))) {
					notificatoreService.notificaEventoMessaggioPut(citId, delegato,xRequestId);
					}
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
