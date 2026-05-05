package com.example.Project_24.Repositories;

import com.example.Project_24.Enums.UserStatus;
import com.example.Project_24.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    //spring leest hier de methode en maakt aan de hand daarvan gewoon de queries
    Optional<User> findByEmail(String email);
    List<User> findByStatus(UserStatus status);
    boolean existsByEmail(String email);
}
