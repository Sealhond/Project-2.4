package com.example.Project_24.Controllers;

import com.example.Project_24.Enums.AccountStatus;
import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.AccountRepository;
import com.example.Project_24.Repositories.UserRepository;
import org.springframework.security.test.context.support.WithMockUser;
import tools.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void createAccount_createsAndReturnsAccount() throws Exception {

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@test.com");

        User savedUser = userRepository.save(user);

        Map<String, Object> request = new HashMap<>();
        request.put("userId", savedUser.getId());
        request.put("accountName", "Main Account");
        request.put("accountType", "CHECKING");
        request.put("accountLimit", -500.0);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountName").value("Main Account"))
                .andExpect(jsonPath("$.accountType").value("CHECKING"))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getAccountByIban_returnsAccount() throws Exception {

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        userRepository.save(user);

        Account account = new Account();
        account.setIBAN("NL69INHO1234567890");
        account.setAccountName("Checking");
        account.setBalance(100.0);
        account.setUser(user);

        accountRepository.save(account);

        mockMvc.perform(get("/accounts/{iban}", account.getIBAN()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.IBAN").value(account.getIBAN()))
                .andExpect(jsonPath("$.accountName").value("Checking"));
    }

    @Test
    void getAccountByIban_whenMissing_returnsNotFound() throws Exception {

        mockMvc.perform(get("/accounts/{iban}", "NOTFOUND"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Account not found: NOTFOUND"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void updateAccount_updatesAccount() throws Exception {

        User user = new User();
        userRepository.save(user);

        Account account = new Account();
        account.setIBAN("NL69INHO1234567890");
        account.setAccountName("Old Name");
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        accountRepository.save(account);

        Map<String, Object> request = new HashMap<>();
        request.put("accountName", "New Name");

        mockMvc.perform(patch("/accounts/{iban}", account.getIBAN())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountName").value("New Name"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void deleteAccount_setsStatusToClosed() throws Exception {

        User user = new User();
        userRepository.save(user);

        Account account = new Account();
        account.setIBAN("NL69INHO1234567890");
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        Account saved = accountRepository.save(account);

        mockMvc.perform(delete("/accounts/{iban}", saved.getIBAN()))
                .andExpect(status().isNoContent());

        Account reloaded = accountRepository.findById(saved.getIBAN()).orElseThrow();

        assertEquals(AccountStatus.CLOSED, reloaded.getAccountStatus());
    }
}