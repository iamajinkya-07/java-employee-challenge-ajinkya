package com.reliaquest.api.controller.impl;

import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.service.IEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Mock
    private IEmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeResponse sampleEmployee;

    @BeforeEach
    void setUp() {
        sampleEmployee = new EmployeeResponse();
        sampleEmployee.setId(UUID.fromString("b25df7a7-8246-4c48-b8b7-d132efed6dd4"));
        sampleEmployee.setEmployee_name("Ajinkya");
        sampleEmployee.setEmployee_salary(100000);
        sampleEmployee.setEmployee_age(26);
        sampleEmployee.setEmployee_title("SSE");
        sampleEmployee.setEmployee_email("ajinkya@gmail.com");
    }

    @Test
    void testGetAllEmployees() {
        when(employeeService.getAllEmployees()).thenReturn(List.of(sampleEmployee));

        ResponseEntity<List<EmployeeResponse>> response = employeeController.getAllEmployees();
        List<EmployeeResponse> responseList = response.getBody();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals("Ajinkya", responseList.get(0).getEmployee_name());
    }

    @Test
    void testGetEmployeesByNameSearch_Found() {
        when(employeeService.getEmployeesByName("Ajinkya")).thenReturn(List.of(sampleEmployee));

        ResponseEntity<List> response = employeeController.getEmployeesByNameSearch("Ajinkya");
        List<EmployeeResponse> employees = response.getBody();

        assertNotNull(employees);
        assertFalse(employees.isEmpty());
        assertEquals("Ajinkya", ((EmployeeResponse) employees.get(0)).getEmployee_name());
    }

    @Test
    void testGetEmployeesByNameSearch_NotFound() {
        when(employeeService.getEmployeesByName("Unknown")).thenReturn(Collections.emptyList());

        ResponseEntity<List> response = employeeController.getEmployeesByNameSearch("Unknown");
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("No employee found with name: Unknown", response.getBody().get(0));
    }

    @Test
    void testGetEmployeeById_Found() {
        when(employeeService.getEmployeeById("b25df7a7-8246-4c48-b8b7-d132efed6dd4")).thenReturn(Optional.of(sampleEmployee));

        ResponseEntity<EmployeeResponse> response = employeeController.getEmployeeById("b25df7a7-8246-4c48-b8b7-d132efed6dd4");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeService.getEmployeeById("XYZ")).thenReturn(Optional.empty());

        ResponseEntity response = employeeController.getEmployeeById("XYZ");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("No employee found with id: XYZ", ((List) response.getBody()).get(0));
    }

    @Test
    void testCreateEmployee() {
        Employee employee = new Employee();
        employee.setName("Ajinkya");
        employee.setSalary(100000);

        when(employeeService.createEmployee(employee)).thenReturn(sampleEmployee);

        ResponseEntity<EmployeeResponse> response = employeeController.createEmployee(employee);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Ajinkya", response.getBody().getEmployee_name());
    }

    @Test
    void testDeleteEmployeeById() {
        when(employeeService.deleteEmployeeById("E001")).thenReturn("Deleted Successfully");

        ResponseEntity<String> response = employeeController.deleteEmployeeById("E001");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Deleted Successfully", response.getBody());
    }
}

