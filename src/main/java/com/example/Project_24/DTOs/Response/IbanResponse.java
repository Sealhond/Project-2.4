package com.example.Project_24.DTOs.Response;

import com.example.Project_24.Enums.AccountStatus;
import com.example.Project_24.Enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IbanResponse {
    private String IBAN;

    private UserSummaryResponse user;
}
