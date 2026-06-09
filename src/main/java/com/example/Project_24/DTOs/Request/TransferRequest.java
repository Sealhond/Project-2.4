package com.example.Project_24.DTOs.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransferRequest {

    @NotBlank
    private String receiverIban;

    @NotBlank
    private String senderIban;

    @NotNull
    private Double amount;
}
