package com.migration.hexa.migrationdb.consumer;

import com.migration.hexa.migrationdb.model.SystemMessage;
import com.migration.hexa.migrationdb.repository.DatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerComponent {

    private static final Logger log = LoggerFactory.getLogger(DatabaseRepository.class);

    @JmsListener(destination = "mongo-migrator_queue")
    public void messageListener(SystemMessage systemMessage){
        log.info("Message Received: {}", systemMessage);
    }
}
