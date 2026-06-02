package com.example.Project_24.Config;

import com.example.Project_24.Enums.AccountStatus;
import com.example.Project_24.Enums.Role;
import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.AccountRepository;
import com.example.Project_24.Repositories.UserRepository;
import com.example.Project_24.Services.AccountService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, AccountRepository accountRepository, AccountService accountService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
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
            customer.setFirstName("Jan");
            customer.setLastName("Smit");

            userRepository.save(customer);

            Account employeeAccount = new Account();
            employeeAccount.setIBAN(accountService.generateIban());
            employeeAccount.setAccountName("Employee Main Account");
            employeeAccount.setBalance(1000.0);
            employeeAccount.setAccountLimit(-5000.0);
            employeeAccount.setAccountStatus(AccountStatus.ACTIVE);
            employeeAccount.setUser(employee);

            accountRepository.save(employeeAccount);

            Account customerAccount = new Account();
            customerAccount.setIBAN(accountService.generateIban());
            customerAccount.setAccountName("Customer Main Account");
            customerAccount.setBalance(0.0);
            customerAccount.setAccountLimit(-1000.0);
            customerAccount.setAccountStatus(AccountStatus.CLOSED);
            customerAccount.setUser(customer);

            accountRepository.save(customerAccount);

            System.out.println("Database seeded!");
        }
    }
}
