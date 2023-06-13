/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.helper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.ModelAssistito;
import it.csi.dma.apicodopsan.dto.PayloadAbilitazione;
import it.csi.dma.apicodopsan.dto.PayloadAbilitazioneTotale;
import it.csi.dma.apicodopsan.dto.custom.CodTRichiestaBatch;
import it.csi.dma.apicodopsan.dto.custom.DmaccDCatalogoLogAudit;
import it.csi.dma.apicodopsan.dto.custom.Soggetto;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.exception.ResponseErrorException;
import it.csi.dma.apicodopsan.integration.auditlog.ApiAuditEnum;
import it.csi.dma.apicodopsan.integration.auditlog.AuditLogService;
import it.csi.dma.apicodopsan.integration.dao.custom.CodDBatchDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodDBatchParametroDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodDErroreDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTRichiestaBatchDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.apicodopsan.integration.dao.custom.DmaccTCatalogoLogAuditDao;
import it.csi.dma.apicodopsan.integration.notificatore.util.AbilitaPazientiCittadinoNotificaAsync;
import it.csi.dma.apicodopsan.integration.notificatore.util.AbilitaPazientiMedicoNotificaAsync;
import it.csi.dma.apicodopsan.integration.notificatore.util.DisabilitaPazientiCittadinoNotificaAsync;
import it.csi.dma.apicodopsan.integration.notificatore.util.DisabilitaPazientiMedicoNotificaAsync;
import it.csi.dma.apicodopsan.integration.service.CodRMessaggioErroreService;
import it.csi.dma.apicodopsan.stub.aura.auraws.services.central.contattodigitale.AssistitiGetService;
import it.csi.dma.apicodopsan.stub.aura.auraws.services.central.contattodigitale.ResponseContattoDigitale;
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.BatchCodEnum;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.HeaderEnum;
import it.csi.dma.apicodopsan.util.enumerator.ListaAssistitiStatusEnum;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateMedicoAdesione;
import it.csi.dma.apicodopsan.util.validator.ValidateMedicoBatchAssistiti;
import it.csi.dma.apicodopsan.util.validator.ValidateUtenteAbilitatoServizio;
import it.csi.dma.apicodopsan.util.validator.impl.ValidateMedicoAssistitiTuttiPostImpl;

@Service
public class AssistitiTuttiPost extends LoggerUtil {

	@Autowired
	@Qualifier("validateMedicoAssistitiTuttiPost")
	ValidateMedicoAssistitiTuttiPostImpl validateMedicoAssistitiTuttiPost;
	@Autowired
	ValidateMedicoAdesione validateMedicoAdesione;
	@Autowired
	ValidateUtenteAbilitatoServizio validateUtenteAbilitatoServizio;
	@Autowired
	CodTSoggettoDao codTSoggettoDao;
	@Autowired
	CodDErroreDao codDErroreDao;
	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;
	@Autowired
	DataSource dataSource;
	@Autowired
	CodDBatchDao codDBatchDao;
	@Autowired
	CodDBatchParametroDao codDBatchParametroDao;
	@Autowired
	ValidateMedicoBatchAssistiti validateMedicoBatchAssistiti;

	@Autowired
	AssistitiGetService assistitiGetService;

	@Autowired
	CodTRichiestaBatchDao codTRichiestaBatchDao;
	@Autowired
	AuditLogService auditLogService;
	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;

	// NOTIFICHE
	@Autowired
	AbilitaPazientiCittadinoNotificaAsync abilitaPazientiCittadinoNotificaAsync;
	@Autowired
	AbilitaPazientiMedicoNotificaAsync abilitaPazientiMedicoNotificaAsync;
	@Autowired
	DisabilitaPazientiCittadinoNotificaAsync disabilitaPazientiCittadinoNotificaAsync;
	@Autowired
	DisabilitaPazientiMedicoNotificaAsync disabilitaPazientiMedicoNotificaAsync;

//	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadAbilitazioneTotale payloadAbilitazioneTotale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		var methodName = "execute";
		Long lock = null;
		boolean checkRemoveLock = false;
		List<ErroreDettaglio> listError = null;
		boolean checkNotificaError=false;
		ErrorBuilder error = null;
		try {
//			2a => validatione obbligatorieta' tutti i campi in input sono required
//			2b => correttezza parametri di input
			listError = validateMedicoAssistitiTuttiPost.validate(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
					xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione,
					payloadAbilitazioneTotale, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateMedicoAssistitiTuttiPost");
			}
//			2c adesione utente
			listError = validateMedicoAdesione.validate(shibIdentitaCodiceFiscale);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateMedicoAdesione");
			}
//			3)verifica utente abilitato
			listError = validateUtenteAbilitatoServizio.validate(xCodiceVerticale, xCodiceServizio,
					shibIdentitaCodiceFiscale, xForwardedFor, xRequestId, ruolo);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateUtenteAbilitatoServizio");
			}
			
			// SUPERATE TUTTE LE VALIDAZIONI 
			checkNotificaError=true;
			
			// creo un lock per non permettere richieste multipe al medico
			lock = codTRichiestaBatchDao.insertLockRichiestaBatchTutti(shibIdentitaCodiceFiscale,
					payloadAbilitazioneTotale);
			if (lock < 0) {
				listError.add(validateMedicoAssistitiTuttiPost.getValueGenericError(CodeErrorEnum.FSE_COD_094.getCode(),
						"Attenzione esiste un lock sulla tabella cod_t_richiesta_batch!"));
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"Attenzione esiste un lock sulla tabella cod_t_richiesta_batch!");
			}
			// registra una richiesta=> inserisce nella tabella COD_T_RICHIESTA_BATCH
//			Se va tutto bene
			List<ModelAssistito> listaAssistiti = null;
			String codiceCatalogo = "";
			if (payloadAbilitazioneTotale.isAbilitazione()) {
				log.info("RICHIAMO SERVIZIO ESTERNO AssistitiTuttiPost assistitiGetService.getAssistitiAura");
				long startTime = System.currentTimeMillis();
				ResponseContattoDigitale resp = assistitiGetService.getAssistitiAura(shibIdentitaCodiceFiscale, 1);
				log.info("RISPOSTA SERVIZIO ESTERNO AssistitiTuttiPost assistitiGetService.getAssistitiAura:"
						+ (System.currentTimeMillis() - startTime) + " Millis");
				log.info("info start function:" + codTRichiestaBatchDao.info());
				if (resp != null && resp.getElencoAssistiti() != null && resp.getElencoAssistiti().size() > 0) {
					List<String> assistiti = resp.getElencoAssistiti().stream().map(v -> v.getCodiceFiscale())
							.collect(Collectors.toList());
					// String[] assistiti=resp.getElencoAssistiti().stream().map(v ->
					// v.getCodiceFiscale()).toArray((String[]::new));
					codiceCatalogo = "COD_MED_ABI_TUT_ASS";
					if (assistiti != null && assistiti.size() > 0) {
						listaAssistiti = codTRichiestaBatchDao.callAbilitazioneTutti(shibIdentitaCodiceFiscale,
								payloadAbilitazioneTotale, assistiti);
						notificaAbilitati(listaAssistiti, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
								xCodiceServizio);
					}
				}
			} else {
				codiceCatalogo = "COD_MED_DIS_TUT_ASS";
				List<String> assistiti = codTRichiestaBatchDao.selectSoggettiDisabilitabili(shibIdentitaCodiceFiscale);
				if (assistiti != null && assistiti.size() > 0) {
					listaAssistiti = codTRichiestaBatchDao.callDisabilitaTutti(shibIdentitaCodiceFiscale,
							payloadAbilitazioneTotale, assistiti);
					notificaDisabilitati(listaAssistiti, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
							xCodiceServizio);
				}
			}
			log.info("info stop function:" + codTRichiestaBatchDao.info());

			log.info("Pazienti elaborati totali: " + (listaAssistiti != null ? listaAssistiti.size() : 0));
			// AUDIT LOG
			String descrizioneCatalogoLogAudit = null;
			Long idCatalogoLogAudit = null;
			DmaccDCatalogoLogAudit catalogoLogAudit = dmaccTCatalogoLogAuditDao
					.selectCatalogoDescrizioneByCodice(codiceCatalogo);
			if (catalogoLogAudit != null) {
				idCatalogoLogAudit = catalogoLogAudit.getId();
				descrizioneCatalogoLogAudit = catalogoLogAudit.getDescrizione().replace("{0}",
						shibIdentitaCodiceFiscale);
			}
			auditLogService.insertAuditLog(xRequestId, xForwardedFor, shibIdentitaCodiceFiscale, xCodiceServizio,
					xCodiceVerticale, ruolo, collocazioneDescrizione, null, ApiAuditEnum.POST_ASSISTITI_TUTTI,
					descrizioneCatalogoLogAudit, idCatalogoLogAudit);
			// rimozione lock con successo
			checkRemoveLock = true;
			return Response.ok().entity(listaAssistiti).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId).build();
//		} catch (DatabaseException e) {
//			logError(methodName, "Errore riguardante database:", e.getMessage());
////			codTRichiestaBatchDao.removeLockRichiestaBatchForced(lock,false);
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			sendNotificaError(checkNotificaError, payloadAbilitazioneTotale, shibIdentitaCodiceFiscale, xRequestId,
//					xForwardedFor);
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
//		} catch (javax.xml.ws.WebServiceException e) {
//			logError(methodName, "Errore riguardante il richiamo del servizio di Anagrafica:", e.getMessage());
//			try {
//				listError.add(validateMedicoAssistitiTuttiPost.getValueGenericError(CodeErrorEnum.FSE_COD_080.getCode(),
//						e.getMessage()));
//			} catch (DatabaseException e1) {
//				log.error(e1.getMessage());
//			}
//			codRMessaggioErroreService.saveError(listError, httpRequest);
//			listError.add(new ErroreDettaglio());
//			sendNotificaError(checkNotificaError, payloadAbilitazioneTotale, shibIdentitaCodiceFiscale, xRequestId,
//					xForwardedFor);
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, listError);
//		} catch (Exception e) {
//			logError(methodName, "Errore non gestito :", e.getMessage());
////			codTRichiestaBatchDao.removeLockRichiestaBatchForced(lock,false);
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			sendNotificaError(checkNotificaError, payloadAbilitazioneTotale, shibIdentitaCodiceFiscale, xRequestId,
//					xForwardedFor);
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
		} catch (javax.xml.ws.WebServiceException e) {
			logError(methodName, "Errore riguardante il richiamo del servizio di Anagrafica:", e.getMessage());
			try {
				listError.add(validateMedicoAssistitiTuttiPost.getValueGenericError(CodeErrorEnum.FSE_COD_080.getCode(),
						e.getMessage()));
			} catch (DatabaseException e1) {
				log.error(e1.getMessage());
			}
			listError.add(new ErroreDettaglio());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listError);
		} catch (DatabaseException e) {
			
			logError(methodName, "Errore riguardante database:", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		} catch (ResponseErrorException e) {
			logError(methodName, "Errore generico response:", e.getMessage());
			error = e.getResponseError();
		} catch (Exception e) {
			logError(methodName, "Errore non gestito :", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		} finally {
			// Elimino lock
			codTRichiestaBatchDao.removeLockRichiestaBatchForced(lock, checkRemoveLock);
		}

		

	sendNotificaError(checkNotificaError, payloadAbilitazioneTotale, shibIdentitaCodiceFiscale, xRequestId,
	xForwardedFor);
	error = codRMessaggioErroreService.saveError(error, httpRequest);
	return error.generateResponseError();
		
		
	}

	private void sendNotificaError(boolean checkNotificaError, PayloadAbilitazioneTotale payloadAbilitazione,
			String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor) {
		if (checkNotificaError) {
			if (payloadAbilitazione.isAbilitazione()) {
				abilitaPazientiMedicoNotificaAsync.notifyErrorAsync(shibIdentitaCodiceFiscale, xRequestId,
						xForwardedFor);
			} else {
				disabilitaPazientiMedicoNotificaAsync.notifyErrorAsync(shibIdentitaCodiceFiscale, xRequestId,
						xForwardedFor);
			}
		}

	}

	private void notificaAbilitati(List<ModelAssistito> listaAssistiti, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio) {
		int countEnabled = 0;
		int countAlreadyEnabled = 0;
		int countOnError = 0;
		List<Soggetto> listaPazienti = null;
		if (listaAssistiti != null && listaAssistiti.size() > 0) {
			listaPazienti = new ArrayList<Soggetto>();
			Soggetto tmp = null;
			for (ModelAssistito assistito : listaAssistiti) {
				// A ==> ABILITATO
				// GIA_ABILITATI
				// B==>IN ERRORE
				if (ListaAssistitiStatusEnum.ABILITATI_SUCCESSO.getCode().equalsIgnoreCase(assistito.getStatoAbilitazione())) {
					tmp = new Soggetto();
					tmp.setSoggettoCf(assistito.getCodiceFiscale());
					tmp.setSoggettoCognome(assistito.getCognome());
					tmp.setSoggettoNome(assistito.getNome());
					listaPazienti.add(tmp);
				} else if (ListaAssistitiStatusEnum.ABILITATI_PRECEDENTEMENTE.getCode().equalsIgnoreCase(assistito.getStatoAbilitazione())) {
					countAlreadyEnabled++;
				}
			}
			countEnabled = listaPazienti.size();
			countOnError = listaAssistiti.size() - (countEnabled + countAlreadyEnabled);
			abilitaPazientiCittadinoNotificaAsync.notifyAsync(shibIdentitaCodiceFiscale, listaPazienti, xRequestId,
					xCodiceServizio);
			if (countAlreadyEnabled > 0 || countOnError > 0) {
				abilitaPazientiMedicoNotificaAsync.notifyAlreadyEnabledAsync(shibIdentitaCodiceFiscale, xRequestId,
						xForwardedFor, countEnabled, countAlreadyEnabled, countOnError);
			} else {
				abilitaPazientiMedicoNotificaAsync.notifyEnabledAsync(shibIdentitaCodiceFiscale, xRequestId,
						xForwardedFor, countEnabled);
			}

		}

	}

	private void notificaDisabilitati(List<ModelAssistito> listaAssistiti, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio) {
		int countDisabled = 0;
		int countAlreadyDisabled = 0;
		int countOnError = 0;
		List<Soggetto> listaPazienti = null;
		if (listaAssistiti != null && listaAssistiti.size() > 0) {
			listaPazienti = new ArrayList<Soggetto>();
			Soggetto tmp = null;
			for (ModelAssistito assistito : listaAssistiti) {
				// B ==> DISABILITATO
				// GIA DISABILITATI
				// NON_DISABILITABILE_SOGGETTO_MANCANTE

				if (ListaAssistitiStatusEnum.DISABILITATI_SUCCESSO.getCode().equalsIgnoreCase(assistito.getStatoAbilitazione())) {
					tmp = new Soggetto();
					tmp.setSoggettoCf(assistito.getCodiceFiscale());
					tmp.setSoggettoCognome(assistito.getCognome());
					tmp.setSoggettoNome(assistito.getNome());
					listaPazienti.add(tmp);
				} else if (ListaAssistitiStatusEnum.DISABILITATI_PRECEDENTEMENTE.getCode().equalsIgnoreCase(assistito.getStatoAbilitazione())) {
					countAlreadyDisabled++;
				}
			}
			countDisabled = listaPazienti.size();
			countOnError = listaAssistiti.size() - (countDisabled + countAlreadyDisabled);
			disabilitaPazientiCittadinoNotificaAsync.notifyAsync(shibIdentitaCodiceFiscale, listaPazienti, xRequestId,
					xCodiceServizio);
			if (countAlreadyDisabled > 0 || countOnError > 0) {
				disabilitaPazientiMedicoNotificaAsync.notifyAlreadyDisabledAsync(shibIdentitaCodiceFiscale, xRequestId,
						xForwardedFor, countDisabled, countAlreadyDisabled, countOnError);
			} else {
				disabilitaPazientiMedicoNotificaAsync.notifyDisabledAsync(shibIdentitaCodiceFiscale, xRequestId,
						xForwardedFor, countDisabled);
			}

		}

	}
}
