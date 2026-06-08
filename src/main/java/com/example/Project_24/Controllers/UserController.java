package com.example.Project_24.Controllers;

import com.example.Project_24.DTOs.Request.UpdateUserRequest;
import com.example.Project_24.DTOs.Response.UserResponse;
import com.example.Project_24.Models.User;
import com.example.Project_24.Services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    //GET ALL USERS
    @GetMapping
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName").ascending());
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    //GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));

    }

    //UPDATE USER
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {

        return ResponseEntity.ok(userService.updateUser(id, request));

    }

    //DELETE USER
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
