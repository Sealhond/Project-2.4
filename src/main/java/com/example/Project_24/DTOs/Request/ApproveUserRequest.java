package com.example.Project_24.DTOs.Request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApproveUserRequest {

    @NotNull(message = "Daily transfer limit is required")
    @DecimalMin(value = "0.0", message = "Daily transfer limit cannot be less than 0")
    @DecimalMax(value = "10000.0", message = "Daily transfer limit cannot be more than 10,000")
    private Double dailyTransferLimit;

    @NotNull(message = "Checking account limit is required")
    @DecimalMin(value = "-100000.0", message = "Checking account limit cannot be less than -100,000")
    private Double checkingAccountLimit;

    @NotNull(message = "Savings account limit is required")
    @DecimalMin(value = "-100000.0", message = "Savings account limit cannot be less than -100,000")
    private Double savingsAccountLimit;
}
