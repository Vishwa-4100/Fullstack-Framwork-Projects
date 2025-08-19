package com.travel.currency.service;

import com.travel.currency.model.CurrencyRate;
import com.travel.currency.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    private final CurrencyRepository repository;

    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    public Double convertCurrency(String base, String target, Double amount) {
        Optional<CurrencyRate> rateOpt =
                repository.findByBaseCurrencyAndTargetCurrency(base, target);

        if (rateOpt.isPresent()) {
            return amount * rateOpt.get().getRate();
        } else {
            throw new RuntimeException("Conversion rate not available for " + base + " to " + target);
        }
    }

    public CurrencyRate saveRate(CurrencyRate rate) {
        return repository.save(rate);
    }

    public List<CurrencyRate> getAllRates() {
        return repository.findAll();
    }

    public void deleteRate(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Currency rate with ID " + id + " not found.");
        }
    }
}
