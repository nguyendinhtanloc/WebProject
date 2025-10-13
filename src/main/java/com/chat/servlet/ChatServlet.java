package com.chat.servlet;

import com.chat.dao.ChatDAO;
import com.chat.dao.UserDAO;
import com.chat.model.Chat;
import com.chat.model.ChatStatus;
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
import java.util.List;
import java.util.Map;

@WebServlet("/api/chat/*")
public class ChatServlet extends HttpServlet {
    
    private ChatDAO chatDAO;
    private UserDAO userDAO;
    private ObjectMapper objectMapper;
    
    @Override
    public void init() throws ServletException {
        super.init();
        chatDAO = new ChatDAO();
        userDAO = new UserDAO();
        objectMapper = new ObjectMapper();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> result = new HashMap<>();
        PrintWriter out = response.getWriter();
        
        try {
            User currentUser = getCurrentUser(request);
            if (currentUser == null) {
                result.put("success", false);
                result.put("message", "Authentication required");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print(objectMapper.writeValueAsString(result));
                return;
            }
            
            if ("/list".equals(pathInfo)) {
                handleGetChats(request, response, currentUser, result);
            } else if ("/open".equals(pathInfo)) {
                handleGetOpenChats(request, response, currentUser, result);
            } else if (pathInfo != null && pathInfo.matches("/\\d+")) {
                // Get specific chat by ID
                Long chatId = Long.parseLong(pathInfo.substring(1));
                handleGetChat(chatId, currentUser, result);
            } else {
                result.put("success", false);
                result.put("message", "Invalid endpoint");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Internal server error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
        out.print(objectMapper.writeValueAsString(result));
        out.flush();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        System.out.println("=== DEBUG ChatServlet doPost ===");
        System.out.println("PathInfo: " + pathInfo);
        System.out.println("Request URL: " + request.getRequestURL());
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> result = new HashMap<>();
        PrintWriter out = response.getWriter();
        
        try {
            User currentUser = getCurrentUser(request);
            if (currentUser == null) {
                result.put("success", false);
                result.put("message", "Authentication required");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print(objectMapper.writeValueAsString(result));
                return;
            }
            
            if ("/create".equals(pathInfo)) {
                handleCreateChat(request, response, currentUser, result);
            } else if ("/assign".equals(pathInfo)) {
                handleAssignChat(request, response, currentUser, result);
            } else if ("/close".equals(pathInfo)) {
                handleCloseChat(request, response, currentUser, result);
            } else {
                result.put("success", false);
                result.put("message", "Invalid endpoint");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Internal server error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
        out.print(objectMapper.writeValueAsString(result));
        out.flush();
    }
    
    private void handleGetChats(HttpServletRequest request, HttpServletResponse response, 
                               User currentUser, Map<String, Object> result) throws Exception {
        
        List<Chat> chats;
        
        if (currentUser.getUserType() == UserType.CUSTOMER) {
            chats = chatDAO.findByCustomer(currentUser);
        } else {
            chats = chatDAO.findByEmployee(currentUser);
        }
        
        result.put("success", true);
        result.put("chats", chats.stream().map(this::createChatResponse).toArray());
    }
    
    private void handleGetOpenChats(HttpServletRequest request, HttpServletResponse response, 
                                   User currentUser, Map<String, Object> result) throws Exception {
        
        if (currentUser.getUserType() != UserType.EMPLOYEE) {
            result.put("success", false);
            result.put("message", "Only employees can view open chats");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        List<Chat> openChats = chatDAO.findOpenChats();
        result.put("success", true);
        result.put("chats", openChats.stream().map(this::createChatResponse).toArray());
    }
    
    private void handleGetChat(Long chatId, User currentUser, Map<String, Object> result) throws Exception {
        Chat chat = chatDAO.findById(chatId).orElse(null);
        
        if (chat == null) {
            result.put("success", false);
            result.put("message", "Chat not found");
            return;
        }
        
        // Check if user has access to this chat
        if (currentUser.getUserType() == UserType.CUSTOMER && !chat.getCustomer().getId().equals(currentUser.getId())) {
            result.put("success", false);
            result.put("message", "Access denied");
            return;
        }
        
        if (currentUser.getUserType() == UserType.EMPLOYEE && 
            (chat.getEmployee() == null || !chat.getEmployee().getId().equals(currentUser.getId()))) {
            result.put("success", false);
            result.put("message", "Access denied");
            return;
        }
        
        result.put("success", true);
        result.put("chat", createChatResponse(chat));
    }
    
    private void handleCreateChat(HttpServletRequest request, HttpServletResponse response, 
                                 User currentUser, Map<String, Object> result) throws Exception {
        
        System.out.println("=== DEBUG handleCreateChat ===");
        System.out.println("Current user: " + currentUser.getUsername());
        System.out.println("User type: " + currentUser.getUserType());
        
        if (currentUser.getUserType() != UserType.CUSTOMER) {
            result.put("success", false);
            result.put("message", "Only customers can create chats");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String subject = request.getParameter("subject");
        System.out.println("Subject parameter: '" + subject + "'");
        
        if (subject == null || subject.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "Subject is required");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        Chat newChat = new Chat(currentUser, subject);
        Chat savedChat = chatDAO.save(newChat);
        
        result.put("success", true);
        result.put("message", "Chat created successfully");
        result.put("chat", createChatResponse(savedChat));
    }
    
    private void handleAssignChat(HttpServletRequest request, HttpServletResponse response, 
                                 User currentUser, Map<String, Object> result) throws Exception {
        
        if (currentUser.getUserType() != UserType.EMPLOYEE) {
            result.put("success", false);
            result.put("message", "Only employees can assign chats");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String chatIdStr = request.getParameter("chatId");
        if (chatIdStr == null || chatIdStr.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "Chat ID is required");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        Long chatId = Long.parseLong(chatIdStr);
        Chat chat = chatDAO.findById(chatId).orElse(null);
        
        if (chat == null) {
            result.put("success", false);
            result.put("message", "Chat not found");
            return;
        }
        
        if (chat.getStatus() != ChatStatus.OPEN) {
            result.put("success", false);
            result.put("message", "Chat is not available for assignment");
            return;
        }
        
        Chat updatedChat = chatDAO.assignEmployee(chat, currentUser);
        
        result.put("success", true);
        result.put("message", "Chat assigned successfully");
        result.put("chat", createChatResponse(updatedChat));
    }
    
    private void handleCloseChat(HttpServletRequest request, HttpServletResponse response, 
                                User currentUser, Map<String, Object> result) throws Exception {
        
        String chatIdStr = request.getParameter("chatId");
        if (chatIdStr == null || chatIdStr.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "Chat ID is required");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        Long chatId = Long.parseLong(chatIdStr);
        Chat chat = chatDAO.findById(chatId).orElse(null);
        
        if (chat == null) {
            result.put("success", false);
            result.put("message", "Chat not found");
            return;
        }
        
        // Check if user can close this chat
        boolean canClose = false;
        if (currentUser.getUserType() == UserType.CUSTOMER && chat.getCustomer().getId().equals(currentUser.getId())) {
            canClose = true;
        } else if (currentUser.getUserType() == UserType.EMPLOYEE && 
                   chat.getEmployee() != null && chat.getEmployee().getId().equals(currentUser.getId())) {
            canClose = true;
        }
        
        if (!canClose) {
            result.put("success", false);
            result.put("message", "Access denied");
            return;
        }
        
        Chat updatedChat = chatDAO.updateStatus(chat, ChatStatus.CLOSED);
        
        result.put("success", true);
        result.put("message", "Chat closed successfully");
        result.put("chat", createChatResponse(updatedChat));
    }
    
    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("user");
        }
        return null;
    }
    
    private Map<String, Object> createChatResponse(Chat chat) {
        Map<String, Object> chatResponse = new HashMap<>();
        chatResponse.put("id", chat.getId());
        chatResponse.put("subject", chat.getSubject());
        chatResponse.put("status", chat.getStatus().toString());
        chatResponse.put("createdAt", chat.getCreatedAt().toString());
        chatResponse.put("updatedAt", chat.getUpdatedAt() != null ? chat.getUpdatedAt().toString() : null);
        chatResponse.put("closedAt", chat.getClosedAt() != null ? chat.getClosedAt().toString() : null);
        
        // Customer info
        Map<String, Object> customerInfo = new HashMap<>();
        customerInfo.put("id", chat.getCustomer().getId());
        customerInfo.put("username", chat.getCustomer().getUsername());
        customerInfo.put("fullName", chat.getCustomer().getFullName());
        customerInfo.put("isOnline", chat.getCustomer().getIsOnline());
        chatResponse.put("customer", customerInfo);
        
        // Employee info (if assigned)
        if (chat.getEmployee() != null) {
            Map<String, Object> employeeInfo = new HashMap<>();
            employeeInfo.put("id", chat.getEmployee().getId());
            employeeInfo.put("username", chat.getEmployee().getUsername());
            employeeInfo.put("fullName", chat.getEmployee().getFullName());
            employeeInfo.put("isOnline", chat.getEmployee().getIsOnline());
            chatResponse.put("employee", employeeInfo);
        }
        
        return chatResponse;
    }
    
    @Override
    public void destroy() {
        super.destroy();
        if (chatDAO != null) {
            chatDAO.close();
        }
        if (userDAO != null) {
            userDAO.close();
        }
    }
}

