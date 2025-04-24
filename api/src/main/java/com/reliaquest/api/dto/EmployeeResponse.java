package com.reliaquest.api.dto;

import java.util.UUID;

/**
 * This class is used as Employee response from remote API
 * @author - Ajinkya Choudhary
 */
public class EmployeeResponse {
    private UUID id;
    private String employee_name;
    private int employee_salary;
    private int employee_age;
    private String employee_title;
    private String employee_email;

    public UUID getId() {
        return id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public int getEmployee_salary() {
        return employee_salary;
    }

    public int getEmployee_age() {
        return employee_age;
    }

    public String getEmployee_title() {
        return employee_title;
    }
    public String getEmployee_email() {
        return employee_email;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public void setEmployee_salary(int employee_salary) {
        this.employee_salary = employee_salary;
    }

    public void setEmployee_age(int employee_age) {
        this.employee_age = employee_age;
    }

    public void setEmployee_title(String employee_title) {
        this.employee_title = employee_title;
    }

    public void setEmployee_email(String employee_email) {
        this.employee_email = employee_email;
    }

}
