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

        tableInformation.put("table2Content", jdbcTemplate.queryForList("SELECT * FROM " + this.databaseConfig.getTable2()));
        tableInformation.put("table2Keys", jdbcTemplate.queryForList("SHOW KEYS FROM " + this.databaseConfig.getTable2() + " WHERE Key_name = 'PRIMARY';"));

        log.info("Information Retrieved Successfully");

        return tableInformation;

    }

    public List<Map<String, Object>> getSingleTableData() {
        String table = this.databaseConfig.getTable1();

        List<Map<String, Object>> tableContent = jdbcTemplate.queryForList("SELECT * FROM " + table);

        log.info("Information Retrieved Successfully");

        return tableContent;
    }

    public Map<String, List<Map<String, Object>>> getAllSingleTableData() {
        Map<String, List<Map<String, Object>>> databaseTables = new HashMap<>();
        List<Map<String, Object>> tablesData = jdbcTemplate.queryForList("SHOW TABLES FROM " + this.databaseConfig.getDatabase());

        for (Map<String, Object> tableRow : tablesData) {
            String actualTable = (String) tableRow.get("Tables_in_" + this.databaseConfig.getDatabase());
            databaseTables.put(actualTable, jdbcTemplate.queryForList("SELECT * FROM " + actualTable));
        }
        log.info("Information Retrieved Successfully");

        return databaseTables;
    }

    public Map<String, List<Map<String, Object>>> getTwoTableRelationshipOneTableData() {

        Map<String, List<Map<String, Object>>> tableInformation = new HashMap<>();
        tableInformation.put("table1Content", jdbcTemplate.queryForList("SELECT * FROM " + this.databaseConfig.getTable1()));
        tableInformation.put("table1Keys", jdbcTemplate.queryForList("SHOW KEYS FROM " + this.databaseConfig.getTable1() + " WHERE Key_name = 'PRIMARY';"));

        tableInformation.put("table2Content", jdbcTemplate.queryForList("SELECT * FROM " + this.databaseConfig.getTable2()));
        tableInformation.put("table2Keys", jdbcTemplate.queryForList("SHOW KEYS FROM " + this.databaseConfig.getTable2() + " WHERE Key_name = 'PRIMARY';"));

        tableInformation.put("table3Content", jdbcTemplate.queryForList("SELECT * FROM " + this.databaseConfig.getTable3()));
        tableInformation.put("table3Keys", jdbcTemplate.queryForList("SHOW KEYS FROM " + this.databaseConfig.getTable3() + " WHERE Key_name = 'PRIMARY';"));
        log.info("Information Retrieved Successfully");

        return tableInformation;
    }

    public Map<String, List<Map<String, Object>>> getManyToManyData() {

        Map<String, List<Map<String, Object>>> tableInformation = new HashMap<>();
        tableInformation.put("table1Content", jdbcTemplate.queryForList("SELECT * FROM " + this.databaseConfig.getTable1()));
        tableInformation.put("table1Keys", jdbcTemplate.queryForList("SHOW KEYS FROM " + this.databaseConfig.getTable1() + " WHERE Key_name = 'PRIMARY';"));

        tableInformation.put("table2Content", jdbcTemplate.queryForList("SELECT * FROM " + this.databaseConfig.getTable2()));
        tableInformation.put("table2Keys", jdbcTemplate.queryForList("SHOW KEYS FROM " + this.databaseConfig.getTable2() + " WHERE Key_name = 'PRIMARY';"));

        tableInformation.put("table3Content", jdbcTemplate.queryForList("SELECT * FROM " + this.databaseConfig.getTable3()));
        tableInformation.put("table3Keys", jdbcTemplate.queryForList("SHOW KEYS FROM " + this.databaseConfig.getTable3() + " WHERE Key_name = 'PRIMARY';"));

        log.info("Information Retrieved Successfully");
        return tableInformation;
    }

}
