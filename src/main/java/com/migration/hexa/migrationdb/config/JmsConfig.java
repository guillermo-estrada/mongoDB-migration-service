package com.migration.hexa.migrationdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JmsConfig {

    @Bean
    public DefaultJmsListenerContainerFactory jmsListener(ConnectionFactory connectionFactory){

        DefaultJmsListenerContainerFactory jmsListener = new DefaultJmsListenerContainerFactory();

        jmsListener.setConnectionFactory(connectionFactory);
        jmsListener.setConcurrency("5-10");

        return  jmsListener;
    }
}
