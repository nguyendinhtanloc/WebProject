// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package com.busbooking.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(
        name = "appuser"
)
public class AppUser implements Serializable {
    private Integer userId;
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
    private LocalDateTime updatedAtOld;
    private String password;

    public AppUser() {
    }

    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }

        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }

    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "userid"
    )
    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(
            name = "useruuid"
    )
    public String getUserUuid() {
        return this.userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    @Column(
            nullable = false
    )
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(
            unique = true,
            nullable = false
    )
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Column(
            name = "status",
            columnDefinition = "userstatus"
    )
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(
            name = "birthdate"
    )
    public LocalDate getBirthDate() {
        return this.birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "gender", nullable = true)
    @org.hibernate.annotations.Type(type = "com.busbooking.entity.GenderEnumType")
    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Column(
            name = "createdat"
    )
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Column(
            name = "updatedat"
    )
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Column(
            name = "birth_date"
    )
    public LocalDate getBirthDateOld() {
        return this.birthDateOld;
    }

    public void setBirthDateOld(LocalDate birthDateOld) {
        this.birthDateOld = birthDateOld;
    }

    @Column(
            name = "user_id"
    )
    public Integer getUserIdOld() {
        return this.userId;
    }

    public void setUserIdOld(Integer userId) {
        this.userId = userId;
    }
}