/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.helper.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.csi.aura.auraws.services.central.anagrafesanitaria.GetProfiloSanitarioResponse;
import it.csi.dma.codcit.dto.Errore;
import it.csi.dma.codcit.dto.ModelMedicoInfo;
import it.csi.dma.codcit.dto.custom.AdesioneSoggetto;
import it.csi.dma.codcit.dto.custom.ErroreDettaglioExt;
import it.csi.dma.codcit.exception.AuraFallimentoException;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.exception.DelegheFallimentoException;
import it.csi.dma.codcit.exception.ResponseErrorException;
import it.csi.dma.codcit.integration.dao.custom.CodDDisabilitazioneMotivoDao;
import it.csi.dma.codcit.integration.dao.custom.CodTAdesioneSoggetto;
import it.csi.dma.codcit.integration.dao.custom.CodTSoggettoAbilitatoDao;
import it.csi.dma.codcit.integration.service.CodDErroreService;
import it.csi.dma.codcit.integration.service.CodRMessaggioErroreService;
import it.csi.dma.codcit.integration.service.DelegheService;
import it.csi.dma.codcit.integration.service.ProfilAuraService;
import it.csi.dma.codcit.util.ErrorBuilder;
import it.csi.dma.codcit.util.enumerator.CodeErrorEnum;
import it.csi.dma.codcit.util.enumerator.StatusEnum;
import it.csi.dma.codcit.util.validator.ValidateGeneric;

@Service
public class CittadiniCitIdInfoMedico extends ExecuteUtil {

	@Autowired
	@Qualifier("validateGeneric")
	ValidateGeneric validateGenericMeritImpl;

	@Autowired
	DelegheService delegheService;

	@Autowired
	CodDErroreService codDErroreService;

	@Autowired
	CodTSoggettoAbilitatoDao codTSoggettoAbilitatoDao;

	@Autowired
	CodDDisabilitazioneMotivoDao codDDisabilitazioneMotivoDao;

	@Autowired
	CodTAdesioneSoggetto codTAdesioneSoggetto;

	@Autowired
	ProfilAuraService profilAuraService;

	@Autowired
	CodRMessaggioErroreService codRMessaggioErroreService;

	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String citId, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		var methodName = "execute";
		ErrorBuilder error = null;

		try {
			// 1,2 validate
			List<ErroreDettaglioExt> listError = validateGenericMeritImpl.validate(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, citId);
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

			// 5. Ricerca assistito su AURA
			log.info("RICHIAMO SERVIZIO ESTERNO CittadiniCitIdInfoMedico profilAuraService.getProfiloSanitarioAura");
			long startTime = System.currentTimeMillis();
			GetProfiloSanitarioResponse profiloSanit= getProfiloSanitarioAura(citId);
			log.info("RISPOSTA SERVIZIO ESTERNO CittadiniCitIdInfoMedico profilAuraService.getProfiloSanitarioAura:"
					+ (System.currentTimeMillis() - startTime) + " Millis");
			var result = new ModelMedicoInfo();
			if (profiloSanit!=null) {
				result.setCodiceFiscale(profiloSanit.getGetProfiloSanitarioResult().getBody().getInfoSan().getCodiceFiscaleMedico());
				result.setCognome(profiloSanit.getGetProfiloSanitarioResult().getBody().getInfoSan().getCognomeMedico());
				result.setNome(profiloSanit.getGetProfiloSanitarioResult().getBody().getInfoSan().getNomeMedico());
			}


			// 6.Ricerca dati sul medico
			if(StringUtils.isNotBlank(result.getCodiceFiscale())) {
				logInfo(methodName, "Ricerca medico per codice fiscale su AURA Riuscita: "+result.getCodiceFiscale());
			}else {
				logError(methodName, "Codice fiscale null or blank impossibile effettuare la ricerca delle adesioni");
				ErrorBuilder errorBuilder = ErrorBuilder.from(StatusEnum.NOT_FOUND);
				throw new AuraFallimentoException(errorBuilder.detail(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_092.getCode())),"Errore Aura non esiste il medico per il cf:"+citId);
			}

			AdesioneSoggetto adesioneSoggetto=codTAdesioneSoggetto.selectAdesioneSoggettoFromCF(result.getCodiceFiscale());
			if(adesioneSoggetto!=null && adesioneSoggetto.getAdesioneId()>-1) {
				//prelevo eventuali informazioni dalla tabella cod_t_soggetto
				result.setDataInizioAdesione(adesioneSoggetto.getAdesioneInizio());
				result.setDataFineAdesione(adesioneSoggetto.getAdesioneFine());
				result.setNotificaLetturaMessaggi(adesioneSoggetto.isMostraLetturaMessaggiAAssistiti());
			} else {
				log.info("CittadiniCitIdInfoMedico::adesioneSoggetto_id nullo o non valido verficare sul db");
				//nessuna adesione trovata esco con 404
				listError.add(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_093.getCode()));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.FORBIDDEN, listError),"Nessuna adesione trovata");
			}
//			if (codTSoggettoAbilitatoDao.selectSoggettoAbilitatoWhereCittadinoAbilitatoDaMedico(cfMedico, citId) > 0) {
//				result.setAbilitazione(ASSISTITO_ABILITATO);
//			} else {
//				var soggettoDisabilitato = codTSoggettoDisabilitatoDao
//						.selectSoggettoDisabilitatoWhereCittadinoAbilitatoDaMedico(cfMedico, citId);
//				if (soggettoDisabilitato != null) {
//					result.setAbilitazione(ASSISTITO_NON_ABILITATO);
//					result.setMotivazioneMedico(soggettoDisabilitato.getDisabilitazioneMotivazione());
//					result.setMotivoBlocco(new ModelMotivoBlocco());
//					var disabilitazioneMotivo = codDDisabilitazioneMotivoDao.selectMotivoCodAndMotivoDescFromMotivoId(
//							soggettoDisabilitato.getDisabilitazioneMotivoId());
//					 result.getMotivoBlocco().setCodice(disabilitazioneMotivo.getDisabilitazioneMotivoCod());
//					result.getMotivoBlocco().setDescrizione(disabilitazioneMotivo.getDisabilitazioneMotivoDesc());
//				}
//			}

			return Response.ok(result).build();

		} catch (DatabaseException e) {
			logError(methodName, "Errore riguardante database:", e.getMessage());
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, null);
		} catch (DelegheFallimentoException e) {
			logError(methodName, "Errore riguardante deleghe:", e.getMessage());
			error = e.getResponseError();
		}catch (AuraFallimentoException e) {
			logError(methodName, "Errore riguardante aura:", e.getMessage());
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

	private GetProfiloSanitarioResponse getProfiloSanitarioAura(String citId)
			throws AuraFallimentoException, DatabaseException {
		GetProfiloSanitarioResponse profiloSanit = null;
		try {
			profiloSanit=profilAuraService.getProfiloAura(citId);
		}catch(AuraFallimentoException auraFallimentoException) {
			throw auraFallimentoException;
		}catch(Exception e) {
			logError("getProfiloSanitarioAura", "exception: "+e.getMessage());
			ErrorBuilder errorBuilder = ErrorBuilder.from(StatusEnum.NOT_FOUND);
			throw new AuraFallimentoException(errorBuilder.detail(codDErroreService.getValueGenericError(CodeErrorEnum.FSE_COD_092.getCode())),"Errore Aura non esiste il medico per il cf:"+citId);
		}
		return profiloSanit;
	}
}
