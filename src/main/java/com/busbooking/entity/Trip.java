package com.busbooking.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @Column(name = "trip_id")
    private UUID tripId;
    
    @Column(name = "company_id", nullable = false)
    private UUID companyId;
    
    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;
    
    @Column(name = "driver_id", nullable = false)
    private UUID driverId;
    
    @Column(name = "departure_place", nullable = false)
    private String departurePlace;
    
    @Column(name = "arrival_place", nullable = false)
    private String arrivalPlace;
    
    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;
    
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;
    
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    
    // @Column(name = "status")
    // private String status = "scheduled";
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private BusCompany busCompany;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", insertable = false, updatable = false)
    private Vehicle vehicle;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", insertable = false, updatable = false)
    private Driver driver;
    
    // Constructors
    public Trip() {}
    
    public Trip(UUID companyId, UUID vehicleId, UUID driverId, String departurePlace, 
                String arrivalPlace, LocalDate departureDate, LocalTime departureTime, BigDecimal price) {
        this.companyId = companyId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.departurePlace = departurePlace;
        this.arrivalPlace = arrivalPlace;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.price = price;
        // this.status = status;
    }
    
    // Getters and Setters
    public UUID getTripId() {
        return tripId;
    }
    
    public void setTripId(UUID tripId) {
        this.tripId = tripId;
    }
    
    public UUID getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
    
    public UUID getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public UUID getDriverId() {
        return driverId;
    }
    
    public void setDriverId(UUID driverId) {
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
    
    public LocalDate getDepartureDate() {
        return departureDate;
    }
    
    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
    
    public LocalTime getDepartureTime() {
        return departureTime;
    }
    
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    // public String getStatus() {
    //    return status;
    // }
    
    // public void setStatus(String status) {
    //      this.status = status;
    // }
    
    public BusCompany getBusCompany() {
        return busCompany;
    }
    
    public void setBusCompany(BusCompany busCompany) {
        this.busCompany = busCompany;
    }
    
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    
    public Driver getDriver() {
        return driver;
    }
    
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}