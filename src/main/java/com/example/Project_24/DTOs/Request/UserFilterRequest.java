package com.example.Project_24.DTOs.Request;

import com.example.Project_24.Enums.Role;
import com.example.Project_24.Enums.UserStatus;

public class UserFilterRequest
{
    private UserStatus status;
    private Role role;
    private String search; // zoekt op naam/email
}

