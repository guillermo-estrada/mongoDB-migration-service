package com.migration.hexa.migrationdb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migration.hexa.migrationdb.repository.DatabaseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class DatabaseControllerTest {

    @InjectMocks
    DatabaseController databaseController;

    @Mock
    DatabaseRepository databaseRepository;

    @Mock
    JmsTemplate jmsTemplate;

    @Test
    @DisplayName("Test Get Data REST API")
    void testGetDataSuccessfully() throws Exception {
        Map<String, Object> table1emp = new HashMap<>();
        table1emp.put("idEmployee", 1);
        table1emp.put("FirstName", "Guillermo");
        table1emp.put("LastName", "Estrada Manriquez");

        List<Map<String, Object>> table1Content = new ArrayList<>();
        table1Content.add(table1emp);

        Map<String, Object> table2dep = new HashMap<>();
        table2dep.put("idDepartment", 1);
        table2dep.put("Name", "GLOBALATM01");

        List<Map<String, Object>> table2Content = new ArrayList<>();
        table2Content.add(table2dep);
        Map<String, List<Map<String, Object>>> data = new HashMap<>();

        data.put("table1Content", table1Content);
        data.put("table2Content", table2Content);

        Mockito.when(databaseRepository.getTwoTableRelationshipData()).thenReturn(data);

        Mockito.doNothing().when(jmsTemplate).convertAndSend(Mockito.anyString(), Mockito.any(Object.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String contentJson = objectMapper.writeValueAsString(data);

        ResponseEntity<String> result = databaseController.twoTableManyToOneMigration();

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(contentJson, result.getBody());
    }

    @Test
    @DisplayName("Error Getting Data in REST API")
    public void testGetDataException(){
        Mockito.when(databaseRepository.getTwoTableRelationshipData()).thenThrow(NullPointerException.class);
        ResponseEntity<String> result = databaseController.twoTableManyToOneMigration();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
}
