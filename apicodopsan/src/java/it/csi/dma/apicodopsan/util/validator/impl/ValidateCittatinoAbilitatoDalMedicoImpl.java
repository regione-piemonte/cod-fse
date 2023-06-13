/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoAbilitatoDao;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.ErrorParamEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateCittadinoAbilitatoDalMedico;
/**
 * Verifica utente abilitato estratto dalla classe di validationeGerericMerit
 * @author 2132
 *
 */
@Service
public class ValidateCittatinoAbilitatoDalMedicoImpl extends BaseValidate implements ValidateCittadinoAbilitatoDalMedico {

	@Autowired
	CodTSoggettoAbilitatoDao codTSoggettoAbilitatoDao;;
	

	@Override
	public List<ErroreDettaglio> validate(Integer idCittadino,Integer IdMedico) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");
		List<ErroreDettaglio> result = new ArrayList<>();
		if(codTSoggettoAbilitatoDao.selectSoggettoAbilitatoWhereCittadinoAbilitatoDaMedicoFromId(IdMedico, idCittadino) == 0) {
			logError(methodName, "Cittadino non abilitato");
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_075.getCode(),
					ErrorParamEnum.ID_CONVERSAZIONE.getCode()));
		}
		return result;
	}
	
}
