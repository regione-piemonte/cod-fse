/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.cxf.binding.soap.interceptor.SoapInterceptor;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.csi.deleghe.deleghebe.ws.DelegheCittadiniService;
import it.csi.dma.apicodopsan.stub.aura.auraws.services.central.contattodigitale.ContattoDigitaleSoap;
import it.csi.dma.apicodopsan.stub.aura.auraws.services.central.contattodigitale.ContattoDigitale_Service;
import it.csi.dma.apicodopsan.util.log.LogEventSenderInCustom;
import it.csi.dma.apicodopsan.util.log.LogEventSenderOutCustom;
import it.csi.dma.apicodopsan.verificaservices.dmacc.VerificaService;
import it.csi.dma.apicodopsan.verificaservices.dmacc.VerificaService_Service;

@Configuration
public class ClientSoapConfiguration {

	//Deleghe
	@Value("${delegheUserBe}")
	private String userDelegheBe;
	@Value("${deleghePassBe}")
	private String passDeleghebe;
	@Value("${delegheServiceeUrl}")
	private String delegheServiceeUrl;

	@Value("${verificaServiceUrl}")
	private String verificaServiceUrl;
	@Value("${verificaServiceUser}")
	private String verificaServiceUser;
	@Value("${verificaServicePassword}")
	private String verificaServicePassword;
	
	@Value("${aura.contattodigitale.user}")
	private String contattoDigitaleSoapUserBe;
	@Value("${aura.contattodigitale.pwd}")
	private String contattoDigitaleSoapPassBe; 
	@Value("${aura.contattodigitale.url}")
	private String contattoDigitaleSoapUrl;
	
	@Bean(name="contattoDigitaleService")
	public ContattoDigitaleSoap generateProxyContattoDigitale() {
		return proxyFactoryBeanContattoDigitale().create(ContattoDigitaleSoap.class);
	}
	
	@Bean(name="verificaService")
	public VerificaService generateProxyVerificaService() {
		return proxyFactoryBeanVerificaService().create(VerificaService.class);
	}

	@Bean(name="delegheCittadiniService")
	public DelegheCittadiniService generateProxyDeleghe() {
		return proxyFactoryBeanDeleghe().create(DelegheCittadiniService.class);
	}
	
	@Bean(name="proxyFactoryBeanVerificaService")
	public JaxWsProxyFactoryBean proxyFactoryBeanVerificaService() {
		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setServiceClass(VerificaService_Service.class);
		proxyFactory.setAddress(verificaServiceUrl);
		//proxyFactory.setUsername(aurafindgetUserBe);
		//proxyFactory.setPassword(aurafindgetPassBe);

		proxyFactory.getOutInterceptors().add(loggingOutInterceptor());
		proxyFactory.getOutInterceptors().add(createAuraVerificaServiceSecurityInterceptor());
		proxyFactory.getInInterceptors().add(loggingInInterceptor());

		return proxyFactory;
	}

	@Bean
	public SoapInterceptor createAuraVerificaServiceSecurityInterceptor() {
		  Map<String, Object> outProps = new HashMap<String, Object>();
		  outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		    outProps.put(WSHandlerConstants.USER, this.verificaServiceUser);
		    outProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");
		    outProps.put(WSHandlerConstants.ADD_USERNAMETOKEN_NONCE, "true");
		    outProps.put(WSHandlerConstants.ADD_USERNAMETOKEN_CREATED, "true");
		    outProps.put(WSHandlerConstants.MUST_UNDERSTAND, "false");
		    outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler() {

		    	@Override
		        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		            for(Callback callBack:callbacks){
		                if(callBack instanceof WSPasswordCallback){
		                    ((WSPasswordCallback)callBack).setPassword(verificaServicePassword);
		                }
		            }
		        }
            });
		    return new WSS4JOutInterceptor(outProps);
	}
	
	@Bean(name="proxyFactoryBeanDeleghe")
	public JaxWsProxyFactoryBean proxyFactoryBeanDeleghe() {
		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setServiceClass(DelegheCittadiniService.class);
		proxyFactory.setAddress(delegheServiceeUrl);
		proxyFactory.setUsername(userDelegheBe);
		proxyFactory.setPassword(passDeleghebe);

		proxyFactory.getOutInterceptors().add(loggingOutInterceptor());
		proxyFactory.getInInterceptors().add(loggingInInterceptor());

		return proxyFactory;
	}
	
	@Bean(name="logIn")
	public LoggingInInterceptor loggingInInterceptor() {
		return new LoggingInInterceptor(new LogEventSenderInCustom());
	}

	@Bean(name="logOut")
	public LoggingOutInterceptor loggingOutInterceptor() {
		return new LoggingOutInterceptor(new LogEventSenderOutCustom());
	}
//WSS4JOutInterceptor

//	@Bean
//	public WSS4JOutInterceptor createAuraFindGetSecurityInterceptor_old() {
//	    Map<String, Object> outProps = new HashMap<>();
//	    outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
//	    outProps.put(WSHandlerConstants.USER, "aurafindgetUserBe");
//	    outProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");
//	    outProps.put("passwordCallbackClass","PasswordCallback");
//	    WSS4JOutInterceptor outInterceptor = new WSS4JOutInterceptor(outProps);
//	    outInterceptor.setPassword(outInterceptor, "aurafindgetPassBe");
//	    return outInterceptor;
//	}

	
	@Bean(name="proxyFactoryBeanContattoDigitale")
	public JaxWsProxyFactoryBean proxyFactoryBeanContattoDigitale() {
		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setServiceClass(ContattoDigitale_Service.class);
		proxyFactory.setAddress(contattoDigitaleSoapUrl);
		//proxyFactory.setUsername(aurafindgetUserBe);
		//proxyFactory.setPassword(aurafindgetPassBe);

		proxyFactory.getOutInterceptors().add(loggingOutInterceptor());
		proxyFactory.getOutInterceptors().add(createAuraContattoDigitaleSecurityInterceptor());
		proxyFactory.getInInterceptors().add(loggingInInterceptor());

		return proxyFactory;
	}

	@Bean
	public SoapInterceptor createAuraContattoDigitaleSecurityInterceptor() {
		  Map<String, Object> outProps = new HashMap<String, Object>();
		  outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		    outProps.put(WSHandlerConstants.USER, this.contattoDigitaleSoapUserBe);
		    outProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");
		    outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler() {

		    	@Override
		        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		            for(Callback callBack:callbacks){
		                if(callBack instanceof WSPasswordCallback){
		                    ((WSPasswordCallback)callBack).setPassword(contattoDigitaleSoapPassBe);
		                }
		            }
		        }
            });
		    return new WSS4JOutInterceptor(outProps);
	}



}
