package com.chat.servlet;

import com.chat.dao.EntityManagerUtil;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test-db")
public class TestDatabaseServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            out.println("Testing database connection...");
            
            EntityManager em = EntityManagerUtil.getEntityManager();
            out.println("EntityManager created successfully!");
            
            // Test a simple query
            String result = (String) em.createNativeQuery("SELECT 'Database connection OK'").getSingleResult();
            out.println("Query result: " + result);
            
            em.close();
            out.println("Database test completed successfully!");
            
        } catch (Exception e) {
            out.println("Database error: " + e.getMessage());
            e.printStackTrace(out);
        }
    }
}