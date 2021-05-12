package com.mobiletechnologies.simregistration.util;

import org.jboss.logging.Logger;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component
@EnableJms
public class MessageConsumer {

    Logger log = Logger.getLogger(MessageConsumer.class);

    @JmsListener(destination = "MSGREGISTERED")
    public void getMessage(final Message jsonMessage) throws JMSException {
        if (jsonMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)jsonMessage;

            String line = textMessage.getText();
            String[] arryLine = line.split(",");

            log.info("Send message to " + arryLine[0] + " | Your number has been registered.");
        }
    }

}
