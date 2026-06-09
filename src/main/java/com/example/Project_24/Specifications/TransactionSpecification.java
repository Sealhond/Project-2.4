package com.example.Project_24.Specifications;

import com.example.Project_24.Models.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionSpecification {

    //has iban
    public static Specification<Transaction> hasIban(String iban) {
        return (root, criteriaQuery, cb) ->
                cb.or(
                        cb.equal(root.get("sender").get("IBAN"), iban),
                        cb.equal(root.get("receiver").get("IBAN"), iban)
                );

    }

    //after date
    public static Specification<Transaction> afterDate(LocalDateTime date) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("timestamp"), date);
    }

    //before date
    public static Specification<Transaction> beforeDate(LocalDateTime date) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("timestamp"), date);
    }

    //min amount
    public static Specification<Transaction> minAmount(Double amount) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("amount"), amount);
    }

    //max amount
    public static Specification<Transaction> maxAmount(Double amount) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("amount"), amount);
    }

    //other iban
    public static Specification<Transaction> otherIban(String iban) {
        return (root, query, cb) ->
                cb.or(
                        cb.equal(root.get("sender").get("IBAN"), iban),
                        cb.equal(root.get("receiver").get("IBAN"), iban)
                );
    }
}
