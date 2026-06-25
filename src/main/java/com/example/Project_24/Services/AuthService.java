package com.example.Project_24.Services;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public LoginResponse login(LoginRequest request)
    {
        //find user by email
        Optional<User> user = userRepository.findByEmail(request.getEmail());


        if(user.isEmpty())
        {
            throw new UsernameNotFoundException("Invalid credentials");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword()))
        {
            throw new InvalidOperationException("Invalid credentials");
        }

        if(user.get().getStatus() == UserStatus.CLOSED) {
            throw new InvalidOperationException("Account is closed");
        }

        //generate jwt token
        String token = jwtService.generateToken(user.get());

        //return response (met token)
        return new LoginResponse(token, user.get().getId(), user.get().getRole(), user.get().getStatus());
    }


    public UserResponse register(RegisterRequest request)
    {
        if(userRepository.existsByEmail(request.getEmail()))
        {
            throw new DuplicateResourceException("Email already exists");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User newUser = userMapper.mapToUser(request);

        User savedUser = userRepository.save(newUser);

        return userMapper.mapToUserResponse(savedUser);
    }
}
