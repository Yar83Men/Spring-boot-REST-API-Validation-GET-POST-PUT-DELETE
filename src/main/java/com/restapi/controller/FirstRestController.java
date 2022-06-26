package com.restapi.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FirstRestController {
    @GetMapping("/first")
    public String first() {
        return "First method";
    }
}
