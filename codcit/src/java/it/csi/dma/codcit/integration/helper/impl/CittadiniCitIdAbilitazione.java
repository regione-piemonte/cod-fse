/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.helper.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.csi.dma.codcit.dto.ModelAbilitazione;
import it.csi.dma.codcit.dto.ModelMotivoBlocco;
import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.exception.DelegheFallimentoException;
import it.csi.dma.codcit.exception.ResponseErrorException;
import it.csi.dma.codcit.integration.dao.custom.CodDDisabilitazioneMotivoDao;
import it.csi.dma.codcit.integration.dao.custom.CodTSoggettoAbilitatoDao;
import it.csi.dma.codcit.integration.dao.custom.CodTSoggettoDisabilitatoDao;
import it.csi.dma.codcit.integration.service.CodDErroreService;
import it.csi.dma.codcit.integration.service.CodRMessaggioErroreService;
import it.csi.dma.codcit.integration.service.DelegheService;
import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.enumerator.CodeErrorEnum;
import it.csi.dma.codcit.util.enumerator.StatusEnum;
import it.csi.dma.codcit.util.validator.ValidateCfMedicoGeneric;

@Service
public class CittadiniCitIdAbilitazione extends ExecuteUtil {

	private static final String ASSISTITO_ABILITATO = "A";
	private static final String ASSISTITO_NON_ABILITATO = "B";
	private static final String ASSISTITO_SENZA_STATO = "N";

	@Autowired
	@Qualifier("validateCfMedicoGeneric")
	ValidateCfMedicoGeneric validateMedicGeneric;

	@Autowired
	CodTSoggettoAbilitatoDao codTSoggettoAbilitatoDao;

	@Autowired
	CodTSoggettoDisabilitatoDao codTSoggettoDisabilitatoDao;

	@Autowired
	DelegheService delegheService;

	@Autowired
	CodDErroreService codDErroreService;

	@Autowired
	CodDDisabilitazioneMotivoDao codDDisabilitazioneMotivoDao;
	
	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String citId, String cfMedico, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		var methodName = "execute";
		ErrorBuilder error = null;

		try {
			// 2.a, 2.b validate
			List<ErroreDettaglioExt> listError = validateMedicGeneric.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, citId, cfMedico);
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),"errore in validate");
			}

			// 3. verifica codici fiscali
			if (!delegheService.checkUtenteAutorizzatoOrDelegato(xRequestId, xCodiceServizio, shibIdentitaCodiceFiscale,
					citId)) {
				logError(methodName, "delegato non autorizzato");
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.CC_ER_185.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.FORBIDDEN, listError),"delegato non autorizzato");
			}

			// 5. verifica abilitazioni
			var result = new ModelAbilitazione();
			result.setAbilitazione(ASSISTITO_SENZA_STATO);
			if (codTSoggettoAbilitatoDao.selectSoggettoAbilitatoWhereCittadinoAbilitatoDaMedico(cfMedico, citId) > 0) {
				result.setAbilitazione(ASSISTITO_ABILITATO);
			} else {
				var soggettoDisabilitato = codTSoggettoDisabilitatoDao
						.selectSoggettoDisabilitatoWhereCittadinoAbilitatoDaMedico(cfMedico, citId);
				if (soggettoDisabilitato != null) {
					result.setAbilitazione(ASSISTITO_NON_ABILITATO);
					result.setMotivazioneMedico(soggettoDisabilitato.getDisabilitazioneMotivazione());
					result.setMotivoBlocco(new ModelMotivoBlocco());
					var disabilitazioneMotivo = codDDisabilitazioneMotivoDao.selectMotivoCodAndMotivoDescFromMotivoId(
							soggettoDisabilitato.getDisabilitazioneMotivoId());
					result.getMotivoBlocco().setCodice(disabilitazioneMotivo.getDisabilitazioneMotivoCod());
					result.getMotivoBlocco().setDescrizione(disabilitazioneMotivo.getDisabilitazioneMotivoDesc());
				}
			}

			return Response.ok(result).build();
			
		} catch (DatabaseException e) {
			logError(methodName, "Errore riguardante database:", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		} catch (DelegheFallimentoException e) {
			logError(methodName, "Errore riguardante deleghe:", e.getMessage());
			error = e.getResponseError();
		} catch (ResponseErrorException e) {
			logError(methodName, "Errore generico response:", e.getMessage());
			error = e.getResponseError();
		} catch (Exception e) {
			logError(methodName, "Errore generico:", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		}
		
		error = codRMessaggioErroreService.saveError(error, httpRequest);
		return error.generateResponseError();
	}
}
