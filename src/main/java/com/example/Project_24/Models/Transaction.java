package com.example.Project_24.Models;

import com.example.Project_24.Enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_iban")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_iban")
    private Account receiver;

    private Double amount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "initiated_by_userid")
    private User initiatedBy;

    //set timestamp when new transaction
    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

}
