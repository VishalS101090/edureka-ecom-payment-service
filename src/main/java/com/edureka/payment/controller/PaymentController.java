package com.edureka.payment.controller;

import com.edureka.payment.model.Payment;
import com.edureka.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository repository;

    // Endpoint to check payment status by Order ID
    @GetMapping("/order/{orderId}")
    public Payment getPaymentByOrderId(@PathVariable String orderId) {
        return repository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment info not found for Order ID: " + orderId));
    }

    // Endpoint to get all payments (for admin/debugging)
    @GetMapping
    public Iterable<Payment> getAllPayments() {
        return repository.findAll();
    }
}