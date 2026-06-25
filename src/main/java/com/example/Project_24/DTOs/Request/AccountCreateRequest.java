package com.example.Project_24.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.example.Project_24.Enums.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Account limit is required")
    @DecimalMin(value = "-100000.0", message = "Account limit cannot be less than -100,000")
    private Double accountLimit;
}