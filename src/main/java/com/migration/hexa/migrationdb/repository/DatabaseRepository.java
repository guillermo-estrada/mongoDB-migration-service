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

    private static final Logger log = LoggerFactory.getLogger(DatabaseRepository.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DatabaseConfig databaseConfig;

    public Map<String, List<Map<String, Object>>> getTwoTableRelationshipData() {

        Map<String, List<Map<String, Object>>> tableInformation = new HashMap<>();
        tableInformation.put("table1Content", jdbcTemplate.queryForList("SELECT * FROM " + this.databaseConfig.getTable1()));
        tableInformation.put("table1Keys", jdbcTemplate.queryForList("SHOW KEYS FROM " + this.databaseConfig.getTable1() + " WHERE Key_name = 'PRIMARY';"));
        //tableInformation.put("table1Metadata", jdbcTemplate.queryForList("select column_name from INFORMATION_SCHEMA.COLUMNS where table_name = '" + this.databaseConfig.getTable1() + "'"));

        tableInformation.put("table2Content", jdbcTemplate.queryForList("SELECT * FROM " + this.databaseConfig.getTable2()));
        tableInformation.put("table2Keys", jdbcTemplate.queryForList("SHOW KEYS FROM " + this.databaseConfig.getTable2() + " WHERE Key_name = 'PRIMARY';"));
        //tableInformation.put("table2Metadata", jdbcTemplate.queryForList("select column_name from INFORMATION_SCHEMA.COLUMNS where table_name = '" + this.databaseConfig.getTable2() + "'"));

        log.info("Information Retrieved Successfully");

        return tableInformation;

    }

    public Map<String, List<Map<String, Object>>> getNoRelationshipTableData() {
        Map<String, List<Map<String, Object>>> databaseTables = new HashMap<>();
        List<Map<String, Object>> tablesData = jdbcTemplate.queryForList("SHOW TABLES FROM " + this.databaseConfig.getDatabase());

        for (Map<String, Object> tableRow : tablesData) {
            String actualTable = (String) tableRow.get("Tables_in_" + this.databaseConfig.getDatabase());
            databaseTables.put(actualTable, jdbcTemplate.queryForList("SELECT * FROM " + actualTable));
        }

        return databaseTables;
    }
}
