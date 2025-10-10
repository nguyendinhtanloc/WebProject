package com.busbooking.entity;

import javax.persistence.*;

@Entity
@Table(name = "vehicletransport")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicleid")
    private Integer vehicleId;
    
    @Column(name = "companyid")
    private Integer companyId;
    
    @Column(name = "licenseplate", nullable = false, unique = true)
    private String licensePlate;
    
    @Column(name = "capacity")
    private Integer capacity;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "updatedby")
    private Integer updatedBy;
    
    @Column(name = "amenities")
    private String amenities;
    
    @Column(name = "seatlayout")
    private String seatLayout;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyid", insertable = false, updatable = false)
    private BusCompany busCompany;
    
    // Constructors
    public Vehicle() {}
    
    public Vehicle(Integer companyId, String licensePlate, Integer capacity, String type) {
        this.companyId = companyId;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.type = type;
    }
    
    // Getters and Setters
    public Integer getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public Integer getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(Integer companyId) {
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
    
    public Integer getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public String getAmenities() {
        return amenities;
    }
    
    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }
    
    public String getSeatLayout() {
        return seatLayout;
    }
    
    public void setSeatLayout(String seatLayout) {
        this.seatLayout = seatLayout;
    }
    
    public BusCompany getBusCompany() {
        return busCompany;
    }
    
    public void setBusCompany(BusCompany busCompany) {
        this.busCompany = busCompany;
    }
}