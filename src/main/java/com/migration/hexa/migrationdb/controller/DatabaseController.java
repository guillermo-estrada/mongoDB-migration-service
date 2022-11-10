package com.migration.hexa.migrationdb.controller;

import com.migration.hexa.migrationdb.repository.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/migration")
public class DatabaseController {

    @Autowired
    DatabaseRepository databaseRepository;

    @GetMapping(path = "/getData")
    public Map<String, List<Map<String, Object>>> getData(){
        return databaseRepository.getData();
    }
}
