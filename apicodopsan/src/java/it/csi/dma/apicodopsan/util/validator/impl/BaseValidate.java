/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.util.validator.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.dto.custom.Ruolo;
import it.csi.dma.apicodopsan.dto.custom.TAdesione;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.integration.dao.custom.CodDErroreDao;
import it.csi.dma.apicodopsan.integration.dao.custom.CodTAdesioneDao;
import it.csi.dma.apicodopsan.integration.dao.custom.DmaccTDecodificaRuoliPuaDao;
import it.csi.dma.apicodopsan.util.Constants;
import it.csi.dma.apicodopsan.util.CreateTemplateMessage;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.ErrorParamEnum;
import it.csi.dma.apicodopsan.verificaservices.dma.RichiedenteInfo;
import it.csi.dma.apicodopsan.verificaservices.dmacc.ApplicativoVerticale;
import it.csi.dma.apicodopsan.verificaservices.dmacc.ApplicazioneRichiedente;
import it.csi.dma.apicodopsan.verificaservices.dmacc.Errore;
import it.csi.dma.apicodopsan.verificaservices.dmacc.RisultatoCodice;
import it.csi.dma.apicodopsan.verificaservices.dmacc.VerificaService;
import it.csi.dma.apicodopsan.verificaservices.dmacc.VerificaUtenteAbilitatoRequest;
import it.csi.dma.apicodopsan.verificaservices.dmacc.VerificaUtenteAbilitatoResponse;

public abstract class BaseValidate extends LoggerUtil {


	@Autowired
	CodDErroreDao codDErroreDao;

	@Autowired
	DmaccTDecodificaRuoliPuaDao dmaccTDecodificaRuoliPuaDao;

	@Autowired
	CodTAdesioneDao codTAdesioneDao;

//    private static final QName SERVICE_NAME = new QName("http://dmacc.csi.it/", "VerificaService");
//	@Value("${verificaServiceUrl}")
//	private String verificaServiceUrl;
//
//	@Value("${verificaServiceUser}")
//	private String verificaServiceUser;
//
//	@Value("${verificaServicePassword}")
//	private String verificaServicePassword;
    @Autowired
    private VerificaService verificaService;
    
/*
 * I metodi "check" restituiscono true se il controllo fallisce e bisogna dare segnalazione di  errore
 * restituisce false se il controllo ha avuto esito positivo
 */
	public ErroreDettaglio getValueGenericError(String key, String param) throws DatabaseException {
		var value = codDErroreDao.selectErroreDescFromErroreCod(key);

		if(param!=null) {
			Map<String, Object> parameter = generateParamList("0", param);
			value = CreateTemplateMessage.generateTextByTemplateAndMap(value, parameter);
		}

		var result1 = setError(key, value);
		return result1;
	}

	public ErroreDettaglio getValueFormalError(String key, String param,String fieldValue) throws DatabaseException {
		var value = codDErroreDao.selectErroreDescFromErroreCod(key);

		if(param!=null) {
			Map<String, Object> parameter = generateParamList("0", param);
			parameter.put("1", fieldValue);
			value = CreateTemplateMessage.generateTextByTemplateAndMap(value, parameter);
		}

		var result1 = setError(key, value);
		return result1;
	}

	private Map<String, Object> generateParamList(String paramKey, String paramValue) {
		Map<String, Object> parameter = new HashMap<>();
		parameter.put(paramKey, paramValue);
		return parameter;
	}

	private ErroreDettaglio setError(String key, String value) {
		var erroreDettaglio = new ErroreDettaglio();
		erroreDettaglio.setChiave(key);
		erroreDettaglio.setValore(value);
		return erroreDettaglio;
	}

	protected boolean formalCheckCF(String cf) {

		if(cf.length() != 11 && cf.length() != 16)
			return true;

		String regexCF 	 = "[a-zA-Z]{6}\\d\\d[a-zA-Z]\\d\\d[a-zA-Z]\\d\\d\\d[a-zA-Z]";
		String regexPIVA = "[0-9]{11}";

		if(cf.length() == 16 &&  !Pattern.matches(regexCF, cf)) {
			return true;
		}

		if(cf.length() == 11 &&  !Pattern.matches(regexPIVA, cf)) {
			return true;
		}

	    return false;
	}

	protected boolean formalCheckPresenzaRuolo(String ruolo, String xCodiceServizio) throws DatabaseException {

		Ruolo existingRole = dmaccTDecodificaRuoliPuaDao.selectRuolo(ruolo,xCodiceServizio);
		if(existingRole == null)
			return true ;
		else
			return false;

	}

	protected boolean functionalCheckAdesione(String shibIdentitaCodiceFiscale,boolean adesione) throws DatabaseException {

		TAdesione existingAdesione = codTAdesioneDao.selectAdesione(shibIdentitaCodiceFiscale);
		if(existingAdesione != null && adesione)
			return true ;

		if(existingAdesione == null && !adesione)
			return true;

		return false;
	}


	protected VerificaUtenteAbilitatoResponse functionalCheckUtenteAbilitato(VerificaUtenteAbilitatoRequest verificaUtenteAbilitato) throws DatabaseException {

//		URL wsdlURL;
		try {
//			wsdlURL = new URL(verificaServiceUrl);
//        VerificaService_Service ss = new VerificaService_Service(wsdlURL, SERVICE_NAME);
//        VerificaService port = ss.getVerificaServicePort();
//
//        org.apache.cxf.endpoint.Client client = ClientProxy.getClient(port);
//        org.apache.cxf.endpoint.Endpoint cxfEndpoint = client.getEndpoint();
//
//        client.getOutInterceptors().add(loggingOutInterceptor());
//		client.getOutInterceptors().add(createVerificaServicesSecurityInterceptor());
//		client.getInInterceptors().add(loggingInInterceptor());

        VerificaUtenteAbilitatoResponse verificaUtenteAbilitatoResp = verificaService.verificaUtenteAbilitato(verificaUtenteAbilitato);
		return verificaUtenteAbilitatoResp;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Errore chiamata servizio : Verifica Service - "+e.getMessage());
		}

	}


//	private LoggingInInterceptor loggingInInterceptor() {
//		return new LoggingInInterceptor();
//	}
//
//
//	private LoggingOutInterceptor loggingOutInterceptor() {
//		return new LoggingOutInterceptor();
//	}
//	private SoapInterceptor createVerificaServicesSecurityInterceptor() {
//		  Map<String, Object> outProps = new HashMap<String, Object>();
//		  outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
//		    outProps.put(WSHandlerConstants.USER, verificaServiceUser);
//		    outProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");
//		    outProps.put(WSHandlerConstants.ADD_USERNAMETOKEN_NONCE, "true");
//		    outProps.put(WSHandlerConstants.ADD_USERNAMETOKEN_CREATED, "true");
//		    outProps.put(WSHandlerConstants.MUST_UNDERSTAND, "false");
//		    outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler() {
//
//		    	@Override
//		        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
//		            for(Callback callBack:callbacks){
//		                if(callBack instanceof WSPasswordCallback){
//		                    ((WSPasswordCallback)callBack).setPassword(verificaServicePassword);
//		                }
//		            }
//		        }
//            });
//
//
//		    return new WSS4JOutInterceptor(outProps);
//	}


	protected boolean formalCheckCodiceVerticale(String xCodiceVerticale) {

		if(xCodiceVerticale.equals(Constants.CODICE_VERTICALE)) {
			return false;
		} else {
			return true;
		}

	}

	protected boolean formalCheckCodiceServizio(String xCodiceServizio)  {

		if(xCodiceServizio.equals(Constants.CODICE_SERVIZIO)) {
			return false;
		} else {
			return true;
		}

	}

	protected boolean formalCheckLimit(int limit) {
		return !(limit > 0);
	}

	protected boolean formalCheckOffset(int offset) {
		return !(offset >= 0);
	}

	protected boolean formalCheckStato(String stato) {
		return !(stato.equals("A") || stato.equals("B"));
	}

	protected boolean formalCheckSolaLettura(String solaLettura) {

		return !(solaLettura.equals("A") || solaLettura.equals("B") || solaLettura.equals("T"));
	}

	protected boolean formalCheckCognome(String cognome) {
		return cognome != null ? !(cognome.length() >= 2) : false;
	}

	protected boolean formalCheckNome(String nome) {
		return nome != null ? !(nome.length() >= 2) : false;
	}

	protected boolean formalCheckCFFilter(String codiceFiscale) {
		return codiceFiscale != null ? !(codiceFiscale.length() >= 2) : false;
	}

	protected boolean formalCheckSesso(String sesso) {
		return sesso != null ? !(sesso.equals("M") || sesso.equals("F")) : false;
	}

	protected boolean formalCheckEtaMin(Integer etaMin) {
		return etaMin != null ? !(etaMin >= 0) : false;
	}

	protected boolean formalCheckEtaMax(Integer etaMin, Integer etaMax) {
		return etaMin != null && etaMax != null ? !(etaMax > etaMin) : false;
	}
	// VERIFICA CHE NON SIANO NULLI I CAMPI COMUNI
	protected List<ErroreDettaglio> notNullCommonCheck(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo,
			String collocazioneCodice, String collocazioneDescrizione) throws DatabaseException {
		if (StringUtils.isBlank(xRequestId)) {
			result.add(
					getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), ErrorParamEnum.X_REQUEST_ID.getCode()));
		}

		if (StringUtils.isEmpty(shibIdentitaCodiceFiscale) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode()));
		}

		if (StringUtils.isBlank(xForwardedFor) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.X_FORWARDED_FOR.getCode()));
		}

		if (StringUtils.isBlank(xCodiceServizio)) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.X_CODICE_SERVIZIO.getCode()));
		}

		if (StringUtils.isBlank(collocazioneDescrizione) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.COLLOCAZIONE_DESCRIZIONE.getCode()));
		}

		if (StringUtils.isBlank(collocazioneCodice) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.COLLOCAZIONE_CODICE.getCode()));
		}

		if (StringUtils.isBlank(ruolo) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(), ErrorParamEnum.RUOLO.getCode()));
		}

		if (StringUtils.isBlank(xCodiceVerticale) ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.X_CODICE_VERTICALE.getCode()));
		}

		return result;
	}
	// NOT NULL CON ELEMENTI DI PAGINAZIONE
	protected List<ErroreDettaglio> notNullCommonCheckForList(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo,
			String collocazioneCodice, String collocazioneDescrizione,Integer limit, Integer offset) throws DatabaseException {
		notNullCommonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo, collocazioneCodice, collocazioneDescrizione);
		if (limit==null ) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.LIMIT.getCode()));
		}

		if (offset==null) {
			result.add(getValueGenericError(CodeErrorEnum.FSE_COD_050.getCode(),
					ErrorParamEnum.OFFSET.getCode()));
		}
		return result;
	}
	// VERIFICA LA CONFORMITA' DEI CAMPI COMUNI
	protected List<ErroreDettaglio> complianceCommonCheck(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo)
			throws DatabaseException {

		if (formalCheckCF(shibIdentitaCodiceFiscale)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(),
					ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode(), shibIdentitaCodiceFiscale));
		}

		if (formalCheckCodiceVerticale(xCodiceVerticale)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(),
					ErrorParamEnum.X_CODICE_VERTICALE.getCode(), xCodiceVerticale));
		}

		if (formalCheckCodiceServizio(xCodiceServizio)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(),
					ErrorParamEnum.X_CODICE_SERVIZIO.getCode(), xCodiceServizio));
		}

		if (formalCheckPresenzaRuolo(ruolo, xCodiceServizio)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_051.getCode(), ErrorParamEnum.RUOLO.getCode(), ruolo));
		}

		return result;

	}

	protected List<ErroreDettaglio> complianceCommonCheckForList(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xForwardedFor, String xCodiceServizio, String xCodiceVerticale, String ruolo,Integer limit, Integer offset)
			throws DatabaseException {
		complianceCommonCheck(result, shibIdentitaCodiceFiscale, xForwardedFor, xCodiceServizio, xCodiceVerticale, ruolo);
		if(formalCheckLimit(limit)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_062.getCode(),
					ErrorParamEnum.LIMIT.getCode(), limit.toString()));
		}
		if(formalCheckOffset(offset)) {
			result.add(getValueFormalError(CodeErrorEnum.FSE_COD_063.getCode(),
					ErrorParamEnum.OFFSET.getCode(), offset.toString()));
		}
		return result;
	}

	protected VerificaUtenteAbilitatoRequest createVerificaUtenteAbilitatoRequest(String xCodiceVerticale,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, String xForwardedFor, String xRequestId,
			String ruolo) {
		// â¢ numero di transazione= x-Request-Id ***********
		// â¢ CF richiedente= Shib-Identita-CodiceFiscale ***********
		// â¢ Codice ruolo=ruolo ??????
		// â¢ Codice Applicazione richiedente= x-Codice-Servizio ***********
		// â¢ Codice Regime di operativitÃ =AMB ????????
		// â¢ Applicativo Verticale.codice= x-Codice-Verticale ***********
		// â¢ Ip=primo ip di x-Forwarded-for ***********
		// â¢ cfUtente= Shib-Identita-CodiceFiscale ????????
		// â¢ ruoloUtente=ruolo ***********

		VerificaUtenteAbilitatoRequest verificaUtenteAbilitatoRequest = new VerificaUtenteAbilitatoRequest();
		RichiedenteInfo richiedenteInfo = new RichiedenteInfo();
		ApplicativoVerticale applicativoVerticale = new ApplicativoVerticale();
		ApplicazioneRichiedente applicazioneRichiedente = new ApplicazioneRichiedente();
		it.csi.dma.apicodopsan.verificaservices.dma.Ruolo ruoloWs = new it.csi.dma.apicodopsan.verificaservices.dma.Ruolo();

		applicativoVerticale.setCodice(xCodiceVerticale);
		richiedenteInfo.setApplicativoVerticale(applicativoVerticale);

		applicazioneRichiedente.setCodice(xCodiceServizio);
		richiedenteInfo.setApplicazione(applicazioneRichiedente);

		ruoloWs.setCodice(ruolo);
		richiedenteInfo.setRuolo(ruoloWs);

		richiedenteInfo.setCodiceFiscale(shibIdentitaCodiceFiscale);
		richiedenteInfo.setIp(xForwardedFor);
		richiedenteInfo.setNumeroTransazione(xRequestId);
		verificaUtenteAbilitatoRequest.setRichiedente(richiedenteInfo);

		return verificaUtenteAbilitatoRequest;
	}

	protected List<ErroreDettaglio> checkVerificaUtenteAbilitatoResponse(List<ErroreDettaglio> result, VerificaUtenteAbilitatoResponse verificaUtenteAbilitatoResp ){
	    if(verificaUtenteAbilitatoResp.getEsito().equals(RisultatoCodice.FALLIMENTO)) {
	    	List<Errore> wsErrors  =verificaUtenteAbilitatoResp.getErrori();

	    	for(Errore wsError : wsErrors) {
	    		ErroreDettaglio erroreDettaglio = new ErroreDettaglio();
	    		erroreDettaglio.setChiave(wsError.getCodice());
	    		erroreDettaglio.setValore(wsError.getDescrizione());
	    		result.add(erroreDettaglio);
	    	}
	    }
	    return result;
	}

}
