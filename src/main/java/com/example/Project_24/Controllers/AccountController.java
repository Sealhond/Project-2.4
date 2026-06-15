package com.example.Project_24.Controllers;

import com.example.Project_24.DTOs.Request.AccountCreateRequest;
import com.example.Project_24.DTOs.Request.AccountUpdateRequest;
import com.example.Project_24.DTOs.Response.AccountResponse;
import com.example.Project_24.DTOs.Response.IbanResponse;
import com.example.Project_24.Services.AccountService;
import com.example.Project_24.Services.JwtService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final JwtService jwtService;

    public AccountController(AccountService accountService, JwtService jwtService) {
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
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
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable String iban, @RequestBody AccountUpdateRequest request) {

        return ResponseEntity.ok(accountService.updateAccount(iban, request));
    }

    @DeleteMapping("/{iban}")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Void> deleteAccount(@PathVariable String iban) {

        accountService.deleteAccount(iban);

        return ResponseEntity.noContent().build();
    }

    // I kept this to be used by employees
    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Page<AccountResponse>> getAccountsByUser(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                accountService.getAccountsByUserFirstNameAndLastName(firstName, lastName, pageable)
        );
    }

    // The actual endpoint for customers to get Iban by User
    @GetMapping("/userIban")
    public ResponseEntity<Page<IbanResponse>> getIbansByUser(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                accountService.getIbansByUserFirstNameAndLastName(firstName, lastName, pageable)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<List<AccountResponse>> getMyAccounts(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);

        Long userId = jwtService.extractUserId(token);

        return ResponseEntity.ok(
                accountService.getAccountsByUserId(userId)
        );
    }
}