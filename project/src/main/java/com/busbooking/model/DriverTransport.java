package com.busbooking.model;

import com.busbooking.model.enums.DriverStatus;
import com.busbooking.util.PostgreSQLEnumType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("driver_id")
    private Integer driverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", nullable = false)
    @JsonIgnore
    private TransportCompany transportCompany;

    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(name = "phone")
    @JsonProperty("phone")
    private String phone;

    @Column(name = "licenseNo")
    @JsonProperty("license_no")
    private String licenseNo;

    @Column(name = "hireDate")
    @JsonProperty("hire_date")
    private LocalDate hireDate;

    @Column(name = "endDate")
    @JsonProperty("end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "DriverStatus")
    @Type(type = "pgsql_enum")
    @JsonProperty("status")
    private DriverStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedBy")
    @JsonIgnore
    private AppUser updatedBy;

    // Getter ảo để hiển thị ID thay vì toàn bộ đối tượng
    @Transient
    @JsonProperty("company_id")
    public Integer getCompanyId() {
        return (transportCompany != null) ? transportCompany.getCompanyId() : null;
    }

    @Transient
    @JsonProperty("updated_by")
    public Integer getUpdatedById() {
        return (updatedBy != null) ? updatedBy.getUserId() : null;
    }

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
