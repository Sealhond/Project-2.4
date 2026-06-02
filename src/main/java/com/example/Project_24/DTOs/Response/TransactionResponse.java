package com.example.Project_24.DTOs.Response;

import com.example.Project_24.Enums.TransactionType;
import java.time.LocalDateTime;

public class TransactionResponse {
    private Long id;
    private String senderIban;
    private String receiverIban;
    private Double amount;
    private LocalDateTime timestamp;
    private TransactionType type;
    private String initiatedByEmail;
}
