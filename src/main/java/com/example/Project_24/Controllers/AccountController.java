package com.example.Project_24.Controllers;

import com.example.Project_24.DTOs.Request.AccountCreateRequest;
import com.example.Project_24.DTOs.Request.AccountUpdateRequest;
import com.example.Project_24.DTOs.Request.BalanceUpdateRequest;
import com.example.Project_24.DTOs.Response.AccountResponse;
import com.example.Project_24.Services.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<Page<AccountResponse>> getAllAccounts(Pageable pageable) {
        return ResponseEntity.ok(accountService.getAllAccounts(pageable));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount( @RequestBody AccountCreateRequest request) {

        AccountResponse account = accountService.createAccount(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/{iban}")
    public ResponseEntity<AccountResponse> getAccountByIban(@PathVariable String iban) {

        return ResponseEntity.ok(accountService.getAccountByIban(iban));
    }

    @PatchMapping("/{iban}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable String iban, @RequestBody AccountUpdateRequest request) {

        return ResponseEntity.ok(accountService.updateAccount(iban, request));
    }

    @DeleteMapping("/{iban}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String iban) {

        accountService.deleteAccount(iban);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{iban}/balance")
    public ResponseEntity<String> updateBalance(@PathVariable String iban, @RequestBody BalanceUpdateRequest request) {

        accountService.updateBalance(iban, request);

        return ResponseEntity.ok("Balance updated");
    }

    @GetMapping("/user")
    public ResponseEntity<List<String>> getAccountsByUser(@RequestParam String firstName, @RequestParam String lastName) {

        return ResponseEntity.ok(accountService.getAccountsByUser(firstName, lastName));
    }
}