package com.example.Project_24.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSummaryResponse {
    private Long id;
    private String firstName;
    private String lastName;
}