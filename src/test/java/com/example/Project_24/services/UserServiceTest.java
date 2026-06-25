package com.example.Project_24.services;

import com.example.Project_24.DTOs.Request.UpdateUserRequest;
import com.example.Project_24.DTOs.Response.UserResponse;
import com.example.Project_24.Enums.UserStatus;
import com.example.Project_24.Exception.UserNotFoundException;
import com.example.Project_24.Mappers.UserMapper;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.UserRepository;
import com.example.Project_24.Services.UserService;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    //test data
    private User mockUser;
    private UserResponse mockUserResponse;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("Emma");
        mockUser.setLastName("Bakker");
        mockUser.setEmail("emma@test.com");
        mockUser.setStatus(UserStatus.APPROVED);

        mockUserResponse = new UserResponse();
        mockUserResponse.setId(1L);
        mockUserResponse.setFirstName("Emma");
        mockUserResponse.setLastName("Bakker");
    }

    //get all: ------------------
    @Test
    void getAllUsers_returnsPageOfUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(mockUser));

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.mapToUserResponse(mockUser)).thenReturn(mockUserResponse);

        Page<UserResponse> result = userService.getAllUsers(pageable, null);

        assertEquals(1, result.getTotalElements());

    }

    @Test
    void getAllUsers_withPendingFilter_returnsPendingUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(mockUser));

        when(userRepository.findByStatusAndAccountsIsEmpty(UserStatus.PENDING, pageable))
                .thenReturn(userPage);
        when(userMapper.mapToUserResponse(mockUser)).thenReturn(mockUserResponse);

        Page<UserResponse> result = userService.getAllUsers(pageable, true);

        assertEquals(1, result.getTotalElements());
    }

    //get by id ---------------
    @Test
    void getUserById_existingId_returnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userMapper.mapToUserResponse(mockUser)).thenReturn(mockUserResponse);

        UserResponse result = userService.getUserById(1L);

        assertEquals("Emma", result.getFirstName());
    }

    @Test
    void getUserById_nonExistingId_throwsUserNotFoundException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
    }

    //update ------------
    @Test
    void updateUser_existingId_returnsUpdatedUser() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("Updated");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userMapper.mapToExistingUser(request, mockUser)).thenReturn(mockUser);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(userMapper.mapToUserResponse(mockUser)).thenReturn(mockUserResponse);

        UserResponse result = userService.updateUser(1L, request);

        assertNotNull(result);
    }

    @Test
    void updateUser_nonExistingId_throwsUserNotFoundException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(999L, new UpdateUserRequest()));
    }

    //delete ------------
    @Test
    void deleteUser_existingId_setsStatusToClosed() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        userService.deleteUser(1L);

        assertEquals(UserStatus.CLOSED, mockUser.getStatus());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void deleteUser_nonExistingId_throwsUserNotFoundException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(999L));
    }


}
