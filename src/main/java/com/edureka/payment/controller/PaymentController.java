package com.edureka.payment.controller;

import com.edureka.payment.model.Payment;
import com.edureka.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    public static final Logger _logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentRepository repository;

    /**
     * Get all payments.
     * Returns 200 OK with list of all payments (empty list if none).
     */
    @GetMapping(value = {"", "/", "/all"})
    public ResponseEntity<?> getAllPayments() {
        _logger.info("Getting all payments");
        return ResponseEntity.ok(repository.findAll());
    }

    /**
     * Create a new payment.
     * Returns 201 Created with the created payment, or 400 for validation errors.
     */
    @PostMapping(value = {"", "/"})
    public ResponseEntity<?> createPayment(@RequestBody Payment payment) {
        if (payment == null) {
            _logger.warn("Payment is null");
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Payment body is required"));
        }
        if (payment.getOrderId() == null || payment.getOrderId().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Payment orderId is required"));
        }
        if (payment.getAmount() != null && payment.getAmount().signum() < 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Payment amount must be non-negative"));
        }

        payment.setId(payment.getId() != null && !payment.getId().isBlank()
                ? payment.getId() : UUID.randomUUID().toString());
        Payment saved = repository.save(payment);
        _logger.info("New payment added successfully: {}", saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Get payment by ID (path variable). Returns 200 with payment or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Payment id is required"));
        }
        _logger.info("Getting payment with id: {}", id);
        return repository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Payment not found for id: " + id)));
    }

    /**
     * Get payment by order ID. Returns 200 with payment or 404 Not Found.
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getPaymentByOrderId(@PathVariable String orderId) {
        if (orderId == null || orderId.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Order id is required"));
        }
        _logger.info("Getting payment for order id: {}", orderId);
        return repository.findByOrderId(orderId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Payment not found for order id: " + orderId)));
    }
}
