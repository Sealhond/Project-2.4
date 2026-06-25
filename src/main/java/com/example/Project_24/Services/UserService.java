package com.example.Project_24.Services;

import com.example.Project_24.DTOs.Request.AccountCreateRequest;
import com.example.Project_24.DTOs.Request.ApproveUserRequest;
import com.example.Project_24.DTOs.Request.UpdateUserRequest;
import com.example.Project_24.DTOs.Response.UserResponse;
import com.example.Project_24.Enums.AccountType;
import com.example.Project_24.Enums.UserStatus;
import com.example.Project_24.Exception.UserNotFoundException;
import com.example.Project_24.Mappers.UserMapper;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final UserMapper userMapper;

    //RETURN ALL USERS ------
    public Page<UserResponse> getAllUsers(Pageable pageable, Boolean isPending) {
        if (isPending != null && isPending) {
            return userRepository.findByStatusAndAccountsIsEmpty(UserStatus.PENDING, pageable)
                    .map(userMapper::mapToUserResponse);

        }
        return userRepository.findAll(pageable)
                .map(userMapper::mapToUserResponse);
    }


    //GET USER BY ID ---------------------------
    public UserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty())
        {
            throw new UserNotFoundException();
        }

        return userMapper.mapToUserResponse(user.get());
    }

    //UPDATE USER --------------------------
    public UserResponse updateUser(Long id, UpdateUserRequest request)
    {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        User updated = userMapper.mapToExistingUser(request, existing);
        User savedUser = userRepository.save(updated);

        return userMapper.mapToUserResponse(savedUser);
    }

    //DELETE USER -------------------------------------------
    public void deleteUser(Long id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        user.setStatus(UserStatus.CLOSED);  //set status to closed
        userRepository.save(user);
    }

    //approve user -------------------------------------------
    //probleem was dat ik eerst alle losse endpoints aanriep vanaf frontend
    @Transactional
    public UserResponse approveUser(Long id, ApproveUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        user.setStatus(UserStatus.APPROVED);
        user.setDailyTransferLimit(request.getDailyTransferLimit());
        userRepository.save(user);

        accountService.createAccount(new AccountCreateRequest(
                user.getId(),
                user.getFirstName() + " Checking",
                AccountType.CHECKING,
                request.getCheckingAccountLimit()
        ));

        accountService.createAccount(new AccountCreateRequest(
                user.getId(),
                user.getFirstName() + " Savings",
                AccountType.SAVINGS,
                request.getSavingsAccountLimit()
        ));

        return userMapper.mapToUserResponse(user);
    }
}
