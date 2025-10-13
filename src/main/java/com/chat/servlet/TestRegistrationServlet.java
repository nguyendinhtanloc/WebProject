package com.chat.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/test-registration")
public class TestRegistrationServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        if ("register".equals(action)) {
            try {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String email = request.getParameter("email");
                String fullName = request.getParameter("fullName");
                String userType = request.getParameter("userType");
                
                // Direct database connection
                String url = "jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:5432/postgres";
                String dbUsername = "postgres.kognqhcifxbpihjrwocg";  
                String dbPassword = "iBJSRPbSPT4MHxay";
                
                Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                
                // Insert user with UUID
                String sql = "INSERT INTO users (username, password_hash, email, full_name, user_type) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password); // In real app, this should be hashed
                stmt.setString(3, email);
                stmt.setString(4, fullName);
                stmt.setString(5, userType);
                
                int result = stmt.executeUpdate();
                
                conn.close();
                
                if (result > 0) {
                    out.println("{");
                    out.println("  \"success\": true,");
                    out.println("  \"message\": \"User registered successfully!\",");
                    out.println("  \"username\": \"" + username + "\"");
                    out.println("}");
                } else {
                    out.println("{\"success\": false, \"message\": \"Registration failed\"}");
                }
                
            } catch (SQLException e) {
                out.println("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
            }
        } else {
            out.println("{\"success\": false, \"message\": \"Unknown action\"}");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h2>Test Registration Servlet</h2>");
        out.println("<form method='POST'>");
        out.println("<input type='hidden' name='action' value='register'>");
        out.println("Username: <input type='text' name='username' required><br><br>");
        out.println("Password: <input type='password' name='password' required><br><br>");
        out.println("Email: <input type='email' name='email' required><br><br>");
        out.println("Full Name: <input type='text' name='fullName' required><br><br>");
        out.println("User Type: <select name='userType'>");
        out.println("  <option value='CUSTOMER'>Customer</option>");
        out.println("  <option value='EMPLOYEE'>Employee</option>");
        out.println("</select><br><br>");
        out.println("<input type='submit' value='Register'>");
        out.println("</form>");
        out.println("</body></html>");
    }
}