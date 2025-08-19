package com.example.bankingsystem.service;

import com.example.bankingsystem.model.Transaction;
import com.example.bankingsystem.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByAccount(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}
