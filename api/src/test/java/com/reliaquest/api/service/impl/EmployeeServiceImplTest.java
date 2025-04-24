package com.reliaquest.api.service.impl;

import com.reliaquest.api.client.IEmployeeApiClient;
import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private IEmployeeApiClient apiClient;

    private List<EmployeeResponse> sampleEmployeeList;
    private EmployeeResponse sampleEmployee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleEmployee = new EmployeeResponse();
        sampleEmployee.setId(UUID.randomUUID());
        sampleEmployee.setEmployee_name("Ajinkya");
        sampleEmployee.setEmployee_salary(200000);
        sampleEmployee.setEmployee_age(26);
        sampleEmployee.setEmployee_title("Senior Software Engineer");
        sampleEmployee.setEmployee_email("ajinkya@company.com");

        sampleEmployeeList = new ArrayList<>();
        sampleEmployeeList.add(sampleEmployee);

        EmployeeResponse anotherEmployee = new EmployeeResponse();
        anotherEmployee.setId(UUID.randomUUID());
        anotherEmployee.setEmployee_name("John");
        anotherEmployee.setEmployee_salary(150000);
        anotherEmployee.setEmployee_age(30);
        anotherEmployee.setEmployee_title("Software Engineer");
        anotherEmployee.setEmployee_email("john@company.com");

        sampleEmployeeList.add(anotherEmployee);
    }

    @Test
    void testGetAllEmployees() {
        when(apiClient.fetchAllEmployees()).thenReturn(sampleEmployeeList);

        List<EmployeeResponse> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(emp -> emp.getEmployee_name().equals("Ajinkya")));
    }

    @Test
    void testGetEmployeesByName_Found() {
        when(apiClient.fetchAllEmployees()).thenReturn(sampleEmployeeList);

        List<EmployeeResponse> result = employeeService.getEmployeesByName("Ajinkya");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Ajinkya", result.get(0).getEmployee_name());
    }

    @Test
    void testGetEmployeesByName_NotFound() {
        when(apiClient.fetchAllEmployees()).thenReturn(sampleEmployeeList);

        List<EmployeeResponse> result = employeeService.getEmployeesByName("NonExistent");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetEmployeeById_Found() {
        when(apiClient.fetchEmployeeById(anyString())).thenReturn(Optional.of(sampleEmployee));

        Optional<EmployeeResponse> result = employeeService.getEmployeeById(sampleEmployee.getId().toString());

        assertTrue(result.isPresent());
        assertEquals("Ajinkya", result.get().getEmployee_name());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(apiClient.fetchEmployeeById(anyString())).thenReturn(Optional.empty());

        Optional<EmployeeResponse> result = employeeService.getEmployeeById("invalid-id");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
        when(apiClient.fetchAllEmployees()).thenReturn(sampleEmployeeList);

        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();

        assertNotNull(highestSalary);
        assertEquals(200000, highestSalary);
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        when(apiClient.fetchAllEmployees()).thenReturn(sampleEmployeeList);

        List<String> topEarners = employeeService.getTopTenHighestEarningEmployeeNames();

        assertNotNull(topEarners);
        assertTrue(topEarners.contains("Ajinkya - 200000"));
    }

    @Test
    void testCreateEmployee_Success() {
        Employee newEmployee = new Employee();
        newEmployee.setName("Jane");
        newEmployee.setSalary(120000);

        EmployeeResponse createdEmployee = new EmployeeResponse();
        createdEmployee.setId(UUID.randomUUID());
        createdEmployee.setEmployee_name("Jane");
        createdEmployee.setEmployee_salary(120000);
        createdEmployee.setEmployee_age(28);
        createdEmployee.setEmployee_title("QA Engineer");
        createdEmployee.setEmployee_email("jane@company.com");

        when(apiClient.postEmployee(newEmployee)).thenReturn(Optional.of(createdEmployee));

        EmployeeResponse result = employeeService.createEmployee(newEmployee);

        assertNotNull(result);
        assertEquals("Jane", result.getEmployee_name());
        assertEquals(120000, result.getEmployee_salary());
    }

    @Test
    void testDeleteEmployeeById_Success() {
        when(apiClient.fetchEmployeeById(anyString())).thenReturn(Optional.of(sampleEmployee));
        when(apiClient.deleteEmployee(anyString())).thenReturn(Optional.of("Employee deleted"));

        String result = employeeService.deleteEmployeeById(sampleEmployee.getId().toString());

        assertEquals("Employee deleted", result);
    }

    @Test
    void testDeleteEmployeeById_NotFound() {
        when(apiClient.fetchEmployeeById(anyString())).thenReturn(Optional.empty());

        String result = employeeService.deleteEmployeeById("invalid-id");

        assertEquals("Employee with ID invalid-id not found.", result);
    }

    @Test
    void testDeleteEmployeeById_Failure() {
        when(apiClient.fetchEmployeeById("E002")).thenReturn(Optional.empty());
        when(apiClient.deleteEmployee("E002")).thenReturn(Optional.empty());

        String result = employeeService.deleteEmployeeById("E002");

        assertEquals("Employee with ID E002 not found.", result);
    }
}
