package com.busbooking.model;

import com.busbooking.model.enums.TripStatus;
import com.busbooking.util.PostgreSQLEnumType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Entity
@Table(name = "TripTransportStatusLog")
public class TripTransportStatusLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logId")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tripId", nullable = false)
    private TripTransport trip;

    @Enumerated(EnumType.STRING)
    @Column(name = "oldStatus", columnDefinition = "TripStatus")
    @Type(type = "pgsql_enum")
    private TripStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "newStatus", columnDefinition = "TripStatus")
    @Type(type = "pgsql_enum")
    private TripStatus newStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changedBy")
    private AppUser changedBy;

    @CreationTimestamp
    @Column(name = "changedAt")
    private LocalDateTime changedAt;

    public TripTransportStatusLog() {
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public TripTransport getTrip() {
        return trip;
    }

    public void setTrip(TripTransport trip) {
        this.trip = trip;
    }

    public TripStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(TripStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public TripStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(TripStatus newStatus) {
        this.newStatus = newStatus;
    }

    public AppUser getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(AppUser changedBy) {
        this.changedBy = changedBy;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
