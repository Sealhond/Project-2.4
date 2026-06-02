package com.example.Project_24.DTOs.Request;

import lombok.Data;

@Data
public class WithdrawalRequest {


    private String senderIban;
    private Double amount;

}
