// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package com.busbooking.service;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class OTPService {
    private static final String OTP_CHARACTERS = "1234567890";
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final Map<String, OTPData> otpStorage = new HashMap();

    public OTPService() {
    }

    public static String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for(int i = 0; i < 6; ++i) {
            int index = random.nextInt("1234567890".length());
            otp.append("1234567890".charAt(index));
        }

        return otp.toString();
    }

    public static void storeOTP(String email, String otp) {
        String normalizedEmail = email.toLowerCase().trim();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5L);
        otpStorage.put(normalizedEmail, new OTPData(otp, expiryTime));
        System.out.println("\ud83d\udcbe OTPService.storeOTP - Stored OTP for email: " + normalizedEmail + ", OTP: '" + otp + "', Expiry: " + String.valueOf(expiryTime));
        System.out.println("\ud83d\udccb OTPService.storeOTP - Total stored OTPs: " + otpStorage.size());
    }

    public static boolean verifyOTP(String email, String inputOTP) {
        String normalizedEmail = email.toLowerCase().trim();
        System.out.println("\ud83d\udd0d OTPService.verifyOTP - Email: " + normalizedEmail + ", Input: '" + inputOTP + "'");
        OTPData otpData = (OTPData)otpStorage.get(normalizedEmail);
        if (otpData == null) {
            System.out.println("❌ OTPService.verifyOTP - No OTP found for email: " + normalizedEmail);
            System.out.println("\ud83d\udccb OTPService.verifyOTP - Current storage keys: " + String.valueOf(otpStorage.keySet()));
            return false;
        } else {
            System.out.println("✅ OTPService.verifyOTP - Found OTP data for email: " + normalizedEmail);
            System.out.println("\ud83d\udcdd OTPService.verifyOTP - Stored OTP: '" + otpData.getOtp() + "'");
            System.out.println("⏰ OTPService.verifyOTP - Expiry time: " + String.valueOf(otpData.getExpiryTime()));
            System.out.println("⏰ OTPService.verifyOTP - Current time: " + String.valueOf(LocalDateTime.now()));
            if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
                System.out.println("❌ OTPService.verifyOTP - OTP expired for email: " + normalizedEmail);
                otpStorage.remove(normalizedEmail);
                return false;
            } else {
                System.out.println("✅ OTPService.verifyOTP - OTP not expired");
                boolean otpMatch = otpData.getOtp().equals(inputOTP);
                System.out.println("\ud83d\udd0d OTPService.verifyOTP - OTP match: " + otpMatch);
                PrintStream var10000 = System.out;
                String var10001 = otpData.getOtp();
                var10000.println("\ud83d\udd0d OTPService.verifyOTP - Stored: '" + var10001 + "' vs Input: '" + inputOTP + "'");
                if (otpMatch) {
                    System.out.println("✅ OTPService.verifyOTP - OTP verified successfully, removing from storage");
                    otpStorage.remove(normalizedEmail);
                    return true;
                } else {
                    System.out.println("❌ OTPService.verifyOTP - OTP mismatch");
                    return false;
                }
            }
        }
    }

    public static void removeOTP(String email) {
        String normalizedEmail = email.toLowerCase().trim();
        otpStorage.remove(normalizedEmail);
        System.out.println("\ud83d\uddd1️ OTPService.removeOTP - Removed OTP for email: " + normalizedEmail);
    }

    public static void debugOTPStorage() {
        System.out.println("\ud83d\udccb OTPService.debugOTPStorage - Current OTP storage:");
        Iterator var0 = otpStorage.entrySet().iterator();

        while(var0.hasNext()) {
            Map.Entry<String, OTPData> entry = (Map.Entry)var0.next();
            PrintStream var10000 = System.out;
            String var10001 = (String)entry.getKey();
            var10000.println("    Email: " + var10001 + " -> OTP: " + ((OTPData)entry.getValue()).getOtp() + ", Expiry: " + String.valueOf(((OTPData)entry.getValue()).getExpiryTime()));
        }

        System.out.println("\ud83d\udccb Total stored OTPs: " + otpStorage.size());
    }
}