package com.busbooking.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "seat")
public class Seat {

    @Column(name = "\"idSeat\"")
    @Id
    private String idSeat;

    @Column(name = "id_vehicle")
    private UUID idVehicle;

    @Column(name = "status_book")
    private boolean statusBook = false;

    @Column(name = "\"numSeat\"")
    private String numSeat;

    @Column(name= "price")
    private float price;

    // Constructors
    public Seat() {}

    public Seat(String idSeat, UUID idVehicle, boolean statusBook, String numSeat, float price) {
        this.idSeat = idSeat;
        this.idVehicle = idVehicle;
        this.statusBook = statusBook;
        this.numSeat = numSeat;
        this.price = price;
    }

    // Getters and Setters

    public String getIdSeat() {
        return idSeat;
    }

    public void setIdSeat(String idSeat) {
        this.idSeat = idSeat;
    }

    public UUID getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(UUID idVehicle) {
        this.idVehicle = idVehicle;
    }

    public boolean isStatusBook() {
        return statusBook;
    }

    public void setStatusBook(boolean statusBook) {
        this.statusBook = statusBook;
    }

    public String getNumSeat() {
        return numSeat;
    }

    public void setNumSeat(String numSeat) {
        this.numSeat = numSeat;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
