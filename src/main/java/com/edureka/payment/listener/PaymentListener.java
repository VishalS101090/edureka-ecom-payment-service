package com.edureka.payment.listener;

import com.edureka.order.event.OrderPlacedEvent; // Copy Event Class here
import com.edureka.payment.model.Payment;
import com.edureka.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentListener {

    @Autowired
    private PaymentRepository repository;

    @KafkaListener(topics = "notificationTopic", groupId = "payment-group")
    public void processPayment(OrderPlacedEvent event) {
        log.info("Payment Service: Processing payment for Order " + event.getOrderNumber());

        Payment payment = new Payment();
        payment.setOrderId(event.getOrderNumber());
        payment.setAmount(event.getAmount());
        payment.setStatus("SUCCESS");

        repository.save(payment);
        log.info("Payment Saved Successfully");
    }
}