package com.chat.servlet;

import com.chat.dao.ChatDAO;
import com.chat.dao.MessageDAO;
import com.chat.dao.UserDAO;
import com.chat.model.Chat;
import com.chat.model.Message;
import com.chat.model.MessageType;
import com.chat.model.User;
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

@WebServlet("/api/message/*")
public class MessageServlet extends HttpServlet {
    
    private MessageDAO messageDAO;
    private ChatDAO chatDAO;
    private UserDAO userDAO;
    private ObjectMapper objectMapper;
    
    @Override
    public void init() throws ServletException {
        super.init();
        messageDAO = new MessageDAO();
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
            
            if (pathInfo != null && pathInfo.matches("/chat/\\d+")) {
                // Get messages for a specific chat
                Long chatId = Long.parseLong(pathInfo.substring(6)); // Remove "/chat/"
                handleGetMessages(chatId, currentUser, result);
            } else if ("/unread".equals(pathInfo)) {
                handleGetUnreadMessages(currentUser, result);
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
            
            if ("/send".equals(pathInfo)) {
                handleSendMessage(request, response, currentUser, result);
            } else if ("/mark-read".equals(pathInfo)) {
                handleMarkAsRead(request, response, currentUser, result);
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
    
    private void handleGetMessages(Long chatId, User currentUser, Map<String, Object> result) throws Exception {
        Chat chat = chatDAO.findById(chatId).orElse(null);
        
        if (chat == null) {
            result.put("success", false);
            result.put("message", "Chat not found");
            return;
        }
        
        // Check if user has access to this chat
        if (!hasAccessToChat(chat, currentUser)) {
            result.put("success", false);
            result.put("message", "Access denied");
            return;
        }
        
        List<Message> messages = messageDAO.findByChat(chat);
        result.put("success", true);
        result.put("messages", messages.stream().map(this::createMessageResponse).toArray());
    }
    
    private void handleGetUnreadMessages(User currentUser, Map<String, Object> result) throws Exception {
        List<Message> unreadMessages = messageDAO.findUnreadMessagesByUser(currentUser);
        long unreadCount = messageDAO.countUnreadMessagesByUser(currentUser);
        
        result.put("success", true);
        result.put("unreadCount", unreadCount);
        result.put("messages", unreadMessages.stream().map(this::createMessageResponse).toArray());
    }
    
    private void handleSendMessage(HttpServletRequest request, HttpServletResponse response, 
                                  User currentUser, Map<String, Object> result) throws Exception {
        
        String chatIdStr = request.getParameter("chatId");
        String content = request.getParameter("content");
        String messageTypeStr = request.getParameter("messageType");
        
        if (chatIdStr == null || chatIdStr.trim().isEmpty() ||
            content == null || content.trim().isEmpty()) {
            
            result.put("success", false);
            result.put("message", "Chat ID and content are required");
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
        
        // Check if user has access to this chat
        if (!hasAccessToChat(chat, currentUser)) {
            result.put("success", false);
            result.put("message", "Access denied");
            return;
        }
        
        // Check if chat is closed
        if (chat.getStatus().toString().equals("CLOSED")) {
            result.put("success", false);
            result.put("message", "Cannot send message to closed chat");
            return;
        }
        
        MessageType messageType = MessageType.TEXT;
        if (messageTypeStr != null && !messageTypeStr.trim().isEmpty()) {
            try {
                messageType = MessageType.valueOf(messageTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                messageType = MessageType.TEXT;
            }
        }
        
        Message newMessage = new Message(chat, currentUser, content, messageType);
        Message savedMessage = messageDAO.save(newMessage);
        
        result.put("success", true);
        result.put("message", "Message sent successfully");
        result.put("messageData", createMessageResponse(savedMessage));
    }
    
    private void handleMarkAsRead(HttpServletRequest request, HttpServletResponse response, 
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
        
        // Check if user has access to this chat
        if (!hasAccessToChat(chat, currentUser)) {
            result.put("success", false);
            result.put("message", "Access denied");
            return;
        }
        
        List<Message> unreadMessages = messageDAO.findUnreadMessagesByChat(chat, currentUser);
        messageDAO.markAsRead(unreadMessages);
        
        result.put("success", true);
        result.put("message", "Messages marked as read");
        result.put("markedCount", unreadMessages.size());
    }
    
    private boolean hasAccessToChat(Chat chat, User user) {
        // Customer can access their own chats
        if (user.getUserType().toString().equals("CUSTOMER") && 
            chat.getCustomer().getId().equals(user.getId())) {
            return true;
        }
        
        // Employee can access chats assigned to them or open chats
        if (user.getUserType().toString().equals("EMPLOYEE")) {
            // Can access chats assigned to them
            if (chat.getEmployee() != null && chat.getEmployee().getId().equals(user.getId())) {
                return true;
            }
            // Can access open chats
            if (chat.getStatus().toString().equals("OPEN")) {
                return true;
            }
        }
        
        return false;
    }
    
    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("user");
        }
        return null;
    }
    
    private Map<String, Object> createMessageResponse(Message message) {
        Map<String, Object> messageResponse = new HashMap<>();
        messageResponse.put("id", message.getId());
        messageResponse.put("content", message.getContent());
        messageResponse.put("messageType", message.getMessageType().toString());
        messageResponse.put("createdAt", message.getCreatedAt().toString());
        messageResponse.put("isRead", message.getIsRead());
        messageResponse.put("readAt", message.getReadAt() != null ? message.getReadAt().toString() : null);
        
        // Sender info
        Map<String, Object> senderInfo = new HashMap<>();
        senderInfo.put("id", message.getSender().getId());
        senderInfo.put("username", message.getSender().getUsername());
        senderInfo.put("fullName", message.getSender().getFullName());
        senderInfo.put("userType", message.getSender().getUserType().toString());
        messageResponse.put("sender", senderInfo);
        
        return messageResponse;
    }
    
    @Override
    public void destroy() {
        super.destroy();
        if (messageDAO != null) {
            messageDAO.close();
        }
        if (chatDAO != null) {
            chatDAO.close();
        }
        if (userDAO != null) {
            userDAO.close();
        }
    }
}

