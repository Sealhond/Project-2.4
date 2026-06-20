package com.example.Project_24.Repositories;

import com.example.Project_24.DTOs.Response.IbanResponse;
import com.example.Project_24.Enums.AccountType;
import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Page<Account> findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(
            String firstName,
            String lastName,
            Pageable pageable
    );

    List<Account> user(User user);
    List<Account> findByUserId(Long userId);

    // This query selects only the IBANs of a certain type (usually checking) and searches by first and last name
    @Query("""
    SELECT new com.example.Project_24.DTOs.Response.IbanResponse(
        a.IBAN,
        new com.example.Project_24.DTOs.Response.UserSummaryResponse(
            u.id,
            u.firstName,
            u.lastName
        )
    )
    FROM Account a
    JOIN a.user u
    WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))
    AND LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))
    AND a.accountType = :type
    """)
    Page<IbanResponse> searchIbans(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("type") AccountType type, Pageable pageable
    );
}
