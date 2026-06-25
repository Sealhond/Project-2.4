package com.example.Project_24.Controllers;

import com.example.Project_24.DTOs.Filters.TransactionFilterParams;
import com.example.Project_24.DTOs.Request.DepositRequest;
import com.example.Project_24.DTOs.Request.TransferRequest;
import com.example.Project_24.DTOs.Request.WithdrawalRequest;
import com.example.Project_24.DTOs.Response.TransactionResponse;
import com.example.Project_24.Models.Transaction;
import com.example.Project_24.Services.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController
{
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService)
    {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasPermission(#request.senderIban, 'account', 'access')")
    public ResponseEntity<TransactionResponse> createTransfer(@RequestBody TransferRequest request) {

        TransactionResponse transactionResponse = transactionService.transfer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
    }

    @PostMapping("/withdrawal")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasPermission(#request.senderIban, 'account', 'access')")
    public ResponseEntity<TransactionResponse> createWithdrawal(@RequestBody WithdrawalRequest request) {

        TransactionResponse transactionResponse = transactionService.withdrawal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);

    }

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasPermission(#request.receiverIban, 'account', 'access')")
    public ResponseEntity<TransactionResponse> createDeposit(@RequestBody DepositRequest request) {
        TransactionResponse transactionResponse = transactionService.deposit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(Pageable pageable)
    {
        return ResponseEntity.ok(transactionService.getAllTransactions(pageable));
    }

    //get per iban (met filter specification enzo)
    @GetMapping("/{iban}")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasPermission(#iban, 'account', 'access')")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByIban(@PathVariable String iban, @ModelAttribute TransactionFilterParams filters, Pageable pageable)
    {
        return ResponseEntity.ok(transactionService.getTransactionsByIban(iban, filters, pageable));
    }






}
