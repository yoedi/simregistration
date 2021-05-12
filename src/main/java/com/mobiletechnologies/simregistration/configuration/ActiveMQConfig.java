package com.yoedi.springjmsproducer.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class ActiveMQConfig {

    @Value("${activemq.broker.url}")
    private String brokerUrl;
    @Value("${activemq.broker.username}")
    private String username;
    @Value("${activemq.broker.password}")
    private String password;

    @Bean
    public ActiveMQConnectionFactory connectionFactory(){

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setPassword(username);
        connectionFactory.setUserName(password);

        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(){

        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        // Enable this for topic
//        template.setPubSubDomain(true);

        return template;
    }

}
