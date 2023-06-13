/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.csi.deleghe.deleghebe.ws.DelegheCittadiniService;
import it.csi.deleghe.deleghebe.ws.model.Cittadino;
import it.csi.deleghe.deleghebe.ws.msg.GetDeleganti;
import it.csi.deleghe.deleghebe.ws.msg.GetDelegantiResponse;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.exception.DelegheFallimentoException;
import it.csi.dma.codcit.integration.service.util.GradoDelega;
import it.csi.dma.codcit.integration.service.util.StatoDelega;
import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.LoggerUtil;
import it.csi.dma.codcit.util.enumerator.CodeErrorEnum;
import it.csi.dma.codcit.util.enumerator.StatusEnum;

@Component
public class DelegheService extends LoggerUtil {

	public static final String DELEGHE_COD_PREFIX = "DELEGHE-COD-";

	@Autowired
	@Qualifier("delegheCittadiniService")
	DelegheCittadiniService delegheCittadiniService;

	@Value("${delegheCodiceServizio}")
	private String codiceServizio;

	@Autowired
	CodDErroreService codDErroreService;

	public boolean checkUtenteAutorizzatoOrDelegato(String xRequestID, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, String cf) throws DelegheFallimentoException, DatabaseException {
		return isUtenteAutorizzato(shibIdentitaCodiceFiscale, cf) || isUtenteDelegato(xRequestID, xCodiceServizio, shibIdentitaCodiceFiscale, cf);
	}

	private boolean isUtenteAutorizzato(String shibIdentitaCodiceFiscale, String cf) {
		return shibIdentitaCodiceFiscale != null && shibIdentitaCodiceFiscale.equalsIgnoreCase(cf);
	}

	private boolean isUtenteDelegato(String xRequestID, String xCodiceServizio, String shibIdentitaCodiceFiscale,
			String cf) throws DelegheFallimentoException, DatabaseException {
		final String METHOD_NAME = "isUtenteDelegato";
		GetDeleganti req = new GetDeleganti();
		it.csi.deleghe.deleghebe.ws.model.Richiedente richiedente = new it.csi.deleghe.deleghebe.ws.model.Richiedente(
				xRequestID, xCodiceServizio, shibIdentitaCodiceFiscale);
		req.setRichiedente(richiedente);

		Cittadino cittDelegante = new Cittadino();
		cittDelegante.setCodiceFiscale(cf);
		req.setCittadinoDelegante(cittDelegante);
		req.setCodiciServizio(Arrays.asList(codiceServizio));
		req.setStatiDelega(Arrays.asList(StatoDelega.ATTIVA.toString(), StatoDelega.INSCADENZA.toString()));

		Cittadino cittDelegato = new Cittadino();
		cittDelegato.setCodiceFiscale(shibIdentitaCodiceFiscale);
		req.setCittadinoDelegato(cittDelegato);

		// per verificare se il cf in path abbia delegato l'utente nell'header
		// (shibidentitacf) preferisco fare una chiamata sui deleganti (ovvero chi ha
		// effettuato l'autenticazione
		// richiama il servizio per vedere se il cf del path sia un suo delegante in
		// questo modo per l'audit la catena ha preciso chi effettua la chiamata
		GetDelegantiResponse gdResp;
		try {
			gdResp = delegheCittadiniService.getDelegantiService(req);
		} catch (Exception e) {
			logError(METHOD_NAME, "Errore getDelegantiService service: ", e.getMessage());
			throw new DelegheFallimentoException(ErrorBuilder.from(StatusEnum.SERVER_ERROR)
					.detail(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_086.getCode())), e);
		}

		if (!it.csi.deleghe.deleghebe.ws.model.RisultatoCodice.SUCCESSO.equals(gdResp.getEsito())) {
			var errorBuilder = ErrorBuilder.from(StatusEnum.SERVER_ERROR_DELEGHE);
			List<it.csi.deleghe.deleghebe.ws.model.Errore> errori = gdResp.getErrori();
			if (errori != null) {
				logError(METHOD_NAME, "Errore isUtenteDelegato service:");
				for (it.csi.deleghe.deleghebe.ws.model.Errore e : errori) {
					logError(METHOD_NAME,
							String.format("Codice: %s, Descrizione: %s", e.getCodice(), e.getDescrizione()));
					errorBuilder.detail(codDErroreService.getValueGenericErrorFromExternalService(DELEGHE_COD_PREFIX,
							e.getCodice(), e.getDescrizione()));
				}
			}

			throw new DelegheFallimentoException(errorBuilder, "Errore isUtenteDelegato service");
		}
		return isPresenteDeleganteAttivo(gdResp);
	}

	private boolean isPresenteDeleganteAttivo(GetDelegantiResponse gdResp) {

		boolean presentiDeleghe = (gdResp.getDeleganti() != null && !gdResp.getDeleganti().isEmpty());

		return (presentiDeleghe && gdResp.getDeleganti().stream().anyMatch(c -> c.getDeleghe().stream()
				.filter(delScerev -> codiceServizio.equals(delScerev.getCodiceServizio())
						&& (StatoDelega.ATTIVA.toString().equals(delScerev.getStatoDelega())
								|| it.csi.dma.codcit.integration.service.util.StatoDelega.INSCADENZA.toString()
										.equals(delScerev.getStatoDelega())
										&& GradoDelega.FORTE.toString().equalsIgnoreCase(delScerev.getGradoDelega())))
				.anyMatch(del -> del.getDataInizioDelega().compareTo(new Date()) <= 0
						&& del.getDataFineDelega().compareTo(new Date()) >= 0)));
	}
}
