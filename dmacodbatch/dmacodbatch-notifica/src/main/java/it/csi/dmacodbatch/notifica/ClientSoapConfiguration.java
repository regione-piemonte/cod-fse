/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.notifica;

import javax.jms.ConnectionFactory;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import it.csi.deleghe.deleghebe.ws.DelegheCittadiniService;

@Configuration
public class ClientSoapConfiguration implements JmsListenerConfigurer{

	// Deleghe
	@Value("${delegheUserBe}")
	private String userDelegheBe;
	@Value("${deleghePassBe}")
	private String passDeleghebe;
	@Value("${delegheServiceeUrl}")
	private String delegheServiceeUrl;

	@Bean(name = "delegheCittadiniService")
	public DelegheCittadiniService generateProxyDeleghe() {
		return proxyFactoryBeanDeleghe().create(DelegheCittadiniService.class);
	}

	@Bean(name = "proxyFactoryBeanDeleghe")
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

	@Bean(name = "logIn")
	public LoggingInInterceptor loggingInInterceptor() {
		return new LoggingInInterceptor();
	}

	@Bean(name = "logOut")
	public LoggingOutInterceptor loggingOutInterceptor() {
		return new LoggingOutInterceptor();
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
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// This provides all boot's default to this factory, including the message
		// converter
		configurer.configure(factory, connectionFactory);
		// You could still override some of Boot's default if necessary.
		return factory;
	}

	@Bean
    public DefaultMessageHandlerMethodFactory handlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(handlerMethodFactory());
    }


}
