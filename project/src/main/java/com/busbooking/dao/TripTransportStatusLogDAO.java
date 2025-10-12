package com.busbooking.dao;

import com.busbooking.model.AppUser;
import com.busbooking.model.TripTransportStatusLog;
import com.busbooking.model.TripTransport;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class TripTransportStatusLogDAO {

    /** Ghi log hành động chuyến xe */
    public void logChange(TripTransport trip, String email, String type) { // type: insert/update/delete
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // Lấy user thực hiện thay đổi
            AppUser user = null;
            try {
                user = em.createQuery(
                        "SELECT u FROM AppUser u WHERE u.email = :email", AppUser.class)
                        .setParameter("email", email)
                        .getSingleResult();
            } catch (NoResultException ignored) {}

            TripTransportStatusLog log = new TripTransportStatusLog();
            log.setTripId(trip != null ? trip.getTripId() : null);
            log.setChangedBy(user);
            log.setType(type); // "insert", "update", "delete"

            // Tạo snapshot cơ bản chuyến xe
            if (trip != null) {
                String snapshot = String.format(
                        "{\"tripId\": %d, \"departurePoint\": \"%s\", \"departureCity\": \"%s\", " +
                        "\"departureAddress\": \"%s\", \"arrivalPoint\": \"%s\", \"arrivalCity\": \"%s\", \"arrivalAddress\": \"%s\", " +
                        "\"status\": \"%s\"}",
                        trip.getTripId(),
                        trip.getDeparturePoint(), trip.getDepartureCity(), trip.getDepartureAddress(),
                        trip.getArrivalPoint(), trip.getArrivalCity(), trip.getArrivalAddress(),
                        trip.getStatus() != null ? trip.getStatus().name() : null
                );
                log.setTripSnapshot(snapshot);
            }

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
            return em.createQuery(
                    "SELECT t FROM TripTransportStatusLog t LEFT JOIN FETCH t.changedBy ORDER BY t.changedAt DESC",
                    TripTransportStatusLog.class)
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
