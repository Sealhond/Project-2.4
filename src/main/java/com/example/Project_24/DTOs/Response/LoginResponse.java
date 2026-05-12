package com.example.Project_24.DTOs.Response;

import com.example.Project_24.Enums.Role;
import com.example.Project_24.Enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long userId;
    private Role role;
    private UserStatus status;
}