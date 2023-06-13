/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.notificatore.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import it.csi.dma.apicodopsan.dto.custom.Soggetto;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.apicodopsan.integration.notificatore.NotificatoreMedicoService;
import it.csi.dma.apicodopsan.integration.service.DelegatiService;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Component
public class DisabilitaPazientiMedicoNotificaAsync  extends LoggerUtil{

	@Autowired
	NotificatoreMedicoService notificatoreMedicoService;
	@Autowired
	CodTSoggettoDao codTSoggettoDao;
	@Autowired
	DelegatiService delegatiService;
	
	@Async
	public void notifyDisabledAsync(String shibIdentitaCodiceFiscale, String xRequestId,String xForwardedFor,Integer numeroAssistitiDisabilitati)   {
		String methodName="notifyDisabledAsync";
		StringBuffer sb = new StringBuffer(" NOTIFICA INVIATA PER "+xRequestId+"- STATO NOTIFICA INVIO= ");
		try {
			Soggetto soggetto = codTSoggettoDao.selectSoggettoByCF(shibIdentitaCodiceFiscale);
				notificatoreMedicoService.notificaDisabilitazione(shibIdentitaCodiceFiscale, soggetto, numeroAssistitiDisabilitati, xRequestId, xForwardedFor);
			sb.append("OK");
		
		} catch (DatabaseException e1) {
			logError(methodName, "Errore riguardante database:"+ e1.getMessage());
			sb.append("Errore riguardante database:"+e1.getMessage());
		} catch (Exception e) {
			logError(methodName,"Exception invio notifica:"+e.getCause() +" - "+e.getMessage());
			sb.append("Exception invio notifica:"+e.getCause() +" - "+e.getMessage());
		}
			logInfo(methodName, sb.toString());
	}
	
	@Async
	public void notifyErrorAsync(String shibIdentitaCodiceFiscale, String xRequestId,String xForwardedFor)   {
		String methodName="notifyErrorAsync";
		StringBuffer sb = new StringBuffer(" NOTIFICA INVIATA PER "+xRequestId+"- STATO NOTIFICA INVIO= ");
		try {
			Soggetto soggetto = codTSoggettoDao.selectSoggettoByCF(shibIdentitaCodiceFiscale);
			notificatoreMedicoService.notificaDisabilitazioneErrore(methodName, soggetto, xRequestId, xForwardedFor);
			sb.append("OK");
		
		} catch (DatabaseException e1) {
			logError(methodName, "Errore riguardante database:"+ e1.getMessage());
			sb.append("Errore riguardante database:"+e1.getMessage());
		} catch (Exception e) {
			logError(methodName,"Exception invio notifica:"+e.getCause() +" - "+e.getMessage());
			sb.append("Exception invio notifica:"+e.getCause() +" - "+e.getMessage());
		}
			logInfo(methodName, sb.toString());
	}
	
	
	@Async
	public void notifyAlreadyDisabledAsync(String shibIdentitaCodiceFiscale, String xRequestId,String xForwardedFor,Integer numeroAssistitiDisabilitati,Integer numeroAssistitiGiaDisabilitati,Integer numeroAssistitiInErrore)   {
		String methodName="notifyAlreadyDisabledAsync";	
		StringBuffer sb = new StringBuffer(" NOTIFICA INVIATA PER "+xRequestId+"- STATO NOTIFICA INVIO= ");
		try {
			Soggetto soggetto = codTSoggettoDao.selectSoggettoByCF(shibIdentitaCodiceFiscale);
				notificatoreMedicoService.notificaDisabilitazioneGiaDisabilitati(shibIdentitaCodiceFiscale, soggetto, numeroAssistitiDisabilitati, numeroAssistitiGiaDisabilitati,numeroAssistitiInErrore, xRequestId, xForwardedFor);
			sb.append("OK");
		
		} catch (DatabaseException e1) {
			logError(methodName, "Errore riguardante database:"+ e1.getMessage());
			sb.append("Errore riguardante database:"+e1.getMessage());
		} catch (Exception e) {
			logError(methodName,"Exception invio notifica:"+e.getCause() +" - "+e.getMessage());
			sb.append("Exception invio notifica:"+e.getCause() +" - "+e.getMessage());
		}
			logInfo(methodName, sb.toString());
	}
	
}
