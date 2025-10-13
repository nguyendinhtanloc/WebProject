package com.busbooking.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column(name = "tripid")
    private int tripId;

    @OneToMany(fetch = FetchType.LAZY)
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

    @Column(name = "expiresAt")
    private LocalDateTime expiresAt;

    @OneToMany(
            mappedBy = "order", // "order" là tên thuộc tính trong class Payment
            cascade = CascadeType.ALL, // Quan trọng: Xóa Order sẽ xóa tất cả Payment liên quan
            orphanRemoval = true // Tự động xóa Payment nếu nó bị gỡ khỏi danh sách này
    )
    private List<Payment> payments = new ArrayList<>();

    // Thêm Getter và Setter cho payments
    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
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
    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;

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
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
