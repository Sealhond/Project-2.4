package com.example.Project_24.Services;

import com.example.Project_24.DTOs.Request.LoginRequest;
import com.example.Project_24.DTOs.Request.RegisterRequest;
import com.example.Project_24.DTOs.Response.LoginResponse;
import com.example.Project_24.DTOs.Response.UserResponse;
import com.example.Project_24.Enums.Role;
import com.example.Project_24.Enums.UserStatus;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request)
    {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isEmpty())
        {
            throw new UsernameNotFoundException("Email not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword()))
        {
            throw new RuntimeException("Invalid credentials");
        }

        //generate jwt token
        String token = jwtService.generateToken(user.get());

        //return response
        return new LoginResponse(token, user.get().getId(), user.get().getRole(), user.get().getStatus());
    }


    public UserResponse register(RegisterRequest request)
    {

        if(userRepository.existsByEmail(request.getEmail()))
        {
            throw new RuntimeException("Email already exists");
        }

        User newUser = new User();

        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setRole(Role.ROLE_CUSTOMER);
        newUser.setStatus(UserStatus.PENDING);
        newUser.setBsn(request.getBsn());
        newUser.setPhoneNumber(request.getPhoneNumber());

        User savedUser = userRepository.save(newUser);
        return mapToUserResponse(savedUser);
    }

    private UserResponse mapToUserResponse(User user)
    {
        UserResponse userResponse = new UserResponse(
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

        return userResponse;
    }
}
