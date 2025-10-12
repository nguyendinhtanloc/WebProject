package com.busbooking.service;

import java.time.LocalDateTime;
import java.util.Random;

public class OTPService {
    private static final String OTP_CHARACTERS = "1234567890";
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;
    
    // In-memory storage for OTP (trong production nen dung Redis hoac database)
    // Use a thread-safe map and normalize email keys (trim + lowercase) to avoid mismatches
    private static final java.util.concurrent.ConcurrentMap<String, OTPData> otpStorage = new java.util.concurrent.ConcurrentHashMap<>();
    
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
        if (email == null) {
            System.out.println("❌ OTPService.storeOTP - email is null, cannot store OTP");
            return;
        }
        String key = normalizeEmail(email);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);
        otpStorage.put(key, new OTPData(otp, expiryTime));
        System.out.println("💾 OTPService.storeOTP - Stored OTP for email(key): " + key + ", Original email: '" + email + "', OTP: '" + otp + "', Expiry: " + expiryTime);
        System.out.println("📋 OTPService.storeOTP - Total stored OTPs: " + otpStorage.size());
    }
    
    // Xac thuc OTP
    public static boolean verifyOTP(String email, String inputOTP) {
        System.out.println("🔍 OTPService.verifyOTP - Email: " + email + ", Input: '" + inputOTP + "'");
        
        if (email == null) {
            System.out.println("❌ OTPService.verifyOTP - email is null");
            System.out.println("📋 OTPService.verifyOTP - Current storage keys: " + otpStorage.keySet());
            return false;
        }
        String key = normalizeEmail(email);
        OTPData otpData = otpStorage.get(key);
        
        if (otpData == null) {
            System.out.println("❌ OTPService.verifyOTP - No OTP found for email: " + email + " (key=" + key + ")");
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
        if (email != null) {
            otpStorage.remove(normalizeEmail(email));
        }
    }

    // Normalize email used as key (trim + lowercase) to avoid mismatches from user input
    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
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
