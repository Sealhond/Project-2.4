package com.example.Project_24.services;

import com.example.Project_24.DTOs.Request.LoginRequest;
import com.example.Project_24.DTOs.Request.RegisterRequest;
import com.example.Project_24.DTOs.Response.LoginResponse;
import com.example.Project_24.DTOs.Response.UserResponse;
import com.example.Project_24.Enums.Role;
import com.example.Project_24.Enums.UserStatus;
import com.example.Project_24.Exception.DuplicateResourceException;
import com.example.Project_24.Exception.InvalidOperationException;
import com.example.Project_24.Mappers.UserMapper;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.UserRepository;
import com.example.Project_24.Services.AuthService;
import com.example.Project_24.Services.JwtService;
import com.example.Project_24.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;


    //testdata
    private User mockUser;
    private UserResponse mockUserResponse;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("Emma");
        mockUser.setLastName("Bakker");
        mockUser.setEmail("emma@test.com");
        mockUser.setPassword("hashedpassword");
        mockUser.setRole(Role.ROLE_CUSTOMER);
        mockUser.setStatus(UserStatus.APPROVED);

        mockUserResponse = new UserResponse();
        mockUserResponse.setId(1L);
        mockUserResponse.setFirstName("Emma");
        mockUserResponse.setLastName("Bakker");
        mockUserResponse.setEmail("emma@test.com");
    }

    //login---------------------
    @Test
    void login_validCredentials_returnsLoginResponse() {
        LoginRequest request = new LoginRequest("emma@test.com", "password123");

        when(userRepository.findByEmail("emma@test.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("password123", "hashedpassword")).thenReturn(true);
        when(jwtService.generateToken(mockUser)).thenReturn("mocked-jwt-token");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals(Role.ROLE_CUSTOMER, response.getRole());
    }

    @Test
    void login_wrongPassword_throwsInvalidOperationException() {
        LoginRequest request = new LoginRequest("emma@test.com", "wrongpassword");

        when(userRepository.findByEmail("emma@test.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongpassword", "hashedpassword")).thenReturn(false);

        assertThrows(InvalidOperationException.class, () -> authService.login(request));
    }

    @Test
    void login_nonExistingEmail_throwsUsernameNotFoundException() {
        LoginRequest request = new LoginRequest("nobody@test.com", "password123");

        when(userRepository.findByEmail("nobody@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.login(request));
    }

    @Test
    void login_closedAccount_throwsInvalidOperationException() {
        mockUser.setStatus(UserStatus.CLOSED);
        LoginRequest request = new LoginRequest("emma@test.com", "password123");

        when(userRepository.findByEmail("emma@test.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("password123", "hashedpassword")).thenReturn(true);

        assertThrows(InvalidOperationException.class, () -> authService.login(request));
    }

    // REGISTER ------------------
    @Test
    void register_newEmail_returnsUserResponse() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Emma");
        request.setLastName("Bakker");
        request.setEmail("emma@test.com");
        request.setPassword("password123");
        request.setPhoneNumber("0612345678");
        request.setBsn("12345678");

        when(userRepository.existsByEmail("emma@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedpassword");
        when(userMapper.mapToUser(request)).thenReturn(mockUser);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(userMapper.mapToUserResponse(mockUser)).thenReturn(mockUserResponse);

        UserResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("emma@test.com", response.getEmail());
        verify(userRepository, times(1)).save(mockUser);
    }

    //email already exists
    @Test
    void register_duplicateEmail_throwsDuplicateResourceException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("emma@test.com");

        when(userRepository.existsByEmail("emma@test.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.register(request));
    }

    //register--------------------




}


