package com.busbooking.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

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
    private Long userId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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