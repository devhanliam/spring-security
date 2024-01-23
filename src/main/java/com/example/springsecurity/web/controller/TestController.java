package com.example.springsecurity.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @PostMapping("/api/v1/test")
    public ResponseEntity postSomething() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.OK).body("auth name : " + email);
    }
    @PostMapping("/api/v1/admin")
    public ResponseEntity postAdmin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.OK).body("auth name : " + email);
    }

    @PostMapping("/api/v1/main")
    public ResponseEntity postMain() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.OK).body("auth name : " + email);
    }
}
