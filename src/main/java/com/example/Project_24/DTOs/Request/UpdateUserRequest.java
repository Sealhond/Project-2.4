package com.example.Project_24.DTOs.Request;

import com.example.Project_24.Enums.UserStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(max = 128, message = "First name must be at most 128 characters")
    private String firstName;

    @Size(max = 128, message = "Last name must be at most 128 characters")
    private String lastName;

    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^[0-9]{10,13}$", message = "Phone number must contain only digits")
    private String phoneNumber;

    @Pattern(regexp = "^[0-9]{8}$", message = "BSN must be 8 digits")
    private String bsn;

    private UserStatus status;

    @DecimalMin(value = "0.0", message = "Daily transfer limit cannot be less than 0")
    @DecimalMax(value = "50000.0", message = "Daily transfer limit cannot be more than 50.000")
    private Double dailyTransferLimit;

//    @DecimalMin(value = "0.0", message = "Absolute transfer limit cannot be less than 0")
//    @DecimalMax(value = "50000.0", message = "Absolute transfer limit cannot be more than 10,000")
//    private Double absoluteTransferLimit;
}
