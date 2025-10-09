package com.busbooking.model;

import com.busbooking.model.enums.SeatStatus;
import com.busbooking.util.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;

@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Entity
@Table(name = "SeatTransport")
public class SeatTransport {

    @Id
    @Column(name = "seatId")
    private String seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicleId", nullable = false)
    private VehicleTransport vehicleTransport;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "SeatStatus")
    @Type(type = "pgsql_enum")
    private SeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedBy")
    private AppUser updatedBy;

    public SeatTransport() {
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public VehicleTransport getVehicleTransport() {
        return vehicleTransport;
    }

    public void setVehicleTransport(VehicleTransport vehicleTransport) {
        this.vehicleTransport = vehicleTransport;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public AppUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(AppUser updatedBy) {
        this.updatedBy = updatedBy;
    }
}
