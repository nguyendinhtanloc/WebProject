package com.chat.servlet;

import com.chat.dao.EntityManagerUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test-jpa")
public class TestJPAServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            // Test JPA connection
            EntityManager em = EntityManagerUtil.getEntityManager();
            
            if (em != null && em.isOpen()) {
                // Try a simple query
                Long count = (Long) em.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();
                
                out.println("{");
                out.println("  \"success\": true,");
                out.println("  \"message\": \"JPA connection successful!\",");
                out.println("  \"userCount\": " + count);
                out.println("}");
                
                em.close();
            } else {
                out.println("{\"success\": false, \"message\": \"EntityManager is null or closed\"}");
            }
            
        } catch (Exception e) {
            out.println("{");
            out.println("  \"success\": false,");
            out.println("  \"message\": \"JPA error: " + e.getMessage().replace("\"", "'") + "\",");
            out.println("  \"type\": \"" + e.getClass().getSimpleName() + "\"");
            out.println("}");
        }
    }
}