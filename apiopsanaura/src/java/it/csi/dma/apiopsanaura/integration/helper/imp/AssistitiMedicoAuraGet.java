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
import it.csi.dma.apiopsanaura.dto.custom.ResponseContattoDigitaleCustom;
import it.csi.dma.apiopsanaura.dto.custom.UtRecAssistitoVOCustom;
import it.csi.dma.apiopsanaura.exception.DatabaseException;
import it.csi.dma.apiopsanaura.integration.auditlog.ApiAuditEnum;
import it.csi.dma.apiopsanaura.integration.auditlog.AuditLogService;
import it.csi.dma.apiopsanaura.integration.dao.custom.DmaccTCatalogoLogAuditDao;
import it.csi.dma.apiopsanaura.service.AssistitiGetService;
import it.csi.dma.apiopsanaura.service.CodRMessaggioErroreService;
import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.contattodigitale.ResponseContattoDigitale;
import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.contattodigitale.UtRecAssistitoVO;
import it.csi.dma.apiopsanaura.util.ErrorBuilder;
import it.csi.dma.apiopsanaura.util.LoggerUtil;
import it.csi.dma.apiopsanaura.util.enumerator.HeaderEnum;
import it.csi.dma.apiopsanaura.util.enumerator.StatusEnum;
import it.csi.dma.apiopsanaura.util.validator.ValidateAssistitiGet;

@Service
public class AssistitiMedicoAuraGet extends LoggerUtil {

	@Autowired
	ValidateAssistitiGet validateAssistitiGet;
	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;
	@Autowired
	AssistitiGetService assistitiGetService;
	@Autowired
	AuditLogService auditLogService;
	@Autowired
	DmaccTCatalogoLogAuditDao dmaccTCatalogoLogAuditDao;

	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneCodice,
			String collocazioneDescrizione, Integer nPagina, String codFiscAss, String cognome, String nome,
			Integer etaMin, Integer etaMax, String sesso, String esenzionePatologia, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = "execute";
		ResponseContattoDigitaleCustom result = new ResponseContattoDigitaleCustom();
		try {
//		List<ModelAssistitoRicerca>result = new ArrayList<ModelAssistitoRicerca>();
			List<ErroreDettaglio> listError = validateAssistitiGet.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, nPagina, codFiscAss, cognome, nome, etaMin, etaMax, sesso,
					esenzionePatologia, securityContext, httpHeaders, httpRequest);
			if (!listError.isEmpty()) {
				codRMessaggioErroreService.saveError(listError, httpRequest);
				return ErrorBuilder.generateResponseError(StatusEnum.BAD_REQUEST, listError);
			}

			ResponseContattoDigitale resp = assistitiGetService.getAssistitiAura(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice,
					collocazioneDescrizione, nPagina, codFiscAss, cognome, nome, etaMin, etaMax, sesso,
					esenzionePatologia, securityContext, httpHeaders, httpRequest);
			if (resp != null) {
				result.setErrorCode(resp.getErrorCode());
				result.setErrorMessage(resp.getErrorMessage());
				result.setRetVal(resp.getRetVal());
				if (resp.getElencoAssistiti() != null & resp.getElencoAssistiti().size() > 0) {
					result.setTotalPage(resp.getTotalPage());
					result.setElencoAssistiti(convertAssistitiList(resp.getElencoAssistiti()));
				}

			}
			if (StringUtils.isNotBlank(result.getErrorCode())) {
				return Response.status(Status.BAD_REQUEST).entity(result).build();
			}
			// AUDIT LOG
			String codiceCatalogo = "COD_MED_CERASS";
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
					xCodiceVerticale, ruolo, collocazioneDescrizione, codFiscAss, ApiAuditEnum.GET_ASSISTITI_AURA,
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

	private List<UtRecAssistitoVOCustom> convertAssistitiList(List<UtRecAssistitoVO> elencoAssistiti) {
		List<UtRecAssistitoVOCustom> result = new ArrayList<UtRecAssistitoVOCustom>();
		UtRecAssistitoVOCustom tmp = null;
		for (UtRecAssistitoVO assistito : elencoAssistiti) {
			tmp = new UtRecAssistitoVOCustom(assistito.getCodiceFiscale(), assistito.getCognome(), assistito.getNome(),
					assistito.getSesso(), assistito.getDataNascita(), assistito.getCodiceComuneNasc(),
					assistito.getDescrizioneComuneNasc(), assistito.getIdAura());
			result.add(tmp);
		}
		return result;
	}
}
