package com.migration.hexa.migrationdb.repository;

import com.migration.hexa.migrationdb.config.DatabaseConfig;
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

        long startTime = System.currentTimeMillis();

        List<Map<String, Object>> table1 = content.get("table1Content");
        List<Map<String, Object>> table2 = content.get("table2Content");
        List<Map<String, Object>> keys1 = content.get("table1Keys");
        List<Map<String, Object>> keys2 = content.get("table2Keys");

        String table1PrimaryKeyColumnName;
        String table2PrimaryKeyColumnName;
        String table2ForeignKeyColumnName;
        List<Map<String, Object>> employeeList = new ArrayList<>();

        // Obtain the table 1 name in order to make it the name of the array inside table 1
        String table1Name = this.databaseConfig.getTable1();

        // Obtain the table 2 name in order to make it the name of the array inside table 1
        String table2Name = this.databaseConfig.getTable2();

        // Get the primary key name from table 1
        table1PrimaryKeyColumnName = keys1.get(0).get("Column_name").toString();

        // Get the primary key name from table 2
        table2PrimaryKeyColumnName = keys2.get(0).get("Column_name").toString();

        // Get the foreign key name from table 2
        table2ForeignKeyColumnName = keys2.get(1).get("Column_name").toString();

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
        }

        for (Map<String, Object> tableRow : table1) {
            tableRow.remove(table1PrimaryKeyColumnName);
        }

        long endTime = System.currentTimeMillis();
        double totalTime = (double) ((endTime - startTime) / 1000);
        log.info("Transformation finished in: " + totalTime + " seconds");

        return table1;
    }

    public List<Map<String, Object>> oneToManyTransformation(Map<String, List<Map<String, Object>>> content) {

        long startTime = System.currentTimeMillis();

        List<Map<String, Object>> table1 = content.get("table1Content");
        List<Map<String, Object>> table2 = content.get("table2Content");
        List<Map<String, Object>> keys1 = content.get("table1Keys");
        List<Map<String, Object>> keys2 = content.get("table2Keys");

        String table1PrimaryKeyColumnName;
        String table2PrimaryKeyColumnName;
        String table2ForeignKeyColumnName;

        // Obtain the table 1 name in order to make it the name of the array inside table 1
        String table1Name = this.databaseConfig.getTable1();

        // Obtain the table 2 name in order to make it the name of the array inside table 1
        String table2Name = this.databaseConfig.getTable2();

        // Get the primary key name from table 1
        table1PrimaryKeyColumnName = keys1.get(0).get("Column_name").toString();

        // Get the primary key name from table 2
        table2PrimaryKeyColumnName = keys2.get(0).get("Column_name").toString();

        // Get the foreign key name from table 2
        table2ForeignKeyColumnName = keys2.get(1).get("Column_name").toString();

        // Iterate over array 2 to retrieve it's values and put each one of them inside table 1 array of maps,
        // according to it's foreign key value
        for (int i = 0; i < table2.size(); i++) {
            // Get the value of the foreign key in table 2, to match it with its correspondant table 1 row
            String foreignKeyValue = table2.get(i).get(table2ForeignKeyColumnName).toString();
            for (Map<String, Object> table1Row : table1) {

                // Obtain table row id value, then check if its the same as the foreign key value
                if (foreignKeyValue.equals(table1Row.get(table1PrimaryKeyColumnName).toString())) {
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

        long endTime = System.currentTimeMillis();
        double totalTime = (double) ((endTime - startTime) / 1000);
        log.info("Transformation finished in: " + totalTime + " seconds");
        return table2;
    }

    public void twoTableRelationshipOneTableTransformation(Map<String, List<Map<String, Object>>> content) {

        long startTime = System.currentTimeMillis();

        Map<String, List<Map<String, Object>>> aux1 = new HashMap<>();

        aux1.put("table1Content", content.get("table1Content"));
        aux1.put("table1Keys", content.get("table1Keys"));
        aux1.put("table2Content", content.get("table2Content"));
        aux1.put("table2Keys", content.get("table2Keys"));

        List<Map<String, Object>> result = this.manyToOneTransformation(aux1);
        this.insertData(result, this.databaseConfig.getTable1());

        this.insertSingleTableData(content.get("table3Content"));

        long endTime = System.currentTimeMillis();
        double totalTime = (double) ((endTime - startTime) / 1000);
        log.info("Transformation finished in: " + totalTime + " seconds");
    }

    public List<Map<String, Object>> manyToManyTransformationV1(Map<String, List<Map<String, Object>>> content) {

        long startTime = System.currentTimeMillis();

        List<Map<String, Object>> table1 = content.get("table1Content");
        List<Map<String, Object>> table2 = content.get("table2Content");
        List<Map<String, Object>> table3 = content.get("table3Content");
        List<Map<String, Object>> keys1 = content.get("table1Keys");
        List<Map<String, Object>> keys3 = content.get("table3Keys");

        String table1PrimaryKeyColumnName = keys1.get(0).get("Column_name").toString();
        String table3PrimaryKeyColumnName = keys3.get(0).get("Column_name").toString();
        String table2Name = this.databaseConfig.getTable2();

        for (int i = 0; i < table1.size(); i++) {
            List<Map<String, Object>> tableRecordMap = new ArrayList<>();
            String primaryKeyValue = table1.get(i).get(table1PrimaryKeyColumnName).toString();

            for (Map<String, Object> table3Row : table3) {

                if (primaryKeyValue.equals(table3Row.get(table3PrimaryKeyColumnName).toString())) {

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
        }

        for (Map<String, Object> tableRow : table1) {
            tableRow.remove(table1PrimaryKeyColumnName);
        }

        long endTime = System.currentTimeMillis();
        double totalTime = (double) ((endTime - startTime) / 1000);
        log.info("Transformation finished in: " + totalTime + " seconds");

        return table1;
    }

    public List<Map<String, Object>> manyToManyTransformationV2(Map<String, List<Map<String, Object>>> content) {

        long startTime = System.currentTimeMillis();

        List<Map<String, Object>> table1 = content.get("table1Content");
        List<Map<String, Object>> table2 = content.get("table2Content");
        List<Map<String, Object>> table3 = content.get("table3Content");
        List<Map<String, Object>> keys2 = content.get("table2Keys");
        List<Map<String, Object>> keys3 = content.get("table3Keys");

        String table2PrimaryKeyColumnName = keys2.get(0).get("Column_name").toString();
        String table3ForeignKeyColumnName = keys3.get(1).get("Column_name").toString();
        String table1Name = this.databaseConfig.getTable1();

        for (int i = 0; i < table2.size(); i++) {
            List<Map<String, Object>> tableRecordMap = new ArrayList<>();
            String primaryKeyValue = table2.get(i).get(table2PrimaryKeyColumnName).toString();

            for (Map<String, Object> table3Row : table3) {

                if (primaryKeyValue.equals(table3Row.get(table3ForeignKeyColumnName).toString())) {

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
        }

        for (Map<String, Object> tableRow : table2) {
            tableRow.remove(table2PrimaryKeyColumnName);
        }

        long endTime = System.currentTimeMillis();
        double totalTime = (double) ((endTime - startTime) / 1000);
        log.info("Transformation finished in: " + totalTime + " seconds");

        return table2;
    }

    public void insertData(List<Map<String, Object>> resultList, String collectionName) {
        long startTime = System.currentTimeMillis();

        try {
            mongoTemplate.insert(resultList, collectionName);
        } catch (Exception e) {
            log.info("Error in insertion: {}", e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        double totalTime = (double) ((endTime - startTime) / 1000);
        log.info("Insertion finished in: " + totalTime + " seconds");
    }

    public void insertSingleTableData(List<Map<String, Object>> resultList) {

        long startTime = System.currentTimeMillis();

        try {
            mongoTemplate.insert(resultList, this.databaseConfig.getTable1());
        } catch (Exception e) {
            log.info("Error in insertion: {}", e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        double totalTime = (double) ((endTime - startTime) / 1000);
        log.info("Insertion finished in: " + totalTime + " seconds");
    }

    public void insertAllSingleTableData(Map<String, List<Map<String, Object>>> resultList) {

        long startTime = System.currentTimeMillis();

        for (Map.Entry<String, List<Map<String, Object>>> entry : resultList.entrySet()) {
            List<Map<String, Object>> contentTable = entry.getValue();
            String collection = entry.getKey();

            try {
                mongoTemplate.insert(contentTable, collection);
            } catch (Exception e) {
                log.info("Error in insertion: {}", e.getMessage());
            }

        }

        long endTime = System.currentTimeMillis();
        double totalTime = (double) ((endTime - startTime) / 1000);
        log.info("Insertion finished in: " + totalTime + " seconds");
    }
}
