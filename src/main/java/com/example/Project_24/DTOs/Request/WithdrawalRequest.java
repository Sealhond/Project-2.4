package com.example.Project_24.DTOs.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WithdrawalRequest {

    @NotBlank
    private String senderIban;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private Double amount;

}
