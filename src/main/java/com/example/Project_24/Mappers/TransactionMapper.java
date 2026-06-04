package com.example.Project_24.Mappers;

import com.example.Project_24.DTOs.Response.TransactionResponse;
import com.example.Project_24.DTOs.Response.UserSummaryResponse;
import com.example.Project_24.Models.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse mapToTransactionResponse(Transaction transaction)
    {
        TransactionResponse transactionResponse = new TransactionResponse();

        transactionResponse.setId(transaction.getId());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setType(transaction.getType());
        transactionResponse.setTimestamp(transaction.getTimestamp());

        //null checks because sender/receiver can be null for ATM transactions
        transactionResponse.setSenderIban(transaction.getSender() != null ? transaction.getSender().getIBAN() : null);
        transactionResponse.setReceiverIban(transaction.getReceiver() != null ? transaction.getReceiver().getIBAN() : null);

        if (transaction.getInitiatedBy() != null) {
            UserSummaryResponse userSummary = new UserSummaryResponse();
            userSummary.setId(transaction.getInitiatedBy().getId());
            userSummary.setFirstName(transaction.getInitiatedBy().getFirstName());
            userSummary.setLastName(transaction.getInitiatedBy().getLastName());
            transactionResponse.setInitiatedBy(userSummary);
        }



        return transactionResponse;
    }
}
