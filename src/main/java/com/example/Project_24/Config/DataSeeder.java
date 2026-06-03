package com.example.Project_24.Config;

import com.example.Project_24.Enums.AccountStatus;
import com.example.Project_24.Enums.Role;
import com.example.Project_24.Enums.UserStatus;
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

            String[][] fakeUsers = {
                    {"Jan",    "de Vries",  "jan.devries@gmail.com",    "0612345678", "123456789"},
                    {"Emma",   "Bakker",    "emma.bakker@gmail.com",    "0623456789", "234567890"},
                    {"Liam",   "Janssen",   "liam.janssen@gmail.com",   "0634567890", "345678901"},
                    {"Sophie", "Visser",    "sophie.visser@gmail.com",  "0645678901", "456789012"},
                    {"Noah",   "Smit",      "noah.smit@gmail.com",      "0656789012", "567890123"},
                    {"Olivia", "Meijer",    "olivia.meijer@gmail.com",  "0667890123", "678901234"},
                    {"Lucas",  "de Boer",   "lucas.deboer@gmail.com",   "0678901234", "789012345"},
                    {"Mia",    "Mulder",    "mia.mulder@gmail.com",     "0689012345", "890123456"},
                    {"Finn",   "Bos",       "finn.bos@gmail.com",       "0690123456", "901234567"},
                    {"Sara",   "Dijkstra",  "sara.dijkstra@gmail.com",  "0611223344", "112345678"},
                    {"Tom",    "Peters",    "tom.peters@gmail.com",     "0622334455", "223456789"},
                    {"Lisa",   "Hendriks",  "lisa.hendriks@gmail.com",  "0633445566", "334567890"},
            };

            for (String[] data : fakeUsers) {
                User user = new User();
                user.setFirstName(data[0]);
                user.setLastName(data[1]);
                user.setEmail(data[2]);
                user.setPhoneNumber(data[3]);
                user.setBsn(Long.parseLong(data[4]));
                user.setPassword(passwordEncoder.encode("password123"));
                user.setRole(Role.ROLE_CUSTOMER);
                user.setStatus(UserStatus.APPROVED);
                user.setDailyTransferLimit(1000.00);
                user.setAbsoluteTransferLimit(50.00);
                userRepository.save(user);
            }

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
            customerAccount.setAccountStatus(AccountStatus.ACTIVE);
            customerAccount.setUser(customer);

            accountRepository.save(customerAccount);

            System.out.println("Database seeded!");
        }
    }
}
