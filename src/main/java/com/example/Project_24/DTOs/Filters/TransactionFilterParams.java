package com.example.Project_24.DTOs.Filters;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionFilterParams {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double minAmount;
    private Double maxAmount;
    private String otherIban;
}
