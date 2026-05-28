package com.example.Project_24.Services;

import com.example.Project_24.DTOs.Request.UpdateUserRequest;
import com.example.Project_24.DTOs.Response.UserResponse;
import com.example.Project_24.Enums.UserStatus;
import com.example.Project_24.Mappers.UserMapper;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
    private final UserMapper userMapper;

    //RETURN ALL USERS (niet paginated) ------
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();

        for (User user : users) {
            userResponseList.add(userMapper.mapToUserResponse(user));
        }

        return userResponseList;
    }

    //GET USER BY ID ---------------------------
    public UserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty())
        {
            throw new RuntimeException("User not found");
        }

        return userMapper.mapToUserResponse(user.get());
    }

    //UPDATE USER --------------------------
    public UserResponse updateUser(Long id, UpdateUserRequest request)
    {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User updated = userMapper.mapToExistingUser(request, existing);
        User savedUser = userRepository.save(existing);

        return userMapper.mapToUserResponse(savedUser);
    }


    //DELETE USER -------------------------------------------
    public void deleteUser(Long id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.CLOSED);  //set status to closed
        userRepository.save(user);
    }

}
