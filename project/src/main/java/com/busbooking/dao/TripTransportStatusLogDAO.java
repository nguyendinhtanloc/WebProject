package com.busbooking.dao;

import com.busbooking.model.AppUser;
import com.busbooking.model.TripTransportStatusLog;
import com.busbooking.model.TripTransport;
import com.busbooking.model.enums.TripStatus;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class TripTransportStatusLogDAO {

    /** Ghi log thay đổi trạng thái chuyến xe */
    public void logChange(TripTransport trip, TripStatus oldStatus, TripStatus newStatus, String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // Lấy thông tin user theo email
            AppUser user = null;
            try {
                user = em.createQuery(
                        "SELECT u FROM AppUser u WHERE u.email = :email", AppUser.class)
                        .setParameter("email", email)
                        .getSingleResult();
            } catch (NoResultException e) {
                // Nếu không tìm thấy user, có thể tạo user ảo hoặc xử lý tùy nhu cầu
                System.out.println("User email không tồn tại: " + email);
            }

            // Tạo log mới
            TripTransportStatusLog log = new TripTransportStatusLog();
            log.setTrip(trip);
            log.setOldStatus(oldStatus);
            log.setNewStatus(newStatus);
            log.setChangedBy(user);

            em.persist(log);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    /** Lấy danh sách log theo trang */
    public List<TripTransportStatusLog> getLogsByPage(int pageNumber, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM TripTransportStatusLog t ORDER BY t.changedAt DESC", TripTransportStatusLog.class)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    /** Lấy tổng số log */
    public int getTotalLogCount() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(t) FROM TripTransportStatusLog t", Long.class)
                    .getSingleResult();
            return count.intValue();
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}
