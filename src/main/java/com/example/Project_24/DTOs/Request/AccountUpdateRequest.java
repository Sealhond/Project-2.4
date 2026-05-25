package com.example.Project_24.DTOs.Request;

import com.example.Project_24.Enums.AccountStatus;
import lombok.Data;

@Data
public class AccountUpdateRequest {

    private Double accountLimit;
    private AccountStatus accountStatus;
}