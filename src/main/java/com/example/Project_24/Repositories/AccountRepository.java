package com.example.Project_24.Repositories;

import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, String> {

    List<Account> findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(
            String firstName,
            String lastName
    );
    List<Account> findAll();
}
