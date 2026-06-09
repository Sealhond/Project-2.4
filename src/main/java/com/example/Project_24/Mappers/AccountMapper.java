package com.example.Project_24.Mappers;

import com.example.Project_24.DTOs.Response.AccountResponse;
import com.example.Project_24.DTOs.Response.UserSummaryResponse;
import com.example.Project_24.Models.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse mapToAccountResponse(Account account) {
        AccountResponse response = new AccountResponse();

        response.setIBAN(account.getIBAN());
        response.setAccountName(account.getAccountName());
        response.setBalance(account.getBalance());
        response.setAccountLimit(account.getAccountLimit());
        response.setDateCreated(account.getDateCreated());
        response.setInterestRate(account.getInterestRate());
        response.setAccountType(account.getAccountType());
        response.setAccountStatus(account.getAccountStatus());

        if (account.getUser() != null) {

            UserSummaryResponse userResponse = new UserSummaryResponse();

            userResponse.setId(account.getUser().getId());
            userResponse.setFirstName(account.getUser().getFirstName());
            userResponse.setLastName(account.getUser().getLastName());

            response.setUser(userResponse);
        }

        return response;
    }

}
