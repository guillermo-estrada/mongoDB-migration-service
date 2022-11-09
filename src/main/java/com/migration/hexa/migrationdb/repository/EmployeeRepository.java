package com.migration.hexa.migrationdb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<Map<String, Object>> getAllEmployees(){
        return jdbcTemplate.queryForList("SELECT * FROM employee");
    }

    public List<Map<String, Object>> getMetadata(){
        return jdbcTemplate.queryForList("select column_name from INFORMATION_SCHEMA.COLUMNS where table_name = 'employee'");
    }

    public Map<String, List<Map<String, Object>>> getEmployee(){
        Map<String, List<Map<String, Object>>> tableInformation = new HashMap<>();
        tableInformation.put("EmployeeInformation", jdbcTemplate.queryForList("SELECT * FROM employee"));
        tableInformation.put("TableInformation", jdbcTemplate.queryForList("select column_name from INFORMATION_SCHEMA.COLUMNS where table_name = 'employee'"));

        return tableInformation;
    }
}
