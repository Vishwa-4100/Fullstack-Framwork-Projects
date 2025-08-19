package com.travel.currency.controller;

import com.travel.currency.model.CurrencyRate;
import com.travel.currency.service.CurrencyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CurrencyService service;

    public AdminController(CurrencyService service) {
        this.service = service;
    }

    @GetMapping
    public String adminHome(Model model) {
        model.addAttribute("rates", service.getAllRates());
        return "admin";
    }

    @PostMapping("/add")
    public String addRate(@ModelAttribute CurrencyRate rate) {
        service.saveRate(rate);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteRate(@PathVariable Long id) {
        service.deleteRate(id);
        return "redirect:/admin";
    }
}
