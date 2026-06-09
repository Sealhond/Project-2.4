package com.example.Project_24.DTOs.Response;

import com.example.Project_24.Enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private Long id;
    private String senderIban;
    private String receiverIban;
    private Double amount;
    private LocalDateTime timestamp;
    private TransactionType type;
    private UserSummaryResponse initiatedBy;
}
