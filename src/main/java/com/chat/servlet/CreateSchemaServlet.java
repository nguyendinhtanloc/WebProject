package com.chat.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@WebServlet("/create-schema")
public class CreateSchemaServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            Class.forName("org.postgresql.Driver");
            
            String url = "jdbc:postgresql://db.kognqhcifxbpihjrwocg.supabase.co:5432/postgres";
            String user = "postgres.kognqhcifxbpihjrwocg";
            String password = "iBJSRPbSPT4MHxay";
            
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            
            // Tạo extension UUID nếu chưa có
            try {
                stmt.execute("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\"");
            } catch (Exception e) {
                // Ignore if extension already exists
            }
            
            // Tạo bảng users nếu chưa có
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id UUID DEFAULT uuid_generate_v4() PRIMARY KEY," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "password_hash VARCHAR(255) NOT NULL," +
                "full_name VARCHAR(100)," +
                "phone VARCHAR(20)," +
                "user_type VARCHAR(20) NOT NULL," +
                "profile_picture VARCHAR(255)," +
                "is_online BOOLEAN DEFAULT FALSE," +
                "last_seen TIMESTAMP," +
                "created_at TIMESTAMP DEFAULT NOW()" +
                ")";
            
            stmt.execute(createUsersTable);
            
            out.println("{");
            out.println("  \"success\": true,");
            out.println("  \"message\": \"Schema created successfully!\"");
            out.println("}");
            
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            out.println("{");
            out.println("  \"success\": false,");
            out.println("  \"error\": \"" + e.getMessage().replace("\"", "'") + "\"");
            out.println("}");
            e.printStackTrace();
        }
    }
}