package com.busbooking.dao;

import com.busbooking.model.LoginLog;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class LoginLogDAO {

    /** Ghi lại thông tin đăng nhập mới */
    public LoginLog save(LoginLog log) { // 1. Thay void thành LoginLog
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            LoginLog managedLog = em.merge(log); // 2. Lấy kết quả trả về
            em.getTransaction().commit();
            return managedLog; // 3. Trả về đối tượng đã được cập nhật (có ID)
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

    public List<LoginLog> getLogs(int offset, int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT l FROM LoginLog l ORDER BY l.loginTime DESC", LoginLog.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public int countLogs() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(l) FROM LoginLog l", Long.class).getSingleResult();
            return count.intValue();
        } finally {
            em.close();
        }
    }
}