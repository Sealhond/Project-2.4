package com.example.Project_24.DTOs.Request;

import com.example.Project_24.Enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    //geen id, geen rol

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Long bsn;
    private UserStatus status;
    private Double dailyTransferLimit;
    private Double absoluteTransferLimit;

}
