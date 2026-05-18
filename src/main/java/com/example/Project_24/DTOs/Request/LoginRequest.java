package com.example.Project_24.DTOs.Request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}