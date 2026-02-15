package com.edureka.payment.repository;

import com.edureka.payment.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    // Custom method to find payment by Order ID
    Optional<Payment> findByOrderId(String orderId);
}