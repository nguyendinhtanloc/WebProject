package com.busbooking.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "triptransport")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tripid")
    private Integer tripId;
    
    @Column(name = "companyid")
    private Integer companyId;
    
    @Column(name = "vehicleid")
    private Integer vehicleId;
    
    @Column(name = "driverid")
    private Integer driverId;
    
    @Column(name = "departurepoint")
    private String departurePoint;
    
    @Column(name = "departurecity")
    private String departureCity;
    
    @Column(name = "arrivalpoint")
    private String arrivalPoint;
    
    @Column(name = "arrivalcity")
    private String arrivalCity;
    
    @Column(name = "arrivaladdress")
    private String arrivalAddress;
    
    @Column(name = "distancekm")
    private Double distanceKm;
    
    @Column(name = "departuretime")
    private LocalTime departureTime;
    
    @Column(name = "departuredate")
    private LocalDate departureDate;
    
    @Column(name = "arrivaldatetime")
    private LocalDateTime arrivalDateTime;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "updatedby")
    private Integer updatedBy;
    
    @Column(name = "price")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyid", insertable = false, updatable = false)
    private BusCompany busCompany;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicleid", insertable = false, updatable = false)
    private Vehicle vehicle;

    // Constructors
    public Trip() {}
    
    public Trip(Integer companyId, Integer vehicleId, Integer driverId, String departureCity, 
                String arrivalCity, LocalDate departureDate, LocalTime departureTime) {
        this.companyId = companyId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
    }
    
    // Getters and Setters
    public Integer getTripId() {
        return tripId;
    }
    
    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }
    
    public Integer getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    
    public Integer getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public Integer getDriverId() {
        return driverId;
    }
    
    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }
    
    public String getDeparturePoint() {
        return departurePoint;
    }
    
    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }
    
    public String getDepartureCity() {
        return departureCity;
    }
    
    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }
    
    public String getArrivalPoint() {
        return arrivalPoint;
    }
    
    public void setArrivalPoint(String arrivalPoint) {
        this.arrivalPoint = arrivalPoint;
    }
    
    public String getArrivalCity() {
        return arrivalCity;
    }
    
    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }
    
    public String getArrivalAddress() {
        return arrivalAddress;
    }
    
    public void setArrivalAddress(String arrivalAddress) {
        this.arrivalAddress = arrivalAddress;
    }
    
    public Double getDistanceKm() {
        return distanceKm;
    }
    
    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }
    
    public LocalTime getDepartureTime() {
        return departureTime;
    }
    
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }
    
    public LocalDate getDepartureDate() {
        return departureDate;
    }
    
    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
    
    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }
    
    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
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
}