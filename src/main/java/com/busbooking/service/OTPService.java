package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPService {
    private static final String OTP_CHARACTERS = "1234567890";
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;
    
    // In-memory storage for OTP (trong production nen dung Redis hoac database)
    private static final Map<String, OTPData> otpStorage = new HashMap<>();
    
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
        System.out.println("💾 OTPService.storeOTP - Stored OTP for email: " + email + ", OTP: '" + otp + "', Expiry: " + expiryTime);
        System.out.println("📋 OTPService.storeOTP - Total stored OTPs: " + otpStorage.size());
    }
    
    // Xac thuc OTP
    public static boolean verifyOTP(String email, String inputOTP) {
        System.out.println("🔍 OTPService.verifyOTP - Email: " + email + ", Input: '" + inputOTP + "'");
        
        OTPData otpData = otpStorage.get(email);
        
        if (otpData == null) {
            System.out.println("❌ OTPService.verifyOTP - No OTP found for email: " + email);
            System.out.println("📋 OTPService.verifyOTP - Current storage keys: " + otpStorage.keySet());
            return false; // Khong tim thay OTP
        }
        
        System.out.println("✅ OTPService.verifyOTP - Found OTP data for email: " + email);
        System.out.println("📝 OTPService.verifyOTP - Stored OTP: '" + otpData.getOtp() + "'");
        System.out.println("⏰ OTPService.verifyOTP - Expiry time: " + otpData.getExpiryTime());
        System.out.println("⏰ OTPService.verifyOTP - Current time: " + LocalDateTime.now());
        
        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            System.out.println("❌ OTPService.verifyOTP - OTP expired for email: " + email);
            otpStorage.remove(email); // Xoa OTP het han
            return false;
        }
        
        System.out.println("✅ OTPService.verifyOTP - OTP not expired");
        
        boolean otpMatch = otpData.getOtp().equals(inputOTP);
        System.out.println("🔍 OTPService.verifyOTP - OTP match: " + otpMatch);
        System.out.println("🔍 OTPService.verifyOTP - Stored: '" + otpData.getOtp() + "' vs Input: '" + inputOTP + "'");
        
        if (otpMatch) {
            System.out.println("✅ OTPService.verifyOTP - OTP verified successfully, removing from storage");
            otpStorage.remove(email); // Xoa OTP sau khi su dung thanh cong
            return true;
        }
        
        System.out.println("❌ OTPService.verifyOTP - OTP mismatch");
        return false;
    }
    
    // Xoa OTP (neu can)
    public static void removeOTP(String email) {
        otpStorage.remove(email);
    }
    
    // Inner class de luu tru OTP data
    private static class OTPData {
        private final String otp;
        private final LocalDateTime expiryTime;
        
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
