package com.kopaev.SecuritySample.controllers;

import com.kopaev.SecuritySample.DTO.Response.ErrorResponse;
import com.kopaev.SecuritySample.DTO.Request.LoginRequest;
import com.kopaev.SecuritySample.DTO.Request.RegisterRequest;
import com.kopaev.SecuritySample.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            var response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            var errorResponse = ErrorResponse.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            var response = service.login(request);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            var errorResponse = ErrorResponse.builder()
                    .message("Wrong username or password")
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            var errorResponse = ErrorResponse.builder()
                    .message(" ")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/checkToken")
    public ResponseEntity<?> checkToken(@RequestParam("token") String token){
        if (service.checkToken(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


}
