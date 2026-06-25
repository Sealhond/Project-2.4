package com.example.Project_24.Mappers;

import com.example.Project_24.DTOs.Response.AccountResponse;
import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    private final AccountMapper mapper = new AccountMapper();

    @Test
    void mapToAccountResponse_mapsAllFields() {

        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        Account account = new Account();
        account.setIBAN("NL69INHO123");
        account.setAccountName("Checking");
        account.setBalance(500.0);
        account.setUser(user);

        AccountResponse response = mapper.mapToAccountResponse(account);

        assertEquals("NL69INHO123", response.getIBAN());
        assertEquals("Checking", response.getAccountName());

        assertNotNull(response.getUser());
        assertEquals(1L, response.getUser().getId());
        assertEquals("John", response.getUser().getFirstName());
    }

    @Test
    void mapToAccountResponse_handlesNullUser() {

        Account account = new Account();
        account.setIBAN("NL69INHO123");

        AccountResponse response = mapper.mapToAccountResponse(account);

        assertNull(response.getUser());
    }
}