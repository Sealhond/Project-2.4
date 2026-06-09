package com.example.Project_24.Mappers;

import com.example.Project_24.DTOs.Request.RegisterRequest;
import com.example.Project_24.DTOs.Request.UpdateUserRequest;
import com.example.Project_24.DTOs.Response.UserResponse;
import com.example.Project_24.Enums.Role;
import com.example.Project_24.Enums.UserStatus;
import com.example.Project_24.Models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse mapToUserResponse(User user) {

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBsn(),
                user.getRole(),
                user.getStatus(),
                user.getDailyTransferLimit()
              //  user.getAbsoluteTransferLimit()
        );

        return userResponse;
    }

    public User mapToUser(RegisterRequest request)
    {
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setRole(Role.ROLE_CUSTOMER);
        newUser.setStatus(UserStatus.PENDING);
        newUser.setBsn(Long.parseLong(request.getBsn()));
        newUser.setPhoneNumber(request.getPhoneNumber());

        return newUser;

    }

    public User mapToExistingUser(UpdateUserRequest request, User existing)
    {
        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setEmail(request.getEmail());
        existing.setPhoneNumber(request.getPhoneNumber());
        existing.setBsn(request.getBsn());
        existing.setStatus(request.getStatus());
        existing.setDailyTransferLimit(request.getDailyTransferLimit());
       // existing.setAbsoluteTransferLimit(request.getAbsoluteTransferLimit());

        return existing;
    }
}
