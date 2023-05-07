package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.Payment;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PaymentRepository extends CrudRepository<Payment, UUID> {

}
