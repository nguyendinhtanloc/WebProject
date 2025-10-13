package com.chat.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/fix-schema")
public class FixSchemaServlet extends HttpServlet {
    
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
            out.println("<h2>Schema Fix Results</h2>");
            
            // Connect to database
            Connection conn = DriverManager.getConnection(url, username, password);
            out.println("<p><strong>Database Connection:</strong> SUCCESS</p>");
            
            // Drop and recreate tables with proper schema
            String[] dropStatements = {
                "DROP TABLE IF EXISTS messages CASCADE",
                "DROP TABLE IF EXISTS chats CASCADE",
                "DROP TABLE IF EXISTS users CASCADE"
            };
            
            String[] createStatements = {
                // Create users table with UUID primary key
                "CREATE TABLE users (" +
                "    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid()," +
                "    username VARCHAR(255) NOT NULL UNIQUE," +
                "    password_hash VARCHAR(255) NOT NULL," +
                "    email VARCHAR(255) NOT NULL UNIQUE," +
                "    full_name VARCHAR(255) NOT NULL," +
                "    phone VARCHAR(20)," +
                "    user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('CUSTOMER', 'EMPLOYEE', 'ADMIN'))," +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    is_active BOOLEAN DEFAULT true," +
                "    is_online BOOLEAN DEFAULT false" +
                ")",
                
                // Create chats table with UUID foreign keys
                "CREATE TABLE chats (" +
                "    id BIGSERIAL PRIMARY KEY," +
                "    customer_id UUID NOT NULL REFERENCES users(user_id)," +
                "    employee_id UUID REFERENCES users(user_id)," +
                "    status VARCHAR(20) NOT NULL DEFAULT 'OPEN' CHECK (status IN ('OPEN', 'IN_PROGRESS', 'CLOSED', 'RESOLVED'))," +
                "    subject VARCHAR(255)," +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    closed_at TIMESTAMP" +
                ")",
                
                // Create messages table
                "CREATE TABLE messages (" +
                "    id BIGSERIAL PRIMARY KEY," +
                "    chat_id BIGINT NOT NULL REFERENCES chats(id)," +
                "    sender_id UUID NOT NULL REFERENCES users(user_id)," +
                "    content TEXT NOT NULL," +
                "    message_type VARCHAR(20) NOT NULL DEFAULT 'TEXT' CHECK (message_type IN ('TEXT', 'FILE', 'IMAGE'))," +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    is_read BOOLEAN DEFAULT false," +
                "    attachment_url VARCHAR(500)" +
                ")"
            };
            
            Statement stmt = conn.createStatement();
            
            // Drop existing tables
            out.println("<h3>Dropping existing tables:</h3>");
            for (String dropSql : dropStatements) {
                try {
                    stmt.execute(dropSql);
                    out.println("<p>✓ " + dropSql + "</p>");
                } catch (SQLException e) {
                    out.println("<p>⚠ " + dropSql + " - " + e.getMessage() + "</p>");
                }
            }
            
            // Create new tables
            out.println("<h3>Creating new tables:</h3>");
            for (String createSql : createStatements) {
                try {
                    stmt.execute(createSql);
                    out.println("<p>✓ Table created successfully</p>");
                    out.println("<pre>" + createSql + "</pre>");
                } catch (SQLException e) {
                    out.println("<p>❌ Error: " + e.getMessage() + "</p>");
                    out.println("<pre>" + createSql + "</pre>");
                }
            }
            
            conn.close();
            out.println("<h3>Schema fix completed!</h3>");
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