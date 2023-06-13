/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.cxf.binding.BindingConfiguration;
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

import it.csi.aura.auraws.services.central.anagrafefind.AnagrafeFind;
import it.csi.aura.auraws.services.central.anagrafefind.AnagrafeFindSoap;
import it.csi.aura.auraws.services.central.anagrafesanitaria.AnagrafeSanitaria;
import it.csi.aura.auraws.services.central.anagrafesanitaria.AnagrafeSanitariaSoap;
import it.csi.deleghe.deleghebe.ws.DelegheCittadiniService;
import it.csi.dma.codcit.util.log.LogEventSenderInCustom;
import it.csi.dma.codcit.util.log.LogEventSenderOutCustom;
import it.csi.statoconsensi.dmacc.CCConsensoINIExtServicePortType;

@Configuration
public class ClientSoapConfiguration {

	//Deleghe
	@Value("${delegheUserBe}")
	private String userDelegheBe;
	@Value("${deleghePassBe}")
	private String passDeleghebe;
	@Value("${delegheServiceeUrl}")
	private String delegheServiceeUrl;

	//Aura find e get
	@Value("${aurafindgetUserBe}")
	private String aurafindgetUserBe;
	@Value("${aurafindgetPassBe}")
	private String aurafindgetPassBe;
	@Value("${aurafindServiceUrl}")
	private String aurafindServiceUrl;
	@Value("${auragetServiceUrl}")
	private String auragetServiceUrl;
	
	//StatoConsensi
	@Value("${statoConsensitUserBe}")
	private String statoConsensitUserBe;
	@Value("${statoConsensiPassBe}")
	private String statoConsensiPassBe;
	@Value("${statoConsensiServiceUrl}")
	private String statoConsensiServiceUrl;	

	@Bean(name="delegheCittadiniService")
	public DelegheCittadiniService generateProxyDeleghe() {
		return proxyFactoryBeanDeleghe().create(DelegheCittadiniService.class);
	}

	@Bean(name="auraFindService")
	public AnagrafeFindSoap generateProxyAuraFind() {
		return proxyFactoryBeanAuraFind().create(AnagrafeFindSoap.class);
	}

	@Bean(name="auraGetService")
	public AnagrafeSanitariaSoap generateProxyAuraGet() {
		return proxyFactoryBeanAuraGet().create(AnagrafeSanitariaSoap.class);
	}
	
	
	@Bean(name="cCConsensoINIExtServicePortType")
	public CCConsensoINIExtServicePortType generateProxyStatoCOnsensi() {
		return proxyFactoryBeanStatoConsensi().create(CCConsensoINIExtServicePortType.class);
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
	
	
	@Bean(name="proxyFactoryBeanStatoConsensi")
	public JaxWsProxyFactoryBean proxyFactoryBeanStatoConsensi() {
		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setServiceClass(CCConsensoINIExtServicePortType.class);
		proxyFactory.setAddress(statoConsensiServiceUrl);
		//proxyFactory.setBindingId(org.apache.servicemix.cxf.binding.jbi.JBIConstants.NS_JBI_BINDING);
		BindingConfiguration config = new BindingConfiguration() {

			    @Override
			    public String getBindingId() {
			            // TODO Auto-generated method stub
			            return "http://www.w3.org/2003/05/soap/bindings/HTTP/";//SOAPVersion.SOAP_12.httpBindingId
			    }
			    };
			    
		proxyFactory.setBindingConfig(config);
		proxyFactory.getOutInterceptors().add(loggingOutInterceptor());
		proxyFactory.getOutInterceptors().add(createStatoConsensiSecurityInterceptor());
		proxyFactory.getInInterceptors().add(loggingInInterceptor());
		return proxyFactory;
	}
	
	@Bean(name="proxyFactoryBeanAuraFind")
	public JaxWsProxyFactoryBean proxyFactoryBeanAuraFind() {
		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setServiceClass(AnagrafeFind.class);
		proxyFactory.setAddress(aurafindServiceUrl);
		//proxyFactory.setUsername(aurafindgetUserBe);
		//proxyFactory.setPassword(aurafindgetPassBe);

		proxyFactory.getOutInterceptors().add(loggingOutInterceptor());
		proxyFactory.getOutInterceptors().add(createAuraFindGetSecurityInterceptor());
		proxyFactory.getInInterceptors().add(loggingInInterceptor());
		return proxyFactory;
	}

	@Bean(name="proxyFactoryBeanAuraGet")
	public JaxWsProxyFactoryBean proxyFactoryBeanAuraGet() {
		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setServiceClass(AnagrafeSanitaria.class);
		proxyFactory.setAddress(auragetServiceUrl);
		//proxyFactory.setUsername(aurafindgetUserBe);
		//proxyFactory.setPassword(aurafindgetPassBe);

		proxyFactory.getOutInterceptors().add(loggingOutInterceptor());
		proxyFactory.getOutInterceptors().add(createAuraFindGetSecurityInterceptor());
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

	
	
	@Bean
	public SoapInterceptor createStatoConsensiSecurityInterceptor() {
		  Map<String, Object> outProps = new HashMap<String, Object>();
		  outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		    outProps.put(WSHandlerConstants.USER, statoConsensitUserBe);
		    outProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordDigest");
		    outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler() {

		    	@Override
		        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		            for(Callback callBack:callbacks){
		                if(callBack instanceof WSPasswordCallback){
		                    ((WSPasswordCallback)callBack).setPassword(statoConsensiPassBe);
		                }
		            }
		        }
            });


		    return new WSS4JOutInterceptor(outProps);
	}	
	
	@Bean
	public SoapInterceptor createAuraFindGetSecurityInterceptor() {
		  Map<String, Object> outProps = new HashMap<String, Object>();
		  outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		    outProps.put(WSHandlerConstants.USER, aurafindgetUserBe);
		    outProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");
		    outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler() {

		    	@Override
		        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		            for(Callback callBack:callbacks){
		                if(callBack instanceof WSPasswordCallback){
		                    ((WSPasswordCallback)callBack).setPassword(aurafindgetPassBe);
		                }
		            }
		        }
            });


		    return new WSS4JOutInterceptor(outProps);
	}




}
