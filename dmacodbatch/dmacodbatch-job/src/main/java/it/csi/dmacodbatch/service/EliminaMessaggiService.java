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

import it.csi.dmacodbatch.entity.RispostaFunzione;
import it.csi.dmacodbatch.exception.BusinessException;
import it.csi.dmacodbatch.repository.FunctionCallRepository;
import it.csi.dmacodbatch.util.Command;
import it.csi.dmacodbatch.util.CommandParameter;
import it.csi.dmacodbatch.util.CommandResult;
import it.csi.dmacodbatch.util.VoidCommandResult;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class EliminaMessaggiService implements Command{
	Logger logger = LoggerFactory.getLogger(EliminaMessaggiService.class);

	@Autowired
	FunctionCallRepository repository;
	
	@Override
	public CommandResult execute(CommandParameter... parameters) throws BusinessException {
		List<RispostaFunzione> risposta = repository.eliminaConversazioniMessaggi();
		logger.info("risposta: " + risposta);
		if(risposta.size() != 1 || risposta.get(0).getCodice()==null || risposta.get(0).getCodice()!=0) {
			throw new BusinessException("Batch non andato a buon fine");
		}
		return new VoidCommandResult();
	}

	
}
