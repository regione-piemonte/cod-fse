/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.helper.impl;

import java.util.ArrayList;
import java.util.List;

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
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.HeaderEnum;
import it.csi.dma.apicodopsan.util.enumerator.ListaAssistitiStatusEnum;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateMedicoAdesione;
import it.csi.dma.apicodopsan.util.validator.ValidateMedicoBatchAssistiti;
import it.csi.dma.apicodopsan.util.validator.ValidateUtenteAbilitatoServizio;
import it.csi.dma.apicodopsan.util.validator.impl.ValidateMedicoAssistitiPostImpl;

@Service
public class AssistitiPost extends LoggerUtil {

	@Autowired
	@Qualifier("validateMedicoAssistitiPost")
	ValidateMedicoAssistitiPostImpl validateMedicoAssistitiPost;
	@Autowired
	ValidateMedicoAdesione validateMedicoAdesione;
	@Autowired
	ValidateUtenteAbilitatoServizio validateUtenteAbilitatoServizio;

	@Autowired
	CodTSoggettoDao codTSoggettoDao;

	@Autowired
	CodDErroreDao codDErroreDao;
	@Autowired
	CodDBatchDao codDBatchDao;
	@Autowired
	CodDBatchParametroDao codDBatchParametroDao;

	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	@Autowired
	DataSource dataSource;

	@Autowired
	ValidateMedicoBatchAssistiti validateMedicoBatchAssistiti;

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

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadAbilitazione payloadAbilitazione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		var methodName = "execute";
		ErrorBuilder error = null;
		Long lock=null;
		boolean checkNotificaError=false;
		try {
//			2a => validatione obbligatorieta' tutti i campi in input sono required
//			2b => correttezza parametri di input
			List<ErroreDettaglio> listError = validateMedicoAssistitiPost.validate(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, payloadAbilitazione, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateMedicoAssistitiPost");
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
			
			//creo un lock per non permettere richieste multipe al medico
			lock=codTRichiestaBatchDao.insertLockRichiestaBatch(shibIdentitaCodiceFiscale,payloadAbilitazione);
			if(lock<0) {
				listError.add(validateMedicoAssistitiPost.getValueGenericError(CodeErrorEnum.FSE_COD_094.getCode(), "Attenzione esiste un lock sulla tabella cod_t_richiesta_batch!"));
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"Attenzione esiste un lock sulla tabella cod_t_richiesta_batch!");
			}
			String codiceCatalogo = "";

			//CodTRichiestaBatch richiesta =  buildRichiestaBatch(shibIdentitaCodiceFiscale, payloadAbilitazione);
			List<ModelAssistito>  listaAssistiti;
			log.info("info1:"+codTRichiestaBatchDao.info());
			if(payloadAbilitazione.isAbilitazione()) {
				codiceCatalogo = "COD_MED_ABI_SING_ASS";
				//test
				//listaAssistiti=new ArrayList<ModelAssistito>();
				//listaAssistiti.add(((ModelAssistito)new ModelAssistitoCustom("", "", "CF", null, "",	"A", null, "")));
				listaAssistiti=codTRichiestaBatchDao.callAbilitazione(shibIdentitaCodiceFiscale,payloadAbilitazione);
				notificaAbilitati(listaAssistiti, shibIdentitaCodiceFiscale, xRequestId,  xForwardedFor,
						 xCodiceServizio);
			} else {
				codiceCatalogo="COD_MED_DIS_SING_ASS";
				//test
				//listaAssistiti=new ArrayList<ModelAssistito>();
				//listaAssistiti.add(((ModelAssistito)new ModelAssistitoCustom("", "", "CF2", null, "", "B", null, "")));
				//listaAssistiti=
				listaAssistiti=codTRichiestaBatchDao.callDisabilita(shibIdentitaCodiceFiscale,payloadAbilitazione);
				//listaAssistiti= new ArrayList<ModelAssistito>();
				notificaDisabilitati(listaAssistiti, shibIdentitaCodiceFiscale, xRequestId,  xForwardedFor,
						 xCodiceServizio);
			}
			// se si hanno problemi con le function public deremmare infox!!!!!!!!
			log.info("info2:"+codTRichiestaBatchDao.info());
			// Elimino lock
			codTRichiestaBatchDao.removeLockRichiestaBatchForced(lock,true);

			//log.info(listaAssistiti!=null?listaAssistiti.toString():"NUL");
			log.info("Pazienti elaborati totali:"+(listaAssistiti!=null?listaAssistiti.size():0));

			// registra una richiesta=> inserisce nella tabella COD_T_RICHIESTA_BATCH
//			Se va tutto bene
			//AUDIT LOG
			String descrizioneCatalogoLogAudit=null;
		    Long idCatalogoLogAudit=null;
		    DmaccDCatalogoLogAudit catalogoLogAudit = dmaccTCatalogoLogAuditDao.selectCatalogoDescrizioneByCodice(codiceCatalogo);
		    if(catalogoLogAudit!=null) {
		    	idCatalogoLogAudit=catalogoLogAudit.getId();
		    	descrizioneCatalogoLogAudit=catalogoLogAudit.getDescrizione().replace("{0}", shibIdentitaCodiceFiscale);
		    }
			auditLogService.insertAuditLog(xRequestId, xForwardedFor,shibIdentitaCodiceFiscale, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneDescrizione,
					null,ApiAuditEnum.POST_ASSISTITI,descrizioneCatalogoLogAudit, idCatalogoLogAudit);
		return Response.ok().entity(listaAssistiti).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId).build();
//		} catch (DatabaseException e) {
//			logError(methodName, "Errore riguardante database:", e.getMessage());
//			codTRichiestaBatchDao.removeLockRichiestaBatchForced(lock,false);
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			sendNotificaError(checkNotificaError,payloadAbilitazione,shibIdentitaCodiceFiscale, xRequestId, xForwardedFor);
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
//		} catch (Exception e) {
//			logError(methodName, "Errore non gestito :", e.getMessage());
//			//faccio update con errore per eliminare il lock
//			codTRichiestaBatchDao.removeLockRichiestaBatchForced(lock,false);
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			sendNotificaError(checkNotificaError,payloadAbilitazione,shibIdentitaCodiceFiscale, xRequestId, xForwardedFor);
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
//			
//		}
		} catch (DatabaseException e) {
			
			logError(methodName, "Errore riguardante database:", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		} catch (ResponseErrorException e) {
			logError(methodName, "Errore generico response:", e.getMessage());
			error = e.getResponseError();
		} catch (Exception e) {
			logError(methodName, "Errore non gestito :", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		}
		codTRichiestaBatchDao.removeLockRichiestaBatchForced(lock,false);
		sendNotificaError(checkNotificaError,payloadAbilitazione,shibIdentitaCodiceFiscale, xRequestId, xForwardedFor);
		error = codRMessaggioErroreService.saveError(error, httpRequest);
		return error.generateResponseError();
		
		

	}

	private void sendNotificaError(boolean checkNotificaError, PayloadAbilitazione payloadAbilitazione,
			String shibIdentitaCodiceFiscale,String xRequestId, String xForwardedFor) {
		if(checkNotificaError) {
			if( payloadAbilitazione.isAbilitazione()) {
				abilitaPazientiMedicoNotificaAsync.notifyErrorAsync(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor);
			}else {
				disabilitaPazientiMedicoNotificaAsync.notifyErrorAsync(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor);
			}
		}
		
	}


	private void notificaAbilitati(List<ModelAssistito>  listaAssistiti,String shibIdentitaCodiceFiscale,String xRequestId, String xForwardedFor,
			String xCodiceServizio) {
		int countEnabled=0;
		int countAlreadyEnabled= 0;
		int countOnError= 0;
		List<Soggetto>listaPazienti= null;
		if(listaAssistiti!=null && listaAssistiti.size()>0) {
			listaPazienti = new ArrayList<Soggetto>();
			Soggetto tmp = null;
			for(ModelAssistito assistito : listaAssistiti) {
				//A ==> ABILITATO
				//GIA_ABILITATI
				//B==>IN ERRORE
				if(ListaAssistitiStatusEnum.ABILITATI_SUCCESSO.getCode().equalsIgnoreCase(assistito.getStatoAbilitazione())) { 
					tmp= new Soggetto();
					tmp.setSoggettoCf(assistito.getCodiceFiscale());
					tmp.setSoggettoCognome(assistito.getCognome());
					tmp.setSoggettoNome(assistito.getNome());
					listaPazienti.add(tmp);
				}else if(ListaAssistitiStatusEnum.ABILITATI_PRECEDENTEMENTE.getCode().equalsIgnoreCase(assistito.getStatoAbilitazione())) {
					countAlreadyEnabled++;
				}
			}
			countEnabled = listaPazienti.size();
			countOnError = listaAssistiti.size()-(countEnabled+countAlreadyEnabled);
			abilitaPazientiCittadinoNotificaAsync.notifyAsync(shibIdentitaCodiceFiscale, listaPazienti, xRequestId, xCodiceServizio);
			if(countAlreadyEnabled>0 || countOnError>0) {
				abilitaPazientiMedicoNotificaAsync.notifyAlreadyEnabledAsync(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, countEnabled, countAlreadyEnabled,countOnError);
			}else  {
				abilitaPazientiMedicoNotificaAsync.notifyEnabledAsync(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, countEnabled);
			}
			
		}
		
	}

	private void notificaDisabilitati(List<ModelAssistito>  listaAssistiti,String shibIdentitaCodiceFiscale,String xRequestId, String xForwardedFor,
			String xCodiceServizio) {
		int countDisabled=0;
		int countAlreadyDisabled= 0;
		int countOnError= 0;
		List<Soggetto>listaPazienti= null;
		if(listaAssistiti!=null && listaAssistiti.size()>0) {
			listaPazienti = new ArrayList<Soggetto>();
			Soggetto tmp = null;
			for(ModelAssistito assistito : listaAssistiti) {
				//B ==> DISABILITATO
				//GIA DISABILITATI 
				//NON_DISABILITABILE_SOGGETTO_MANCANTE
				
				if(ListaAssistitiStatusEnum.DISABILITATI_SUCCESSO.getCode().equalsIgnoreCase(assistito.getStatoAbilitazione())) { 
					tmp= new Soggetto();
					tmp.setSoggettoCf(assistito.getCodiceFiscale());
					tmp.setSoggettoCognome(assistito.getCognome());
					tmp.setSoggettoNome(assistito.getNome());
					listaPazienti.add(tmp);
				}else if(ListaAssistitiStatusEnum.DISABILITATI_PRECEDENTEMENTE.getCode().equalsIgnoreCase(assistito.getStatoAbilitazione())) {
					countAlreadyDisabled++;
				}
			}
			countDisabled = listaPazienti.size();
			countOnError = listaAssistiti.size()-(countDisabled+countAlreadyDisabled);
			disabilitaPazientiCittadinoNotificaAsync.notifyAsync(shibIdentitaCodiceFiscale, listaPazienti, xRequestId, xCodiceServizio);
			 if(countAlreadyDisabled>0 || countOnError>0) { 
				disabilitaPazientiMedicoNotificaAsync.notifyAlreadyDisabledAsync(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, countDisabled, countAlreadyDisabled,countOnError);
			}else  {
				disabilitaPazientiMedicoNotificaAsync.notifyDisabledAsync(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, countDisabled);
			}
			
	
		}

	}
	
	
}
