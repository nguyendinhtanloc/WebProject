package com.busbooking.service;

import com.busbooking.dao.AppUserDAO;
import com.busbooking.entity.AppUser;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AuthService {
    private final AppUserDAO userDAO = new AppUserDAO();
    private static final String SUPABASE_PROJECT = "YOUR_SUPABASE_PROJECT_REF";
    private static final String SUPABASE_ANON_KEY = "YOUR_SUPABASE_ANON_KEY";

    public void register(String name, String email) throws Exception {
        if (userDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        AppUser u = new AppUser();
        u.setName(name);
        u.setEmail(email);
        u.setStatus("inactive");
        u.setRole("user");
        userDAO.save(u);

        sendSignupRequest(email);
    }

    private void sendSignupRequest(String email) throws Exception {
        String urlStr = "https://" + SUPABASE_PROJECT + ".supabase.co/auth/v1/signup";
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

    public AppUser login(String email) {
        AppUser u = userDAO.findByEmail(email);
        if (u != null && "active".equalsIgnoreCase(u.getStatus())) {
            return u;
        }
        return null;
    }
}