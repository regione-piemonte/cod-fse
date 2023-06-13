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
import it.csi.dma.apicodopsan.integration.dao.custom.CodTRichiestaBatchDao;
import it.csi.dma.apicodopsan.util.validator.ValidateMedicoBatchAssistiti;

@Service
public class ValidateMedicoBatchAssistitiImpl extends BaseValidate implements ValidateMedicoBatchAssistiti {

//	DAO COD_T_RICHIESTA_BATCH 
	
	@Autowired
	CodTRichiestaBatchDao codTRichiestaBatchDao;
	@Override
	public List<ErroreDettaglio> validate(String shibIdentitaCodiceFiscale) throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();
		Integer richiesteMedico= codTRichiestaBatchDao.countRichiesteMedico(shibIdentitaCodiceFiscale);
		Integer richiesteMedicoEseguite=codTRichiestaBatchDao.countRichiesteConEsecuzioniMedico(shibIdentitaCodiceFiscale);
//		Integer richiesteMedicoEseguiteInErrore = codTRichiestaBatchDao.countRichiesteConEsecuzioniAndStatoErrorMedico(shibIdentitaCodiceFiscale, "ERR");
//		if(richiesteMedico>0 && richiesteMedicoEseguite>0 &&  richiesteMedico==richiesteMedicoEseguite && richiesteMedicoEseguiteInErrore==0) {
//			FSE-COD-087
//		}
		
		
		
		
		
		return result;
	}

	

}
