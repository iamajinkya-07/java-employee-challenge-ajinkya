package com.reliaquest.api.controller.impl;


import com.reliaquest.api.controller.IEmployeeController;
import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * This employee controller class which handles all incoming HTTP requests related to
 * employee operations like create, fetch, search, and delete.
 *
 * @author Ajinkya Choudhary
 */
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController implements IEmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    /**
     * Handles GET requests to fetch and return a list of all employee details from the system.
     * @return List of all employees
     */
    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    /**
     * Handles GET requests to get all employee by name
     * @param searchName - name to be searched
     * @return list of all employee with matched name
     */
    @GetMapping("/search/{searchName}")
    public ResponseEntity<List> getEmployeesByNameSearch(String searchName) {
        List<EmployeeResponse> requiredNames = employeeService.getEmployeesByName(searchName);

        if(requiredNames.isEmpty()) {
            String message = "No employee found with name: " + searchName;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonList(message));
        }
        return ResponseEntity.ok(requiredNames);
    }


    /**
     * Handles GET requests to get employee by id
     * @param id - id to be searched
     * @return Employee with matched id
     */
    @GetMapping("/employeeById/{id}")
    public ResponseEntity getEmployeeById(String id) {
        Optional<EmployeeResponse> employee = employeeService.getEmployeeById(id);
        if(employee.isEmpty()) {
            String message = "No employee found with id: " + id;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonList(message));
        }
        return ResponseEntity.ok(employee);
    }

    /**
     * Handles GET requests to get the highest salary
     * @return highest salary
     */
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
       return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployees());
    }

    /**
     * Handles GET requests to get top ten highest earning employee names
     * @return List of ten employee having the highest salary
     */
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> employeeNames = employeeService.getTopTenHighestEarningEmployeeNames();
        return ResponseEntity.ok(employeeNames);
    }

    /**
     * Handles POST requests to add new employee in employee data
     * @param employeeInput - employeeInput which needs to add
     * @return the created employee details as a JSON response
     */
    @PostMapping("/postEmployee")
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody Employee employeeInput) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeInput));

    }

    /**
     * Handles DELETE requests to remove an employee by their ID.
     * @param id the ID of the employee to delete
     * @return a confirmation message after deletion
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.deleteEmployeeById(id));
    }
}
