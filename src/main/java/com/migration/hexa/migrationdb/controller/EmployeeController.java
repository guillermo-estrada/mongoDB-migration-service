package com.migration.hexa.migrationdb.controller;

import com.migration.hexa.migrationdb.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping(path = "/getAllEmployees")
    public List<Map<String, Object>> getAllEmployees(){
        return employeeRepository.getAllEmployees();
    }

    @GetMapping(path = "/getEmployeeMetadata")
    public List<Map<String, Object>> getEmployeeMetada(){
        return employeeRepository.getMetadata();
    }

    @GetMapping(path = "/getEmployee")
    public Map<String, List<Map<String, Object>>> getEmployee(){
        return employeeRepository.getEmployee();
    }
}
