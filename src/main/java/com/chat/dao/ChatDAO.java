package com.chat.dao;

import com.chat.model.Chat;
import com.chat.model.ChatStatus;
import com.chat.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ChatDAO {
    
    private EntityManager entityManager;
    
    public ChatDAO() {
        this.entityManager = EntityManagerUtil.getEntityManager();
    }
    
    public Chat save(Chat chat) {
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(chat);
            entityManager.getTransaction().commit();
            return chat;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    
    public Optional<Chat> findById(Long id) {
        Chat chat = entityManager.find(Chat.class, id);
        return Optional.ofNullable(chat);
    }
    
    public List<Chat> findByCustomer(User customer) {
        TypedQuery<Chat> query = entityManager.createQuery(
                "SELECT c FROM Chat c WHERE c.customer = :customer ORDER BY c.updatedAt DESC, c.createdAt DESC", Chat.class);
        query.setParameter("customer", customer);
        return query.getResultList();
    }
    
    public List<Chat> findByEmployee(User employee) {
        TypedQuery<Chat> query = entityManager.createQuery(
                "SELECT c FROM Chat c WHERE c.employee = :employee ORDER BY c.updatedAt DESC, c.createdAt DESC", Chat.class);
        query.setParameter("employee", employee);
        return query.getResultList();
    }
    
    public List<Chat> findByStatus(ChatStatus status) {
        TypedQuery<Chat> query = entityManager.createQuery(
                "SELECT c FROM Chat c WHERE c.status = :status ORDER BY c.createdAt DESC", Chat.class);
        query.setParameter("status", status);
        return query.getResultList();
    }
    
    public List<Chat> findOpenChats() {
        return findByStatus(ChatStatus.OPEN);
    }
    
    public List<Chat> findInProgressChats() {
        return findByStatus(ChatStatus.IN_PROGRESS);
    }
    
    public List<Chat> findAllOrderByLastActivity() {
        TypedQuery<Chat> query = entityManager.createQuery(
                "SELECT c FROM Chat c ORDER BY c.updatedAt DESC, c.createdAt DESC", Chat.class);
        return query.getResultList();
    }
    
    public Chat assignEmployee(Chat chat, User employee) {
        entityManager.getTransaction().begin();
        try {
            chat.setEmployee(employee);
            chat.setStatus(ChatStatus.IN_PROGRESS);
            chat.setUpdatedAt(LocalDateTime.now());
            Chat updatedChat = entityManager.merge(chat);
            entityManager.getTransaction().commit();
            return updatedChat;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    
    public Chat updateStatus(Chat chat, ChatStatus status) {
        entityManager.getTransaction().begin();
        try {
            chat.setStatus(status);
            chat.setUpdatedAt(LocalDateTime.now());
            if (status == ChatStatus.CLOSED) {
                chat.setClosedAt(LocalDateTime.now());
            }
            Chat updatedChat = entityManager.merge(chat);
            entityManager.getTransaction().commit();
            return updatedChat;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    
    public Chat update(Chat chat) {
        entityManager.getTransaction().begin();
        try {
            Chat updatedChat = entityManager.merge(chat);
            entityManager.getTransaction().commit();
            return updatedChat;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    
    public void delete(Long id) {
        entityManager.getTransaction().begin();
        try {
            Chat chat = entityManager.find(Chat.class, id);
            if (chat != null) {
                entityManager.remove(chat);
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

