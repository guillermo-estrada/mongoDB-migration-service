package com.migration.hexa.migrationdb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<String> getAllEmployees(){
        List<String> employeeList = new ArrayList<>();

        employeeList.addAll(jdbcTemplate.queryForList("SELECT FirstName FROM employee", String.class));
        return  employeeList;
    }
}
