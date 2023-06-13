/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dmacodbatch.dto.BatchFineServiceCommandParameter;
import it.csi.dmacodbatch.dto.BatchInizioServiceCommandResult;
//import it.csi.dmacodbatch.dto.NotificaSupport;
import it.csi.dmacodbatch.entity.ModelAssistito;
import it.csi.dmacodbatch.exception.BusinessException;
import it.csi.dmacodbatch.exception.NotPossibleException;
import it.csi.dmacodbatch.notifica.ConversazioneMessaggiNotifica;
import it.csi.dmacodbatch.notifica.core.dto.NotificaSupport;
import it.csi.dmacodbatch.repository.FunctionCallRepository;
import it.csi.dmacodbatch.repository.SoggettoRepository;
import it.csi.dmacodbatch.util.Command;
import it.csi.dmacodbatch.util.CommandParameter;
import it.csi.dmacodbatch.util.CommandResult;
import it.csi.dmacodbatch.util.Constants;
import it.csi.dmacodbatch.util.VoidCommandResult;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class DisabilitaAssistitiService implements Command {
	Logger logger = LoggerFactory.getLogger(DisabilitaAssistitiService.class);

	@Autowired
	FunctionCallRepository repository;
	@Autowired
	SoggettoRepository soggettoRepository;
	@Autowired
	BatchInizioService batchService;
	@Autowired
	BatchFineService batchFineService;
	@Autowired
	ConversazioneMessaggiNotifica conversazioneMessaggiNotifica;

//	@Autowired
//	JmsTemplate jmsTemplate;
	
	@Override
	public CommandResult execute(CommandParameter... parameters) throws BusinessException {
		BatchInizioServiceCommandResult result = (BatchInizioServiceCommandResult) batchService.execute();
		
		List<String> soggetti = null;
		if(result.getUltimaEsecuzione()!=null) {
			soggetti = soggettoRepository.getCodiciFiscali(result.getUltimaEsecuzione());
		}else {
			logger.info("Ultima esecuzione null!!");
			soggetti = soggettoRepository.getCodiciFiscali();
		}
		if (soggetti.size() == 1 && StringUtils.isNotBlank(soggetti.get(0))) {
			List<ModelAssistito> risposta = repository.callDisabilita(soggetti.get(0));
			logger.info("risposta: " + risposta);
			if (risposta == null || risposta.size() == 0) {
				throw new NotPossibleException("funzione disabilita non ha restituito nulla");
			} else {
				if (risposta.get(0).getCf() == null) {
					BatchFineServiceCommandParameter parameter = new BatchFineServiceCommandParameter(Constants.STATO_BATCH_KO, result.getBatchId(),
							risposta.get(0).getStato(), result.getEsecuzione(), result.getRequestId());
					batchFineService.execute(parameter);
					logger.info("funzione andata male cf null");
					
				} else {
					BatchFineServiceCommandParameter parameter = new BatchFineServiceCommandParameter(Constants.STATO_BATCH_OK, result.getBatchId(),
							"Batch andato a buon fine", result.getEsecuzione(), result.getRequestId());
					batchFineService.execute(parameter);
					logger.info("Batch finito con cf. Ora iva notifiche");
					
					for(ModelAssistito modelAssistito:risposta) {
						try {
							NotificaSupport message = new NotificaSupport();
							message.setShibIdentitaCodiceFiscale(modelAssistito.getCf());
							message.setCodeNotificaPrincipale("EVCOD30");
							message.setCodeNotificaSecondario("EVCOD31");
							UUID uuid =  UUID.randomUUID();
							message.setRequestId(uuid.toString());
							message.setCodiceServizio("SANMEDCOD");
							List<Long> riceventeList = soggettoRepository.selectSoggettoByCF(modelAssistito.getCf());
							if(riceventeList==null|| riceventeList.size() != 1 ) {
								throw new BusinessException("Troppi soggetti");
							}
							message.setSoggettoRicevente(riceventeList.get(0));
							logger.info("Invio notifica: "+ message);
//							jmsTemplate.convertAndSend("mailbox", message);
							conversazioneMessaggiNotifica.notify(message);
						} catch (Exception e) {
							logger.error("notifica non inviata",e);
						}
					}
				}
			}
		} else {
			BatchFineServiceCommandParameter parameter = new BatchFineServiceCommandParameter(Constants.STATO_BATCH_OK, result.getBatchId(),
					"Non ci sono CF da elaborare", result.getEsecuzione(), result.getRequestId());
			batchFineService.execute(parameter);
			logger.info("Non ci sono CF da elaborare");
		}
		
		
		return new VoidCommandResult();
	}

}
