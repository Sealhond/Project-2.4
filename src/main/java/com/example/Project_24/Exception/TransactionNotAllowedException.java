package com.example.Project_24.Exception;

public class TransactionNotAllowedException extends RuntimeException {
    public TransactionNotAllowedException(String message) {
        super(message);
    }
}
