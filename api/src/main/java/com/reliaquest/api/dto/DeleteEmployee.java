package com.reliaquest.api.dto;

/**
 * Class is used to delete employee by its name from remote API
 * @author - Ajinkya Choudhary
 */
public class DeleteEmployee {

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
