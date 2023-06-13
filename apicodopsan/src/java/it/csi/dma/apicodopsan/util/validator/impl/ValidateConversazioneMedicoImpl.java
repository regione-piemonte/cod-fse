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
import it.csi.dma.apicodopsan.dto.custom.TConversazione;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTConversazioneDao;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.ErrorParamEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateConversazioneMedico;
/**
 * Verifica utente abilitato estratto dalla classe di validationeGerericMerit
 * @author 2132
 *
 */
@Service
public class ValidateConversazioneMedicoImpl extends BaseValidate implements ValidateConversazioneMedico {

	@Autowired
	CodTConversazioneDao codTconversazioneDao;
	
	@Override
	public List<ErroreDettaglio> validate(String idConversazione, String shibIdentitaCodiceFiscale, boolean checkBloccoConversazione)
			throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");
		List<ErroreDettaglio> result = new ArrayList<>();
		List<TConversazione> listaConversazioni=codTconversazioneDao.selectListConversazioneByCodAndSoggettoCf(idConversazione, shibIdentitaCodiceFiscale);
		if(listaConversazioni==null || listaConversazioni.isEmpty()) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_081.getCode(),
					ErrorParamEnum.ID_CONVERSAZIONE.getCode()));
		}else if(checkBloccoConversazione) {
			TConversazione conv = listaConversazioni.get(0);
			if(conv.getConversazioneDataBlocco()!= null) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_085.getCode(),
						ErrorParamEnum.ID_CONVERSAZIONE.getCode()));
			}
		}
		return result;
	}
	
}
