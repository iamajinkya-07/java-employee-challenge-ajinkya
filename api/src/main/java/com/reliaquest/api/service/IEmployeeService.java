package com.reliaquest.api.service;

import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeResponse;

import java.util.List;
import java.util.Optional;

/**
 * This is interface for service layer
 * @author - Ajinkya Choudhary
 */
public interface IEmployeeService {
    List<EmployeeResponse> getAllEmployees();

    List<EmployeeResponse> getEmployeesByName(String searchName);

    Optional<EmployeeResponse> getEmployeeById(String id);

    Integer getHighestSalaryOfEmployees();

    List<String> getTopTenHighestEarningEmployeeNames();

    EmployeeResponse createEmployee(Employee employeeInput);

    String deleteEmployeeById(String id);
}
