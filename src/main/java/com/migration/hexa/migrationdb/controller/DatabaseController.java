package com.migration.hexa.migrationdb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migration.hexa.migrationdb.repository.DatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/migration")
public class DatabaseController {
    private static final Logger log = LoggerFactory.getLogger(DatabaseController.class);

    @Autowired
    DatabaseRepository databaseRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @GetMapping(path = "/TwoTableManyToOneMigration")
    public ResponseEntity<String> twoTableManyToOneMigration(){

        Map<String, List<Map<String, Object>>> result = databaseRepository.getTwoTableRelationshipData();

        try{
            ObjectMapper objectMapper = new ObjectMapper();

            String contentJson = objectMapper.writeValueAsString(result);

            jmsTemplate.convertAndSend("mongo-migrator_queue", result);
            return new ResponseEntity<>(contentJson, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/TwoTableOneToManyMigration")
    public ResponseEntity<String> twoTableOneToManyMigration(){

        Map<String, List<Map<String, Object>>> result = databaseRepository.getTwoTableRelationshipData();
        try{
            ObjectMapper objectMapper = new ObjectMapper();

            String contentJson = objectMapper.writeValueAsString(result);

            jmsTemplate.convertAndSend("mongo-migrator_queue2", result);
            return new ResponseEntity<>(contentJson, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/NoRelationshipTablesMigration")
    public ResponseEntity<String> noRelationshipTablesMigration(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();

            String contentJson = objectMapper.writeValueAsString(databaseRepository.getNoRelationshipTableData());

            jmsTemplate.convertAndSend("mongo-migrator_queue3", databaseRepository.getNoRelationshipTableData());
            return new ResponseEntity<>(contentJson, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/ManyToManyMigration/V1")
    public ResponseEntity<String> ManyToManyMigrationV1(){

        Map<String, List<Map<String, Object>>> result = databaseRepository.getManyToManyData();

        try{
            ObjectMapper objectMapper = new ObjectMapper();

            String contentJson = objectMapper.writeValueAsString(result);

            jmsTemplate.convertAndSend("mongo-migrator_queue4", result);
            return new ResponseEntity<>(contentJson, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/ManyToManyMigration/V2")
    public ResponseEntity<String> ManyToManyMigrationV2(){

        Map<String, List<Map<String, Object>>> result = databaseRepository.getManyToManyData();

        try{
            ObjectMapper objectMapper = new ObjectMapper();

            String contentJson = objectMapper.writeValueAsString(result);

            jmsTemplate.convertAndSend("mongo-migrator_queue5", result);
            return new ResponseEntity<>(contentJson, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
