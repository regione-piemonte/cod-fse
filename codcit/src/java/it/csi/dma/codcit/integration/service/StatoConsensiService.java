/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.exception.StatoConsensiFallimentoException;
import it.csi.dma.codcit.integration.service.util.StatoConsensi;
import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.LoggerUtil;
import it.csi.dma.codcit.util.enumerator.StatusEnum;
import it.csi.statoconsensi.dma.ApplicazioneRichiedente;
import it.csi.statoconsensi.dma.Paziente;
import it.csi.statoconsensi.dma.RegimeDMA;
import it.csi.statoconsensi.dma.RichiedenteExt;
import it.csi.statoconsensi.dma.RisultatoCodice;
import it.csi.statoconsensi.dma.RuoloDMA;
import it.csi.statoconsensi.dma.StatoConsensiIN;
import it.csi.statoconsensi.dma.StatoConsensiOUT;
import it.csi.statoconsensi.dmacc.CCConsensoINIExtServicePortType;
import it.csi.statoconsensi.dmacc.StatoConsensiExtRequeste;
import it.csi.statoconsensi.dmacc.StatoConsensiResponse;

@Service
public class StatoConsensiService extends LoggerUtil {
	
	@Autowired
	CCConsensoINIExtServicePortType cCConsensoINIExtServicePortType;
	
	
	public StatoConsensiOUT getStatoConsensi(String cf, String shibIdentitaCodiceFiscale, String xCodiceServizio, String xRequestId) throws StatoConsensiFallimentoException, DatabaseException{
		
		StatoConsensiExtRequeste request = new StatoConsensiExtRequeste();
		
		//TODO Verificare la composizione della request
		StatoConsensiIN statoConsensiIn = new StatoConsensiIN();
		
		//statoConsensiIn.setContestoOperativo(CostantiConsenso.CONTESTO_OPERATIVO);
		statoConsensiIn.setIdentificativoAssistitoConsenso(cf); //path param
		statoConsensiIn.setIdentificativoAssistitoGenitoreTutore(shibIdentitaCodiceFiscale);//identita
		statoConsensiIn.setIdentificativoOrganizzazione(StatoConsensi.IDENTIFICATIVO_ORGANIZZAZIONE.toString());
		statoConsensiIn.setIdentificativoUtente(shibIdentitaCodiceFiscale); //identita
		statoConsensiIn.setStrutturaUtente(StatoConsensi.STRUTTURA_UTENTE.toString());
		//statoConsensiIn.setRuoloUtente(CostantiConsenso.RUOLO_UTENTE);
		//statoConsensiIn.setPresaInCarico(CostantiConsenso.PRESA_IN_CARICO);
		statoConsensiIn.setTipoAttivita(StatoConsensi.TIPO_ATTIVITA.toString());
		 
		request.setPaziente(newPaziente(cf));// cf path param
		//TODO verificare il consenso
		request.setRichiedente(newRichiedenteConsenso(cf, shibIdentitaCodiceFiscale, xCodiceServizio, xRequestId)); //cf identita
		request.setStatoConsensiIN(statoConsensiIn);
	

		//prima faccio la chiamata allo statoconsensi per recuperare l'informativa corrente
		StatoConsensiResponse responseStatoConsensi = cCConsensoINIExtServicePortType.statoConsensi(request);
		
		//TODO rivedere l'errore
		if(responseStatoConsensi.getEsito().equals(RisultatoCodice.FALLIMENTO)) {
			throw new StatoConsensiFallimentoException(ErrorBuilder.from(StatusEnum.SERVER_ERROR_FATAL).title("Errore chiamata stato consensi"), "Errore chiamata stato consensi");
		}
		
		return responseStatoConsensi.getStatoConsensiOUT();
	}
	
	private RichiedenteExt newRichiedenteConsenso(String cf,  String shibIdentitaCodiceFiscale, String xCodiceServizio, String xRequestID) {
		RichiedenteExt rich = new RichiedenteExt();
		ApplicazioneRichiedente appRichiedente = new ApplicazioneRichiedente();
		//appRichiedente.setCodice(CostantiConsenso.APPLICAZIONE_RICHIEDENTE); //avrei messo il codice servizio ma non va (mail Tom 22/11)
		appRichiedente.setCodice(xCodiceServizio);
		rich.setApplicazione(appRichiedente);
		rich.setCodiceFiscale(shibIdentitaCodiceFiscale);
			
		rich.setNumeroTransazione(xRequestID); 
		rich.setRegime(newRegimeDMA());
		rich.setRuolo(newRuoloDma(cf));
		rich.setTokenOperazione(xRequestID); 
		
		return rich;
	}
	
	private Paziente newPaziente(String cf) {
		Paziente paz = new Paziente();
		paz.setCodiceFiscale(cf);
		
		return paz;
	}
	
	protected RuoloDMA newRuoloDma(String citId) {
		RuoloDMA ruolo = new RuoloDMA();
		ruolo.setCodice(StatoConsensi.RUOLO.toString());
		return ruolo;
	}
	
	protected RegimeDMA newRegimeDMA() {
		RegimeDMA regime = new RegimeDMA();
		regime.setCodice(StatoConsensi.REGIME.toString());
		return regime;
	}	
}
