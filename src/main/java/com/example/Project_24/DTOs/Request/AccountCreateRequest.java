package com.example.Project_24.DTOs.Request;

import lombok.Data;
import com.example.Project_24.Enums.AccountType;

@Data
public class AccountCreateRequest {

    private Long userId;

    private String accountName;

    private AccountType accountType;

    private Double accountLimit;
}