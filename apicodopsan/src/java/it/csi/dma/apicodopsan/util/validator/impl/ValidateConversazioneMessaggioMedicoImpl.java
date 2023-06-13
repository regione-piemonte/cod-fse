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
import it.csi.dma.apicodopsan.dto.ModelMessaggioNuovo;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTMessaggioAllegatoCustomDao;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.ErrorParamEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateConversazioneMessaggioMedico;
/**
 * Verifica utente abilitato estratto dalla classe di validationeGerericMerit
 * @author 2132
 *
 */
@Service
public class ValidateConversazioneMessaggioMedicoImpl extends BaseValidate implements ValidateConversazioneMessaggioMedico {

	@Autowired
	CodTMessaggioAllegatoCustomDao codTMessaggioAllegatoCustomDao;

	@Override
	public List<ErroreDettaglio> validate(Long idConversazione, String shibIdentitaCodiceFiscale, String idMessaggio)
			throws DatabaseException {
		var methodName = "validate";
		logInfo(methodName, "BEGIN");
		List<ErroreDettaglio> result = new ArrayList<>();
		Integer checkMessaggioEsistente = codTMessaggioAllegatoCustomDao.checkMessaggioEsistente(idMessaggio);
		if(checkMessaggioEsistente==null || checkMessaggioEsistente ==0) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_091.getCode(),
					ErrorParamEnum.MESSAGGIO_NON_PRESENTE.getCode()));
		}else {
			ModelMessaggioNuovo mess= codTMessaggioAllegatoCustomDao.checkMessaggioProprietario(idMessaggio,shibIdentitaCodiceFiscale,idConversazione);
			if(mess==null) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_091.getCode(),
						ErrorParamEnum.MESSAGGIO_NON_PROPRIETARIO.getCode()));
			}else if( !mess.isModificabile().booleanValue()) {
				result.add(getValueGenericError(CodeErrorEnum.FSE_COD_091.getCode(),
						ErrorParamEnum.MESSAGGIO_NON_MODIFICABILE.getCode()));
			}
			
		}
		
		
		return result;
	}
	
}
