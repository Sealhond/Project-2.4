package com.example.Project_24.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Project_24.Models.Transaction;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction>
{
    List<Transaction> findBySenderIBANOrReceiverIBAN(String senderIban, String receiverIban);



}
