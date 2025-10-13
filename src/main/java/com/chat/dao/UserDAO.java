package com.chat.dao;

import com.chat.model.User;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class UserDAO {
    
    public UserDAO() {
    }
    
    public User save(User user) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            return user;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
    
    public Optional<User> findByUsername(String username) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            
            try {
                User user = query.getSingleResult();
                return Optional.of(user);
            } catch (Exception e) {
                return Optional.empty();
            }
        } finally {
            entityManager.close();
        }
    }
    
    public User update(User user) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            User updatedUser = entityManager.merge(user);
            entityManager.getTransaction().commit();
            return updatedUser;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
    
    public Optional<User> findByEmail(String email) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            
            try {
                User user = query.getSingleResult();
                return Optional.of(user);
            } catch (Exception e) {
                return Optional.empty();
            }
        } finally {
            entityManager.close();
        }
    }
    
    public Optional<User> findById(java.util.UUID id) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        try {
            User user = entityManager.find(User.class, id);
            return user != null ? Optional.of(user) : Optional.empty();
        } finally {
            entityManager.close();
        }
    }
    
    public void close() {
        // No-op - EntityManager is closed per operation
    }
}
