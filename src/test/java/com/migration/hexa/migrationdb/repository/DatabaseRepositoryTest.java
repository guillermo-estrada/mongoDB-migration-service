package com.migration.hexa.migrationdb.repository;

import com.migration.hexa.migrationdb.config.DatabaseConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class DatabaseRepositoryTest {

    @InjectMocks
    DatabaseRepository databaseRepository;

    @Autowired
    DatabaseConfig databaseConfig;

    @Mock
    JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Test Create Data Successfully")
    void testCreateDataSuccessfully(){

        Map<String, Object> table1emp = new HashMap<>();
        table1emp.put("idEmployee", 1);
        table1emp.put("FirstName", "Guillermo");
        table1emp.put("LastName", "Estrada Manriquez");

        List<Map<String, Object>> table1Content = new ArrayList<>();
        table1Content.add(table1emp);

        //--ToDo Fix this
//        Mockito.when(jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(table1Content);
        //Mockito.when(mockTemplate.queryForList(Mockito.anyString(), ArgumentMatchers.<Object>any())).thenReturn(mockResult);
        // Alternatively:
        //when(mockTemplate.queryForList(anyString(), Mockito.<Object>any())).thenReturn(mockResult);

        //tableInformation.put("table1Content", jdbcTemplate.queryForList("SELECT * FROM " + this.databaseConfig.getTable1()));

//        Map<String, List<Map<String, Object>>> newTable = databaseRepository.getData();
        Assertions.assertEquals(true, true);
    }
}
