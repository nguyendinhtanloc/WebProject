package com.chat.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerUtil {
    
    private static EntityManagerFactory emf;
    
    static {
        try {
            // Create EntityManagerFactory với persistence unit name từ persistence.xml
            emf = Persistence.createEntityManagerFactory("chat-pu");
        } catch (Exception e) {
            System.err.println("Failed to initialize EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Cannot initialize JPA EntityManagerFactory", e);
        }
    }
    
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

