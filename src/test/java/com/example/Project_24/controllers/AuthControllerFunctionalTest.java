package com.example.Project_24.controllers;

import com.example.Project_24.Enums.Role;
import com.example.Project_24.Enums.UserStatus;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Boots the full Spring application context so this test exercises real wiring.
@SpringBootTest
// Exposes MockMvc from the context, allowing HTTP-style endpoint testing without a real server.
@AutoConfigureMockMvc
// Wraps each test in a transaction and rolls it back afterward to keep database state isolated.
@Transactional
class AuthControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void register_validRequest_returns200() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("firstName", "Emma");
        request.put("lastName", "Bakker");
        request.put("email", "emma.test@test.com");
        request.put("password", "password123");
        request.put("phoneNumber", "0612345678");
        request.put("bsn", "12345678");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("emma.test@test.com"))
                .andExpect(jsonPath("$.firstName").value("Emma"));
    }

    @Test
    void register_duplicateEmail_returns409() throws Exception {
        //save a user directly
        User existing = new User();
        existing.setFirstName("Emma");
        existing.setLastName("Bakker");
        existing.setEmail("duplicate@test.com");
        existing.setPassword(passwordEncoder.encode("password123"));
        existing.setRole(Role.ROLE_CUSTOMER);
        existing.setStatus(UserStatus.PENDING);
        userRepository.save(existing);

        Map<String, Object> request = new HashMap<>();
        request.put("firstName", "Emma");
        request.put("lastName", "Bakker");
        request.put("email", "duplicate@test.com");
        request.put("password", "password123");
        request.put("phoneNumber", "0612345678");
        request.put("bsn", "12345678");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void login_validCredentials_returnsToken() throws Exception {
        User user = new User();
        user.setFirstName("Emma");
        user.setLastName("Bakker");
        user.setEmail("login.test@test.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.ROLE_CUSTOMER);
        user.setStatus(UserStatus.APPROVED);
        userRepository.save(user);

        Map<String, Object> request = new HashMap<>();
        request.put("email", "login.test@test.com");
        request.put("password", "password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.role").value("ROLE_CUSTOMER"));
    }

    @Test
    void login_wrongPassword_returns400() throws Exception {
        User user = new User();
        user.setFirstName("Emma");
        user.setLastName("Bakker");
        user.setEmail("wrong.pass@test.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.ROLE_CUSTOMER);
        user.setStatus(UserStatus.APPROVED);
        userRepository.save(user);

        Map<String, Object> request = new HashMap<>();
        request.put("email", "wrong.pass@test.com");
        request.put("password", "wrongpassword");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }
}