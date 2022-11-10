package com.migration.hexa.migrationdb.repository;

import com.migration.hexa.migrationdb.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DatabaseRepository {

    Logger log = LoggerFactory.getLogger(DatabaseRepository.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DatabaseConfig databaseConfig;

    public Map<String, List<Map<String, Object>>> getInformation(){
        Map<String, List<Map<String, Object>>> tableInformation = new HashMap<>();
        tableInformation.put("table1Content", jdbcTemplate.queryForList("SELECT * FROM " + this.databaseConfig.getTable1()));
        tableInformation.put("table1Metadata", jdbcTemplate.queryForList("select column_name from INFORMATION_SCHEMA.COLUMNS where table_name = '" + this.databaseConfig.getTable1() + "'"));

        tableInformation.put("table2Content", jdbcTemplate.queryForList("SELECT * FROM " + this.databaseConfig.getTable2()));
        tableInformation.put("table2Metadata", jdbcTemplate.queryForList("select column_name from INFORMATION_SCHEMA.COLUMNS where table_name = '" + this.databaseConfig.getTable2() + "'"));

        log.info("Information Retrieved Successfully");
        return tableInformation;
    }
}