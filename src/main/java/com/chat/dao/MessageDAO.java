package com.chat.dao;

import com.chat.model.Chat;
import com.chat.model.Message;
import com.chat.model.MessageType;
import com.chat.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class MessageDAO {
    
    private EntityManager entityManager;
    
    public MessageDAO() {
        this.entityManager = EntityManagerUtil.getEntityManager();
    }
    
    public Message save(Message message) {
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(message);
            // Update chat's updatedAt timestamp
            Chat chat = message.getChat();
            chat.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(chat);
            entityManager.getTransaction().commit();
            return message;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    
    public List<Message> findByChat(Chat chat) {
        TypedQuery<Message> query = entityManager.createQuery(
                "SELECT m FROM Message m WHERE m.chat = :chat ORDER BY m.createdAt ASC", Message.class);
        query.setParameter("chat", chat);
        return query.getResultList();
    }
    
    public List<Message> findByChatId(Long chatId) {
        TypedQuery<Message> query = entityManager.createQuery(
                "SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY m.createdAt ASC", Message.class);
        query.setParameter("chatId", chatId);
        return query.getResultList();
    }
    
    public List<Message> findUnreadMessagesByChat(Chat chat, User user) {
        TypedQuery<Message> query = entityManager.createQuery(
                "SELECT m FROM Message m WHERE m.chat = :chat AND m.sender != :user AND m.isRead = false ORDER BY m.createdAt ASC", Message.class);
        query.setParameter("chat", chat);
        query.setParameter("user", user);
        return query.getResultList();
    }
    
    public List<Message> findUnreadMessagesByUser(User user) {
        TypedQuery<Message> query = entityManager.createQuery(
                "SELECT m FROM Message m JOIN m.chat c WHERE (c.customer = :user OR c.employee = :user) AND m.sender != :user AND m.isRead = false ORDER BY m.createdAt ASC", Message.class);
        query.setParameter("user", user);
        return query.getResultList();
    }
    
    public long countUnreadMessagesByUser(User user) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(m) FROM Message m JOIN m.chat c WHERE (c.customer = :user OR c.employee = :user) AND m.sender != :user AND m.isRead = false", Long.class);
        query.setParameter("user", user);
        return query.getSingleResult();
    }
    
    public void markAsRead(List<Message> messages) {
        if (messages.isEmpty()) return;
        
        entityManager.getTransaction().begin();
        try {
            LocalDateTime now = LocalDateTime.now();
            for (Message message : messages) {
                message.setIsRead(true);
                message.setReadAt(now);
                entityManager.merge(message);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    
    public void markAsRead(Message message) {
        entityManager.getTransaction().begin();
        try {
            message.setIsRead(true);
            message.setReadAt(LocalDateTime.now());
            entityManager.merge(message);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    
    public Message update(Message message) {
        entityManager.getTransaction().begin();
        try {
            Message updatedMessage = entityManager.merge(message);
            entityManager.getTransaction().commit();
            return updatedMessage;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    
    public void delete(Long id) {
        entityManager.getTransaction().begin();
        try {
            Message message = entityManager.find(Message.class, id);
            if (message != null) {
                entityManager.remove(message);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    
    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}

