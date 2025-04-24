package com.reliaquest.api.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.dto.DeleteEmployee;
import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.client.IEmployeeApiClient;
import com.reliaquest.api.wrapper.EmployeeResponseWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeApiClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeApiClientImpl employeeApiClient;

    private final String baseUrl = "http://localhost:8112/api/v1/employee";

    private String employeeId;
    private EmployeeResponse mockResponse;
    private Employee mockInput;

    @BeforeEach
    void setUp() {
        employeeId = "123e4567-e89b-12d3-a456-426614174000";

        mockResponse = new EmployeeResponse();
        mockResponse.setId(UUID.fromString(employeeId));
        mockResponse.setEmployee_name("John Doe");
        mockResponse.setEmployee_email("john.doe@example.com");
        mockResponse.setEmployee_age(30);
        mockResponse.setEmployee_salary(50000);
        mockResponse.setEmployee_title("Developer");

        mockInput = new Employee();
        mockInput.setName("John Doe");
        mockInput.setSalary(50000);
    }

    @Test
    void testFetchAllEmployees_Success() throws Exception {
        String json = """
            {
                "data": [
                    {
                        "id": "123e4567-e89b-12d3-a456-426614174000",
                        "employee_name": "John Doe",
                        "employee_email": "john.doe@example.com",
                        "employee_age": 30,
                        "employee_salary": 50000,
                        "employee_title": "Developer"
                    }
                ]
            }
        """;

        when(restTemplate.getForObject(baseUrl, String.class)).thenReturn(json);

        List<EmployeeResponse> employees = employeeApiClient.fetchAllEmployees();

        assertEquals(1, employees.size());
        assertEquals("John Doe", employees.get(0).getEmployee_name());
    }

    @Test
    void testFetchEmployeeById_Success() throws Exception {
        String responseJson = """
            {
                "data": {
                    "id": "123e4567-e89b-12d3-a456-426614174000",
                    "employee_name": "John Doe",
                    "employee_email": "john.doe@example.com",
                    "employee_age": 30,
                    "employee_salary": 50000,
                    "employee_title": "Developer"
                }
            }
        """;

        when(restTemplate.getForObject(baseUrl + "/" + employeeId, String.class))
                .thenReturn(responseJson);

        Optional<EmployeeResponse> result = employeeApiClient.fetchEmployeeById(employeeId);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getEmployee_name());
        assertEquals(employeeId, result.get().getId().toString());
    }

    @Test
    void testPostEmployee_Success() {
        Map<String, Object> mockResponseMap = Map.of(
                "data", Map.of(
                        "id", employeeId,
                        "employee_name", "John Doe",
                        "employee_email", "john.doe@example.com",
                        "employee_age", 30,
                        "employee_salary", 50000,
                        "employee_title", "Developer"
                )
        );

        when(restTemplate.postForObject(baseUrl, mockInput, Map.class)).thenReturn(mockResponseMap);

        Optional<EmployeeResponse> result = employeeApiClient.postEmployee(mockInput);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getEmployee_name());
    }

    @Test
    void testDeleteEmployee_Success() {
        String employeeName = "John Doe";

        DeleteEmployee deleteEmployee = new DeleteEmployee();
        deleteEmployee.setName(employeeName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DeleteEmployee> request = new HttpEntity<>(deleteEmployee, headers);

        ResponseEntity<String> mockResponseEntity = new ResponseEntity<>("Deleted", HttpStatus.OK);

        when(restTemplate.exchange(eq(baseUrl), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponseEntity);

        Optional<String> result = employeeApiClient.deleteEmployee(employeeName);

        assertTrue(result.isPresent());
        assertTrue(result.get().contains("deleted successfully"));
    }

    @Test
    void testFetchAllEmployees_ReturnsEmptyListOnError() {
        when(restTemplate.getForObject(baseUrl, String.class)).thenThrow(RuntimeException.class);
        List<EmployeeResponse> employees = employeeApiClient.fetchAllEmployees();
        assertTrue(employees.isEmpty());
    }

    @Test
    void testFetchEmployeeById_ReturnsEmptyOptionalOnError() {
        when(restTemplate.getForObject(baseUrl + "/" + employeeId, String.class))
                .thenThrow(RuntimeException.class);

        Optional<EmployeeResponse> result = employeeApiClient.fetchEmployeeById(employeeId);
        assertTrue(result.isEmpty());
    }

    @Test
    void testPostEmployee_ReturnsEmptyOptionalOnError() {
        when(restTemplate.postForObject(baseUrl, mockInput, Map.class))
                .thenThrow(RuntimeException.class);

        Optional<EmployeeResponse> result = employeeApiClient.postEmployee(mockInput);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteEmployee_ReturnsEmptyOptionalOnError() {
        when(restTemplate.exchange(eq(baseUrl), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(String.class)))
                .thenThrow(RuntimeException.class);

        Optional<String> result = employeeApiClient.deleteEmployee("John Doe");
        assertTrue(result.isEmpty());
    }

    @Test
    void testPostEmployee_ResponseMissingData_ReturnsEmptyOptional() {
        when(restTemplate.postForObject(baseUrl, mockInput, Map.class)).thenReturn(Map.of()); // no 'data' key

        Optional<EmployeeResponse> result = employeeApiClient.postEmployee(mockInput);

        assertTrue(result.isEmpty(), "Should return empty Optional");
    }

    @Test
    void testPostEmployee_NullApiResponse_ReturnsEmptyOptional() {
        when(restTemplate.postForObject(baseUrl, mockInput, Map.class)).thenReturn(null);

        Optional<EmployeeResponse> result = employeeApiClient.postEmployee(mockInput);

        assertTrue(result.isEmpty(), "Should return empty Optional if postForObject returns null");
    }

    @Test
    void testFetchAllEmployees_InvalidJson_ReturnsEmptyList() {
        String malformedJson = "{ invalid json }";

        when(restTemplate.getForObject(baseUrl, String.class)).thenReturn(malformedJson);

        List<EmployeeResponse> employees = employeeApiClient.fetchAllEmployees();

        assertTrue(employees.isEmpty(), "Should return empty list when JSON is invalid");
    }

    @Test
    void testFetchEmployeeById_NullResponse_ReturnsEmptyOptional() {
        when(restTemplate.getForObject(baseUrl + "/" + employeeId, String.class)).thenReturn(null);

        Optional<EmployeeResponse> result = employeeApiClient.fetchEmployeeById(employeeId);

        assertTrue(result.isEmpty(), "Should return empty Optional when response is null");
    }

    @Test
    void testFetchEmployeeById_InvalidUUIDFormat_ReturnsEmptyOptional() {
        // ID format is invalid for UUID
        String invalidJson = """
        {
            "data": {
                "id": "123", 
                "employee_name": "Bad UUID", 
                "employee_salary": 1000,
                "employee_age": 40,
                "employee_email": "bad@example.com",
                "employee_title": "Tester"
            }
        }
    """;

        when(restTemplate.getForObject(baseUrl + "/123", String.class)).thenReturn(invalidJson);

        Optional<EmployeeResponse> result = employeeApiClient.fetchEmployeeById("123");

        assertTrue(result.isEmpty(), "Should return empty Optional when UUID format is invalid");
    }
}
