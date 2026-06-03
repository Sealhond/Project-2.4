package com.example.Project_24.Services;

import com.example.Project_24.DTOs.Request.AccountCreateRequest;
import com.example.Project_24.DTOs.Request.AccountUpdateRequest;
import com.example.Project_24.DTOs.Request.BalanceUpdateRequest;
import com.example.Project_24.DTOs.Response.AccountResponse;
import com.example.Project_24.DTOs.Response.UserSummaryResponse;
import com.example.Project_24.Enums.AccountStatus;
import com.example.Project_24.Enums.AccountType;
import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.AccountRepository;
import com.example.Project_24.Repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Page<AccountResponse> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public AccountResponse createAccount(
            AccountCreateRequest request
    ) {

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        Account account = new Account();

        account.setIBAN(generateIban());
        account.setAccountName(request.getAccountName());
        account.setBalance(0.0);
        account.setAccountLimit(request.getAccountLimit());
        account.setDateCreated(new Date());

        account.setAccountType(request.getAccountType());
        account.setInterestRate(request.getInterestRate());
        account.setAccountStatus(AccountStatus.CLOSED);
        account.setInterestRate(request.getAccountType() == AccountType.SAVINGS ? 1.5 : 0.0);
        account.setUser(user);

        Account saved = accountRepository.save(account);

        return toResponse(saved);
    }

    public AccountResponse getAccountByIban(String iban) {

        Account account = accountRepository.findById(iban)
                .orElseThrow(() -> new RuntimeException( "Account not found"));

        return toResponse(account);
    }

    public AccountResponse updateAccount(String iban, AccountUpdateRequest request) {

        Account account = accountRepository.findById(iban)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (request.getAccountLimit() != null) {
            account.setAccountLimit(request.getAccountLimit());
        }

        if (request.getAccountStatus() != null) {
            account.setAccountStatus(request.getAccountStatus());
        }

        Account updatedAccount = accountRepository.save(account);

        return toResponse(updatedAccount);
    }

    public void deleteAccount(String iban) {

        if (!accountRepository.existsById(iban)) {
            throw new RuntimeException("Account not found");
        }

        accountRepository.deleteById(iban);
    }

    public void updateBalance(String iban, BalanceUpdateRequest request) {

        Account account = accountRepository.findById(iban)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Double newBalance = account.getBalance() + request.getAmount();

        account.setBalance(newBalance);

        accountRepository.save(account);
    }

    public Page<AccountResponse> getAccountsByUser(String firstName, String lastName, Pageable pageable) {
        String first = firstName == null ? "" : firstName;
        String last = lastName == null ? "" : lastName;

        return accountRepository.findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(
                        first, last, pageable)
                .map(this::toResponse);
    }

    private AccountResponse toResponse(Account account) {

        AccountResponse response = new AccountResponse();

        response.setIBAN(account.getIBAN());
        response.setAccountName(account.getAccountName());
        response.setBalance(account.getBalance());
        response.setAccountLimit(account.getAccountLimit());
        response.setDateCreated(account.getDateCreated());
        response.setInterestRate(account.getInterestRate());
        response.setAccountType(account.getAccountType());
        response.setAccountStatus(account.getAccountStatus());

        if (account.getUser() != null) {

            UserSummaryResponse userResponse = new UserSummaryResponse();

            userResponse.setId(account.getUser().getId());
            userResponse.setFirstName(account.getUser().getFirstName());
            userResponse.setLastName(account.getUser().getLastName());

            response.setUser(userResponse);
        }

        return response;
    }

    public String generateIban() {
        String iban;

        do {
            iban = generateRandomIban();
        } while (accountRepository.existsById(iban));

        return iban;
    }

    private String generateRandomIban() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("NL69SCAM");

        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}