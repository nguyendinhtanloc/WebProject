package com.busbooking.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.busbooking.entity.AppUser;

public class AppUserDAO {
    private EntityManagerFactory emf;

    public AppUserDAO() {
        emf = Persistence.createEntityManagerFactory("busbookingPU");
    }

    public void save(AppUser user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public AppUser findByEmail(String email) {
        EntityManager em = null;
        try {
            System.out.println("🔍 DEBUG AppUserDAO: Creating EntityManager...");
            em = emf.createEntityManager();
            System.out.println("✅ DEBUG AppUserDAO: EntityManager created successfully");
            
            System.out.println("🔍 DEBUG AppUserDAO: Creating query to find email: " + email);
            TypedQuery<AppUser> query = em.createQuery("SELECT u FROM AppUser u WHERE u.email = :email", AppUser.class);
            query.setParameter("email", email);
            
            System.out.println("🔍 DEBUG AppUserDAO: Executing query...");
            List<AppUser> list = query.getResultList();
            System.out.println("✅ DEBUG AppUserDAO: Query executed, found " + list.size() + " results");
            
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            System.err.println("❌ ERROR AppUserDAO.findByEmail: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database error when finding user by email", e);
        } finally {
            if (em != null) {
                try {
                    em.close();
                    System.out.println("✅ DEBUG AppUserDAO: EntityManager closed");
                } catch (Exception e) {
                    System.err.println("❌ ERROR closing EntityManager: " + e.getMessage());
                }
            }
        }
    }

    public AppUser login(String email, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<AppUser> query = em.createQuery(
                    "SELECT u FROM AppUser u WHERE u.email = :email AND u.password = :password AND u.status = 'active'",
                    AppUser.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            List<AppUser> list = query.getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    public void delete(AppUser user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            AppUser managedUser = em.find(AppUser.class, user.getUseruuid());
            if (managedUser != null) {
                em.remove(managedUser);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}