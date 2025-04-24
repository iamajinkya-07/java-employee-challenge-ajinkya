package com.reliaquest.api.dto;


import lombok.Data;
import lombok.Getter;

/**
 * This Class is used as input Employee
 * @author - Ajinkya Choudhary
 */
@Data
@Getter
public class Employee {

    private String id;
    private String name;

    private Integer salary;

    private Integer age;

    private String title;

    private String email;

    public Integer getSalary() {
        return salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
