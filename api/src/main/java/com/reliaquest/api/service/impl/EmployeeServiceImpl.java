package com.reliaquest.api.service.impl;

import com.reliaquest.api.client.IEmployeeApiClient;
import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class provides the implementation of the employee-related business logic.
 * @author - Ajinkya Choudhary
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements IEmployeeService {
    @Autowired
    private IEmployeeApiClient apiClient;

    /**
     * Retrieves a list of all employees.
     * @return A list of all employee details
     */
    @Override
    public List<EmployeeResponse> getAllEmployees() {
        //log.info("Fetching all employees");
        return apiClient.fetchAllEmployees();
    }

    /**
     * Searches for employees by their name.
     * @param searchName The name to search for.
     * @return A list of employees that match the given name
     */
    @Override
    public List<EmployeeResponse> getEmployeesByName(String searchName) {
        return apiClient.fetchAllEmployees().stream().filter(emp-> emp.getEmployee_name().equalsIgnoreCase(searchName))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an employee by their unique ID.
     * @param id The ID of the employee to be fetched.
     * @return EmployeeResponse if found, or empty Optional if no employee is found with the given ID.
     */
    @Override
    public Optional<EmployeeResponse> getEmployeeById(String id) {
        return apiClient.fetchEmployeeById(id);
    }

    /**
     * Retrieves the highest salary among all employees.
     * @return  The highest salary
     */
    @Override
    public Integer getHighestSalaryOfEmployees() {
        return apiClient.fetchAllEmployees().stream()
                .map(EmployeeResponse::getEmployee_salary)
                .max(Integer::compareTo)
                .orElse(0);
    }

    /**
     * Retrieves the names of the top ten highest earning employees.
     * @return A list of the names of the top ten highest earning employees.
     */
    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        return apiClient.fetchAllEmployees().stream()
                .sorted(Comparator.comparingInt(EmployeeResponse::getEmployee_salary).reversed())
                .limit(10)
                .map(emp-> emp.getEmployee_name() + " - "+emp.getEmployee_salary())
                .collect(Collectors.toList());

    }

    /**
     *  Creates a new employee and add it in the system.
     * @param employeeInput - employee The employee data to be created
     * @return The created containing employee details
     */
    @Override
    public EmployeeResponse createEmployee(Employee employeeInput) {
        return apiClient.postEmployee(employeeInput)
                .orElseThrow(() -> new RuntimeException("Failed to create employee"));

    }

    /**
     * Deletes an employee by their unique ID.
     * @param id - The ID of the employee to be deleted.
     * @return  A confirmation message indicating whether the deletion was successful.
     */
    @Override
    public String deleteEmployeeById(String id) {
        Optional<EmployeeResponse> employee = getEmployeeById(id);
        if (employee.isEmpty()) {
            return "Employee with ID " + id + " not found.";
        }
        String nameToDelete = employee.get().getEmployee_name();

        return apiClient.deleteEmployee(nameToDelete)
                .orElseThrow(() -> new RuntimeException("Employee not found or could not be deleted"));
    }

}
