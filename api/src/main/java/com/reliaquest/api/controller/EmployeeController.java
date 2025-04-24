package com.reliaquest.api.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {


    @GetMapping
    public String getData() {
        System.out.println("Ajinkya Here...");
        return "Ajinkya Here...";
    }
}
