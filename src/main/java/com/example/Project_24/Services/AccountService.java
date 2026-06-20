package com.example.Project_24.Services;

import com.example.Project_24.DTOs.Request.AccountCreateRequest;
import com.example.Project_24.DTOs.Request.AccountUpdateRequest;
import com.example.Project_24.DTOs.Response.AccountResponse;
import com.example.Project_24.DTOs.Response.IbanResponse;
import com.example.Project_24.DTOs.Response.UserSummaryResponse;
import com.example.Project_24.Enums.AccountStatus;
import com.example.Project_24.Enums.AccountType;
import com.example.Project_24.Exception.AccountNotFoundException;
import com.example.Project_24.Exception.UserNotFoundException;
import com.example.Project_24.Mappers.AccountMapper;
import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.AccountRepository;
import com.example.Project_24.Repositories.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
    }

    public Page<AccountResponse> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable)
                .map(accountMapper::mapToAccountResponse);
    }

    public AccountResponse createAccount(AccountCreateRequest request) {

        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();

        account.setIBAN(generateIban());
        account.setAccountName(request.getAccountName());
        account.setBalance(0.0);
        account.setAccountLimit(request.getAccountLimit());
        account.setDateCreated(new Date());

        account.setAccountType(request.getAccountType());
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setInterestRate(request.getAccountType() == AccountType.SAVINGS ? 1.5 : 0.0);
        account.setUser(user);

        Account saved = accountRepository.save(account);

        return accountMapper.mapToAccountResponse(saved);
    }

    public AccountResponse getAccountByIban(String iban) {

        Account account = accountRepository.findById(iban).orElseThrow(() -> new AccountNotFoundException(iban));

        return accountMapper.mapToAccountResponse(account);
    }

    public AccountResponse updateAccount(String iban, AccountUpdateRequest request) {
        Account account = accountRepository.findById(iban).orElseThrow(() -> new AccountNotFoundException(iban));

        if (request.getAccountName() != null) {
            account.setAccountName(request.getAccountName());
        }
        if (request.getAccountLimit() != null) {
            account.setAccountLimit(request.getAccountLimit());
        }
        if (request.getAccountStatus() != null) {
            account.setAccountStatus(request.getAccountStatus());
        }

        Account updatedAccount = accountRepository.save(account);
        return accountMapper.mapToAccountResponse(updatedAccount);
    }

    public void deleteAccount(String iban) {
        Account account = accountRepository.findById(iban).orElseThrow(() -> new AccountNotFoundException(iban));

        account.setAccountStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
    }

    public void updateBalance(String iban, Double amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        Account account = accountRepository.findById(iban).orElseThrow(() -> new AccountNotFoundException(iban));

        Double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);

        accountRepository.save(account);
    }

    public Page<AccountResponse> getAccountsByUserFirstNameAndLastName(String firstName, String lastName, Pageable pageable) {
        String first = firstName == null ? "" : firstName;
        String last = lastName == null ? "" : lastName;

        return accountRepository.findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(first, last, pageable)
                .map(accountMapper::mapToAccountResponse);
    }

    public List<AccountResponse> getAccountsByCurrentUser() {
        Long userId = this.getCurrentUserId();
        return accountRepository.findByUserId(userId).stream().map(accountMapper::mapToAccountResponse).toList();
    }

    public Page<IbanResponse> getIbansByUserFirstNameAndLastName(String firstName, String lastName, Pageable pageable) {
        String first = firstName == null ? "" : firstName;
        String last = lastName == null ? "" : lastName;

        return accountRepository.searchIbans(first, last, AccountType.CHECKING, pageable);
    }

    // Helper methods

    public String generateIban() {
        String iban;

        do {
            iban = generateRandomIban();
        } while (accountRepository.existsById(iban));

        return iban;
    }

    private String generateRandomIban() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder("NL69INHO");

        for (int i = 0; i < 10; i++) {
            builder.append(random.nextInt(10));
        }

        return builder.toString();
    }

    private Long getCurrentUserId()
    {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User initiatedBy = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        return initiatedBy.getId();
    }
}