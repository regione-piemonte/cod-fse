/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.util;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonMessageConverter implements MessageConverter {

	@Bean
	@Primary
	public ObjectMapper objectMapper() {
	    return new ObjectMapper()
	      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//	      .registerModule(module)
	      ;
	}

    /**
     * Converts message to JSON. Used mostly by {@link org.springframework.jms.core.JmsTemplate}
     */
    @Override
    public javax.jms.Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        String json;

        try {
            json = objectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new MessageConversionException("Message cannot be parsed. ", e);
        }

        TextMessage message = session.createTextMessage();
        message.setText(json);

        return message;
    }

    /**
     * Extracts JSON payload for further processing by JacksonMapper.
     */
    @Override
    public Object fromMessage(javax.jms.Message message) throws JMSException, MessageConversionException {
        return ((TextMessage) message).getText();
    }
}
