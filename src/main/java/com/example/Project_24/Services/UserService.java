package com.example.Project_24.Services;

import com.example.Project_24.DTOs.Response.UserResponse;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //RETURN ALL USERS (niet paginated) ------
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();

        for (User user : users) {
            userResponseList.add(mapToUserResponse(user));
        }

        return userResponseList;
    }

    //GET USER BY ID -----------
    public UserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty())
        {
            throw new RuntimeException("User not found");
        }

        return mapToUserResponse(user.get());
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBsn(),
                user.getRole(),
                user.getStatus(),
                user.getDailyTransferLimit(),
                user.getAbsoluteTransferLimit()
        );
    }
}
