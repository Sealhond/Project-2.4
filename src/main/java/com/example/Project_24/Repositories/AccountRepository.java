package com.example.Project_24.Repositories;

import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    Page<Account> findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(
            String firstName,
            String lastName,
            Pageable pageable
    );
    

}
