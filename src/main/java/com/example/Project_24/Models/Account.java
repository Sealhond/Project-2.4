package com.example.Project_24.Models;

import com.example.Project_24.Enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private Long IBAN;

    private Double balance;
    private Double limit;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
}
