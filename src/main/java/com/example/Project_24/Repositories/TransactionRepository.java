package com.example.Project_24.Repositories;

import com.example.Project_24.Enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Project_24.Models.Transaction;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction>
{
    List<Transaction> findBySenderIBANOrReceiverIBAN(String senderIban, String receiverIban);

    //som van alle transacties van 1 user op 1 dag, voor daily transfer limit
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.sender.user.id = :userId " +
            "AND t.timestamp >= :startOfDay")
    Double sumTodayByUserId(@Param("userId") Long userId,
                            @Param("startOfDay") LocalDateTime startOfDay);


    //som van alle deposits op 1 dag, mag niet > 3000 zijn
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.receiver.user.id = :userId " +
            "AND t.type = :type " +
            "AND t.timestamp >= :startOfDay")
    Double sumDepositsTodayByUserId(@Param("userId") Long userId,
                                    @Param("startOfDay") LocalDateTime startOfDay,
                                    @Param("type") TransactionType type);
}
