/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.notificatore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.csi.dma.codcit.dto.custom.DmaccDEventoPerNotificatore;
import it.csi.dma.codcit.dto.custom.DmaccREvnotDestMsg;
import it.csi.dma.codcit.dto.custom.Soggetto;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.dao.custom.CodCParametroDao;
import it.csi.dma.codcit.integration.dao.custom.DmaccDEventoPerNotificatoreDao;
import it.csi.dma.codcit.integration.dao.custom.DmaccREvnotDestMsgDao;
import it.csi.dma.codcit.integration.notificatore.dto.EmailPayload;
import it.csi.dma.codcit.integration.notificatore.dto.MexPayload;
import it.csi.dma.codcit.integration.notificatore.dto.NotificaCustom;
import it.csi.dma.codcit.integration.notificatore.dto.PayloadNotifica;
import it.csi.dma.codcit.integration.notificatore.dto.PushPayload;
import it.csi.dma.codcit.integration.notificatore.enumeration.NotificatoreEventCode;
import it.csi.dma.codcit.util.LoggerUtil;
import it.csi.dma.codcit.util.enumerator.ApiHeaderParamEnum;

@Service
public class NotificatoreService extends LoggerUtil {
//	https://gitlab.csi.it/user-notification-platform/unpdocumentazione/blob/master/documentazione%20fornitori/NOTIFY-specifiche.md
	
	@Value("${notificatore.cittadino.applicazione}")
	private String nomeApplicazione;
	@Value("${notificatore.cittadino.url}")
	private String urlNotificatore;
	@Value("${notificatore.cittadino.token}")
	private String tokenApplicativo;
	@Value("${notificatore.cittadino.tag}")
	private String tag;
	@Value("${notificatore.cittadino.templateId}")
	private String templateId;
	private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	private static final DateFormat DATE_FORMAT_ISO_8601= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	@Autowired
	private DmaccDEventoPerNotificatoreDao dmaccDEventoPerNotificatoreDao;
	@Autowired
	private CodCParametroDao codCParametroDao;
	@Autowired
	private DmaccREvnotDestMsgDao dmaccREvnotDestMsgDao;
	
	/**
	 * SEND notifica
	 * 
	 * @param cfSoggetto
	 * @param uuid
	 * @param codiceEventoTitle
	 * @param codiceEventoBody
	 * @throws DatabaseException
	 */
	
	public void notificaEventoMessaggioPost(String cfMittente,Soggetto soggettoRicevente,String xRequestId) throws Exception {
		 notificaMessaggio(cfMittente,soggettoRicevente,xRequestId,  NotificatoreEventCode.EVENTO_POST_MESSAGGIO_CITTADINO_DELEGATO.getCode(), NotificatoreEventCode.EVENTO_POST_MESSAGGIO_CITTADINO_DELEGATO.getCode());
	}
	
	public void notificaEventoMessaggioPut(String cfMittente,Soggetto soggettoRicevente,String xRequestId) throws Exception {
		 notificaMessaggio(cfMittente,soggettoRicevente,xRequestId,  NotificatoreEventCode.EVENTO_PUT_MESSAGGIO_CITTADINO_DELEGATO.getCode(), NotificatoreEventCode.EVENTO_PUT_MESSAGGIO_CITTADINO_DELEGATO.getCode());
	}
	
	private String notificaMessaggio(String cfMittente,Soggetto soggettoRicevente,String xRequestId, String codiceEventoTitle, String codiceEventoBody)
			throws Exception {
		String delegate=cfMittente;
		NotificaCustom notificaCustom = buildNotificaCustom(soggettoRicevente,delegate,  codiceEventoTitle, codiceEventoBody);
		String result= sendNotificaEvento(notificaCustom, cfMittente, tokenApplicativo,xRequestId);
		return result;
				
	}

	private NotificaCustom buildNotificaCustom(Soggetto soggettoRicevente,String cfDelegante, String codiceEventoTitle,
			String codiceEventoBody) throws DatabaseException {
		NotificaCustom notifica = new NotificaCustom();
		String uuidIn=UUID.randomUUID().toString();
		notifica.setUuid(uuidIn);
		notifica.setExpireAt(DATE_FORMAT_ISO_8601.format(DateUtils.addHours(new Date(), 1)));
		PayloadNotifica payload = buildPayloadNotifica( soggettoRicevente, cfDelegante, codiceEventoTitle, codiceEventoBody,uuidIn);
		notifica.setPayload(payload);
		return notifica;
	}

	private PayloadNotifica buildPayloadNotifica(Soggetto soggettoRicevente,String cfDelegante, String codiceEventoTitle, String codiceEventoBody,String uuidIn)
			throws DatabaseException {
		PayloadNotifica payload = new PayloadNotifica();
		payload.setId(uuidIn);
		payload.setUserId(soggettoRicevente.getSoggettoCf());
		payload.setTag(tag);
		logInfo("buildPayloadNotifica", "codiceEventoBody"+codiceEventoTitle);
		DmaccDEventoPerNotificatore eventoTitle = dmaccDEventoPerNotificatoreDao.selectDmaccDEventoPerNotificatoreByCodiceEvento(codiceEventoTitle);
		String descrizioneEvento= eventoTitle.getDescrizioneEvento();
		DmaccREvnotDestMsg eventoBody = dmaccREvnotDestMsgDao.selectDmaccREvnotDestMsgDaoByCodiceEvento(codiceEventoBody);
		String urlCodCit =codCParametroDao.selectValoreParametroFromParametroNome("URL_COD_CIT");
		
		EmailPayload email = buildEmailPayload( eventoBody, descrizioneEvento,soggettoRicevente,cfDelegante,urlCodCit);
		payload.setEmail(email);
		
		PushPayload push = buildPushPayload(eventoBody, descrizioneEvento,soggettoRicevente,cfDelegante,urlCodCit);
		payload.setPush(push);
		
		MexPayload mex = buildMexPayload(eventoBody, descrizioneEvento,soggettoRicevente,cfDelegante,urlCodCit);
		payload.setMex(mex);
		return payload;
		

	}

	private MexPayload buildMexPayload(DmaccREvnotDestMsg eventoBody, String descrizioneEvento, Soggetto soggettoRicevente, String cfDelegante,String urlCodCit) {
		MexPayload mex = new MexPayload();
		mex.setTitle(descrizioneEvento);
		String body = buildBody(eventoBody.getMsg_mex(),soggettoRicevente,cfDelegante,urlCodCit);
		mex.setBody(body);

		return mex;
	}
//	sostituire nei vari body
//	{0}SOGGETTO_NOME soggettoRicevente
//	{1}SOGGETTO_COGNOME soggettoRicevente
//	{5}URL_COD_CIT DA COD_C_PARAMETRI soggettoRicevente
//	if cfdelegante
	//{2} codice fiscale dell assistito che partecipa alla conversazione (Delegante) cfDelegato
	private String buildBody(String msg, Soggetto soggettoRicevente, String cfDelegante,String urlCodCit) {
		String result= msg;
		result=result.replace("{0}", soggettoRicevente.getSoggettoNome());
		result=result.replace("{1}", soggettoRicevente.getSoggettoCognome());
		if(StringUtils.isNotBlank(urlCodCit)) {
			result=result.replace("{5}", urlCodCit);
		}
		if(StringUtils.isNotBlank(cfDelegante)) {
			result=result.replace("{2}", cfDelegante);
		}
		return result;
	}

	private PushPayload buildPushPayload(DmaccREvnotDestMsg eventoBody, String descrizioneEvento, Soggetto soggettoRicevente, String cfDelegante,String urlCodCit) {
		PushPayload push = new PushPayload();
		push.setTitle(descrizioneEvento);
		String body = buildBody(eventoBody.getMsg_push(),soggettoRicevente,cfDelegante,urlCodCit);
		push.setBody(body);
		return push;
	}

	private EmailPayload buildEmailPayload( DmaccREvnotDestMsg eventoBody,
			String descrizioneEvento, Soggetto soggettoRicevente, String cfDelegante,String urlCodCit) {
		EmailPayload email = new EmailPayload();
		email.setSubject(descrizioneEvento);
		String body = buildBody(eventoBody.getMsg_mail(),soggettoRicevente,cfDelegante,urlCodCit);
		email.setBody(body);
		email.setTemplateId(templateId);
		return email;
	}

	private String sendNotificaEvento(NotificaCustom notifica, String cfSoggetto, String tokenApplicativo, String xRequestId)
			throws Exception {
		logInfo("sendNotificaEvento ", 
				"params: \n" + ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode() + ": "+ cfSoggetto + ", \n" 
						+ ApiHeaderParamEnum.X_AUTHENTICATION.getCode() + ": " + tokenApplicativo+"\n"
						+ ApiHeaderParamEnum.X_REQUEST_ID.getCode()+": "+xRequestId+"\n"
						+ " payload ID: "+notifica.getPayload().getId()+"\n");
		
		String result = null;
		String jsonPayloadString = fromObjectToJsonString(notifica);
		logInfo("sendNotificaEvento-","payload"+jsonPayloadString);
		HttpRequest request = HttpRequest.newBuilder().POST(BodyPublishers.ofString(jsonPayloadString))
				.uri(URI.create(urlNotificatore))
				.setHeader(ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode(), cfSoggetto)
				.setHeader(ApiHeaderParamEnum.X_AUTHENTICATION.getCode(), tokenApplicativo)
				.setHeader(ApiHeaderParamEnum.X_REQUEST_ID.getCode(), xRequestId)
				.setHeader("Content-Type", "application/json").build();

		HttpResponse<String> response=null;
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			// print status code
			logInfo("sendNotificaEvento ", "status:"+ response.statusCode() +" - notifica_uuid: "+ notifica.getUuid());
			logInfo("sendNotificaEvento","response: "+response.toString());
			logInfo("sendNotificaEvento","responsebody: "+response.body());
		
			if(!(response.statusCode()==200 || response.statusCode()==201)) {
				logError("sendNotificaEvento: ",  response.body() +" - notifica_uuid:"+notifica.getUuid());
			}
			result=response.body();
		return result;
	}

	private String fromObjectToJsonString(NotificaCustom notificaCustom) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(notificaCustom);
	}
}

