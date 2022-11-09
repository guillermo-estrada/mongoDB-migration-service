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

    public Map<String, List<Map<String, Object>>> getInformation(){
        Map<String, List<Map<String, Object>>> tableInformation = new HashMap<>();
        tableInformation.put("table1Content", jdbcTemplate.queryForList("SELECT * FROM employee"));
        tableInformation.put("table1Metadata", jdbcTemplate.queryForList("select column_name from INFORMATION_SCHEMA.COLUMNS where table_name = 'employee'"));

        tableInformation.put("table2Content", jdbcTemplate.queryForList("SELECT * FROM department"));
        tableInformation.put("table2Metadata", jdbcTemplate.queryForList("select column_name from INFORMATION_SCHEMA.COLUMNS where table_name = 'department'"));

        return tableInformation;
    }
}
