package com.busbooking.service;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class SupabaseService {
    
    // Cấu hình Supabase - Đã cập nhật với thông tin thực tế
    private static final String SUPABASE_URL = "https://kognqhcifxbpihjrwocg.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZ25xaGNpZnhicGloanJ3b2NnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTY2MDkzNDEsImV4cCI6MjA3MjE4NTM0MX0.P2thkCIF98_bnuJdcy6Nwxp3-9uOSvbebx1rfkU2j04";
    private static final String SUPABASE_SERVICE_ROLE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZ25xaGNpZnhicGloanJ3b2NnIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1NjYwOTM0MSwiZXhwIjoyMDcyMTg1MzQxfQ.Q99X90uqc8I41Tay_DuSPV9XTvasWuJ5dNYVdF0NCIc"; // Cần lấy service role key từ Supabase Dashboard
    
    /**
     * Tạo user trong Supabase Authentication
     * @param email Email của user
     * @param password Mật khẩu của user
     * @param name Tên của user
     * @param phone Số điện thoại của user
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public static boolean createUserInSupabase(String email, String password, String name, String phone) {
        System.out.println("🚀 Creating Supabase user (auto-confirm): " + email);
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost(SUPABASE_URL + "/auth/v1/admin/users");
            request.addHeader("Authorization", "Bearer " + SUPABASE_SERVICE_ROLE_KEY);
            request.addHeader("apikey", SUPABASE_SERVICE_ROLE_KEY);
            request.addHeader("Content-Type", "application/json");
            JSONObject payload = new JSONObject();
            payload.put("email", email);
            payload.put("password", password);
            payload.put("email_confirm", true);
            
            // Sử dụng user_metadata - Supabase sẽ tự động copy vào raw_user_meta_data
            JSONObject metadata = new JSONObject();
            metadata.put("name", name);
            metadata.put("phone", phone);
            payload.put("user_metadata", metadata);
            
            // Debug: In ra payload để kiểm tra
            System.out.println("📤 DEBUG: Supabase payload: " + payload.toString());
            System.out.println("📤 DEBUG: Name = '" + name + "', Phone = '" + phone + "'");
            
            request.setEntity(new StringEntity(payload.toString(), "UTF-8"));
            HttpResponse response = httpClient.execute(request);
            int code = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            System.out.println("📩 Supabase response (" + code + "): " + body);
            
            // Xử lý các trường hợp thành công
            if (code == 200 || code == 201) {
                return true;
            }
            
            // Xử lý trường hợp email đã tồn tại
            if (code == 422 && body.contains("email_exists")) {
                System.out.println("⚠️ WARNING: Email already exists in Supabase, but treating as success for registration flow");
                return true; // Coi như thành công vì user đã tồn tại
            }
            
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Kiểm tra email đã tồn tại trong Supabase chưa
     * @param email Email cần kiểm tra
     * @return true nếu email đã tồn tại, false nếu chưa
     */
    public static boolean checkEmailExists(String email) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost(SUPABASE_URL + "/auth/v1/admin/users");
            
            // Set headers để query users
            request.addHeader("Authorization", "Bearer " + SUPABASE_SERVICE_ROLE_KEY);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("apikey", SUPABASE_ANON_KEY);
            
            // Thực hiện request để lấy danh sách users
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            
            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                // Kiểm tra xem email có trong response không
                return responseBody.contains("\"email\":\"" + email + "\"");
            }
            
            return false;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Đăng nhập user với Supabase
     * @param email Email của user
     * @param password Mật khẩu của user
     * @return Access token nếu thành công, null nếu thất bại
     */
    public static String signIn(String email, String password) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost(SUPABASE_URL + "/auth/v1/token?grant_type=password");
            
            // Set headers
            request.addHeader("Content-Type", "application/json");
            request.addHeader("apikey", SUPABASE_ANON_KEY);
            
            // Tạo JSON payload
            JSONObject payload = new JSONObject();
            payload.put("email", email);
            payload.put("password", password);
            
            // Set entity
            StringEntity entity = new StringEntity(payload.toString(), "UTF-8");
            request.setEntity(entity);
            
            // Thực hiện request
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            
            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject jsonResponse = new JSONObject(responseBody);
                return jsonResponse.getString("access_token");
            }
            
            return null;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}