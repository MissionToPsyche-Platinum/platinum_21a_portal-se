package com.psyche.portal_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/status")
    public String getStatus() {
        return "The Spring Boot backend is runnning successfully.";
    }
}
