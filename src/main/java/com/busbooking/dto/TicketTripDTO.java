package com.busbooking.dto;

import com.busbooking.entity.Ticket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class TicketTripDTO {
    private int ticketId;
    private long userId;
    private String seatId;
    private LocalDateTime bookingTime;
    private Ticket.TicketStatus status;
    private double price;
    private int tripId;
    private String departureCity;
    private String arrivalCity;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private double distanceKm;
    private LocalDateTime arrivalDateTime;
    private double tripPrice;

    public Date getDepartureDateAsDate() {
        return this.departureDate == null ? null : Date.from(this.departureDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Date getArrivalDateTimeAsDate() {
        return this.arrivalDateTime == null ? null : Date.from(this.arrivalDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public TicketTripDTO(int ticketId, long userId, String seatId, LocalDateTime bookingTime, Ticket.TicketStatus status, double price, int tripId, String departureCity, String arrivalCity, LocalDate departureDate, LocalTime departureTime, double distanceKm, LocalDateTime arrivalDateTime, double tripPrice) {
        this.ticketId = ticketId;
        this.userId= userId;
        this.seatId = seatId;
        this.bookingTime = bookingTime;
        this.status = status;
        this.price = price;
        this.tripId = tripId;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.distanceKm = distanceKm;
        this.arrivalDateTime = arrivalDateTime;
        this.tripPrice = tripPrice;
    }

    public int getTicketId() {
        return this.ticketId;
    }

    public long getUserId() {
        return this.userId;
    }

    public String getSeatId() {
        return this.seatId;
    }

    public LocalDateTime getBookingTime() {
        return this.bookingTime;
    }

    public Ticket.TicketStatus getStatus() {
        return this.status;
    }

    public double getPrice() {
        return this.price;
    }

    public int getTripId() {
        return this.tripId;
    }

    public String getDepartureCity() {
        return this.departureCity;
    }

    public String getArrivalCity() {
        return this.arrivalCity;
    }

    public LocalDate getDepartureDate() {
        return this.departureDate;
    }

    public LocalTime getDepartureTime() {
        return this.departureTime;
    }

    public double getDistanceKm() {
        return this.distanceKm;
    }

    public LocalDateTime getArrivalDateTime() {
        return this.arrivalDateTime;
    }

    public double getTripPrice() {
        return this.tripPrice;
    }
}
