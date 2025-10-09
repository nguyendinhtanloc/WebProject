package com.busbooking.model;

import com.busbooking.model.enums.LoginStatus;
import com.busbooking.util.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Entity
@Table(name = "LoginLog")
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logId")
    private Integer logId;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private AppUser user;

    @Column(name = "loginTime")
    private LocalDateTime loginTime;

    @Column(name = "logoutTime")
    private LocalDateTime logoutTime;

    @Column(name = "ipAddress")
    private String ipAddress;

    @Column(name = "userAgent")
    private String userAgent;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "LoginStatus")
    @Type(type = "pgsql_enum")
    private LoginStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    public LoginLog() {
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LoginStatus getStatus() {
        return status;
    }

    public void setStatus(LoginStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
