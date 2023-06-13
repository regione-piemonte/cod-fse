/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.integration.helper.imp;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.apiopsanaura.dto.ErroreDettaglio;
import it.csi.dma.apiopsanaura.dto.custom.DmaccDCatalogoLogAudit;
import it.csi.dma.apiopsanaura.dto.custom.ResponseElencoEsenzioniCustom;
import it.csi.dma.apiopsanaura.dto.custom.UtRecEsenzioniVOCustom;
import it.csi.dma.apiopsanaura.exception.DatabaseException;
import it.csi.dma.apiopsanaura.integration.auditlog.ApiAuditEnum;
import it.csi.dma.apiopsanaura.integration.auditlog.AuditLogService;
import it.csi.dma.apiopsanaura.integration.dao.custom.DmaccTCatalogoLogAuditDao;
import it.csi.dma.apiopsanaura.service.CodRMessaggioErroreService;
import it.csi.dma.apiopsanaura.service.ElencoEsenzioniGetService;
import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.elencoesenzioni.ResponseElencoEsenzioni;
import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.elencoesenzioni.UtRecEsenzioniVO;
import it.csi.dma.apiopsanaura.util.ErrorBuilder;
import it.csi.dma.apiopsanaura.util.LoggerUtil;
import it.csi.dma.apiopsanaura.util.enumerator.HeaderEnum;
import it.csi.dma.apiopsanaura.util.enumerator.StatusEnum;
import it.csi.dma.apiopsanaura.util.validator.ValidateElencoEsenzioniGet;

@Service
public class ElencoEsenzioniMedicoGet extends LoggerUtil {

	@Autowired
	ValidateElencoEsenzioniGet validateElencoEsenzioniGet;
	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;
	@Autowired
	ElencoEsenzioniGetService elencoEsenzioniGetService;
	@Autowired
	AuditLogService auditLogService;
	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;

	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, String tipoEsenzione, String listaDiagnosi, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = "execute";
		ResponseElencoEsenzioniCustom result = null;
		try {

			List<ErroreDettaglio> listError = validateElencoEsenzioniGet.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, tipoEsenzione, listaDiagnosi, securityContext, httpHeaders, httpRequest);
			if (!listError.isEmpty()) {
				codRMessaggioErroreService.saveError(listError, httpRequest);
				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
			}
			ResponseElencoEsenzioni rsp = elencoEsenzioniGetService.getElencoEsenzioniAura(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, tipoEsenzione, listaDiagnosi, securityContext, httpHeaders, httpRequest);

			if (rsp != null) {
				result = buildElencoEsenzioniCustom(rsp);
			}

			if (StringUtils.isNotBlank(result.getErrorCode())) {
				return Response.status(Status.BAD_REQUEST).entity(result).build();
			}
			// AUDIT LOG
			String codiceCatalogo = "COD_MED_LISTPAT";
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
					xCodiceVerticale, ruolo, collocazioneDescrizione, null, ApiAuditEnum.GET_ESENZIONI_AURA,
					descrizioneCatalogoLogAudit, idCatalogoLogAudit);
			return Response.ok(result).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId).build();
		} catch (DatabaseException e) {
			logError(methodName, "Errore riguardante database:", e.getMessage());
			return ErrorBuilder.generateResponseError(StatusEnum.SERVER_ERROR, null);

		} catch (Exception e) {
			e.printStackTrace();
			logError(methodName, "Exception: " + e.getMessage() + ", " + e.getCause());
			return ErrorBuilder.generateResponseErrorWithTitle(StatusEnum.SERVER_ERROR, e.getMessage(), null);
		}

	}

	private ResponseElencoEsenzioniCustom buildElencoEsenzioniCustom(ResponseElencoEsenzioni rsp) {
		ResponseElencoEsenzioniCustom res = new ResponseElencoEsenzioniCustom();
		res.setErrorCode(rsp.getErrorCode());
		res.setErrorMessage(rsp.getErrorMessage());
		res.setRetVal(rsp.getRetVal());
		res.setElencoEsenzioni(buildListRecEsenzioniVO(rsp.getElencoEsenzioni()));
		return res;
	}

	private List<UtRecEsenzioniVOCustom> buildListRecEsenzioniVO(List<UtRecEsenzioniVO> elencoEsenzioni) {
		List<UtRecEsenzioniVOCustom> result = new ArrayList<UtRecEsenzioniVOCustom>();
		UtRecEsenzioniVOCustom tmp;
		for (UtRecEsenzioniVO esenzioneIn : elencoEsenzioni) {
			tmp = new UtRecEsenzioniVOCustom();
			tmp.setCodDiagnosi(esenzioneIn.getCodDiagnosi());
			tmp.setCodEsenzione(esenzioneIn.getCodEsenzione());
			tmp.setDataFineValidita(esenzioneIn.getDataFineValidita());
			tmp.setDataInizioValidita(esenzioneIn.getDataInizioValidita());
			tmp.setDescDiagnosi(esenzioneIn.getDescDiagnosi());
			tmp.setDescEsenzione(esenzioneIn.getDescEsenzione());
			tmp.setTipoEsenzione(esenzioneIn.getTipoEsenzione());
			result.add(tmp);
		}
		return result;
	}
}
