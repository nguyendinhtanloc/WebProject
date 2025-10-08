package com.busbooking.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "bus_company")
public class BusCompany {
    @Id
    @Column(name = "company_id")
    private UUID companyId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "contact")
    private String contact;
    
    @Column(name = "status")
    private String status = "active";
    
    // Constructors
    public BusCompany() {}
    
    public BusCompany(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
    }
    
    // Getters and Setters
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
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
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
}