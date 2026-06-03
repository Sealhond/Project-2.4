package com.example.Project_24.Services;

import com.example.Project_24.DTOs.Request.DepositRequest;
import com.example.Project_24.DTOs.Request.TransferRequest;
import com.example.Project_24.DTOs.Request.WithdrawalRequest;
import com.example.Project_24.DTOs.Response.TransactionResponse;
import com.example.Project_24.Enums.TransactionType;
import com.example.Project_24.Mappers.TransactionMapper;
import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.Transaction;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.AccountRepository;
import com.example.Project_24.Repositories.TransactionRepository;
import com.example.Project_24.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

//@Transactional annotation hier ergens gebruiken

@Service
public class TransactionService
{

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService (TransactionRepository transactionRepository, AccountRepository accountRepository, UserRepository userRepository, TransactionMapper transactionMapper)
    {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
    }

    //POST /transactions/transfer
    public TransactionResponse transfer(TransferRequest request) {

        //find sender account
        //find receiver account
        //check if sender has enough limit (check absolute limit)
        //check daily limit
        //deduct from sender balance
        //add to receiver balance
        //save both accounts
        //save transaction
        //return transaction response

        return null;

    }

    //POST /transactions/deposit
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {

        //find receiver account
        Account receiver = accountRepository.findById(request.getReceiverIban())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        //add to receiver balance
        receiver.setBalance(receiver.getBalance() + request.getAmount());

        //save account
        accountRepository.save(receiver);

        //save transaction
        Transaction transaction = new Transaction();

        transaction.setReceiver(receiver);
        transaction.setSender(null);
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setInitiatedBy(getCurrentUser());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.mapToTransactionResponse(savedTransaction);
    }

    //POST /transactions/withdrawal
    public TransactionResponse withdrawal(WithdrawalRequest request) {

        //find sender account
        //check balance
        //deduct sender balance
        //save account

        return null;
    }

    //GET /transactions ->getallll
    public List<TransactionResponse> getAllTransactions() {

        return null;
    }


    //GET /transactions/{iban} + filtering
    //public List<TransactionResponse> getTransactionsByIban(String iban, TransactionFilterParams filters) {}


    private User getCurrentUser()
    {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User initiatedBy = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return initiatedBy;
    }


}
