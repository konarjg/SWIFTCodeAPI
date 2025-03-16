package com.github.konarjg.SWIFTCodeAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorHandlerController implements org.springframework.boot.web.servlet.error.ErrorController {
    @GetMapping("/error")
    public ResponseEntity<?> handleErrors() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error processing the request!");
    }
}
