package com.example.Project_24.DTOs.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DepositRequest {

    @NotBlank
    private String receiverIban;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @DecimalMax(value = "3000.0", message = "Cannot deposit more than €3,000 per day")
    private Double amount;

}
