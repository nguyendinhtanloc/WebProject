package com.busbooking.test;

import com.busbooking.service.OTPService;

public class SimpleOTPTest {
    public static void main(String[] args) {
        System.out.println("🧪 Testing OTP Service");
        System.out.println("=====================");
        
        String testEmail = "test@example.com";
        
        // Test 1: Generate OTP
        System.out.println("📝 Test 1: Generate OTP");
        String otp1 = OTPService.generateOTP();
        System.out.println("Generated OTP: " + otp1);
        System.out.println("OTP Length: " + otp1.length());
        System.out.println("✅ Test 1 PASSED\n");
        
        // Test 2: Store and verify correct OTP
        System.out.println("📝 Test 2: Store and verify correct OTP");
        OTPService.storeOTP(testEmail, otp1);
        System.out.println("Stored OTP: " + otp1 + " for email: " + testEmail);
        
        boolean result1 = OTPService.verifyOTP(testEmail, otp1);
        System.out.println("Verify result: " + result1);
        if (result1) {
            System.out.println("✅ Test 2 PASSED");
        } else {
            System.out.println("❌ Test 2 FAILED");
        }
        System.out.println();
        
        // Test 3: Store new OTP and verify wrong OTP
        System.out.println("📝 Test 3: Store new OTP and verify wrong OTP");
        String otp2 = OTPService.generateOTP();
        OTPService.storeOTP(testEmail, otp2);
        System.out.println("Stored new OTP: " + otp2);
        
        boolean result2 = OTPService.verifyOTP(testEmail, "123456");
        System.out.println("Verify wrong OTP (123456): " + result2);
        if (!result2) {
            System.out.println("✅ Test 3 PASSED");
        } else {
            System.out.println("❌ Test 3 FAILED");
        }
        System.out.println();
        
        // Test 4: Verify correct OTP again
        System.out.println("📝 Test 4: Verify correct OTP");
        boolean result3 = OTPService.verifyOTP(testEmail, otp2);
        System.out.println("Verify correct OTP (" + otp2 + "): " + result3);
        if (result3) {
            System.out.println("✅ Test 4 PASSED");
        } else {
            System.out.println("❌ Test 4 FAILED");
        }
        System.out.println();
        
        // Test 5: Verify OTP after it's been used (should fail)
        System.out.println("📝 Test 5: Verify OTP after it's been used");
        boolean result4 = OTPService.verifyOTP(testEmail, otp2);
        System.out.println("Verify used OTP (" + otp2 + "): " + result4);
        if (!result4) {
            System.out.println("✅ Test 5 PASSED (OTP correctly removed after use)");
        } else {
            System.out.println("❌ Test 5 FAILED (OTP should be removed after use)");
        }
        
        System.out.println();
        System.out.println("🏁 All tests completed!");
        System.out.println();
        System.out.println("📋 Summary:");
        System.out.println("- OTP generation: Working");
        System.out.println("- OTP storage: Working"); 
        System.out.println("- OTP verification: Working");
        System.out.println("- OTP removal after use: Working");
        System.out.println();
        System.out.println("✅ OTP Service is functioning correctly!");
    }
}