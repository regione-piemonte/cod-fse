/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.helper.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.ModelAssistitoMessaggio;
import it.csi.dma.apicodopsan.dto.ModelConversazione;
import it.csi.dma.apicodopsan.dto.ModelUltimoMessaggio;
import it.csi.dma.apicodopsan.dto.custom.DmaccDCatalogoLogAudit;
import it.csi.dma.apicodopsan.dto.custom.Soggetto;
import it.csi.dma.apicodopsan.dto.custom.TMessaggio;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.exception.ResponseErrorException;
import it.csi.dma.apicodopsan.integration.auditlog.ApiAuditEnum;
import it.csi.dma.apicodopsan.integration.auditlog.AuditLogService;
import it.csi.dma.apicodopsan.integration.dao.custom.CodDDisabilitazioneMotivoDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodDErroreDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodSMessaggioDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTConversazioneDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTMessaggioDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoAbilitatoDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoDisabilitatoDao;
import it.csi.dma.apicodopsan.integration.dao.custom.DmaccTCatalogoLogAuditDao;
import it.csi.dma.apicodopsan.integration.dao.utils.SConversazioneMapper;
import it.csi.dma.apicodopsan.integration.service.CodCParametroService;
import it.csi.dma.apicodopsan.integration.service.CodCParametroService.ConfigParam;
import it.csi.dma.apicodopsan.integration.service.CodRMessaggioErroreService;
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.HeaderEnum;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;
import it.csi.dma.apicodopsan.util.validator.ValidateCodiceFiscale;
import it.csi.dma.apicodopsan.util.validator.impl.ValidateGenericMeritWhitMedicoImpl;

@Service
public class Conversazioni extends LoggerUtil {

	@Autowired
	ValidateGenericMeritWhitMedicoImpl validateMedicGeneric;

	@Autowired
	CodTConversazioneDao codTconversazioneDao;

	@Autowired
	CodTSoggettoAbilitatoDao codTSoggettoAbilitatoDao;

	@Autowired
	CodTMessaggioDao codTMessaggioDao;

	@Autowired
	CodSMessaggioDao codSMessaggioDao;

	@Autowired
	CodDDisabilitazioneMotivoDao codDDisabilitazioneMotivo;

	@Autowired
	private CodTSoggettoDisabilitatoDao codTSoggettoDisabilitatoDao;

	@Autowired
	CodTSoggettoDao codTSoggettoDao;

	@Autowired
	CodDErroreDao codDErroreDao;

	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	@Autowired
	DataSource dataSource;

	@Autowired
	CodCParametroService codCParametroService;
	@Autowired
	AuditLogService auditLogService;
	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;
	@Autowired
	ValidateCodiceFiscale validateCodiceFiscale;

	// Lettura Messagio
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String idConversazione, String idMessaggio, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		var methodName = "execute";
		ErrorBuilder error = null;
		try {
			// validate
			List<ErroreDettaglio> listError = validateMedicGeneric.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, idConversazione, idMessaggio, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateMedicGeneric");
			}
		
			/*
			 * INIZIO LOGICA APPLICATIVA SUPERATI TUTTI I CONTROLLI
			 *
			 */

			TMessaggio tMessaggio = codTMessaggioDao.selectTMessaggio(idConversazione, idMessaggio,
					shibIdentitaCodiceFiscale);
			if (tMessaggio == null) {
				commonError(httpRequest);
			}

			if (tMessaggio.getMessaggioLetturaData() != null) {
				ErroreDettaglio erroreDettaglio = new ErroreDettaglio();
				erroreDettaglio.setChiave(CodeErrorEnum.FSE_COD_088.getCode());
				erroreDettaglio
						.setValore(codDErroreDao.selectErroreDescFromErroreCod(CodeErrorEnum.FSE_COD_088.getCode()));
				List<ErroreDettaglio> result = new ArrayList<>();
				result.add(erroreDettaglio);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, result),
						"errore in messaggio sola lettura");
//				codRMessaggioErroreService.saveError(result, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, result);

			}

			if (codSMessaggioDao.insertSMessaggio(tMessaggio)) {
				codTMessaggioDao.updateTMessaggio(tMessaggio.getMessaggioId(), shibIdentitaCodiceFiscale);
			}
			// AUDIT LOG
			String codiceCatalogo = "COD_MED_NTFYREAD";
			String descrizioneCatalogoLogAudit = null;
			Long idCatalogoLogAudit = null;
			Soggetto ricevente = codTSoggettoDao.selectSoggettoById(tMessaggio.getSoggettoIdA());
			DmaccDCatalogoLogAudit catalogoLogAudit = dmaccTCatalogoLogAuditDao
					.selectCatalogoDescrizioneByCodice(codiceCatalogo);
			if (catalogoLogAudit != null) {
				idCatalogoLogAudit = catalogoLogAudit.getId();
				descrizioneCatalogoLogAudit = catalogoLogAudit.getDescrizione().replace("{0}",
						shibIdentitaCodiceFiscale);
			}
			auditLogService.insertAuditLog(xRequestId, xForwardedFor, shibIdentitaCodiceFiscale, xCodiceServizio,
					xCodiceVerticale, ruolo, collocazioneDescrizione, ricevente.getSoggettoCf(),
					ApiAuditEnum.PUT_CONVERSAZIONE_MESSAGG_LETTURA, descrizioneCatalogoLogAudit, idCatalogoLogAudit);
			return Response.ok().header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId).build();
//		} catch (DatabaseException e) {
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			logError(methodName, "Errore riguardante database:", e.getMessage());
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
//		} catch (Exception e) {
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			logError(methodName, "Errore non gestito :", e.getMessage());
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
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
		error = codRMessaggioErroreService.saveError(error, httpRequest);
		return error.generateResponseError();
	}

	// ListaConversazioni
	// TODO
	// paginazion
	// String idConversazione
	// motivo_blocco restituisce l'id e non il codice. NOn funziona filtro per id
	// conversazione

	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer limit, Integer offset, String solaLettura, String cognome,
			String codiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, String idConversazione) {
		var methodName = "execute";
		ErrorBuilder error = null;
		try {
			// validate
			List<ErroreDettaglio> listError = validateMedicGeneric.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, limit, offset, solaLettura, cognome, codiceFiscale, securityContext,
					httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateMedicGeneric");
			}
			/*
			 * Modifica 04-11-2022
			 */
			if(StringUtils.isNotBlank(codiceFiscale))
			listError=validateCodiceFiscale.validate(xCodiceVerticale, xCodiceServizio, codiceFiscale, xForwardedFor, xRequestId);
			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateCodiceFiscale");
			}
			
			
			/*
			 * INIZIO LOGICA APPLICATIVA SUPERATI TUTTI I CONTROLLI
			 *
			 */

			List<ModelConversazione> rstLst = new ArrayList<>();
			// query che restituisce join di conversazioni e soggetto
			List<Map<String, Object>> rstConvLst = codTconversazioneDao.selectLstConversazioniCompleta(
					shibIdentitaCodiceFiscale, solaLettura, cognome, codiceFiscale, idConversazione, limit, offset);
			Integer totalElements = codTconversazioneDao.countTotalLstConversazioniCompleta(shibIdentitaCodiceFiscale,
					solaLettura, cognome, codiceFiscale, idConversazione);
			if (rstConvLst.isEmpty())
				return Response.status(200).entity(rstLst).build();
			;

			for (Map<String, Object> objCOnv : rstConvLst) {

				ModelConversazione mConv = new ModelConversazione();
				SConversazioneMapper sCM = new SConversazioneMapper();

				mConv = sCM.mapObj(objCOnv);
				int convId = Integer.parseInt(objCOnv.get("conversazione_id") + "");
				int soggettoId = Integer.parseInt(objCOnv.get("soggetto_id_autore") + "");

				// count messaggi non letti
				mConv.setNMessaggiNonLetti(codTMessaggioDao.countMessNonLetti(convId, shibIdentitaCodiceFiscale));

				// set Ultimo Messaggio
				ModelUltimoMessaggio ultimoMessaggio = codTMessaggioDao.selectUltimoMess(convId);
				mConv.setUltimoMessaggio(ultimoMessaggio);

				// query set motivoBlocco
				if (objCOnv.get("disabilitazione_motivo_id") != null) {
					String disMotString = objCOnv.get("disabilitazione_motivo_id").toString();
					mConv.setMotivoBlocco(
							codDDisabilitazioneMotivo.selectMotivoDisabilitazione(Integer.parseInt(disMotString)));
					mConv.setMotivazioneMedico(codTSoggettoDisabilitatoDao.selectMotivazioneBlocco(soggettoId,Long.parseLong(objCOnv.get("soggetto_id_partecipante") + "")));
				}

				Soggetto sogAs = codTSoggettoDao.selectSoggettoAssistito(soggettoId, cognome, codiceFiscale);

				// gestisco la scadenza della conversazione
				Integer giorniEliminazione = codCParametroService
						.get(ConfigParam.NUM_GIORNI_ELIMINAZIONE_CONVERSAZIONE);
				mConv.setDataEliminazione(addDays(
						(ultimoMessaggio != null ? ultimoMessaggio.getDataCreazione() : mConv.getDataCreazione()),
						giorniEliminazione));

				ModelAssistitoMessaggio assistito = new ModelAssistitoMessaggio();
				if (sogAs != null) {
					assistito.setNome(sogAs.getSoggettoNome());
					assistito.setCognome(sogAs.getSoggettoCognome());
					assistito.setCodiceFiscale(sogAs.getSoggettoCf());
					mConv.setAssistito(assistito);

					// AGGIUNGO CONTROLLO CONV SCADUTA..
					// Escludo dalla lista le conversazioni con messaggi scaduti
					if (mConv.getDataEliminazione().after(new Date())) {
						rstLst.add(mConv);
					}

				}

			}
			;
			// AUDIT LOG
			String codiceCatalogo = "COD_MED_CONV";
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
					xCodiceVerticale, ruolo, collocazioneDescrizione, null, ApiAuditEnum.GET_CONVERSAZIONI,
					descrizioneCatalogoLogAudit, idCatalogoLogAudit);
			return Response.ok(rstLst).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId)
					.header(HeaderEnum.X_TOTAL_ELEMENTS.getCode(), totalElements).build();
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			logError(methodName, "Errore riguardante database:", e.getMessage());
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			logError(methodName, "Errore non gestito :", e.getMessage());
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
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
		error = codRMessaggioErroreService.saveError(error, httpRequest);
		return error.generateResponseError();
	}

	private void commonError(HttpServletRequest httpRequest) throws DatabaseException, ResponseErrorException {

		ErroreDettaglio erroreDettaglio = new ErroreDettaglio();
		erroreDettaglio.setChiave(CodeErrorEnum.FSE_COD_067.getCode());
		erroreDettaglio.setValore(codDErroreDao.selectErroreDescFromErroreCod(CodeErrorEnum.FSE_COD_067.getCode()));
		List<ErroreDettaglio> result = new ArrayList<>();

		result.add(erroreDettaglio);
		throw new ResponseErrorException(
				ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, result),
				"errore in commonError: "+erroreDettaglio.getValore());

	}

	protected static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();

	}
}
