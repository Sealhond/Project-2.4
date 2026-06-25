package com.example.Project_24.Config;

import com.example.Project_24.Enums.*;
import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.Transaction;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.AccountRepository;
import com.example.Project_24.Repositories.TransactionRepository;
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
    private final TransactionRepository transactionRepository;

    public DataSeeder(UserRepository userRepository, AccountRepository accountRepository, AccountService accountService, PasswordEncoder passwordEncoder, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) {

            //EMPLOYEE  --------------------------------------------------
            User employee = new User();
            employee.setEmail("employee@bank.com");
            employee.setPassword(passwordEncoder.encode("password123"));
            employee.setFirstName("employeeFirstName");
            employee.setLastName("employeeLastName");
            employee.setRole(Role.ROLE_EMPLOYEE);
            employee.setStatus(UserStatus.APPROVED);
            employee.setDailyTransferLimit(10000.00);
            userRepository.save(employee);

            //CUSTOMER ---------------------------------------------------
            User customer = new User();
            customer.setEmail("customer@bank.com");
            customer.setPassword(passwordEncoder.encode("password123"));
            customer.setRole(Role.ROLE_CUSTOMER);
            customer.setFirstName("CustomerFirstName");
            customer.setLastName("CustomerLastName");
            customer.setDailyTransferLimit(1000.00);
            customer.setStatus(UserStatus.APPROVED);
            userRepository.save(customer);

            //CUSTOMER 2 ---------------------------------------------------
            User customer2 = new User();
            customer2.setEmail("customer2@bank.com");
            customer2.setPassword(passwordEncoder.encode("password123"));
            customer2.setRole(Role.ROLE_CUSTOMER);
            customer2.setFirstName("Customer2FirstName");
            customer2.setLastName("Customer2LastName");
            customer2.setDailyTransferLimit(1000.00);
            customer2.setStatus(UserStatus.APPROVED);
            userRepository.save(customer2);

            // extra random customers ------------------------------------
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
                userRepository.save(user);
            }

            //pending accounts
            String[][] pendingUsers = {
                    {"Daan",   "van Dijk",   "daan.vandijk@gmail.com",   "0612345679", "998877661"},
                    {"Fleur",  "Vermeer",    "fleur.vermeer@gmail.com",  "0623456780", "998877662"},
                    {"Jesse",  "Kuiper",     "jesse.kuiper@gmail.com",   "0634567891", "998877663"},
                    {"Noor",   "Willems",    "noor.willems@gmail.com",   "0645678902", "998877664"},
                    {"Bram",   "van den Berg","bram.vandenberg@gmail.com","0656789013", "998877665"},
            };

            for (String[] data : pendingUsers) {
                User user = new User();
                user.setFirstName(data[0]);
                user.setLastName(data[1]);
                user.setEmail(data[2]);
                user.setPhoneNumber(data[3]);
                user.setBsn(Long.parseLong(data[4]));
                user.setPassword(passwordEncoder.encode("password123"));
                user.setRole(Role.ROLE_CUSTOMER);
                user.setStatus(UserStatus.PENDING);
                user.setDailyTransferLimit(0.00);
                userRepository.save(user);
            }

            //ACCOUNT VAN EMPLOYEE ----------------------------------------
            Account employeeAccount = new Account();
            employeeAccount.setIBAN("NL69INHO0000000001");
            employeeAccount.setAccountName("Employee Main Account");
            employeeAccount.setBalance(10000.0);
            employeeAccount.setAccountLimit(-5000.0);
            employeeAccount.setAccountStatus(AccountStatus.ACTIVE);
            employeeAccount.setAccountType(AccountType.CHECKING);
            employeeAccount.setUser(employee);
            accountRepository.save(employeeAccount);

            //ACCOUNT VAN CUSTOMER 1 ---------------------------------------
            Account customerAccount = new Account();
            customerAccount.setIBAN("NL69INHO0000000002");
            customerAccount.setAccountName("Customer Main Account");
            customerAccount.setBalance(1500.0);
            customerAccount.setAccountLimit(-500.0);
            customerAccount.setAccountStatus(AccountStatus.ACTIVE);
            customerAccount.setAccountType(AccountType.CHECKING);
            customerAccount.setUser(customer);
            accountRepository.save(customerAccount);

            //SAVINGS ACCOUNT VAN CUSTOMER 1 ---------------------------------------
            Account customerSavings = new Account();
            customerSavings.setIBAN("NL69INHO0000000003");
            customerSavings.setAccountName("Customer Savings Account");
            customerSavings.setBalance(5000.0);
            customerSavings.setAccountLimit(0.0);
            customerSavings.setAccountStatus(AccountStatus.ACTIVE);
            customerSavings.setAccountType(AccountType.SAVINGS);
            customerSavings.setUser(customer);
            accountRepository.save(customerSavings);

            //ACCOUNT VAN CUSTOMER 2 --------------------------------------
            Account receiverAccount = new Account();
            receiverAccount.setIBAN("NL69INHO0000000004");
            receiverAccount.setAccountName("Customer2 Main Account");
            receiverAccount.setBalance(800.0);
            receiverAccount.setAccountLimit(-200.0);
            receiverAccount.setAccountStatus(AccountStatus.ACTIVE);
            receiverAccount.setAccountType(AccountType.CHECKING);
            receiverAccount.setUser(customer2);
            accountRepository.save(receiverAccount);

            // TRANSACTIONS -----------------------------------------------
            Transaction t1 = new Transaction();
            t1.setSender(customerAccount);
            t1.setReceiver(receiverAccount);
            t1.setAmount(100.0);
            t1.setType(TransactionType.TRANSFER);
            t1.setInitiatedBy(customer);
            transactionRepository.save(t1);

            Transaction t2 = new Transaction();
            t2.setSender(null);
            t2.setReceiver(customerAccount);
            t2.setAmount(500.0);
            t2.setType(TransactionType.DEPOSIT);
            t2.setInitiatedBy(customer);
            transactionRepository.save(t2);

            Transaction t3 = new Transaction();
            t3.setSender(customerAccount);
            t3.setReceiver(null);
            t3.setAmount(50.0);
            t3.setType(TransactionType.WITHDRAW);
            t3.setInitiatedBy(customer);
            transactionRepository.save(t3);

            Transaction t4 = new Transaction();
            t4.setSender(receiverAccount);
            t4.setReceiver(customerAccount);
            t4.setAmount(200.0);
            t4.setType(TransactionType.TRANSFER);
            t4.setInitiatedBy(employee);
            transactionRepository.save(t4);

            System.out.println("✅ Database seeded!");
            System.out.println("👤 employee@bank.com / password123");
            System.out.println("👤 customer@bank.com / password123");
            System.out.println("👤 customer2@bank.com / password123");
            System.out.println("🏦 NL69INHO0000000001 -> employee");
            System.out.println("🏦 NL69INHO0000000002 -> customer checking");
            System.out.println("🏦 NL69INHO0000000003 -> customer savings");
            System.out.println("🏦 NL69INHO0000000004 -> customer2 checking");
        }

    }
}
