package com.busbooking.model;

import java.io.*;
import java.sql.*;

public class Trips implements Serializable {
    private String tripId;
    private String companyId;
    private String vehicleId;
    private String driverId;
    private String departurePlace;
    private String arrivalPlace;
    private Date departureDate;
    private Time departureTime;
    private Float price;
    private String status;

    public Trips() {}

    public Trips(String tripId, String companyId, String vehicleId, String driverId, String departurePlace,
                 String arrivalPlace, Date departureDate, Time departureTime, Float price, String status) {
        this.tripId = tripId;
        this.companyId = companyId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.departurePlace = departurePlace;
        this.arrivalPlace = arrivalPlace;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.price = price;
        this.status = status;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDeparturePlace() {
        return departurePlace;
    }

    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    public String getArrivalPlace() {
        return arrivalPlace;
    }

    public void setArrivalPlace(String arrivalPlace) {
        this.arrivalPlace = arrivalPlace;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
