package com.reliaquest.server.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteMockEmployeeInput {

    @NotBlank
    private String name;
    public String getName() {
        return name;
    }
}
