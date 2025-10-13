package com.busbooking.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticketid")
    private int ticketId;

    @Column(name = "userid")
    private long userId;

    @Column(name = "tripid")
    private int tripId;

    @Column(name = "seatid")
    private String seatId;

    @Column(name = "bookingtime")
    private LocalDateTime bookingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TicketStatus status = TicketStatus.booked;

    @Column(name = "price")
    private double price;

    @Column(name = "cancelledat")
    private LocalDateTime cancelledAt;

    @Column(name = "checkedinat")
    private LocalDateTime checkedInAt;

    @Column(name = "updatedby")
    private Integer updatedBy;

    public Ticket() {
        this.bookingTime = LocalDateTime.now();
        this.status = TicketStatus.booked;
    }

    // Getters và setters
    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }
    public String getSeatId() { return seatId; }
    public void setSeatId(String seatId) { this.seatId = seatId; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
    public LocalDateTime getCheckedInAt() { return checkedInAt; }
    public void setCheckedInAt(LocalDateTime checkedInAt) { this.checkedInAt = checkedInAt; }
    public Integer getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Integer updatedBy) { this.updatedBy = updatedBy; }

    public enum TicketStatus {
        booked,
        cancelled,
        refunded,
        checkedin;
    }
}
