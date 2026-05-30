package com.example.Project_24.DTOs.Response;

import com.example.Project_24.Enums.AccountStatus;
import com.example.Project_24.Enums.AccountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AccountResponse {
    private String IBAN;
    private String accountName;
    private Double balance;
    private Double accountLimit;
    private Date dateCreated;
    private Double interestRate;
    private AccountType accountType;
    private AccountStatus accountStatus;

    private UserSummaryResponse user;
}
