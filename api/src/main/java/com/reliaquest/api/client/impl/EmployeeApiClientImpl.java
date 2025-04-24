package com.reliaquest.api.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.client.IEmployeeApiClient;
import com.reliaquest.api.dto.DeleteEmployee;
import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.wrapper.EmployeeResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This client class is used to handle communication with the external employee data service.
 * @author - Ajinkya Choudhary
 */
@Component
public class EmployeeApiClientImpl implements IEmployeeApiClient {

    private static final Logger log = LoggerFactory.getLogger(EmployeeApiClientImpl.class);
    private static final String BASE_URL = "http://localhost:8112/api/v1/employee";
    private final RestTemplate restTemplate;

    public EmployeeApiClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Method is used to fetches all employees from the remote employee API.
     * @return a list of EmployeeResponse objects; returns an empty list if the API call or parsing fails
     */
    @Override
    public List<EmployeeResponse> fetchAllEmployees() {
        try {
            String response = restTemplate.getForObject(BASE_URL, String.class);
            ObjectMapper mapper = new ObjectMapper();
            EmployeeResponseWrapper employeeResponseWrapper = mapper.readValue(response,EmployeeResponseWrapper.class);
            return employeeResponseWrapper.getEmployees();

        } catch (Exception e) {
            log.error("Error fetching employees", e);
            return List.of();
        }
    }

    /**
     * Fetches a single employee by ID from the remote employee API.
     * @param id - id the UUID of the employee to retrieve
     * @return EmployeeResponse if found; otherwise, Optional.empty()
     */
    @Override
    public Optional<EmployeeResponse> fetchEmployeeById(String id) {
        try {
            String response = restTemplate.getForObject(BASE_URL + "/" + id, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);

            // Extract the "data" node from the JSON response
            JsonNode dataNode = rootNode.get("data");

            // Convert the "data" node directly into an Employee object
            EmployeeResponse employee = mapper.treeToValue(dataNode, EmployeeResponse.class);

            return Optional.ofNullable(employee);
        } catch (Exception e) {
            log.warn("Employee not found: {}", id);
            return Optional.empty();
        }
    }

    /**
     * Sends a POST request to create a new employee in the remote system.
     * @param employeeInput - Employee object containing new employee details
     * @return created EmployeeResponse if successful; otherwise, Optional.empty()
     */
    @Override
    public Optional<EmployeeResponse> postEmployee(Employee employeeInput) {
        try {
            Map<String,Object> map = restTemplate.postForObject(BASE_URL, employeeInput, Map.class);
            Object data = map.get("data");
            ObjectMapper objectMapper = new ObjectMapper();
            EmployeeResponse employee = objectMapper.convertValue(data, EmployeeResponse.class);
            return Optional.ofNullable(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Sends a DELETE request to remove an employee by name from the remote API.
     * @param employeeName - name of the employee to be deleted
     * @return success message if deletion succeeds; otherwise, Optional.empty()
     */
    @Override
    public Optional<String> deleteEmployee(String employeeName) {
        try {
            DeleteEmployee deleteEmployee = new DeleteEmployee();
            deleteEmployee.setName(employeeName);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<DeleteEmployee> request = new HttpEntity<>(deleteEmployee, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    BASE_URL,
                    HttpMethod.DELETE,
                    request,
                    String.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.of("Employee with name " + employeeName  + " deleted successfully.");
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
