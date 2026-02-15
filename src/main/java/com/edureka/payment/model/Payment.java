package com.edureka.payment.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Document(collection = "payments")
@Data
public class Payment {
    @Id
    private String id;
    private String orderId;
    private BigDecimal amount;
    private String status;
}