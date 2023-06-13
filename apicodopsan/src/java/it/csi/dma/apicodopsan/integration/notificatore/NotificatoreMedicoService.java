/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.notificatore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.csi.dma.apicodopsan.dto.custom.DmaccDEventoPerNotificatore;
import it.csi.dma.apicodopsan.dto.custom.DmaccREvnotDestMsg;
import it.csi.dma.apicodopsan.dto.custom.Soggetto;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.integration.dao.custom.CodCParametroDao;
import it.csi.dma.apicodopsan.integration.dao.custom.DmaccDEventoPerNotificatoreDao;
import it.csi.dma.apicodopsan.integration.dao.custom.DmaccREvnotDestMsgDao;
import it.csi.dma.apicodopsan.integration.notificatore.dto.medico.Email;
import it.csi.dma.apicodopsan.integration.notificatore.dto.medico.Mex;
import it.csi.dma.apicodopsan.integration.notificatore.dto.medico.NotificaMedicoCustom;
import it.csi.dma.apicodopsan.integration.notificatore.dto.medico.PayloadMedicoCustom;
import it.csi.dma.apicodopsan.integration.notificatore.dto.medico.Push;
import it.csi.dma.apicodopsan.integration.notificatore.enumeration.NotificatoreEventCode;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.ApiHeaderParamEnum;

@Service
public class NotificatoreMedicoService extends LoggerUtil {

	@Value("${notificatore.medico.applicazione}")
	private String nomeApplicazione;
	@Value("${notificatore.medico.url}")
	private String urlNotificatoreMedico = "";
	@Value("${notificatore.medico.token}")
	private String tokenApplicativo;
	@Value("${notificatore.medico.tag}")
	private String tag;
	@Value("${notificatore.medico.templateId}")
	private String templateId;
	private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	@Autowired
	private DmaccDEventoPerNotificatoreDao dmaccDEventoPerNotificatoreDao;
	@Autowired
	private DmaccREvnotDestMsgDao dmaccREvnotDestMsgDao;
	@Autowired
	private CodCParametroDao codCParametroDao;
	// TARARE SU SER 50

	public void notificaAbilitazione(String cfMittente, Soggetto soggettoRicevente, Integer numeroAssistitiElaborati,
			String xRequestId, String xForwardedFor) throws Exception {
		this.notificaMessaggioMedico(cfMittente, soggettoRicevente, xRequestId,
				NotificatoreEventCode.EVENTO_MEDICO_ABILITA.getCode(),
				NotificatoreEventCode.EVENTO_MEDICO_ABILITA.getCode(), xForwardedFor, numeroAssistitiElaborati, null,null);
	}

	public void notificaAbilitazioneGiaAbilitati(String cfMittente, Soggetto soggettoRicevente,
			Integer numeroAssistitiElaborati, Integer numeroAssistitiGiaElaborati, Integer numeroAssistitiInErrore,
			String xRequestId, String xForwardedFor) throws Exception {
		this.notificaMessaggioMedico(cfMittente, soggettoRicevente, xRequestId,
				NotificatoreEventCode.EVENTO_MEDICO_ABILITA_GIA_ABILITATI.getCode(),
				NotificatoreEventCode.EVENTO_MEDICO_ABILITA_GIA_ABILITATI.getCode(), xForwardedFor,
				numeroAssistitiElaborati, numeroAssistitiGiaElaborati,numeroAssistitiInErrore);
	}

	public void notificaAbilitazioneErrore(String cfMittente, Soggetto soggettoRicevente, String xRequestId,
			String xForwardedFor) throws Exception {
		this.notificaMessaggioMedico(cfMittente, soggettoRicevente, xRequestId,
				NotificatoreEventCode.EVENTO_MEDICO_ABILITA_ERRORE.getCode(),
				NotificatoreEventCode.EVENTO_MEDICO_ABILITA_ERRORE.getCode(), xForwardedFor, null, null,null);
	}

	public void notificaDisabilitazione(String cfMittente, Soggetto soggettoRicevente, Integer numeroAssistitiElaborati,
			String xRequestId, String xForwardedFor) throws Exception {
		this.notificaMessaggioMedico(cfMittente, soggettoRicevente, xRequestId,
				NotificatoreEventCode.EVENTO_MEDICO_DISABILITA.getCode(),
				NotificatoreEventCode.EVENTO_MEDICO_DISABILITA.getCode(), xForwardedFor, numeroAssistitiElaborati,
				null,null);
	}

	public void notificaDisabilitazioneGiaDisabilitati(String cfMittente, Soggetto soggettoRicevente,
			Integer numeroAssistitiElaborati, Integer numeroAssistitiGiaElaborati, Integer numeroAssistitiInErrore,
			String xRequestId, String xForwardedFor) throws Exception {
		this.notificaMessaggioMedico(cfMittente, soggettoRicevente, xRequestId,
				NotificatoreEventCode.EVENTO_MEDICO_DISABILITA_GIA_DISABILITATI.getCode(),
				NotificatoreEventCode.EVENTO_MEDICO_DISABILITA_GIA_DISABILITATI.getCode(), xForwardedFor,
				numeroAssistitiElaborati, numeroAssistitiGiaElaborati, numeroAssistitiInErrore);
	}

	public void notificaDisabilitazioneErrore(String cfMittente, Soggetto soggettoRicevente, String xRequestId,
			String xForwardedFor) throws Exception {
		this.notificaMessaggioMedico(cfMittente, soggettoRicevente, xRequestId,
				NotificatoreEventCode.EVENTO_MEDICO_DISABILITA_ERRORE.getCode(),
				NotificatoreEventCode.EVENTO_MEDICO_DISABILITA_ERRORE.getCode(), xForwardedFor, null, null, null);
	}

	/**
	 * SEND NOTIFICA MEDICO
	 * 
	 * @param cfMittente
	 * @param soggettoRicevente
	 * @param cfDelegante
	 * @param xRequestId
	 * @param codiceEventoTitle
	 * @param codiceEventoBody
	 * @param xForwardedFor
	 * @return
	 * @throws Exception
	 */

	public String notificaMessaggioMedico(String cfMittente, Soggetto soggettoRicevente, String xRequestId,
			String codiceEventoTitle, String codiceEventoBody, String xForwardedFor, Integer numeroAssistitiElaborati,
			Integer numeroAssistitiGiaElaborati, Integer numeroAssistitiInErrore) throws Exception {
		NotificaMedicoCustom notificaCustom = buildNotificaMedicoCustom(cfMittente, soggettoRicevente,
				numeroAssistitiElaborati, numeroAssistitiGiaElaborati, numeroAssistitiInErrore, codiceEventoTitle,
				codiceEventoBody);
		String result = sendNotificaEventoMedico(notificaCustom, cfMittente, xRequestId, xForwardedFor);
		return result;

	}

	private NotificaMedicoCustom buildNotificaMedicoCustom(String cfMittente, Soggetto soggettoRicevente,
			Integer numeroAssistitiElaborati, Integer numeroAssistitiGiaElaborati, Integer numeroAssistitiInErrore,
			String codiceEventoTitle, String codiceEventoBody) throws DatabaseException {
		NotificaMedicoCustom notifica = new NotificaMedicoCustom();
		String uuidIn = UUID.randomUUID().toString();
		notifica.setUuid(uuidIn);
		notifica.setToBeRetried(false);
		PayloadMedicoCustom payload = buildPayloadMedicoCustom(cfMittente, soggettoRicevente, numeroAssistitiElaborati,
				numeroAssistitiGiaElaborati, numeroAssistitiInErrore, codiceEventoTitle, codiceEventoBody, uuidIn);
		notifica.setPayload(payload);
		return notifica;
	}

	private PayloadMedicoCustom buildPayloadMedicoCustom(String cfMittente, Soggetto soggettoRicevente,
			Integer numeroAssistitiElaborati, Integer numeroAssistitiGiaElaborati, Integer numeroAssistitiInErrore,
			String codiceEventoTitle, String codiceEventoBody, String uuidIn) throws DatabaseException {
		PayloadMedicoCustom payload = new PayloadMedicoCustom();
		payload.setId(uuidIn);
		payload.setUserId(soggettoRicevente.getSoggettoCf());
		payload.setApplicazione(nomeApplicazione);
		payload.setTag(tag);
		payload.setBulkId(UUID.randomUUID().toString());
		logInfo("buildPayloadMedicoCustom", "codiceEventoBody" + codiceEventoTitle);
		DmaccDEventoPerNotificatore eventoTitle = dmaccDEventoPerNotificatoreDao
				.selectDmaccDEventoPerNotificatoreByCodiceEvento(codiceEventoTitle);
		String descrizioneEvento = eventoTitle.getDescrizioneEvento();
		DmaccREvnotDestMsg eventoBody = dmaccREvnotDestMsgDao
				.selectDmaccREvnotDestMsgDaoByCodiceEvento(codiceEventoBody);
		String urlCodMed = codCParametroDao.selectValoreParametroFromParametroNome("URL_COD_MED");

		Email email = buildEmail(eventoBody, descrizioneEvento, soggettoRicevente, numeroAssistitiElaborati,
				numeroAssistitiGiaElaborati, numeroAssistitiInErrore, urlCodMed);
		payload.setEmail(email);

		Push push = buildPush(eventoBody, descrizioneEvento, soggettoRicevente, numeroAssistitiElaborati,
				numeroAssistitiGiaElaborati, numeroAssistitiInErrore, urlCodMed);
		payload.setPush(push);

		Mex mex = buildMex(eventoBody, descrizioneEvento, soggettoRicevente, numeroAssistitiElaborati,
				numeroAssistitiGiaElaborati, numeroAssistitiInErrore, urlCodMed);
		payload.setMex(mex);
		return payload;

	}

	private Mex buildMex(DmaccREvnotDestMsg eventoBody, String descrizioneEvento, Soggetto soggettoRicevente,
			Integer numeroAssistitiElaborati, Integer numeroAssistitiGiaElaborati, Integer numeroAssistitiInErrore,
			String urlCodMed) {
		Mex mex = new Mex();
		mex.setTitle(descrizioneEvento);
		String body = buildBody(eventoBody.getMsg_mex(), soggettoRicevente, numeroAssistitiElaborati,
				numeroAssistitiGiaElaborati, numeroAssistitiInErrore, urlCodMed);
		mex.setBody(body);

		return mex;
	}

//	sostituire nei vari body
//	{0}SOGGETTO_NOME soggettoRicevente
//	{1}SOGGETTO_COGNOME soggettoRicevente
//	{5}URL_COD_CIT DA COD_C_PARAMETRI soggettoRicevente
//	if cfdelegante
	// {2} codice fiscale dell assistito che partecipa alla conversazione
	// (Delegante) cfDelegato
	private String buildBody(String msg, Soggetto soggettoRicevente, Integer numeroAssistitiElaborati,
			Integer numeroAssistitiGiaElaborati, Integer numeroAssistitiInErrore, String urlCodMed) {
		String result = msg;
		result = result.replace("{0}", soggettoRicevente.getSoggettoNome());
		result = result.replace("{1}", soggettoRicevente.getSoggettoCognome());
		if (StringUtils.isNotBlank(urlCodMed)) {
			result = result.replace("{5}", urlCodMed);
		}
		if (numeroAssistitiElaborati != null) {
			result = result.replace("{2}", numeroAssistitiElaborati.toString());
		}
		if (numeroAssistitiGiaElaborati != null) {
			result = result.replace("{3}", numeroAssistitiGiaElaborati.toString());
		}
		if (numeroAssistitiInErrore != null) {
			result = result.replace("{4}", numeroAssistitiInErrore.toString());
		}
		return result;
	}

	private Push buildPush(DmaccREvnotDestMsg eventoBody, String descrizioneEvento, Soggetto soggettoRicevente,
			Integer numeroAssistitiElaborati, Integer numeroAssistitiGiaElaborati, Integer numeroAssistitiInErrore,
			String urlCodMed) {
		Push push = new Push();
		push.setTitle(descrizioneEvento);
		String body = buildBody(eventoBody.getMsg_push(), soggettoRicevente, numeroAssistitiElaborati,
				numeroAssistitiGiaElaborati, numeroAssistitiInErrore, urlCodMed);
		push.setBody(body);
		return push;
	}

	private Email buildEmail(DmaccREvnotDestMsg eventoBody, String descrizioneEvento, Soggetto soggettoRicevente,
			Integer numeroAssistitiElaborati, Integer numeroAssistitiGiaElaborati, Integer numeroAssistitiInErrore,
			String urlCodMed) {
		Email email = new Email();
		email.setSubject(descrizioneEvento);
		String body = buildBody(eventoBody.getMsg_mail(), soggettoRicevente, numeroAssistitiElaborati,
				numeroAssistitiGiaElaborati, numeroAssistitiInErrore, urlCodMed);
		email.setBody(body);
		email.setTemplateId(templateId);
		return email;
	}

	private String sendNotificaEventoMedico(NotificaMedicoCustom notifica, String cfSoggetto, String xRequestId,
			String xForwardedFor) throws Exception {
		logInfo("sendNotificaEventoMedico ",
				"params:\n" + ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode() + ": " + cfSoggetto + ", \n"
						+ ApiHeaderParamEnum.X_AUTHENTICATION.getCode() + ": " + tokenApplicativo + "\n"
						+ ApiHeaderParamEnum.X_REQUEST_ID.getCode() + ": " + xRequestId + "\n" + " payload ID: "
						+ notifica.getPayload().getId() + "\n");
		String result = null;
		String jsonPayloadString = fromObjectToJsonString(notifica);
		logInfo("sendNotificaEventoMedico-", "payload: " + jsonPayloadString);
		HttpRequest request = HttpRequest.newBuilder().POST(BodyPublishers.ofString(jsonPayloadString))
				.uri(URI.create(urlNotificatoreMedico)).setHeader(ApiHeaderParamEnum.X_REQUEST_ID.getCode(), xRequestId)
				.setHeader(ApiHeaderParamEnum.X_FORWARDED_FOR.getCode(), xForwardedFor)
				.setHeader(ApiHeaderParamEnum.X_AUTHENTICATION.getCode(), tokenApplicativo)
				.setHeader(ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode(), cfSoggetto)
				.setHeader(ApiHeaderParamEnum.AUTHORIZATION.getCode(), "Basic YWRtaW46YWRtaW4=")
				.setHeader(ApiHeaderParamEnum.CONTENT_TYPE.getCode(), "application/json").build();

		HttpResponse<String> response = null;
		response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		// print status code
		logInfo("sendNotificaEventoMedico",
				"status:" + response.statusCode() + " - notifica_uuid: " + notifica.getUuid());
		logInfo("sendNotificaEventoMedico", "response: " + response.toString());
		logInfo("sendNotificaEventoMedico", "responsebody: " + response.body());

		if (!(response.statusCode() == 200 || response.statusCode() == 201)) {
			logError("sendNotificaEvento", response.body() + " - notifica_uuid:" + notifica.getUuid());
		}
		result = response.body();
		return result;
	}

	private String fromObjectToJsonString(NotificaMedicoCustom notificaCustom) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(notificaCustom);
	}
}
