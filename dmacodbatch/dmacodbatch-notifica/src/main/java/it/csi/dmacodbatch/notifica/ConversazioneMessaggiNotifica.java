/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.notifica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dmacodbatch.notifica.core.NotificatoreService;
import it.csi.dmacodbatch.notifica.core.dto.NotificaSupport;
import it.csi.dmacodbatch.notifica.core.dto.custom.Soggetto;
import it.csi.dmacodbatch.notifica.core.enumeration.NotificatoreEventCode;
import it.csi.dmacodbatch.notifica.core.repository.CodTSoggettoDao;
import it.csi.dmacodbatch.notifica.core.service.DelegatiService;
import it.csi.dmacodbatch.notifica.core.util.DatabaseException;
import it.csi.dmacodbatch.notifica.core.util.LoggerUtil;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class ConversazioneMessaggiNotifica extends LoggerUtil {

	@Autowired
	NotificatoreService notificatoreService;
	@Autowired
	CodTSoggettoDao codTSoggettoDao;
	@Autowired
	DelegatiService delegatiService;

	public void notify(NotificaSupport notifica) {
		StringBuffer sb = new StringBuffer(" NOTIFICA INVIATA PER " + notifica.getRequestId() + "- STATO NOTIFICA INVIO= ");
		try {
			Soggetto soggetto = codTSoggettoDao.selectSoggettoById(notifica.getSoggettoRicevente());
			notificatoreService.notificaEventoMessaggio(notifica.getShibIdentitaCodiceFiscale(), soggetto, notifica.getRequestId(), notifica.getCodeNotificaPrincipale());
			List<Soggetto> listaDelegati = delegatiService.cercaDelegati(notifica.getRequestId(), notifica.getCodiceServizio(),
					notifica.getShibIdentitaCodiceFiscale(), soggetto.getSoggettoCf());
			if (listaDelegati != null && listaDelegati.size() > 0) {
				for (Soggetto delegato : listaDelegati) {
					notificatoreService.notificaEventoMessaggioPerDelegato(notifica.getShibIdentitaCodiceFiscale(), delegato, soggetto.getSoggettoCf(),
							notifica.getRequestId(), notifica.getCodeNotificaSecondario());
				}
			}
			sb.append("OK");

		} catch (DatabaseException e1) {
			logError("notifyAsync", "Errore riguardante database:" + e1.getMessage());
			sb.append("Errore riguardante database:" + e1.getMessage());
		} catch (Exception e) {
			logError("notifyAsync", "Exception invio notifica:" + e.getCause() + " - " + e.getMessage());
			sb.append("Exception invio notifica:" + e.getCause() + " - " + e.getMessage());
		}
		logInfo("execute-notifyAsync", sb.toString());
	}

}
