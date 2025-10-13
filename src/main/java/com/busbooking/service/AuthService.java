// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package com.busbooking.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class AuthService {
    private static final String SUPABASE_URL = "https://kognqhcifxbpihjrwocg.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZ25xaGNpZnhicGloanJ3b2NnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTY2MDkzNDEsImV4cCI6MjA3MjE4NTM0MX0.P2thkCIF98_bnuJdcy6Nwxp3-9uOSvbebx1rfkU2j04";
    private static final String SUPABASE_SERVICE_ROLE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZ25xaGNpZnhicGloanJ3b2NnIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1NjYwOTM0MSwiZXhwIjoyMDcyMTg1MzQxfQ.Q99X90uqc8I41Tay_DuSPV9XTvasWuJ5dNYVdF0NCIc";

    public AuthService() {
    }

    public void createUser(String name, String email, String password) throws Exception {
        String urlStr = "https://kognqhcifxbpihjrwocg.supabase.co/auth/v1/admin/users";
        HttpURLConnection conn = (HttpURLConnection)(new URL(urlStr)).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZ25xaGNpZnhicGloanJ3b2NnIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1NjYwOTM0MSwiZXhwIjoyMDcyMTg1MzQxfQ.Q99X90uqc8I41Tay_DuSPV9XTvasWuJ5dNYVdF0NCIc");
        conn.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZ25xaGNpZnhicGloanJ3b2NnIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1NjYwOTM0MSwiZXhwIjoyMDcyMTg1MzQxfQ.Q99X90uqc8I41Tay_DuSPV9XTvasWuJ5dNYVdF0NCIc");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("password", password);
        body.put("email_confirm", true);
        JSONObject metadata = new JSONObject();
        metadata.put("name", name);
        body.put("user_metadata", metadata);
        OutputStream os = conn.getOutputStream();

        try {
            os.write(body.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Throwable var12) {
            if (os != null) {
                try {
                    os.close();
                } catch (Throwable var11) {
                    var12.addSuppressed(var11);
                }
            }

            throw var12;
        }

        if (os != null) {
            os.close();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200 && responseCode != 201) {
            System.out.println("❌ [AuthService] Failed to create user. HTTP Code: " + responseCode);
            throw new RuntimeException("Supabase create user failed (HTTP " + responseCode + ")");
        } else {
            System.out.println("✅ [AuthService] User created successfully in Supabase Authentication!");
        }
    }

    public boolean signIn(String email, String password) throws Exception {
        try {
            String urlStr = "https://kognqhcifxbpihjrwocg.supabase.co/auth/v1/token?grant_type=password";
            HttpURLConnection conn = (HttpURLConnection)(new URL(urlStr)).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZ25xaGNpZnhicGloanJ3b2NnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTY2MDkzNDEsImV4cCI6MjA3MjE4NTM0MX0.P2thkCIF98_bnuJdcy6Nwxp3-9uOSvbebx1rfkU2j04");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            JSONObject body = new JSONObject();
            body.put("email", email);
            body.put("password", password);
            OutputStream os = conn.getOutputStream();

            try {
                os.write(body.toString().getBytes(StandardCharsets.UTF_8));
            } catch (Throwable var10) {
                if (os != null) {
                    try {
                        os.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }
                }

                throw var10;
            }

            if (os != null) {
                os.close();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("✅ [AuthService] User signed in successfully via Supabase Auth!");
                return true;
            } else {
                System.out.println("⚠️ [AuthService] Supabase sign-in failed. HTTP Code: " + responseCode);
                return false;
            }
        } catch (Exception var11) {
            System.out.println("❌ [AuthService] Error during Supabase sign-in: " + var11.getMessage());
            throw var11;
        }
    }
}