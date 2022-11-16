package com.migration.hexa.migrationdb.consumer;

import com.migration.hexa.migrationdb.repository.DatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ConsumerComponent {

    private static final Logger log = LoggerFactory.getLogger(DatabaseRepository.class);

    @JmsListener(destination = "mongo-migrator_queue")
    public void messageListener(Map<String, List<Map<String, Object>>> content){
        log.info("Message Received: {}", content);

        List<Map<String, Object>> table1 = content.get("table1Content");
        List<Map<String, Object>> table2 = content.get("table2Content");
        List<Map<String, Object>> keys1 = content.get("table1Keys");
        List<Map<String, Object>> keys2 = content.get("table2Keys");

        log.info("Table 1 info {}", table1);
        log.info("Table 2 info {}", table2);
        log.info("Table 1 keys {}", keys1.get(0).get("Column_name"));
        log.info("Table 2 keys {}", keys2.get(0).get("Column_name"));
        log.info("Table 2 keys {}", keys2.get(1).get("Column_name"));
    }
}
