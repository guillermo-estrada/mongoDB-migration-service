package com.migration.hexa.migrationdb.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migration.hexa.migrationdb.config.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ConsumerRepositoryTest {
    @Test
    void transformationProcessPerformsCorrectly() {
        String table1Name = "table1";
        String table2Name = "table2";
        String table1IdName = "table1Id";
        String table2IdName = "table2Id";
        String table2ForeignKeyName = "foreignIdTable2";

        // Instantiating the class to test
        ConsumerRepository consumerRepository = new ConsumerRepository();
        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setTable1(table1Name);
        databaseConfig.setTable2(table2Name);

        // Injecting databaseConfig object
        ReflectionTestUtils.setField(consumerRepository, "databaseConfig", databaseConfig);

        // Creating a map to hold the structure of lists that the method is supposed to receive
        Map<String, List<Map<String, Object>>> map = new HashMap<>();

        // Creating the different Lists that will be into the map
        List<Map<String, Object>> table1Content = new ArrayList<>();
        List<Map<String, Object>> table2Content = new ArrayList<>();
        List<Map<String, Object>> table1Keys = new ArrayList<>();
        List<Map<String, Object>> table2Keys = new ArrayList<>();

        // Filling Lists with data following the structure that the method expects
        // Each map would represent one row of the table, therefore the list represents a table from SQL
        Map<String, Object> table1Data = new HashMap<>();
        table1Data.put(table1IdName, 1);
        table1Data.put("table1Value", "Table1Value");
        table1Content.add(table1Data);

        // Table 2 always is the table with foreign key, from the way we retrieve data from SQL
        Map<String, Object> table2Data = new HashMap<>();
        table2Data.put(table2IdName, 1);
        table2Data.put(table2ForeignKeyName, 1);
        table2Data.put("table2Value", "Table2Value");
        table2Content.add(table2Data);

        // table1Keys and table2Keys are tables with Metadata from SQL where we extract the keys of the tables
        // This is the reason the keys will be named ad the id names from above. We will only set the needed values.
        Map<String, Object> table1KeysData = new HashMap<>();
        table1KeysData.put("Column_name", table1IdName);
        table1KeysData.put("Table", table1Name);
        table1Keys.add(table1KeysData);

        // Table 2 keys data will always have 2 entries since it has the foreign key, being the 2nd in sequence
        // the foreign key
        Map<String, Object> table2KeysDataRow1 = new HashMap<>();
        table2KeysDataRow1.put("Column_name", table2IdName);
        table2KeysDataRow1.put("Table", table2Name);
        table2Keys.add(table2KeysDataRow1);

        Map<String, Object> table2KeysDataRow2 = new HashMap<>();
        table2KeysDataRow2.put("Column_name", table2ForeignKeyName);
        table2KeysDataRow2.put("Table", table2Name);
        table2Keys.add(table2KeysDataRow2);

        // Finally we add the lists to the initial map and run the method to test.
        map.put("table1Content", table1Content);
        map.put("table2Content", table2Content);
        map.put("table1Keys", table1Keys);
        map.put("table2Keys", table2Keys);

        List<Map<String, Object>> result = consumerRepository.transformationProcess(map);

        // Obtain the embedded table as a result of the transformation
        // Note: ObjectMapper is used to avoid an unchecked cast warning.
        ArrayList<Map<String, Object>> embeddedTable = new ObjectMapper().convertValue(result.get(0).get(table2Name),
                new TypeReference<ArrayList<Map<String, Object>>>() {});

        // Check the embedded table is not empty
        Assertions.assertFalse(embeddedTable.isEmpty());

        // Check the embedded table has correct data inside
        Assertions.assertEquals("Table2Value", embeddedTable.get(0).get("table2Value"));

    }

}
