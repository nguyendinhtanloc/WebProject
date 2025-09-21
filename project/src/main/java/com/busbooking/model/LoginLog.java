package com.busbooking.model;

import java.time.LocalDateTime;

public class LoginLog {
    private long id;
    private String email;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private String ipAddress;
    private String userAgent;
    private String loginTimeFormatted;
    private String logoutTimeFormatted;

    // Getter & Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getLoginTimeFormatted() {
        return loginTimeFormatted;
    }

    public void setLoginTimeFormatted(String loginTimeFormatted) {
        this.loginTimeFormatted = loginTimeFormatted;
    }

    public String getLogoutTimeFormatted() {
        return logoutTimeFormatted;
    }

    public void setLogoutTimeFormatted(String logoutTimeFormatted) {
        this.logoutTimeFormatted = logoutTimeFormatted;
    }

}
