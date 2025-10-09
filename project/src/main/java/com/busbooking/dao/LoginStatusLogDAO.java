package com.busbooking.dao;

import com.busbooking.model.LoginStatusLog;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class LoginStatusLogDAO {

    /** Ghi log thay đổi trạng thái đăng nhập (ví dụ: pending -> success) */
    public void save(LoginStatusLog statusLog) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(statusLog);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /** Lấy toàn bộ log trạng thái theo loginLogId */
    public List<LoginStatusLog> getLogsByLoginLog(Long loginLogId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT s FROM LoginStatusLog s WHERE s.loginLog.loginLogId = :lid ORDER BY s.changedAt DESC",
                    LoginStatusLog.class)
                    .setParameter("lid", loginLogId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** Lấy toàn bộ log trạng thái của một user */
    public List<LoginStatusLog> getLogsByUser(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT s FROM LoginStatusLog s WHERE s.user.userId = :uid ORDER BY s.changedAt DESC",
                    LoginStatusLog.class)
                    .setParameter("uid", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<LoginStatusLog> getLogs(int offset, int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT l FROM LoginStatusLog l ORDER BY l.changedAt DESC", LoginStatusLog.class)
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
            Long count = em.createQuery("SELECT COUNT(l) FROM LoginStatusLog l", Long.class)
                    .getSingleResult();
            return count.intValue();
        } finally {
            em.close();
        }
    }
}
