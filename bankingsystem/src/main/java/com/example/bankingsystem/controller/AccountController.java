package com.example.bankingsystem.controller;

import com.example.bankingsystem.model.Account;
import com.example.bankingsystem.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("account", new Account());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Account account) {
        accountService.register(account);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Account account = accountService.login(email, password);
        if (account != null) {
            session.setAttribute("account", account);
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        return "dashboard";
    }

    @GetMapping("/deposit")
    public String showDepositForm() {
        return "deposit";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam double amount, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        accountService.deposit(account, amount);
        session.setAttribute("account", accountService.login(account.getEmail(), account.getPassword()));
        return "redirect:/dashboard";
    }

    @GetMapping("/withdraw")
    public String showWithdrawForm() {
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam double amount, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        accountService.withdraw(account, amount);
        session.setAttribute("account", accountService.login(account.getEmail(), account.getPassword()));
        return "redirect:/dashboard";
    }

    @GetMapping("/balance")
    public String balance(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        return "balance";
    }

    @GetMapping("/transactions")
    public String transactions(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("transactions", accountService.getTransactions(account.getId()));
        return "transactions";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
