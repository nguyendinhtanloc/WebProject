package com.busbooking.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "busbookingPU";
    private static final EntityManagerFactory factory;
    
    static {
        try {
              // Lấy biến môi trường từ hệ thống (Render inject trực tiếp)
            Map<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            properties.put("javax.persistence.jdbc.url", System.getenv("JDBC_DATABASE_URL"));
            properties.put("javax.persistence.jdbc.user", System.getenv("JDBC_DATABASE_USERNAME"));
            properties.put("javax.persistence.jdbc.password", System.getenv("JDBC_DATABASE_PASSWORD"));

            // Hibernate config
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.show_sql", "true");

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
