package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.RentalPrice;
import org.springframework.data.repository.CrudRepository;

import java.util.Currency;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public interface RentalPriceRepository extends CrudRepository<RentalPrice, UUID> {
    Optional<RentalPrice> findRentalPriceByValueAndCurrencyAndTimeUnit(double value, Currency currency, TimeUnit timeUnit);
}
