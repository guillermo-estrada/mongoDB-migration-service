package com.migration.hexa.migrationdb.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migration.hexa.migrationdb.config.DatabaseConfig;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ConsumerRepository {

    private static final Logger log = LoggerFactory.getLogger(ConsumerRepository.class);

    @Autowired
    DatabaseConfig databaseConfig;

    @Autowired
    MongoTemplate mongoTemplate;

    public List<Map<String, Object>> manyToOneTransformation(Map<String, List<Map<String, Object>>> content) {
        List<Map<String, Object>> table1 = content.get("table1Content");
        List<Map<String, Object>> table2 = content.get("table2Content");
        List<Map<String, Object>> keys1 = content.get("table1Keys");
        List<Map<String, Object>> keys2 = content.get("table2Keys");

        String table1PrimaryKeyColumnName;
        String table2PrimaryKeyColumnName;
        String table2ForeignKeyColumnName;
        List<Map<String, Object>> employeeList = new ArrayList<>();

        log.info("Table 1 info {}", table1);
        log.info("Table 2 info {}", table2);
        log.info("Table 1 keys {}", keys1.get(0).get("Column_name"));
        log.info("Table 2 keys {}", keys2.get(0).get("Column_name"));
        log.info("Table 2 keys {}", keys2.get(1).get("Column_name"));

        // Obtain the table 1 name in order to make it the name of the array inside table 1
        String table1Name = this.databaseConfig.getTable1();

        // Obtain the table 2 name in order to make it the name of the array inside table 1
        String table2Name = this.databaseConfig.getTable2();

        // Get the primary key name from table 1
        table1PrimaryKeyColumnName = keys1.get(0).get("Column_name").toString();
        log.info("The primary key name in " + table1Name + " is: {}", table1PrimaryKeyColumnName);

        // Get the primary key name from table 2
        table2PrimaryKeyColumnName = keys2.get(0).get("Column_name").toString();
        log.info("The primary key name in " + table2Name + " is: {}", table2PrimaryKeyColumnName);

        // Get the foreign key name from table 2
        table2ForeignKeyColumnName = keys2.get(1).get("Column_name").toString();
        log.info("The foreign key name in " + table2Name + " is: {}", table2ForeignKeyColumnName);

        // Initialize empty map arrays inside each table1 row with table's 2 name
        log.info("Assigning empty maps to " + table2Name + " with name: {}", keys2.get(0).get("Table"));

        for (Map<String, Object> tableRow : table1) {
            tableRow.put(table2Name, new ArrayList<Map<String, Object>>());
        }

        // Iterate over array 2 to retrieve it's values and put each one of them inside table 1 array of maps,
        // according to it's foreign key value
        for (int i = 0; i < table2.size(); i++) {
            // Get the value of the foreign key in table 2, to match it with its correspondant table 1 row
            String foreignKeyValue = table2.get(i).get(table2ForeignKeyColumnName).toString();
            for (Map<String, Object> table1Row : table1) {

                // Obtain table row id value, then check if its the same as the foreign key value
                if (foreignKeyValue.equals(table1Row.get(table1PrimaryKeyColumnName).toString())) {
                    log.info("Match between " + table2Name + " and " + table1Name + " has been found");

                    // Move the information from the employee in table 2 to new employee record in table 1
                    Map<String, Object> newEmployee = new HashMap<>();
                    // Transfer data fields from old employee record to new employee record, except for the id
                    for (Map.Entry<String, Object> entry : table2.get(i).entrySet()) {
                        if (!entry.getKey().equals(table2PrimaryKeyColumnName) && !entry.getKey().equals(table2ForeignKeyColumnName)) {
                            newEmployee.put(entry.getKey(), entry.getValue());
                        }
                    }
                    //TODO: check this line
                    employeeList = (List<Map<String, Object>>) table1Row.get(table2Name);

                    employeeList.add(newEmployee);
                }
            }
            log.info(table2Name + " list is: {}", employeeList);
        }
        return table1;
    }

    public List<Map<String, Object>> oneToManyTransformation(Map<String, List<Map<String, Object>>> content) {

        List<Map<String, Object>> table1 = content.get("table1Content");
        List<Map<String, Object>> table2 = content.get("table2Content");
        List<Map<String, Object>> keys1 = content.get("table1Keys");
        List<Map<String, Object>> keys2 = content.get("table2Keys");

        String table1PrimaryKeyColumnName;
        String table2PrimaryKeyColumnName;
        String table2ForeignKeyColumnName;

        log.info("Table 1 info {}", table1);
        log.info("Table 2 info {}", table2);
        log.info("Table 1 keys {}", keys1.get(0).get("Column_name"));
        log.info("Table 2 keys {}", keys2.get(0).get("Column_name"));
        log.info("Table 2 keys {}", keys2.get(1).get("Column_name"));

        // Obtain the table 1 name in order to make it the name of the array inside table 1
        String table1Name = this.databaseConfig.getTable1();

        // Obtain the table 2 name in order to make it the name of the array inside table 1
        String table2Name = this.databaseConfig.getTable2();

        // Get the primary key name from table 1
        table1PrimaryKeyColumnName = keys1.get(0).get("Column_name").toString();
        log.info("The primary key name in " + table1Name + " is: {}", table1PrimaryKeyColumnName);

        // Get the primary key name from table 2
        table2PrimaryKeyColumnName = keys2.get(0).get("Column_name").toString();
        log.info("The primary key name in " + table2Name + " is: {}", table2PrimaryKeyColumnName);

        // Get the foreign key name from table 2
        table2ForeignKeyColumnName = keys2.get(1).get("Column_name").toString();
        log.info("The foreign key name in " + table2Name + " is: {}", table2ForeignKeyColumnName);

        // Initialize empty map arrays inside each table2 row with table's 1 name
        log.info("Assigning empty maps to " + table2Name + " with name: {}", keys2.get(0).get("Table"));

        // Iterate over array 2 to retrieve it's values and put each one of them inside table 1 array of maps,
        // according to it's foreign key value
        for (int i = 0; i < table2.size(); i++) {
            // Get the value of the foreign key in table 2, to match it with its correspondant table 1 row
            String foreignKeyValue = table2.get(i).get(table2ForeignKeyColumnName).toString();
            for (Map<String, Object> table1Row : table1) {

                // Obtain table row id value, then check if its the same as the foreign key value
                if (foreignKeyValue.equals(table1Row.get(table1PrimaryKeyColumnName).toString())) {
                    log.info("Match between " + table2Name + " and " + table1Name + " has been found");

                    // Move the information from the table 1 record to table 2 record.
                    Map<String, Object> newTable1Record = new HashMap<>();

                    // Get Data for the table 1 record and put it inside newTable1Record, except for the id
                    for (Map.Entry<String, Object> entry : table1Row.entrySet()) {
                        if (!entry.getKey().equals(table1PrimaryKeyColumnName)) {
                            newTable1Record.put(entry.getKey(), entry.getValue());
                        }
                    }
                    // Add the new record to table 2
                    table2.get(i).put(table1Name, newTable1Record);
                }
            }
        }

        // Keys deletion
        for (Map<String, Object> tableRow : table2) {
            // tableRow.put(table1Name, new HashMap<String, Object>());
            tableRow.remove(table2ForeignKeyColumnName);
            tableRow.remove(table2PrimaryKeyColumnName);
        }

        return table2;
    }


    public void insertData(List<Map<String, Object>> resultList, String collectionName) {
        //Print the list to see the result
        log.info("Starting insertion");
        log.info("Result List is: {}", resultList);

        //
        for (Map<String, Object> document : resultList) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                String contentJson = objectMapper.writeValueAsString(document);
                log.info("Document inserted: {}", contentJson);

                Document doc = Document.parse(contentJson);
                mongoTemplate.insert(doc, collectionName);

            } catch (Exception e) {
                log.info("Error: {}", e.getMessage());
            }
        }
    }

    public void insertSingleTableData(Map<String, List<Map<String, Object>>> resultList) {

        for (Map.Entry<String, List<Map<String, Object>>> entry : resultList.entrySet()) {
            List<Map<String, Object>> contentTable = entry.getValue();
            String collection = entry.getKey();
            log.info("Collection: {}", collection);
            log.info("Content {}", contentTable);

            for (Map<String, Object> document : contentTable) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();

                    String contentJson = objectMapper.writeValueAsString(document);
                    log.info("Document inserted: {}", contentJson);

                    Document doc = Document.parse(contentJson);
                    mongoTemplate.insert(doc, collection);

                } catch (Exception e) {
                    log.info("Error: {}", e.getMessage());
                }
            }

        }
    }

    public List<Map<String, Object>> manyToManyTransformationV1(Map<String, List<Map<String, Object>>> content) {

        List<Map<String, Object>> table1 = content.get("table1Content");
        List<Map<String, Object>> table2 = content.get("table2Content");
        List<Map<String, Object>> table3 = content.get("table3Content");
        List<Map<String, Object>> keys1 = content.get("table1Keys");
        List<Map<String, Object>> keys2 = content.get("table2Keys");
        List<Map<String, Object>> keys3 = content.get("table3Keys");

        String table1PrimaryKeyColumnName;
        String table2PrimaryKeyColumnName;
        String table3PrimaryKeyColumnName;
        String table3ForeignKeyColumnName;

        String table1Name = this.databaseConfig.getTable1();
        String table2Name = this.databaseConfig.getTable2();
        String table3Name = this.databaseConfig.getTable3();

        table1PrimaryKeyColumnName = keys1.get(0).get("Column_name").toString();
        log.info("The primary key name in " + table1Name + " is: {}", table1PrimaryKeyColumnName);

        table2PrimaryKeyColumnName = keys2.get(0).get("Column_name").toString();
        log.info("The primary key name in " + table2Name + " is: {}", table2PrimaryKeyColumnName);

        table3PrimaryKeyColumnName = keys3.get(0).get("Column_name").toString();
        log.info("The primary key name in " + table3Name + " is: {}", table3PrimaryKeyColumnName);

        table3ForeignKeyColumnName = keys3.get(1).get("Column_name").toString();
        log.info("The foreign key name in " + table3Name + " is: {}", table3ForeignKeyColumnName);

        for (int i = 0; i < table1.size(); i++) {
            List<Map<String, Object>> tableRecordMap = new ArrayList<>();
            String primaryKeyValue = table1.get(i).get(table1PrimaryKeyColumnName).toString();

            for (Map<String, Object> table3Row : table3) {

                if (primaryKeyValue.equals(table3Row.get(table3PrimaryKeyColumnName).toString())) {
                    log.info("Match between " + table1Name + " and " + table2Name + " has been found");

                    Map<String, Object> newTableRecord = new HashMap<>();

                    for (Map.Entry<String, Object> entry : table3Row.entrySet()) {
                        if (!entry.getKey().equals(table3PrimaryKeyColumnName)) {
                            newTableRecord = table2.get((Integer.parseInt(entry.getValue().toString())) - 1);
                        }
                    }
                    tableRecordMap.add(newTableRecord);
                }
                table1.get(i).put(table2Name, tableRecordMap);
            }
            log.info(table1Name + " list is: {}", table1);
        }

        for (Map<String, Object> tableRow : table1) {
            tableRow.remove(table1PrimaryKeyColumnName);
        }

        return table1;
    }

    public List<Map<String, Object>> manyToManyTransformationV2(Map<String, List<Map<String, Object>>> content) {

        List<Map<String, Object>> table1 = content.get("table1Content");
        List<Map<String, Object>> table2 = content.get("table2Content");
        List<Map<String, Object>> table3 = content.get("table3Content");
        List<Map<String, Object>> keys1 = content.get("table1Keys");
        List<Map<String, Object>> keys2 = content.get("table2Keys");
        List<Map<String, Object>> keys3 = content.get("table3Keys");

        String table1PrimaryKeyColumnName;
        String table2PrimaryKeyColumnName;
        String table3PrimaryKeyColumnName;
        String table3ForeignKeyColumnName;

        String table1Name = this.databaseConfig.getTable1();
        String table2Name = this.databaseConfig.getTable2();
        String table3Name = this.databaseConfig.getTable3();

        table1PrimaryKeyColumnName = keys1.get(0).get("Column_name").toString();
        log.info("The primary key name in " + table1Name + " is: {}", table1PrimaryKeyColumnName);

        table2PrimaryKeyColumnName = keys2.get(0).get("Column_name").toString();
        log.info("The primary key name in " + table2Name + " is: {}", table2PrimaryKeyColumnName);

        table3PrimaryKeyColumnName = keys3.get(0).get("Column_name").toString();
        log.info("The primary key name in " + table3Name + " is: {}", table3PrimaryKeyColumnName);

        table3ForeignKeyColumnName = keys3.get(1).get("Column_name").toString();
        log.info("The foreign key name in " + table3Name + " is: {}", table3ForeignKeyColumnName);

        for (int i = 0; i < table2.size(); i++) {
            List<Map<String, Object>> tableRecordMap = new ArrayList<>();
            String primaryKeyValue = table2.get(i).get(table2PrimaryKeyColumnName).toString();

            for (Map<String, Object> table3Row : table3) {

                if (primaryKeyValue.equals(table3Row.get(table3ForeignKeyColumnName).toString())) {
                    log.info("Match between " + table1Name + " and " + table2Name + " has been found");

                    Map<String, Object> newTableRecord = new HashMap<>();

                    for (Map.Entry<String, Object> entry : table3Row.entrySet()) {
                        if (!entry.getKey().equals(table3ForeignKeyColumnName)) {
                            newTableRecord = table1.get((Integer.parseInt(entry.getValue().toString())) - 1);
                        }
                    }
                    tableRecordMap.add(newTableRecord);
                }
                table2.get(i).put(table1Name, tableRecordMap);
            }
            log.info(table1Name + " list is: {}", table2);
        }

        for (Map<String, Object> tableRow : table2) {
            tableRow.remove(table2PrimaryKeyColumnName);
        }

        return table2;
    }
}
