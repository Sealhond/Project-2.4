package com.example.Project_24.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    //jakarta, @Valid in de controller
    //@NotBlank(message = "first name is required)
    //@Size(max = 128, message = "first name must be at most 128 characters"
    private String firstName;


    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Long bsn;
}
