package com.busbooking.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "appuser")
public class AppUser implements Serializable {

    private String userUuid;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;
    private LocalDate birthDate;
    private String address;
    private String gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate birthDateOld;
    private LocalDateTime createdAtOld;
    private String password;

    public AppUser() {
        this.userUuid = UUID.randomUUID().toString();
    }

    // ===== Lifecycle hooks =====
    @PrePersist
    public void onCreate() {
        if (userUuid == null) {
            userUuid = UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ===== Getters & Setters =====

    @Id
    @Column(name = "useruuid")
    public String getUserUuid() {
        return userUuid;
    }
    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(unique = true, nullable = false)
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

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "birthdate")
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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

    @Column(name = "createdat")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "updatedat")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Mapping cho birth_date column trong Supabase
    @Column(name = "birth_date")
    public LocalDate getBirthDateOld() {
        return birthDateOld;
    }
    public void setBirthDateOld(LocalDate birthDateOld) {
        this.birthDateOld = birthDateOld;
    }

    // Mapping cho created_at column trong Supabase
    @Column(name = "created_at")
    public LocalDateTime getCreatedAtOld() {
        return createdAtOld;
    }
    public void setCreatedAtOld(LocalDateTime createdAtOld) {
        this.createdAtOld = createdAtOld;
    }
}