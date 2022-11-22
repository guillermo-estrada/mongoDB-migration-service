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

@RestController
@RequestMapping(path = "/migration")
public class DatabaseController {
    private static final Logger log = LoggerFactory.getLogger(DatabaseController.class);

    @Autowired
    DatabaseRepository databaseRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @GetMapping(path = "/getData")
    public ResponseEntity<String> getData(){

        try{
            ObjectMapper objectMapper = new ObjectMapper();

            String contentJson = objectMapper.writeValueAsString(databaseRepository.getData());

            jmsTemplate.convertAndSend("mongo-migrator_queue", databaseRepository.getData());
            return new ResponseEntity<>(contentJson, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
