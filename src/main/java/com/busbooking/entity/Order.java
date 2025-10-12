package com.busbooking.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders") // Giả sử bảng tên orders
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid")
    private Long orderId;

    @Column(name = "amount")
    private BigDecimal amount;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "orderid") // khóa ngoại trong bảng Seat
    private List<Seat> seatBooked;

    @Column(name = "userid")
    private int userId;

    @Column(name = "customername")
    private String customerName;

    @Column(name = "customerphone")
    private String customerPhone;

    @Column(name = "customeremail")
    private String customerEmail;

    @Column(name = "orderstatus")
    private String orderStatus;

    // Constructors
    public Order() {}

    // Getters và Setters

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<Seat> getSeatBooked() {
        return seatBooked;
    }

    public void setSeatBooked(List<Seat> seatBooked) {
        this.seatBooked = seatBooked;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}