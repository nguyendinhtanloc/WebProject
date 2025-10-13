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
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/simple-db-test")
public class SimpleDbTestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            Class.forName("org.postgresql.Driver");
            
            String url = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres";
            String user = "postgres.kognqhcifxbpihjrwocg";
            String password = "iBJSRPbSPT4MHxay";
            
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 as test_value");
            
            if (rs.next()) {
                out.println("{");
                out.println("  \"success\": true,");
                out.println("  \"message\": \"Database connection successful!\",");
                out.println("  \"testValue\": " + rs.getInt("test_value"));
                out.println("}");
            }
            
            rs.close();
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