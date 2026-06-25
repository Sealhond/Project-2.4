package com.example.Project_24.DTOs.Request;

import com.example.Project_24.Enums.AccountStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest {

    @DecimalMin(value = "-100000.0", message = "Account limit cannot be less than -100,000")
    private Double accountLimit;

    private AccountStatus accountStatus;

    @Size(min = 1, max = 100, message = "Account name must be between 1 and 100 characters")
    private String accountName;
}