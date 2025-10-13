package com.chat.websocket;

import com.chat.dao.ChatDAO;
import com.chat.dao.MessageDAO;
import com.chat.dao.UserDAO;
import com.chat.model.Chat;
import com.chat.model.Message;
import com.chat.model.MessageType;
import com.chat.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/chat/{chatId}/{userId}")
public class ChatWebSocketEndpoint {
    
    private static final Set<Session> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static final Map<String, Session> userSessions = new ConcurrentHashMap<>();
    private static final Map<Session, String> sessionUsers = new ConcurrentHashMap<>();
    private static final Map<Session, Long> sessionChats = new ConcurrentHashMap<>();
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MessageDAO messageDAO = new MessageDAO();
    private final ChatDAO chatDAO = new ChatDAO();
    private final UserDAO userDAO = new UserDAO();
    
    @OnOpen
    public void onOpen(Session session, @PathParam("chatId") Long chatId, @PathParam("userId") String userId) {
        try {
            System.out.println("WebSocket connection opened for user: " + userId + ", chat: " + chatId);
            
            // Validate user and chat
            User user = userDAO.findById(UUID.fromString(userId)).orElse(null);
            Chat chat = chatDAO.findById(chatId).orElse(null);
            
            if (user == null || chat == null) {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Invalid user or chat"));
                return;
            }
            
            // Check if user has access to this chat
            if (!hasAccessToChat(chat, user)) {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Access denied"));
                return;
            }
            
            sessions.add(session);
            userSessions.put(userId, session);
            sessionUsers.put(session, userId);
            sessionChats.put(session, chatId);
            
            // Send connection confirmation
            ObjectNode response = objectMapper.createObjectNode();
            response.put("type", "connection");
            response.put("status", "connected");
            response.put("message", "WebSocket connected successfully");
            
            session.getBasicRemote().sendText(objectMapper.writeValueAsString(response));
            
        } catch (Exception e) {
            System.err.println("Error in WebSocket onOpen: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            System.out.println("WebSocket message received: " + message);
            
            JsonNode jsonNode = objectMapper.readTree(message);
            String type = jsonNode.get("type").asText();
            
            String userId = sessionUsers.get(session);
            Long chatId = sessionChats.get(session);
            
            if (userId == null || chatId == null) {
                sendError(session, "Session not properly initialized");
                return;
            }
            
            switch (type) {
                case "message":
                    handleMessage(jsonNode, session, userId, chatId);
                    break;
                case "typing":
                    handleTyping(jsonNode, session, userId, chatId);
                    break;
                default:
                    sendError(session, "Unknown message type: " + type);
                    break;
            }
            
        } catch (Exception e) {
            System.err.println("Error processing WebSocket message: " + e.getMessage());
            e.printStackTrace();
            sendError(session, "Error processing message");
        }
    }
    
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("WebSocket connection closed: " + reason.getReasonPhrase());
        
        String userId = sessionUsers.get(session);
        if (userId != null) {
            userSessions.remove(userId);
            sessionUsers.remove(session);
        }
        sessionChats.remove(session);
        sessions.remove(session);
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
        throwable.printStackTrace();
        
        String userId = sessionUsers.get(session);
        if (userId != null) {
            userSessions.remove(userId);
            sessionUsers.remove(session);
        }
        sessionChats.remove(session);
        sessions.remove(session);
    }
    
    private void handleMessage(JsonNode jsonNode, Session session, String userId, Long chatId) {
        try {
            String content = jsonNode.get("content").asText();
            
            if (content == null || content.trim().isEmpty()) {
                sendError(session, "Message content cannot be empty");
                return;
            }
            
            User user = userDAO.findById(UUID.fromString(userId)).orElse(null);
            Chat chat = chatDAO.findById(chatId).orElse(null);
            
            if (user == null || chat == null) {
                sendError(session, "Invalid user or chat");
                return;
            }
            
            // Check if chat is closed
            if (chat.getStatus().toString().equals("CLOSED")) {
                sendError(session, "Cannot send message to closed chat");
                return;
            }
            
            // Create and save message
            Message newMessage = new Message(chat, user, content, MessageType.TEXT);
            Message savedMessage = messageDAO.save(newMessage);
            
            // Broadcast message to all users in this chat
            broadcastToChatMembers(chatId, createMessageResponse(savedMessage));
            
        } catch (Exception e) {
            System.err.println("Error handling message: " + e.getMessage());
            e.printStackTrace();
            sendError(session, "Error sending message");
        }
    }
    
    private void handleTyping(JsonNode jsonNode, Session session, String userId, Long chatId) {
        try {
            boolean isTyping = jsonNode.get("isTyping").asBoolean();
            
            ObjectNode response = objectMapper.createObjectNode();
            response.put("type", "typing");
            response.put("userId", userId);
            response.put("isTyping", isTyping);
            
            // Broadcast typing indicator to other users in this chat (not the sender)
            broadcastToChatMembers(chatId, response, session);
            
        } catch (Exception e) {
            System.err.println("Error handling typing: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void broadcastToChatMembers(Long chatId, ObjectNode message) {
        broadcastToChatMembers(chatId, message, null);
    }
    
    private void broadcastToChatMembers(Long chatId, ObjectNode message, Session excludeSession) {
        try {
            String messageStr = objectMapper.writeValueAsString(message);
            
            for (Session session : sessions) {
                if (session.equals(excludeSession)) continue;
                
                Long sessionChatId = sessionChats.get(session);
                if (chatId.equals(sessionChatId) && session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(messageStr);
                    } catch (IOException e) {
                        System.err.println("Error sending message to session: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error broadcasting message: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private ObjectNode createMessageResponse(Message message) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("type", "message");
        
        ObjectNode data = objectMapper.createObjectNode();
        data.put("id", message.getId());
        data.put("content", message.getContent());
        data.put("messageType", message.getMessageType().toString());
        data.put("createdAt", message.getCreatedAt().toString());
        data.put("isRead", message.getIsRead());
        
        ObjectNode sender = objectMapper.createObjectNode();
        sender.put("id", message.getSender().getId().toString());
        sender.put("username", message.getSender().getUsername());
        sender.put("fullName", message.getSender().getFullName());
        sender.put("userType", message.getSender().getUserType().toString());
        data.set("sender", sender);
        
        response.set("data", data);
        return response;
    }
    
    private void sendError(Session session, String errorMessage) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("type", "error");
            response.put("message", errorMessage);
            
            if (session.isOpen()) {
                session.getBasicRemote().sendText(objectMapper.writeValueAsString(response));
            }
        } catch (Exception e) {
            System.err.println("Error sending error message: " + e.getMessage());
        }
    }
    
    private boolean hasAccessToChat(Chat chat, User user) {
        // Customer can access their own chats
        if (user.getUserType().toString().equals("CUSTOMER") && 
            chat.getCustomer().getId().equals(user.getId())) {
            return true;
        }
        
        // Employee can access chats assigned to them or open chats
        if (user.getUserType().toString().equals("EMPLOYEE")) {
            if (chat.getEmployee() != null && chat.getEmployee().getId().equals(user.getId())) {
                return true;
            }
            // Allow employees to access open chats
            if (chat.getStatus().toString().equals("OPEN")) {
                return true;
            }
            // Allow employees to access in-progress chats assigned to them
            if (chat.getStatus().toString().equals("IN_PROGRESS") && 
                chat.getEmployee() != null && chat.getEmployee().getId().equals(user.getId())) {
                return true;
            }
        }
        
        return false;
    }
}