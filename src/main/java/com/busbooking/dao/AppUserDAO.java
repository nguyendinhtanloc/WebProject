package com.busbooking.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.busbooking.entity.AppUser;

public class AppUserDAO {
    private final EntityManagerFactory emf;

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

    public AppUser update(AppUser user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            AppUser updatedUser = em.merge(user); // Dùng merge để cập nhật
            tx.commit();
            return updatedUser;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public AppUser findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<AppUser> query = em.createQuery("SELECT u FROM AppUser u WHERE u.email = :email", AppUser.class);
            query.setParameter("email", email);
            List<AppUser> list = query.getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
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
            AppUser managedUser = em.find(AppUser.class, user.getUserUuid());
            if (managedUser != null) {
                em.remove(managedUser);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
