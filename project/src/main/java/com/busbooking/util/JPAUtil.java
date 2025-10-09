package com.busbooking.util;

import io.github.cdimascio.dotenv.Dotenv;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "busBookingPU";
    private static final EntityManagerFactory emf;

    static {
        try {
            // Load biến môi trường từ file .env (root project hoặc src/main/resources)
            Dotenv dotenv = Dotenv.configure()
                                //   .directory("src/main/resources")
                                  .ignoreIfMalformed()
                                  .ignoreIfMissing()
                                  .load();

            Map<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            properties.put("javax.persistence.jdbc.url", dotenv.get("SUPABASE_DB_URL"));
            properties.put("javax.persistence.jdbc.user", dotenv.get("SUPABASE_DB_USER"));
            properties.put("javax.persistence.jdbc.password", dotenv.get("SUPABASE_DB_PASS"));

            // Hibernate config
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.show_sql", "true");

            // Khởi tạo EntityManagerFactory programmatically
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Lỗi khởi tạo EntityManagerFactory: " + e);
        }
    }

    /** Lấy EntityManager */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /** Đóng EntityManagerFactory khi shutdown */
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
