package com.example.Project_24.Controllers;

import com.example.Project_24.DTOs.Request.LoginRequest;
import com.example.Project_24.DTOs.Response.LoginResponse;
import com.example.Project_24.Services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController (AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        //@RequestBody tells spring to take json body & map it to LoginRequest

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);

    }

    //TODO: /register
}
