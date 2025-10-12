package com.busbooking.entity;

import javax.persistence.*;

@Entity
@Table(name = "payment_method")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "method_id", updatable = false, nullable = false)
    private Long methodId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    //Constructor
    public PaymentMethod() {}

    //Getter and Setter
    public Long getMethodId() { return methodId; }
    public void setMethodId(Long methodId) { this.methodId = methodId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}