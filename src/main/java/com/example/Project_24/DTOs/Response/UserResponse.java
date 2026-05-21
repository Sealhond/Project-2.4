package com.example.Project_24.DTOs.Response;

import com.example.Project_24.Enums.Role;
import com.example.Project_24.Enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    //use DTO instead of returning the user entity directly is to control what data goes in and out of our api
    //no password field bc never send the password back in the response

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Long bsn;
    private Role role;
    private UserStatus status;
    private Double dailyTransferLimit;
    private Double absoluteTransferLimit;
}
