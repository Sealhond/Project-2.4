package com.example.Project_24.Config;

import com.example.Project_24.Enums.Role;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) {

            User employee = new User();
            employee.setEmail("employee@bank.com");
            employee.setPassword(passwordEncoder.encode("password123"));
            employee.setRole(Role.ROLE_EMPLOYEE);

            userRepository.save(employee);

            User customer = new User();
            customer.setEmail("customer@bank.com");
            customer.setPassword(passwordEncoder.encode("password123"));
            customer.setRole(Role.ROLE_CUSTOMER);

            userRepository.save(customer);

            System.out.println("Database seeded!");
        }
    }
}
