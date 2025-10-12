package com.busbooking.model;

import com.busbooking.model.enums.TripStatus;
import com.busbooking.util.PostgreSQLEnumType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.security.PrivateKey;
import java.time.LocalDateTime;

@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Entity
@Table(name = "TripTransportStatusLog")
public class TripTransportStatusLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logId")
    private Integer logId;

    @Column(name = "tripId", nullable = false)
    private Integer tripId;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changedBy")
    private AppUser changedBy;

    @CreationTimestamp
    @Column(name = "changedAt", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "tripSnapshot", columnDefinition = "TEXT")
    private String tripSnapshot;

    public TripTransportStatusLog() {
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTripSnapshot() {
        return tripSnapshot;
    }

    public void setTripSnapshot(String tripSnapshot) {
        this.tripSnapshot = tripSnapshot;
    }
}
