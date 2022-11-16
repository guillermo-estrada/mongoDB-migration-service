package com.migration.hexa.migrationdb.consumer;

import com.migration.hexa.migrationdb.model.SystemMessage;
import com.migration.hexa.migrationdb.repository.DatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("unchecked")
public class ConsumerComponent {

    private static final Logger log = LoggerFactory.getLogger(DatabaseRepository.class);

    @JmsListener(destination = "mongo-migrator_queue")
    public void messageListener(Map<String, List<Map<String, Object>>> content){
        log.info("Message Received: {}", content);

        List<Map<String, Object>> table1 = content.get("table1Content");
        List<Map<String, Object>> table2 = content.get("table2Content");
        List<Map<String, Object>> keys1 = content.get("table1Keys");
        List<Map<String, Object>> keys2 = content.get("table2Keys");

        log.info("Table 1 info {}", table1);
        log.info("Table 2 info {}", table2);
        log.info("Table 1 keys {}", keys1.get(0).get("Column_name"));
        log.info("Table 2 keys {}", keys2.get(0).get("Column_name"));
        log.info("Table 2 keys {}", keys2.get(1).get("Column_name"));

        // Get the primary key name from table 1
        String table1PrimaryKeyColumnName = keys1.get(0).get("Column_name").toString();
        log.info("The primary key name in table 1 is: {}", table1PrimaryKeyColumnName);

        // Get the primary key name from table 2
        String table2PrimaryKeyColumnName = keys2.get(0).get("Column_name").toString();
        log.info("The primary key name in table 1 is: {}", table2PrimaryKeyColumnName);

        // Get the foreign key name from table 2
        String table2ForeignKeyColumnName = keys2.get(1).get("Column_name").toString();
        log.info("The foreign key name in table 2 is: {}", table2ForeignKeyColumnName);
        
        // Obtain the table 2 name in order to make it the name of the array inside table 1
        String table2Name = keys2.get(0).get("Table").toString();

        // Initialize empty map arrays inside each table1 row with table's 2 name
        log.info("Assigning empty maps to table 2 with name: {}", keys2.get(0).get("Table"));
        for (Map<String,Object> tableRow : table1) {
            tableRow.put(table2Name, new ArrayList<Map<String, Object>>());
        }

        // Iterate over array 2 to retrieve it's values and put each one of them inside table 1 array of maps,
        // according to it's foreign key value
        for(int i=0;i<table2.size();i++){
            // Get the value of the foreign key in table 2, to match it with its correspondant table 1 row
            String foreignKeyValue = table2.get(i).get(table2ForeignKeyColumnName).toString();
            for (Map<String,Object> table1Row : table1) {

                // Obtain table row id value, then check if its the same as the foreign key value
                if(foreignKeyValue.equals(table1Row.get(table1PrimaryKeyColumnName).toString())){
                    log.info("Match between employee and department has been found");

                    // Move the information from the employee in table 2 to new employee record in table 1
                    Map<String, Object> newEmployee = new HashMap<>();
                    // Transfer data fields from old employee record to new employee record, except for the id
                    for(Map.Entry<String, Object> entry : table2.get(i).entrySet()){
                        if(!entry.getKey().equals(table2PrimaryKeyColumnName) && !entry.getKey().equals(table2ForeignKeyColumnName)){
                            newEmployee.put(entry.getKey(), entry.getValue());
                        }
                    }
                    ArrayList<Map<String, Object>> employeeList = (ArrayList<Map<String, Object>>) table1Row.get(table2Name);

                    employeeList.add(newEmployee);

                    log.info("Employee list is: {}", employeeList);
                    
                }
            }
            
        }
    }
}
