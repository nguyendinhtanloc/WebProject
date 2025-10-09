package com.busbooking.dao;

import com.busbooking.model.LoginLog;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class LoginLogDAO {

    /** Ghi lại thông tin đăng nhập mới */
    public void save(LoginLog log) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(log);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public LoginLog findById(Integer loginLogId) { 
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(LoginLog.class, loginLogId);
        } finally {
            em.close();
        }
    }

    public List<LoginLog> getLogsByUser(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT l FROM LoginLog l WHERE l.user.userId = :uid ORDER BY l.loginTime DESC",
                    LoginLog.class)
                    .setParameter("uid", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public LoginLog getLatestLogByUser(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT l FROM LoginLog l WHERE l.user.userId = :uid ORDER BY l.loginTime DESC",
                    LoginLog.class)
                    .setParameter("uid", userId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}