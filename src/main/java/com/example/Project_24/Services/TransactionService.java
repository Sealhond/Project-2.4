package com.example.Project_24.Services;

import com.example.Project_24.DTOs.Filters.TransactionFilterParams;
import com.example.Project_24.DTOs.Request.DepositRequest;
import com.example.Project_24.DTOs.Request.TransferRequest;
import com.example.Project_24.DTOs.Request.WithdrawalRequest;
import com.example.Project_24.DTOs.Response.AccountResponse;
import com.example.Project_24.DTOs.Response.TransactionResponse;
import com.example.Project_24.Enums.TransactionType;
import com.example.Project_24.Exception.AccountNotFoundException;
import com.example.Project_24.Exception.TransactionNotAllowedException;
import com.example.Project_24.Exception.UserNotFoundException;
import com.example.Project_24.Mappers.TransactionMapper;
import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.Transaction;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.AccountRepository;
import com.example.Project_24.Repositories.TransactionRepository;
import com.example.Project_24.Repositories.UserRepository;
import com.example.Project_24.Specifications.TransactionSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Transactional
    public TransactionResponse transfer(TransferRequest request) {

        //find sender account
        Account sender = accountRepository.findById(request.getSenderIban())
                .orElseThrow(() -> new AccountNotFoundException(request.getSenderIban()));

        //find receiver account
        Account receiver = accountRepository.findById(request.getReceiverIban())
                .orElseThrow(() -> new AccountNotFoundException(request.getReceiverIban()));

        //check limits
        checkAbsoluteLimit(sender, request.getAmount());
        checkDailyLimit(sender, request.getAmount());

        //deduct from sender balance
        sender.setBalance(sender.getBalance() - request.getAmount());

        //add to receiver balance
        receiver.setBalance(receiver.getBalance() + request.getAmount());

        //save both accounts
        accountRepository.save(receiver);
        accountRepository.save(sender);

        //save transaction
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setInitiatedBy(getCurrentUser());

        Transaction savedTransaction = transactionRepository.save(transaction);

        //return transaction response
        return transactionMapper.mapToTransactionResponse(savedTransaction);
    }


    @Transactional
    public TransactionResponse deposit(DepositRequest request) {

        //find receiver account
        Account receiver = accountRepository.findById(request.getReceiverIban())
                .orElseThrow(() -> new AccountNotFoundException(request.getReceiverIban()));

        //check limits
        checkAbsoluteLimit(receiver, request.getAmount());
        checkDailyLimit(receiver, request.getAmount());

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


    @Transactional
    public TransactionResponse withdrawal(WithdrawalRequest request) {

        //find sender account
        Account sender = accountRepository.findById(request.getSenderIban())
                .orElseThrow(() -> new AccountNotFoundException(request.getSenderIban()));

        //check limits
        checkAbsoluteLimit(sender, request.getAmount());
        checkDailyLimit(sender, request.getAmount());

        //subtract from balance
        sender.setBalance(sender.getBalance() - request.getAmount());

        //save account
        accountRepository.save(sender);

        Transaction transaction = new Transaction();

        transaction.setSender(sender);
        transaction.setReceiver(null);
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setInitiatedBy(getCurrentUser());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.mapToTransactionResponse(savedTransaction);

    }

    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map(transactionMapper::mapToTransactionResponse);
    }

    //GET /transactions/{iban} + filtering
    public Page<TransactionResponse> getTransactionsByIban(String iban, TransactionFilterParams filters, Pageable pageable) {

        Specification<Transaction> spec = Specification.where(TransactionSpecification.hasIban(iban));

        if (filters != null) {
            if (filters.getStartDate() != null) {
                spec = spec.and(TransactionSpecification.afterDate(filters.getStartDate()));
            }

            if (filters.getEndDate() != null) {
                spec = spec.and(TransactionSpecification.beforeDate(filters.getEndDate()));
            }

            if (filters.getMinAmount() != null) {
                spec = spec.and(TransactionSpecification.minAmount(filters.getMinAmount()));
            }

            if (filters.getMaxAmount() != null) {
                spec = spec.and(TransactionSpecification.maxAmount(filters.getMaxAmount()));
            }

            if (filters.getOtherIban() != null) {
                spec = spec.and(TransactionSpecification.otherIban(filters.getOtherIban()));
            }
        }

        return transactionRepository.findAll(spec, pageable)
                .map(transactionMapper::mapToTransactionResponse);

    }



    //HELPER methods -----------------------------------------------
    private User getCurrentUser()
    {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User initiatedBy = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());

        return initiatedBy;
    }

    private void checkAbsoluteLimit(Account sender, Double amount) {
        if (sender.getBalance() - amount < sender.getAccountLimit()) {
            throw new TransactionNotAllowedException("Transaction would exceed absolute limit");
        }
    }

    private void checkDailyLimit(Account sender, Double amount) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        Double amountTransferredToday = transactionRepository.sumTodayByUserId(
                sender.getUser().getId(), startOfDay
        );
        if (amountTransferredToday + amount > sender.getUser().getDailyTransferLimit()) {
            throw new TransactionNotAllowedException("Daily transfer limit exceeded");
        }
    }


}
