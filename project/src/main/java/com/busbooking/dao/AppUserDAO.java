package com.busbooking.dao;

import com.busbooking.model.AppUser;
import com.busbooking.util.JPAUtil;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class AppUserDAO {

    /** Tìm user theo email */
    public AppUser findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT u FROM AppUser u WHERE u.email = :email",
                    AppUser.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public static long countUsersByRole() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM AppUser u WHERE u.role = :role", Long.class);
            query.setParameter("role", "user");
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // === Lấy danh sách tất cả user (nếu cần dùng) ===
    public List<AppUser> selectAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<AppUser> query = em.createQuery("SELECT u FROM AppUser u", AppUser.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
