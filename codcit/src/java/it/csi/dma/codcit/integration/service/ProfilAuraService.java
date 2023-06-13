/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.aura.auraws.services.central.anagrafefind.AnagrafeFindSoap;
import it.csi.aura.auraws.services.central.anagrafefind.DatiAnagraficiMsg;
import it.csi.aura.auraws.services.central.anagrafefind.FindProfiliAnagraficiRequest;
import it.csi.aura.auraws.services.central.anagrafefind.Message;
import it.csi.aura.auraws.services.central.anagrafesanitaria.AnagrafeSanitariaSoap;
import it.csi.aura.auraws.services.central.anagrafesanitaria.GetProfiloSanitarioResponse;
import it.csi.aura.auraws.services.central.anagrafesanitaria.SoggettoAuraMsg;
import it.csi.dma.codcit.exception.AuraFallimentoException;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.LoggerUtil;
import it.csi.dma.codcit.util.enumerator.CodeErrorEnum;
import it.csi.dma.codcit.util.enumerator.StatusEnum;

@Component
public class ProfilAuraService extends LoggerUtil{

	public static final String AURA_COD_PREFIX = "AURA-COD-";
	public static final String AURA_ERROR_E0010 = "E0010";

	@Autowired
	CodDErroreService codDErroreService;

	@Autowired
	@Qualifier("auraFindService")
	AnagrafeFindSoap auraFindService;

	@Autowired
	@Qualifier("auraGetService")
	AnagrafeSanitariaSoap auraGetService;


	public GetProfiloSanitarioResponse getProfiloAura(String cf) throws AuraFallimentoException, DatabaseException {
		final String METHOD_NAME = "ProfilAuraService::getProfiloAura";
		//find
		FindProfiliAnagraficiRequest f= new FindProfiliAnagraficiRequest();
		f.setCodiceFiscale(cf);
		f.setFlagDecesso("0");
		DatiAnagraficiMsg auraFind= auraFindService.findProfiliAnagrafici(f);
		if (auraFind!=null && auraFind.getBody()!=null && auraFind.getBody().getElencoProfili()!=null && auraFind.getBody().getElencoProfili().getDatianagrafici()!=null && auraFind.getBody().getElencoProfili().getDatianagrafici().size()>0) {
			if(auraFind.getBody().getElencoProfili().getDatianagrafici().get(0).getIdProfiloAnagrafico()!=null) {
				//get
				SoggettoAuraMsg profilo=auraGetService.getProfiloSanitario(auraFind.getBody().getElencoProfili().getDatianagrafici().get(0).getIdProfiloAnagrafico()+"");
				if(profilo!=null && profilo.getBody()!=null && profilo.getBody().getInfoSan()!=null && profilo.getBody().getInfoSan().getCodiceFiscaleMedico()!=null && profilo.getBody().getInfoSan().getTipoMovimento()!=null && profilo.getBody().getInfoSan().getTipoMovimento().equalsIgnoreCase("S")) {
					//trovato info sanitarie
					GetProfiloSanitarioResponse response=new GetProfiloSanitarioResponse();
					response.setGetProfiloSanitarioResult(profilo);
					return response;
				} else {
					//non esiste il tag per le info sanitarie restituiamo 404
					var errorBuilder = ErrorBuilder.from(StatusEnum.NOT_FOUND);
					throw new AuraFallimentoException(errorBuilder.detail(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_092.getCode())),"Errore Aura non esiste il medico per il cf:"+cf);
				}
			}
		} else {
			logInfo(METHOD_NAME, "Aura ha restituito un errore ");
			var errorBuilder = ErrorBuilder.from(StatusEnum.SERVER_ERROR_AURA);
			if(auraFind!=null&& auraFind.getFooter()!=null&&auraFind.getFooter().getMessages()!=null&&auraFind.getFooter().getMessages().getMessage()!=null) {
				Optional<Message> mess = auraFind.getFooter().getMessages().getMessage().stream().filter(c -> c.getCodice().equals(AURA_ERROR_E0010)).findFirst();

				if(mess.isPresent()) {
					throw new AuraFallimentoException(errorBuilder.detail(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_079.getCode())),"Errore Aura");

	//				throw new AuraFallimentoException(errorBuilder.detail(codDErroreService.getValueGenericErrorFromExternalService(AURA_COD_PREFIX,
	//						AURA_ERROR_E0010, mess.get().getDescrizione())),"TODO");
				}
			} else {
				throw new AuraFallimentoException(errorBuilder.detail(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_080.getCode())),"Errore Aura");
			}
		}

		return null;
	}
}
