package com.github.konarjg.SWIFTCodeAPI.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping(path = "/token", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getToken(@RequestBody Map<String, Object> claims) {
        try {
            String jwtToken = jwtService.generateJWT(claims);
            return ResponseEntity.ok(jwtToken);
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need to include a valid API key to gain admin rights!");
        }
    }
}
