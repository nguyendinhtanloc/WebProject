package com.busbooking.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "appuser")
public class AppUser {

    @Id
    @Column(name = "useruuid")
    private UUID useruuid;

    private String name;

    @Column(unique = true)
    private String email;

    private String phone;
    private String password;
    private String role;
    private String status; // active/inactive
    private java.sql.Date birth_date;
    private String address;
    private String gender;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @PrePersist
    public void onCreate() {
        if (useruuid == null)
            useruuid = UUID.randomUUID();
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updated_at = LocalDateTime.now();
    }

    // Getters và Setters
    public UUID getUseruuid() {
        return useruuid;
    }

    public void setUseruuid(UUID useruuid) {
        this.useruuid = useruuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public java.sql.Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(java.sql.Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }
}