package com.chat.servlet;

import com.chat.dao.UserDAO;
import com.chat.model.User;
import com.chat.model.UserType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/create-users")
public class CreateUsersServlet extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Create admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@test.com");
            admin.setFullName("Admin User");
            admin.setPasswordHash("admin123");
            admin.setUserType(UserType.EMPLOYEE);
            admin.setIsOnline(false);
            
            User savedAdmin = userDAO.save(admin);
            
            // Create customer user
            User customer = new User();
            customer.setUsername("customer");
            customer.setEmail("customer@test.com");
            customer.setFullName("Customer User");
            customer.setPasswordHash("customer123");
            customer.setUserType(UserType.CUSTOMER);
            customer.setIsOnline(false);
            
            User savedCustomer = userDAO.save(customer);
            
            out.println("{");
            out.println("  \"success\": true,");
            out.println("  \"message\": \"Users created successfully!\",");
            out.println("  \"adminId\": \"" + savedAdmin.getId() + "\",");
            out.println("  \"customerId\": \"" + savedCustomer.getId() + "\"");
            out.println("}");
            
        } catch (Exception e) {
            out.println("{");
            out.println("  \"success\": false,");
            out.println("  \"message\": \"Error: " + e.getMessage().replace("\"", "'") + "\"");
            out.println("}");
            e.printStackTrace();
        }
    }
}