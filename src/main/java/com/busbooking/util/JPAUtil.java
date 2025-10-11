package com.busbooking.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "busbookingPU";
    private static final EntityManagerFactory factory;
    
    static {
        try {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Throwable ex) {
            System.err.println("Failed to create EntityManagerFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
    
    public static void close() {
        if (factory != null) {
            factory.close();
        }
    }
}