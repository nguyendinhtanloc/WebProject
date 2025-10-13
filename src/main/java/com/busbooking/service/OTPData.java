package com.busbooking.service;

import java.time.LocalDateTime;

public class OTPData {
    private String otp;
    private LocalDateTime expiryTime;
    
    public OTPData(String otp, LocalDateTime expiryTime) {
        this.otp = otp;
        this.expiryTime = expiryTime;
    }
    
    public String getOtp() {
        return otp;
    }
    
    public void setOtp(String otp) {
        this.otp = otp;
    }
    
    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }
    
    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }
}