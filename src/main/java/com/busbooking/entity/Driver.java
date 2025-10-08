package com.busbooking.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "driver")
public class Driver {
    @Id
    @Column(name = "driver_id")
    private UUID driverId;
    
    @Column(name = "company_id")
    private UUID companyId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "license_no", nullable = false, unique = true)
    private String licenseNo;
    
    @Column(name = "experience_years")
    private Integer experienceYears;
    
    @Column(name = "status")
    private String status = "active";
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private BusCompany busCompany;
    
    // Constructors
    public Driver() {}
    
    public Driver(UUID companyId, String name, String phone, String licenseNo, Integer experienceYears) {
        this.companyId = companyId;
        this.name = name;
        this.phone = phone;
        this.licenseNo = licenseNo;
        this.experienceYears = experienceYears;
    }
    
    // Getters and Setters
    public UUID getDriverId() {
        return driverId;
    }
    
    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }
    
    public UUID getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getLicenseNo() {
        return licenseNo;
    }
    
    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }
    
    public Integer getExperienceYears() {
        return experienceYears;
    }
    
    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BusCompany getBusCompany() {
        return busCompany;
    }
    
    public void setBusCompany(BusCompany busCompany) {
        this.busCompany = busCompany;
    }
}