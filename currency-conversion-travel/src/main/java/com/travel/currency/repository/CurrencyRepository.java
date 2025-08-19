package com.travel.currency.repository;

import com.travel.currency.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<CurrencyRate, Long> {
    Optional<CurrencyRate> findByBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency);
}
