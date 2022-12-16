package com.migration.hexa.migrationdb.consumer;

import com.migration.hexa.migrationdb.config.DatabaseConfig;
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

    @Autowired
    DatabaseConfig databaseConfig;

    @JmsListener(destination = "mongo-migrator_queue")
    public void manyToOneListener(Map<String, List<Map<String, Object>>> content) {

        log.info("Message Received: {}", content);

        consumerRepository.insertData(consumerRepository.manyToOneTransformation(content), this.databaseConfig.getTable1());
    }

    @JmsListener(destination = "mongo-migrator_queue2")
    public void oneToManyListener(Map<String, List<Map<String, Object>>> content) {

        log.info("Message Received: {}", content);

        consumerRepository.insertData(consumerRepository.oneToManyTransformation(content), this.databaseConfig.getTable1());
    }

    @JmsListener(destination = "mongo-migrator_queue3")
    public void noRelationshipListener(Map<String, List<Map<String, Object>>> content) {

        log.info("Message Received: {}", content);

        consumerRepository.insertSingleTableData(content);
    }

    @JmsListener(destination = "mongo-migrator_queue4")
    public void manyToManyListenerV1(Map<String, List<Map<String, Object>>> content) {

        log.info("Message Received: {}", content);

        consumerRepository.insertData(consumerRepository.manyToManyTransformationV1(content), this.databaseConfig.getTable1());
    }

    @JmsListener(destination = "mongo-migrator_queue5")
    public void manyToManyListenerV2(Map<String, List<Map<String, Object>>> content) {

        log.info("Message Received: {}", content);

        consumerRepository.insertData(consumerRepository.manyToManyTransformationV2(content), this.databaseConfig.getTable2());
    }


}
