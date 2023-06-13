/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.integration;

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

import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.contattodigitale.ContattoDigitaleSoap;
import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.contattodigitale.ContattoDigitale_Service;
import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.elencoesenzioni.ElencoEsenzioniSoap;
import it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.elencoesenzioni.ElencoEsenzioni_Service;
import it.csi.dma.apiopsanaura.util.log.LogEventSenderInCustom;
import it.csi.dma.apiopsanaura.util.log.LogEventSenderOutCustom;

@Configuration
public class ClientSoapConfiguration {


	@Value("${aura.contattodigitale.user}")
	private String contattoDigitaleSoapUserBe;
	@Value("${aura.contattodigitale.pwd}")
	private String contattoDigitaleSoapPassBe; 
	@Value("${aura.contattodigitale.url}")
	private String contattoDigitaleSoapUrl;
	
	@Value("${aura.elencoesenzioni.user}")
	private String elencoEsenzioniSoapUserBe;
	@Value("${aura.elencoesenzioni.pwd}")
	private String elencoEsenzioniSoapPassBe;
	@Value("${aura.elencoesenzioni.url}")
	private String elencoEsenzioniSoapUrl;

	@Bean(name="contattoDigitaleService")
	public ContattoDigitaleSoap generateProxyContattoDigitale() {
		return proxyFactoryBeanContattoDigitale().create(ContattoDigitaleSoap.class);
	}
	
	@Bean(name="elencoEsenzioniService")
	public ElencoEsenzioniSoap generateProxyElencoEsenzioni() {
		return proxyFactoryBeanElencoEsenzioni().create(ElencoEsenzioniSoap.class);
	}
//	@Bean(name="proxyFactoryBeanAuraGet")
//	public JaxWsProxyFactoryBean proxyFactoryBeanAuraGet() {
//		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
//		proxyFactory.setServiceClass(ContattoDigitaleSoap.class);
//		proxyFactory.setAddress(contattoDigitaleSoapUrl);
//		proxyFactory.getOutInterceptors().add(loggingOutInterceptor());
//		proxyFactory.getOutInterceptors().add(createAuraContattoDigitaletSecurityInterceptor());
//		proxyFactory.getInInterceptors().add(loggingInInterceptor());
//
//		return proxyFactory;
//	}
	
	
	
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
	
	@Bean
	public SoapInterceptor createAuraElencoEsenzioniSecurityInterceptor() {
		  Map<String, Object> outProps = new HashMap<String, Object>();
		  outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		    outProps.put(WSHandlerConstants.USER, this.elencoEsenzioniSoapUserBe);
		    outProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");
		    outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler() {

		    	@Override
		        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		            for(Callback callBack:callbacks){
		                if(callBack instanceof WSPasswordCallback){
		                    ((WSPasswordCallback)callBack).setPassword(elencoEsenzioniSoapPassBe);
		                }
		            }
		        }
            });
		    return new WSS4JOutInterceptor(outProps);
	}

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

	@Bean(name="proxyFactoryBeanElencoEsenzioni")
	public JaxWsProxyFactoryBean proxyFactoryBeanElencoEsenzioni() {
		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setServiceClass(ElencoEsenzioni_Service.class);
		proxyFactory.setAddress(elencoEsenzioniSoapUrl);
		//proxyFactory.setUsername(aurafindgetUserBe);
		//proxyFactory.setPassword(aurafindgetPassBe);

		proxyFactory.getOutInterceptors().add(loggingOutInterceptor());
		proxyFactory.getOutInterceptors().add(createAuraElencoEsenzioniSecurityInterceptor());
		proxyFactory.getInInterceptors().add(loggingInInterceptor());

		return proxyFactory;
	}

}
