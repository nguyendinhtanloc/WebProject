package com.busbooking.model;

import com.busbooking.model.enums.TripStatus;
import com.busbooking.util.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Entity
@Table(name = "TripTransport")
public class TripTransport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tripId")
    private Integer tripId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", nullable = false)
    private TransportCompany transportCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicleId")
    private VehicleTransport vehicleTransport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driverId")
    private DriverTransport driverTransport;

    @Column(name = "departurePoint")
    private String departurePoint;

    @Column(name = "departureCity")
    private String departureCity;

    @Column(name = "departureAddress")
    private String departureAddress;

    @Column(name = "arrivalPoint")
    private String arrivalPoint;

    @Column(name = "arrivalCity")
    private String arrivalCity;

    @Column(name = "arrivalAddress")
    private String arrivalAddress;

    @Column(name = "distanceKm", nullable = false)
    private BigDecimal distanceKm;

    @Column(name = "departureDatetime")
    private LocalDateTime departureDatetime;

    @Column(name = "arrivalDatetime")
    private LocalDateTime arrivalDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "TripStatus")
    @Type(type = "pgsql_enum") 
    private TripStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedBy")
    private AppUser updatedBy;

    @Transient
    private List<SeatTransport> seats;

    public TripTransport() {
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public TransportCompany getTransportCompany() {
        return transportCompany;
    }

    public void setTransportCompany(TransportCompany transportCompany) {
        this.transportCompany = transportCompany;
    }

    public VehicleTransport getVehicleTransport() {
        return vehicleTransport;
    }

    public void setVehicleTransport(VehicleTransport vehicleTransport) {
        this.vehicleTransport = vehicleTransport;
    }

    public DriverTransport getDriverTransport() {
        return driverTransport;
    }

    public void setDriverTransport(DriverTransport driverTransport) {
        this.driverTransport = driverTransport;
    }

    public String getDeparturePoint() {
        return departurePoint;
    }

    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDepartureAddress() {
        return departureAddress;
    }

    public void setDepartureAddress(String departureAddress) {
        this.departureAddress = departureAddress;
    }

    public String getArrivalPoint() {
        return arrivalPoint;
    }

    public void setArrivalPoint(String arrivalPoint) {
        this.arrivalPoint = arrivalPoint;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getArrivalAddress() {
        return arrivalAddress;
    }

    public void setArrivalAddress(String arrivalAddress) {
        this.arrivalAddress = arrivalAddress;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public LocalDateTime getDepartureDatetime() {
        return departureDatetime;
    }

    public void setDepartureDatetime(LocalDateTime departureDatetime) {
        this.departureDatetime = departureDatetime;
    }

    public LocalDateTime getArrivalDatetime() {
        return arrivalDatetime;
    }

    public void setArrivalDatetime(LocalDateTime arrivalDatetime) {
        this.arrivalDatetime = arrivalDatetime;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public AppUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(AppUser updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<SeatTransport> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatTransport> seats) {
        this.seats = seats;
    }
}
