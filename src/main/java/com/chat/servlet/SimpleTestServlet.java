package com.chat.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/simple-test")
public class SimpleTestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> result = new HashMap<>();
        PrintWriter out = response.getWriter();
        
        try {
            result.put("success", true);
            result.put("message", "Simple test successful!");
            result.put("server", "Tomcat 7");
            result.put("java", System.getProperty("java.version"));
            result.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        ObjectMapper mapper = new ObjectMapper();
        out.print(mapper.writeValueAsString(result));
        out.flush();
    }
}

