package com.example.Project_24.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Project_24.Models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>
{



}
