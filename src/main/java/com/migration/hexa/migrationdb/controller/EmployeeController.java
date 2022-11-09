package com.migration.hexa.migrationdb.controller;

import com.migration.hexa.migrationdb.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping(path = "/getAllEmployees")
    public List<String> getAllEmployees(){
        return employeeRepository.getAllEmployees();
    }
}
