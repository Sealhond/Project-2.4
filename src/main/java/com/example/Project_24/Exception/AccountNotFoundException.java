package com.example.Project_24.Exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String iban) {
        super("Account not found: " + iban);
    }
}