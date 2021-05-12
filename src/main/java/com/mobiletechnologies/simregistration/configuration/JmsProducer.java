package com.mobiletechnologies.simregistration.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsProducer {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMessage(final String queueName, final Object message) {
        try {
            jmsTemplate.convertAndSend(queueName, message);
        } catch (Exception e) {
            logger.error("Send message failed: ", e);
        }
    }
}
