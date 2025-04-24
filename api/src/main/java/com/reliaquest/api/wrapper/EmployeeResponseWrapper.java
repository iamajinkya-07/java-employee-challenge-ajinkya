package com.reliaquest.api.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeResponse;
import lombok.Data;

import java.util.List;

/**
 * This Wrapper class for employee response containing status and list of employee data.
 * @author - Ajinkya Choudhary
 */
@Data
public class EmployeeResponseWrapper {
    private String status;

    @JsonProperty("data")
    private List<EmployeeResponse> employees;

    public List<EmployeeResponse> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeResponse> employees) {
        this.employees = employees;
    }
}