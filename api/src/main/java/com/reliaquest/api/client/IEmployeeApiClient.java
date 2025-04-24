package com.reliaquest.api.client;

import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface for talking to the Employee API to get, add, or delete employee details.
 * @author - Ajinkya Choudhary
 */
public interface IEmployeeApiClient {
    List<EmployeeResponse> fetchAllEmployees();
    Optional<EmployeeResponse> fetchEmployeeById(String id);

    Optional<EmployeeResponse> postEmployee(Employee employeeInput);

    Optional<String> deleteEmployee(String employeeName);
}
