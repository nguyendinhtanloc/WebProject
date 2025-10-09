package com.busbooking.model;

import com.busbooking.model.enums.LoginStatus;
import com.busbooking.util.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Entity
@Table(name = "LoginStatusLog")
public class LoginStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logId")
    private Integer logId;

    @ManyToOne
    @JoinColumn(name = "loginId", referencedColumnName = "logId")
    private LoginLog loginLog;

    @Enumerated(EnumType.STRING)
    @Column(name = "oldStatus", nullable = false, columnDefinition = "LoginStatus")
    @Type(type = "pgsql_enum")
    private LoginStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "newStatus", nullable = false, columnDefinition = "LoginStatus")
    @Type(type = "pgsql_enum")
    private LoginStatus newStatus;

    @ManyToOne
    @JoinColumn(name = "changedBy", referencedColumnName = "userId")
    private AppUser changedBy;

    @Column(name = "changedAt")
    private LocalDateTime changedAt;

    @Column(name = "note")
    private String note;

    public LoginStatusLog() {
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public LoginLog getLoginLog() {
        return loginLog;
    }

    public void setLoginLog(LoginLog loginLog) {
        this.loginLog = loginLog;
    }

    public LoginStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(LoginStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public LoginStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(LoginStatus newStatus) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
