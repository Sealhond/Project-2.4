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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //helper to get jwt token
    private String getEmployeeToken() throws Exception {
        Map<String, Object> loginRequest = new HashMap<>();
        loginRequest.put("email", "employee@bank.com");
        loginRequest.put("password", "password123"); // fix this

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void getAllUsers_asEmployee_returns200() throws Exception {
        String token = getEmployeeToken();

        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers_unauthenticated_returns403() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserById_existingId_returns200() throws Exception {
        String token = getEmployeeToken();

        User user = new User();
        user.setFirstName("Emma");
        user.setLastName("Bakker");
        user.setEmail("emma.get@test.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.ROLE_CUSTOMER);
        user.setStatus(UserStatus.APPROVED);
        User saved = userRepository.save(user);

        mockMvc.perform(get("/users/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Emma"))
                .andExpect(jsonPath("$.email").value("emma.get@test.com"));
    }

    @Test
    void getUserById_nonExistingId_returns404() throws Exception {
        String token = getEmployeeToken();

        mockMvc.perform(get("/users/999999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void deleteUser_existingId_returns204() throws Exception {
        String token = getEmployeeToken();

        User user = new User();
        user.setFirstName("Delete");
        user.setLastName("Me");
        user.setEmail("delete.me@test.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.ROLE_CUSTOMER);
        user.setStatus(UserStatus.APPROVED);
        User saved = userRepository.save(user);

        mockMvc.perform(delete("/users/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        //verify status is closed
        User updated = userRepository.findById(saved.getId()).get();
        assertEquals(UserStatus.CLOSED, updated.getStatus());
    }

    @Test
    void deleteUser_nonExistingId_returns404() throws Exception {
        String token = getEmployeeToken();

        mockMvc.perform(delete("/users/999999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_existingId_returns200() throws Exception {
        String token = getEmployeeToken();

        User user = new User();
        user.setFirstName("Emma");
        user.setLastName("Bakker");
        user.setEmail("emma.update@test.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.ROLE_CUSTOMER);
        user.setStatus(UserStatus.APPROVED);
        User saved = userRepository.save(user);

        Map<String, Object> request = new HashMap<>();
        request.put("firstName", "Updated");

        mockMvc.perform(put("/users/" + saved.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }
}
