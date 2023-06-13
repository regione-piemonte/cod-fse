/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.notifica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import it.csi.dmacodbatch.notifica.core.dto.NotificaSupport;
import it.csi.dmacodbatch.notifica.core.util.LoggerUtil;


//@Component
//public class MessageReceiver extends LoggerUtil{
//	
//	@Autowired
//	ConversazioneMessaggiNotifica conversazioneMessaggiNotifica;
//	
//	@JmsListener(destination = "mailbox")
//	public void receiveMessage(NotificaSupport paramter) {
//		logInfo("Received", paramter.toString());
//		conversazioneMessaggiNotifica.notify(paramter);
//	}
//
//}
