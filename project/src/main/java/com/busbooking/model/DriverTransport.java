package com.busbooking.model;

import com.busbooking.model.enums.DriverStatus;
import com.busbooking.util.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Entity
@Table(name = "DriverTransport")
public class DriverTransport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driverId")
    private Integer driverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", nullable = false)
    private TransportCompany transportCompany;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "licenseNo")
    private String licenseNo;

    @Column(name = "hireDate")
    private LocalDate hireDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "DriverStatus")
    @Type(type = "pgsql_enum")
    private DriverStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedBy")
    private AppUser updatedBy;

    public DriverTransport() {
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public TransportCompany getTransportCompany() {
        return transportCompany;
    }

    public void setTransportCompany(TransportCompany transportCompany) {
        this.transportCompany = transportCompany;
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

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public AppUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(AppUser updatedBy) {
        this.updatedBy = updatedBy;
    }
}
