package com.busbooking.entity;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "seat")
public class Seat {

    @Column(name = "\"idSeat\"")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSeat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicleid")
    private Vehicle idVehicle;

    @Column(name = "status")
    private String statusBook;

    @Column(name = "\"numSeat\"")
    private String numSeat;

    // Constructors
    public Seat() {}

    public Seat(Integer idSeat, Vehicle idVehicle, String statusBook, String numSeat, float price) {
        this.idSeat = idSeat;
        this.idVehicle = idVehicle;
        this.statusBook = statusBook;
        this.numSeat = numSeat;
    }

    // Getters and Setters

    public Integer getIdSeat() {
        return idSeat;
    }

    public void setIdSeat(Integer idSeat) { this.idSeat = idSeat; }

    public String getStatusBook() { return statusBook; }

    public void setStatusBook(String statusBook) {
        this.statusBook = statusBook;
    }

    public String getNumSeat() {
        return numSeat;
    }

    public void setNumSeat(String numSeat) {
        this.numSeat = numSeat;
    }

    public Vehicle getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(Vehicle idVehicle) {
        this.idVehicle = idVehicle;
    }
}