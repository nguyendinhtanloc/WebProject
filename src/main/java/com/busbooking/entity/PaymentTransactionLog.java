package com.busbooking.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transaction_log")
public class PaymentTransactionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", updatable = false, nullable = false)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Lob
    @Column(name = "request_payload")
    private String requestPayload;

    @Lob
    @Column(name = "response_payload")
    private String responsePayload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    //Constructor
    public PaymentTransactionLog(){}

    //Getter and Setter
    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public String getRequestPayload() { return requestPayload; }
    public void setRequestPayload(String requestPayload) { this.requestPayload = requestPayload; }

    public String getResponsePayload() { return responsePayload; }
    public void setResponsePayload(String responsePayload) { this.responsePayload = responsePayload; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}