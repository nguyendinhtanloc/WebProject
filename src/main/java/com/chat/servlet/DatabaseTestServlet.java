package com.chat.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


@WebServlet("/database-test")
public class DatabaseTestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try {
            // Use database connection info from persistence.xml
            String url = "jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:5432/postgres";
            String username = "postgres.kognqhcifxbpihjrwocg";  
            String password = "iBJSRPbSPT4MHxay";
            
            out.println("<html><body>");
            out.println("<h2>Database Schema Information</h2>");
            
            // Connect to database
            Connection conn = DriverManager.getConnection(url, username, password);
            out.println("<p><strong>Database Connection:</strong> SUCCESS</p>");
            
            // Check users table structure
            out.println("<h3>Users Table Structure:</h3>");
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "users", null);
            out.println("<table border='1'>");
            out.println("<tr><th>Column Name</th><th>Data Type</th><th>Type Name</th></tr>");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                int dataType = columns.getInt("DATA_TYPE");
                String typeName = columns.getString("TYPE_NAME");
                out.println("<tr><td>" + columnName + "</td><td>" + dataType + "</td><td>" + typeName + "</td></tr>");
            }
            out.println("</table>");
            
            // Check chats table structure  
            out.println("<h3>Chats Table Structure:</h3>");
            columns = metaData.getColumns(null, null, "chats", null);
            out.println("<table border='1'>");
            out.println("<tr><th>Column Name</th><th>Data Type</th><th>Type Name</th></tr>");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                int dataType = columns.getInt("DATA_TYPE");
                String typeName = columns.getString("TYPE_NAME");
                out.println("<tr><td>" + columnName + "</td><td>" + dataType + "</td><td>" + typeName + "</td></tr>");
            }
            out.println("</table>");
            
            conn.close();
            out.println("</body></html>");
            
        } catch (Exception e) {
            out.println("<html><body>");
            out.println("<h2>Database Error</h2>");
            out.println("<p>Error: " + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
            out.println("</body></html>");
        }
    }
}