/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.helper.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.ModelAdesione;
import it.csi.dma.apicodopsan.dto.PayloadAdesione;
import it.csi.dma.apicodopsan.dto.custom.DmaccDCatalogoLogAudit;
import it.csi.dma.apicodopsan.dto.custom.Soggetto;
import it.csi.dma.apicodopsan.dto.custom.TAdesione;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.exception.ResponseErrorException;
import it.csi.dma.apicodopsan.integration.auditlog.ApiAuditEnum;
import it.csi.dma.apicodopsan.integration.auditlog.AuditLogService;
import it.csi.dma.apicodopsan.integration.dao.custom.CodSAdesioneDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodSConversazioneDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTAdesioneDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTConversazioneDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoAbilitatoDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTSoggettoDisabilitatoDao;
import it.csi.dma.apicodopsan.integration.dao.custom.DmaccTCatalogoLogAuditDao;
import it.csi.dma.apicodopsan.integration.service.CodRMessaggioErroreService;
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;
import it.csi.dma.apicodopsan.util.validator.impl.ValidateGenericMeritWhitMedicoImpl;

@Service
public class AdesioneRevocaMedico extends LoggerUtil {

	@Autowired
	ValidateGenericMeritWhitMedicoImpl validateMedicoRequest;

	@Autowired
	CodTSoggettoDao codTSoggettoDao;

	@Autowired
	CodTSoggettoDisabilitatoDao codTSoggettoDisabilitatoDao;

	@Autowired
	CodTSoggettoAbilitatoDao codTSoggettoAbilitatoDao;

	@Autowired
	CodTConversazioneDao codTConversazioneDao;

	@Autowired
	CodSConversazioneDao codSConversazioneDao;

	@Autowired
	CodTAdesioneDao codTAdesioneDao;

	@Autowired
	CodSAdesioneDao codSAdesioneDao;

	@Autowired
	DataSource dataSource;

	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	@Autowired
	AuditLogService auditLogService;

	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;
	
	@Transactional
	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, PayloadAdesione payloadAdesione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		var methodName = "execute";
		ErrorBuilder error = null;
		try {
			// validate
			List<ErroreDettaglio> listError = validateMedicoRequest.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, payloadAdesione, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
//				codRMessaggioErroreService.saveError(listError, httpRequest);
//				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validateMedicoRequest");
			}

			/*
			 * INIZIO LOGICA APPLICATIVA SUPERATI TUTTI I CONTROLLI
			 */

			TAdesione adesione = null;
			String codiceCatalogo = "COD_MED_ADE";
			/*
			 * ADESIONE
			 */
			if (payloadAdesione.isAdesione()) {
				Soggetto soggetto = codTSoggettoDao.selectSoggettoByCF(shibIdentitaCodiceFiscale);
				if (soggetto == null) {
					Soggetto soggettoInsert = new Soggetto();
					soggettoInsert.setSoggettoCf(shibIdentitaCodiceFiscale);
					soggettoInsert.setSoggettoCognome(payloadAdesione.getCognomeMedico());
					soggettoInsert.setSoggettoNome(payloadAdesione.getNomeMedico());
					soggettoInsert.setUtenteCreazione(shibIdentitaCodiceFiscale);
					soggettoInsert.setUtenteModifica(shibIdentitaCodiceFiscale);
					codTSoggettoDao.insertSoggetto(soggettoInsert);
					soggetto = codTSoggettoDao.selectSoggetto(shibIdentitaCodiceFiscale);
				} else if(!soggetto.getSoggettoIsMedico()) {
					/*Modificato analisi il 26/10/2022 Se il record ha SOGGETTO_IS_MEDICO=false, aggiornare i campi:
					â¢	SOGGETTO_IS_MEDICO= true
					â¢	DATA_MODIFICA=data e ora corrente
					â¢	UTENTE_MODIFICA= valore campo Shib-Identita-CodiceFiscale passato in input
					 */
					codTSoggettoDao.updateCittadinoToMedico(shibIdentitaCodiceFiscale, soggetto.getSoggettoId());
				}
				codTAdesioneDao.insertAdesione(soggetto);
				adesione = codTAdesioneDao.selectAdesione(shibIdentitaCodiceFiscale);
			}

			/*
			 * REVOCA
			 */
			if (!payloadAdesione.isAdesione()) {
				codiceCatalogo = "COD_MED_REVADE";
				adesione = codTAdesioneDao.selectAdesione(shibIdentitaCodiceFiscale);
				codSAdesioneDao.insertSAdesione(adesione);
				adesione = codTAdesioneDao.updateAdesione(adesione);
				codSConversazioneDao.insertSConversazione(adesione);
				codTConversazioneDao.updateConversazioniRevoca(adesione);
				codTSoggettoDisabilitatoDao.insertSoggettoDisabilitato(adesione);
				codTSoggettoAbilitatoDao.deleteTSocggettoAbilitato(adesione);
			}

			ModelAdesione ma = new ModelAdesione();
			ma.setDataInizioAdesione(adesione.getAdesioneInizio());
			ma.setDataFineAdesione(adesione.getAdesioneFine() != null ? adesione.getAdesioneFine() : null);

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
					xCodiceVerticale, ruolo, collocazioneDescrizione, null, ApiAuditEnum.POST_ADESIONE,
					descrizioneCatalogoLogAudit, idCatalogoLogAudit);
			return Response.status(200).entity(ma).build();

		} catch (DatabaseException e) {
//			codRMessaggioErroreService.saveError(null, httpRequest);
			logError(methodName, "Errore riguardante database:", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		} catch (ResponseErrorException e) {
			logError(methodName, "Errore generico response:", e.getMessage());
			error = e.getResponseError();
		} catch (Exception e) {
//			codRMessaggioErroreService.saveError(null, httpRequest);
//			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);
			logError(methodName, "Errore non gestito :", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		}
		error = codRMessaggioErroreService.saveError(error, httpRequest);
		return error.generateResponseError();
	}

}
