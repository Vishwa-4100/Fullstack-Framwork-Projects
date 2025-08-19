package com.example.bankingsystem.service;

import com.example.bankingsystem.model.Account;
import com.example.bankingsystem.model.Transaction;
import com.example.bankingsystem.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    public Account register(Account account) {
        account.setBalance(0.0);
        return accountRepository.save(account);
    }

    public Account login(String email, String password) {
        return accountRepository.findByEmailAndPassword(email, password);
    }

    public void deposit(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setType("Deposit");
        tx.setAmount(amount);
        tx.setDateTime(LocalDateTime.now());
        tx.setAccount(account);
        transactionService.saveTransaction(tx);
    }

    public void withdraw(Account account, double amount) {
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);

            Transaction tx = new Transaction();
            tx.setType("Withdraw");
            tx.setAmount(amount);
            tx.setDateTime(LocalDateTime.now());
            tx.setAccount(account);
            transactionService.saveTransaction(tx);
        }
    }

    public List<Transaction> getTransactions(Long accountId) {
        return transactionService.getTransactionsByAccount(accountId);
    }
}
