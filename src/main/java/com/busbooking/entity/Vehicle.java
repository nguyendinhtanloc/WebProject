package com.busbooking.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @Column(name = "vehicle_id")
    private UUID vehicleId;
    
    @Column(name = "company_id")
    private UUID companyId;
    
    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;
    
    @Column(name = "capacity")
    private Integer capacity;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "status")
    private String status = "available";
    
    @Column(name = "lane")
    private Integer lane = 3;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private BusCompany busCompany;
    
    // Constructors
    public Vehicle() {}
    
    public Vehicle(UUID companyId, String licensePlate, Integer capacity, String type) {
        this.companyId = companyId;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.type = type;
    }
    
    // Getters and Setters
    public UUID getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public UUID getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getLane() {
        return lane;
    }
    
    public void setLane(Integer lane) {
        this.lane = lane;
    }
    
    public BusCompany getBusCompany() {
        return busCompany;
    }
    
    public void setBusCompany(BusCompany busCompany) {
        this.busCompany = busCompany;
    }
}