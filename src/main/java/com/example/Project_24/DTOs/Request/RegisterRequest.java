package com.example.Project_24.DTOs.Request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 128, message = "First name must be at most 128 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 128, message = "Last name must be at most 128 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,13}$", message = "Phone number must be between 10 - 13 digits")
    private String phoneNumber;

    @NotBlank(message = "BSN is required")
    @Pattern(regexp = "^[0-9]{8}$", message = "BSN must be 8 digits")
    private String bsn;
}
