package com.example.Project_24.Services;

import com.example.Project_24.DTOs.Request.AccountCreateRequest;
import com.example.Project_24.DTOs.Request.AccountUpdateRequest;
import com.example.Project_24.DTOs.Request.BalanceUpdateRequest;
import com.example.Project_24.Enums.AccountStatus;
import com.example.Project_24.Models.Account;
import com.example.Project_24.Repositories.AccountRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.math.BigInteger.ZERO;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public @Nullable List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    public Account createAccount(AccountCreateRequest request) {
        Account account = new Account();

        account.setIBAN(generateIban());
        account.setBalance((double) 0);
        account.setAccountLimit(request.getAccountLimit());
        account.setAccountStatus(AccountStatus.ACTIVE);

        return accountRepository.save(account);
    }

    public @Nullable Account getAccountByIban(String iban) {
        return accountRepository.findById(iban).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public @Nullable Account updateAccount(String iban, AccountUpdateRequest request) {
        Account account = getAccountByIban(iban);

        if (request.getAccountLimit() != null) {
            account.setAccountLimit(request.getAccountLimit());
        }

        if (request.getAccountStatus() != null) {
            account.setAccountStatus(request.getAccountStatus());
        }

        return accountRepository.save(account);
    }

    public void deleteAccount(String iban) {
    }

    public void updateBalance(String iban, BalanceUpdateRequest request) {
        Account account = getAccountByIban(iban);
        Double newBalance = account.getBalance() + request.getAmount();

        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    public @Nullable List<String> getAccountsByUser(String firstName, String lastName) {
        return accountRepository.findByUserFirstNameAndUserLastName(firstName, lastName)
                .stream()
                .map(Account::getIBAN)
                .toList();
    }

    private String generateIban() {
        return "NL69SCAM" + UUID.randomUUID()
                .toString()
                .substring(0, 10);
    }
}
