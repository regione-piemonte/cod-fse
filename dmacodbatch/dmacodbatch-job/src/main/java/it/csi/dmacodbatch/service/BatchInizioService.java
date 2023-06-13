/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dmacodbatch.dto.BatchInizioServiceCommandResult;
import it.csi.dmacodbatch.exception.BusinessException;
import it.csi.dmacodbatch.exception.NotPossibleException;
import it.csi.dmacodbatch.repository.BatchEsecuzioneRepository;
import it.csi.dmacodbatch.repository.BatchRepository;
import it.csi.dmacodbatch.repository.BatchStatoRepository;
import it.csi.dmacodbatch.repository.RichiestaBatchRepository;
import it.csi.dmacodbatch.util.Command;
import it.csi.dmacodbatch.util.CommandParameter;
import it.csi.dmacodbatch.util.CommandResult;
import it.csi.dmacodbatch.util.Constants;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class BatchInizioService implements Command{
	Logger logger = LoggerFactory.getLogger(BatchInizioService.class);

	@Autowired
	BatchRepository batchRepository;
	@Autowired
	BatchStatoRepository batchStatoRepository;
	@Autowired
	BatchEsecuzioneRepository batchEsecuzioneRepository;
	@Autowired
	RichiestaBatchRepository richiestaBatchRepository;
	
	@Override
	public CommandResult execute(CommandParameter... parameters) throws BusinessException {
		List<Integer> batchId = batchRepository.cercaBatchId(Constants.CODICE_BATCH_DISABILITA_ASSISTITI);
		if(batchId==null || batchId.size()!=1) {
			throw new NotPossibleException("batch non trovato");
		}
		List<Integer> statoBatch = batchStatoRepository.cercaStatoBatchId(Constants.STATO_BATCH_OK);
		if(statoBatch==null || statoBatch.size()!=1) {
			throw new NotPossibleException("stato batch non trovato");
		}
		Date ultimaEsecuzioneDate = null;
		List<Date> ultimaEsecuzione = batchEsecuzioneRepository.cercaUltimaEsecuzione(batchId.get(0), statoBatch.get(0));
		if(ultimaEsecuzione==null || ultimaEsecuzione.size()>1) {
			throw new NotPossibleException("trovate troppe date di ultima esecuzione");
		}
		if(ultimaEsecuzione.size()==0) {
			ultimaEsecuzioneDate= null;
		}
		if(ultimaEsecuzione.size()==1) {
			ultimaEsecuzioneDate = ultimaEsecuzione.get(0);
			logger.info("data ultima esecuzione: "+ultimaEsecuzioneDate.toString());
		}
		Integer richiestaId = richiestaBatchRepository.insertRichiestaBatch(batchId.get(0), Constants.UTENTE);
		logger.info("inserita richiesta"+richiestaId);

		List<Integer> statoBatchInCorso = batchStatoRepository.cercaStatoBatchId(Constants.STATO_BATCH_INCORSO);
		if(statoBatch==null || statoBatch.size()!=1) {
			throw new NotPossibleException("stato batch non trovato");
		}
		Integer esecuzione = batchEsecuzioneRepository.inserisciNuovaEsecuzione(batchId.get(0),richiestaId, Constants.UTENTE, statoBatchInCorso.get(0));
		logger.info("inserita esecuzione"+esecuzione);
		return new BatchInizioServiceCommandResult(esecuzione, ultimaEsecuzioneDate, batchId.get(0), richiestaId);
	}
	
	
}
