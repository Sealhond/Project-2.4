package com.example.Project_24.Services;

import com.example.Project_24.DTOs.Request.AccountCreateRequest;
import com.example.Project_24.DTOs.Request.AccountUpdateRequest;
import com.example.Project_24.DTOs.Response.AccountResponse;
import com.example.Project_24.DTOs.Response.IbanResponse;
import com.example.Project_24.Enums.AccountStatus;
import com.example.Project_24.Enums.AccountType;
import com.example.Project_24.Exception.AccountNotFoundException;
import com.example.Project_24.Exception.UserNotFoundException;
import com.example.Project_24.Mappers.AccountMapper;
import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.AccountRepository;
import com.example.Project_24.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    private Account account;
    private User user;
    private AccountResponse response;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(69L);

        account = new Account();
        account.setIBAN("NL69INHO1234567890");
        account.setAccountName("Checking");
        account.setBalance(1000.0);
        account.setAccountLimit(-500.0);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setAccountType(AccountType.CHECKING);
        account.setUser(user);

        response = new AccountResponse();
        response.setIBAN(account.getIBAN());
    }

    @Test
    void createAccount_createsAndReturnsAccount() {
        AccountCreateRequest request = new AccountCreateRequest();
        request.setUserId(69L);
        request.setAccountName("Checking");
        request.setAccountType(AccountType.CHECKING);
        request.setAccountLimit(-500.0);

        when(userRepository.findById(69L)).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.mapToAccountResponse(account)).thenReturn(response);

        AccountResponse result = accountService.createAccount(request);
        assertEquals(response, result);

        verify(userRepository).findById(69L);
        verify(accountRepository).save(any(Account.class));
        verify(accountMapper).mapToAccountResponse(account);
    }

    @Test
    void createAccount_throwsWhenUserNotFound() {
        AccountCreateRequest request = new AccountCreateRequest();
        request.setUserId(99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountService.createAccount(request));

        verify(accountRepository, never()).save(any());
    }

    @Test
    void getAccountByIban_returnsAccount() {
        when(accountRepository.findById(account.getIBAN())).thenReturn(Optional.of(account));
        when(accountMapper.mapToAccountResponse(account)).thenReturn(response);

        AccountResponse result = accountService.getAccountByIban(account.getIBAN());
        assertEquals(response, result);

        verify(accountRepository).findById(account.getIBAN());
    }

    @Test
    void getAccountByIban_throwsWhenNotFound() {
        when(accountRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountByIban("INVALID"));
    }

    @Test
    void updateAccount_updatesFields() {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setAccountName("Updated Name");
        request.setAccountLimit(-1000.0);
        request.setAccountStatus(AccountStatus.CLOSED);

        when(accountRepository.findById(account.getIBAN())).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.mapToAccountResponse(account)).thenReturn(response);

        AccountResponse result = accountService.updateAccount(account.getIBAN(), request);

        assertEquals(response, result);
        assertEquals("Updated Name", account.getAccountName());
        assertEquals(-1000.0, account.getAccountLimit());
        assertEquals(AccountStatus.CLOSED, account.getAccountStatus());

        verify(accountRepository).save(account);
    }

    @Test
    void updateAccount_throwsWhenNotFound() {
        when(accountRepository.findById("INVALID")).thenReturn(Optional.empty());

        AccountUpdateRequest request = new AccountUpdateRequest();

        assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount("INVALID", request));
    }

    @Test
    void deleteAccount_setsStatusToClosed() {
        when(accountRepository.findById(account.getIBAN())).thenReturn(Optional.of(account));

        accountService.deleteAccount(account.getIBAN());

        assertEquals(AccountStatus.CLOSED, account.getAccountStatus());

        verify(accountRepository).save(account);
    }

    @Test
    void deleteAccount_throwsWhenNotFound() {
        when(accountRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount("INVALID"));
    }

    @Test
    void updateBalance_updatesBalance() {
        when(accountRepository.findById(account.getIBAN())).thenReturn(Optional.of(account));

        accountService.updateBalance(account.getIBAN(), 250.0);

        assertEquals(1250.0, account.getBalance());

        verify(accountRepository).save(account);
    }

    @Test
    void updateBalance_throwsWhenAmountNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> accountService.updateBalance(account.getIBAN(), null));

        assertEquals(
            "Amount cannot be null",
            exception.getMessage()
        );

        verify(accountRepository, never()).findById(any());
    }

    @Test
    void updateBalance_throwsWhenAccountNotFound() {
        when(accountRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.updateBalance("INVALID", 100.0));
    }

    @Test
    void generateIban_returnsUniqueIban() {
        when(accountRepository.existsById(anyString())).thenReturn(false);

        String iban = accountService.generateIban();

        assertNotNull(iban);
        assertTrue(iban.startsWith("NL69INHO"));
        assertEquals(18, iban.length());
    }

    @Test
    void getAllAccounts_returnsMappedAccounts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Account> accountPage = new PageImpl<>(java.util.List.of(account));

        when(accountRepository.findAll(pageable)).thenReturn(accountPage);
        when(accountMapper.mapToAccountResponse(account)).thenReturn(response);

        Page<AccountResponse> result = accountService.getAllAccounts(pageable);

        assertEquals(1, result.getTotalElements());

        verify(accountRepository).findAll(pageable);
        verify(accountMapper).mapToAccountResponse(account);
    }

    @Test
    void getAccountsByUserFirstNameAndLastName_returnsMappedAccounts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Account> accountPage = new PageImpl<>(java.util.List.of(account));

        when(accountRepository.findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(
                "John", "Doe", pageable))
                .thenReturn(accountPage);

        when(accountMapper.mapToAccountResponse(account)).thenReturn(response);

        Page<AccountResponse> result = accountService.getAccountsByUserFirstNameAndLastName(
                "John", "Doe", pageable);

        assertEquals(1, result.getContent().size());

        verify(accountRepository).findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(
                "John", "Doe", pageable);
    }

    @Test
    void getAccountsByUserFirstNameAndLastName_handlesNullParameters() {
        Pageable pageable = PageRequest.of(0, 10);

        when(accountRepository.findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(
                "", "", pageable))
                .thenReturn(Page.empty());

        accountService.getAccountsByUserFirstNameAndLastName(null, null, pageable);

        verify(accountRepository).findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(
                "", "", pageable);
    }

    @Test
    void getIbansByUserFirstNameAndLastName_returnsIbans() {
        Pageable pageable = PageRequest.of(0, 10);

        IbanResponse ibanResponse = new IbanResponse();
        ibanResponse.setIBAN(account.getIBAN());

        Page<IbanResponse> page = new PageImpl<>(java.util.List.of(ibanResponse));

        when(accountRepository.searchIbans("John", "Doe", AccountType.CHECKING, pageable))
                .thenReturn(page);

        Page<IbanResponse> result =
                accountService.getIbansByUserFirstNameAndLastName("John", "Doe", pageable);

        assertEquals(1, result.getContent().size());

        verify(accountRepository).searchIbans("John", "Doe", AccountType.CHECKING, pageable);
    }

    @Test
    void getAccountsByCurrentUser_returnsAccounts() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("test@test.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(accountRepository.findByUserId(user.getId())).thenReturn(java.util.List.of(account));
        when(accountMapper.mapToAccountResponse(account)).thenReturn(response);

        var result = accountService.getAccountsByCurrentUser();

        assertEquals(1, result.size());

        verify(userRepository).findByEmail("test@test.com");
        verify(accountRepository).findByUserId(user.getId());

        SecurityContextHolder.clearContext();
    }

    @Test
    void getAccountsByCurrentUser_throwsWhenUserNotFound() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("missing@test.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> accountService.getAccountsByCurrentUser()
        );

        SecurityContextHolder.clearContext();
    }
}