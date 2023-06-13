/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dmacodbatch.dto.BatchFineServiceCommandParameter;
import it.csi.dmacodbatch.exception.BusinessException;
import it.csi.dmacodbatch.exception.NotPossibleException;
import it.csi.dmacodbatch.repository.BatchEsecuzioneDetRepository;
import it.csi.dmacodbatch.repository.BatchEsecuzioneRepository;
import it.csi.dmacodbatch.repository.BatchStatoRepository;
import it.csi.dmacodbatch.repository.RichiestaBatchRepository;
import it.csi.dmacodbatch.util.Command;
import it.csi.dmacodbatch.util.CommandParameter;
import it.csi.dmacodbatch.util.CommandResult;
import it.csi.dmacodbatch.util.Constants;
import it.csi.dmacodbatch.util.VoidCommandResult;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class BatchFineService implements Command {

	Logger logger = LoggerFactory.getLogger(BatchFineService.class);

	@Autowired
	BatchEsecuzioneDetRepository resultRepository;
	@Autowired
	BatchStatoRepository batchStatoRepository;

	@Autowired
	BatchEsecuzioneRepository batchEsecuzioneRepository;
	@Autowired
	RichiestaBatchRepository richiestaRepository;

	@Override
	public CommandResult execute(CommandParameter... parameters) throws BusinessException {
		BatchFineServiceCommandParameter parameter = (BatchFineServiceCommandParameter) parameters[0];
		List<Integer> statoBatch = batchStatoRepository.cercaStatoBatchId(parameter.getStatoBatch());
		if (statoBatch.size() != 1) {
			throw new NotPossibleException("stato batch non trovato");
		}

		resultRepository.insertRisultatoBatch(parameter.getEsecuzione(), parameter.getNote(), statoBatch.get(0), Constants.UTENTE);
		logger.info("inserita risultati batch");

		batchEsecuzioneRepository.chiudiEsecuzione(parameter.getEsecuzione(), statoBatch.get(0));
		logger.info("chiusa esecuzione" + parameter.getEsecuzione());
		richiestaRepository.chiudiEsecuzione(parameter.getRequestId());
		logger.info("chiusa richiesta" + parameter.getRequestId());
		return new VoidCommandResult();
	}

}
