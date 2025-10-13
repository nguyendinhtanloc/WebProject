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

@WebServlet("/test-supabase-connection")
public class TestSupabaseConnectionServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        // Test multiple Supabase connection strings
        String[] testConnections = {
            "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres",
            "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/postgres",
            "jdbc:postgresql://db.kognqhcifxbpihjrwocg.supabase.co:5432/postgres"
        };
        
        String user = "postgres.kognqhcifxbpihjrwocg";
        String password = "iBJSRPbSPT4MHxay";
        
        out.println("{");
        out.println("  \"connectionTests\": [");
        
        for (int i = 0; i < testConnections.length; i++) {
            String url = testConnections[i];
            out.println("    {");
            out.println("      \"url\": \"" + url + "\",");
            
            try {
                Class.forName("org.postgresql.Driver");
                Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT version()");
                
                if (rs.next()) {
                    String version = rs.getString(1);
                    out.println("      \"success\": true,");
                    out.println("      \"message\": \"Connection successful!\",");
                    out.println("      \"version\": \"" + version.substring(0, Math.min(50, version.length())) + "...\"");
                }
                
                rs.close();
                stmt.close();
                conn.close();
                
            } catch (Exception e) {
                out.println("      \"success\": false,");
                out.println("      \"error\": \"" + e.getMessage().replace("\"", "'") + "\"");
            }
            
            out.println("    }" + (i < testConnections.length - 1 ? "," : ""));
        }
        
        out.println("  ]");
        out.println("}");
    }
}