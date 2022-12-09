package com.migration.hexa.migrationdb.consumer;

import com.migration.hexa.migrationdb.repository.ConsumerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("unchecked")
public class ConsumerComponent {

    private static final Logger log = LoggerFactory.getLogger(ConsumerComponent.class);

    @Autowired
    ConsumerRepository consumerRepository;

    @JmsListener(destination = "mongo-migrator_queue")
    public void manyToOneListener(Map<String, List<Map<String, Object>>> content) {

        log.info("Message Received: {}", content);

        consumerRepository.insertData(consumerRepository.manyToOneTransformation(content));
    }

    @JmsListener(destination = "mongo-migrator_queue2")
    public void oneToManyListener(Map<String, List<Map<String, Object>>> content) {

        log.info("Message Received: {}", content);

        consumerRepository.insertSingleTableData(content);
    }


}
