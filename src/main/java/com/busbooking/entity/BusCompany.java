package com.busbooking.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transportcompany")
public class BusCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "companyid")
    private Integer companyId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "contactinfo")
    private String contactInfo;
    
    @Column(name = "contact")
    private String contact;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "createdat")
    private LocalDateTime createdAt;
    
    @Column(name = "updatedby")
    private Integer updatedBy;
    
    // Constructors
    public BusCompany() {}
    
    public BusCompany(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
    }
    
    // Getters and Setters
    public Integer getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    public String getContact() {
        return contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Integer getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
}