package com.example.Project_24.Repositories;

import com.example.Project_24.Enums.UserStatus;
import com.example.Project_24.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //spring leest hier de methode en maakt aan de hand daarvan gewoon de queries
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);


    //get all users is buildin in jpa repository
    //get user by id is buildin in jpa repository


    //update user by id:


    //delete user


}
