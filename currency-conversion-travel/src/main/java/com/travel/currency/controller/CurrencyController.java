package com.travel.currency.controller;

import com.travel.currency.service.CurrencyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CurrencyController {

    private final CurrencyService service;

    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("rates", service.getAllRates());
        return "index";
    }

    @PostMapping("/convert")
    public String convert(@RequestParam String base,
                          @RequestParam String target,
                          @RequestParam Double amount,
                          Model model) {
        Double result = service.convertCurrency(base, target, amount);
        model.addAttribute("result", result);
        model.addAttribute("currency", target);
        return "estimate";
    }
}
