package com.busbooking.model;

import com.busbooking.model.enums.VehicleStatus;
import com.busbooking.util.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Entity
@Table(name = "VehicleTransport")
public class VehicleTransport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicleId")
    private Integer vehicleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", nullable = false)
    private TransportCompany transportCompany;

    @Column(name = "type")
    private String type;

    @Column(name = "licensePlate", nullable = false, unique = true)
    private String licensePlate;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "seatLayout", columnDefinition = "TEXT")
    private String seatLayout;

    @Column(name = "amenities", columnDefinition = "TEXT")
    private String amenities;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VehicleStatus")
    @Type(type = "pgsql_enum")
    private VehicleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedBy")
    private AppUser updatedBy;

    public VehicleTransport() {
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public TransportCompany getTransportCompany() {
        return transportCompany;
    }

    public void setTransportCompany(TransportCompany transportCompany) {
        this.transportCompany = transportCompany;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getSeatLayout() {
        return seatLayout;
    }

    public void setSeatLayout(String seatLayout) {
        this.seatLayout = seatLayout;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public AppUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(AppUser updatedBy) {
        this.updatedBy = updatedBy;
    }
}
