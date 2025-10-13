package com.chat.servlet;

import com.chat.dao.UserDAO;
import com.chat.model.User;
import com.chat.model.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    
    private UserDAO userDAO;
    private ObjectMapper objectMapper;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        objectMapper = new ObjectMapper();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Auth API is working");
        result.put("availableActions", new String[]{"login", "register", "logout", "status"});
        result.put("usage", "Send POST request with 'action' parameter");
        
        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(result));
        out.flush();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> result = new HashMap<>();
        PrintWriter out = response.getWriter();
        
        try {
            String action = request.getParameter("action");
            
            if (action == null || action.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "Action parameter is required. Valid actions: login, register, logout, status");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else if ("login".equals(action)) {
                handleLogin(request, response, result);
            } else if ("register".equals(action)) {
                handleRegister(request, response, result);
            } else if ("logout".equals(action)) {
                handleLogout(request, response, result);
            } else if ("status".equals(action)) {
                handleStatus(request, response, result);
            } else {
                result.put("success", false);
                result.put("message", "Invalid action: " + action + ". Valid actions: login, register, logout, status");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Internal server error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
        out.print(objectMapper.writeValueAsString(result));
        out.flush();
    }
    
    private void handleLogin(HttpServletRequest request, HttpServletResponse response, Map<String, Object> result) throws Exception {
        System.out.println("=== DEBUG handleLogin ===");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        System.out.println("Username: " + username);
        System.out.println("Password: " + (password != null ? "***" : "null"));
        
        if (username == null || username.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "Username is required");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "Password is required");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        // Simple authentication - check password hash
        User user = userDAO.findByUsername(username).orElse(null);
        
        if (user != null && password.equals(user.getPasswordHash())) {
            // Update online status
            user.setIsOnline(true);
            user.setLastSeen(java.time.LocalDateTime.now());
            userDAO.update(user);
            
            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userType", user.getUserType().toString());
            
            result.put("success", true);
            result.put("message", "Login successful");
            result.put("user", createUserResponse(user));
        } else {
            result.put("success", false);
            result.put("message", "Invalid username");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
    
    private void handleRegister(HttpServletRequest request, HttpServletResponse response, Map<String, Object> result) throws Exception {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String userTypeStr = request.getParameter("userType");
        
        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty() ||
            userTypeStr == null || userTypeStr.trim().isEmpty()) {
            
            result.put("success", false);
            result.put("message", "All fields are required");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        // Check if username already exists
        if (userDAO.findByUsername(username).isPresent()) {
            result.put("success", false);
            result.put("message", "Username already exists");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        
        // Check if email already exists
        if (userDAO.findByEmail(email).isPresent()) {
            result.put("success", false);
            result.put("message", "Email already exists");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        
        UserType userType;
        try {
            userType = UserType.valueOf(userTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            result.put("success", false);
            result.put("message", "Invalid user type");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        String password = request.getParameter("password");
        // In production, hash the password properly
        String hashedPassword = password; // TODO: Use BCrypt or similar
        
        User newUser = new User(username, hashedPassword, email, fullName, userType);
        User savedUser = userDAO.save(newUser);
        
        result.put("success", true);
        result.put("message", "Registration successful");
        result.put("user", createUserResponse(savedUser));
    }
    
    private void handleLogout(HttpServletRequest request, HttpServletResponse response, Map<String, Object> result) throws Exception {
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                // Update online status
                user.setIsOnline(false);
                user.setLastSeen(java.time.LocalDateTime.now());
                userDAO.update(user);
            }
            
            session.invalidate();
        }
        
        result.put("success", true);
        result.put("message", "Logout successful");
    }
    
    private void handleStatus(HttpServletRequest request, HttpServletResponse response, Map<String, Object> result) throws Exception {
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                result.put("success", true);
                result.put("user", createUserResponse(user));
                return;
            }
        }
        
        result.put("success", false);
        result.put("message", "Not authenticated");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
    
    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("username", user.getUsername());
        userResponse.put("email", user.getEmail());
        userResponse.put("fullName", user.getFullName());
        userResponse.put("userType", user.getUserType().toString());
        userResponse.put("isOnline", user.getIsOnline());
        return userResponse;
    }
    
    @Override
    public void destroy() {
        super.destroy();
        if (userDAO != null) {
            userDAO.close();
        }
    }
}

