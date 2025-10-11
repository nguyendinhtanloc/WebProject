package com.busbooking.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class AuthService {
    // Removed database dependency - AuthService only handles Supabase API

    // Cấu hình Supabase - đồng bộ với SupabaseService
    private static final String SUPABASE_URL = "https://kognqhcifxbpihjrwocg.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZ25xaGNpZnhicGloanJ3b2NnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTY2MDkzNDEsImV4cCI6MjA3MjE4NTM0MX0.P2thkCIF98_bnuJdcy6Nwxp3-9uOSvbebx1rfkU2j04";

    public void register(String name, String email) throws Exception {
        sendSignupRequest(email);
    }

    private void sendSignupRequest(String email) throws Exception {
        String urlStr = SUPABASE_URL + "/auth/v1/signup";
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("apikey", SUPABASE_ANON_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("password", java.util.UUID.randomUUID().toString());
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.toString().getBytes(StandardCharsets.UTF_8));
        }

        if (conn.getResponseCode() >= 400)
            throw new RuntimeException("Supabase signup failed");
    }

    /**
     * Sign in với Supabase Authentication
     */
    public boolean signIn(String email, String password) throws Exception {
        try {
            String urlStr = SUPABASE_URL + "/auth/v1/token?grant_type=password";
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("apikey", SUPABASE_ANON_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject body = new JSONObject();
            body.put("email", email);
            body.put("password", password);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.toString().getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                // Đăng nhập thành công với Supabase
                return true;
            } else {
                // Đăng nhập thất bại
                System.out.println("Supabase signin failed with code: " + responseCode);
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error during Supabase signin: " + e.getMessage());
            throw e;
        }
    }
}