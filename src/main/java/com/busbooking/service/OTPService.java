package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPService {
    private static final String OTP_CHARACTERS = "ABCDEFGHIK1234567890";
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;
    
    // In-memory storage for OTP (trong production nen dung Redis hoac database)
    private static Map<String, OTPData> otpStorage = new HashMap<>();
    
    // Tao OTP ngau nhien
    public static String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        
        for (int i = 0; i < OTP_LENGTH; i++) {
            int index = random.nextInt(OTP_CHARACTERS.length());
            otp.append(OTP_CHARACTERS.charAt(index));
        }
        
        return otp.toString();
    }
    
    // Luu OTP voi thoi han
    public static void storeOTP(String email, String otp) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);
        otpStorage.put(email, new OTPData(otp, expiryTime));
    }
    
    // Xac thuc OTP
    public static boolean verifyOTP(String email, String inputOTP) {
        OTPData otpData = otpStorage.get(email);
        
        if (otpData == null) {
            return false; // Khong tim thay OTP
        }
        
        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            otpStorage.remove(email); // Xoa OTP het han
            return false;
        }
        
        if (otpData.getOtp().equals(inputOTP)) {
            otpStorage.remove(email); // Xoa OTP sau khi su dung thanh cong
            return true;
        }
        
        return false;
    }
    
    // Xoa OTP (neu can)
    public static void removeOTP(String email) {
        otpStorage.remove(email);
    }
    
    // Inner class de luu tru OTP data
    private static class OTPData {
        private String otp;
        private LocalDateTime expiryTime;
        
        public OTPData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
        
        public String getOtp() {
            return otp;
        }
        
        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }
}
