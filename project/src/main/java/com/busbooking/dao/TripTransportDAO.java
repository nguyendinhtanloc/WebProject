package com.busbooking.dao;

import com.busbooking.model.TripTransport;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class TripTransportDAO {

    private final TripTransportStatusLogDAO tripTransportStatusLogDAO = new TripTransportStatusLogDAO();

    // Thêm Trip mới
    // Thêm mới
    public void insertTrip(TripTransport trip, String userEmail) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(trip);
            em.getTransaction().commit();

            tripTransportStatusLogDAO.logChange(null, trip, userEmail, "insert");
        } finally {
            em.close();
        }
    }

    // Cập nhật
    public void updateTrip(TripTransport trip, String userEmail) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // Lấy dữ liệu cũ trước khi merge
            TripTransport oldTrip = em.find(TripTransport.class, trip.getTripId());
            TripTransport oldSnapshot = oldTrip != null ? cloneTrip(oldTrip) : null;

            em.merge(trip);
            em.getTransaction().commit();

            tripTransportStatusLogDAO.logChange(oldSnapshot, trip, userEmail, "update");
        } finally {
            em.close();
        }
    }

    // Xóa
    public void deleteTrip(Integer tripId, String userEmail) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            TripTransport oldTrip = em.find(TripTransport.class, tripId);
            if (oldTrip != null) {
                TripTransport snapshot = cloneTrip(oldTrip);
                em.remove(oldTrip);
                em.getTransaction().commit();

                tripTransportStatusLogDAO.logChange(snapshot, null, userEmail, "delete");
            } else {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    // Helper: sao chép nhanh dữ liệu cũ (tránh lỗi lazy load)
    private TripTransport cloneTrip(TripTransport src) {
        TripTransport t = new TripTransport();
        t.setTripId(src.getTripId());
        t.setDeparturePoint(src.getDeparturePoint());
        t.setDepartureCity(src.getDepartureCity());
        t.setDepartureAddress(src.getDepartureAddress());
        t.setArrivalPoint(src.getArrivalPoint());
        t.setArrivalCity(src.getArrivalCity());
        t.setArrivalAddress(src.getArrivalAddress());
        t.setStatus(src.getStatus());
        return t;
    }

    // Lấy Trip theo ID
    public TripTransport getTripById(Integer tripId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT t FROM TripTransport t " +
                            "LEFT JOIN FETCH t.transportCompany " +
                            "LEFT JOIN FETCH t.vehicleTransport " +
                            "LEFT JOIN FETCH t.driverTransport " +
                            "WHERE t.tripId = :tripId",
                    TripTransport.class)
                    .setParameter("tripId", tripId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Lấy danh sách Trip theo trang
    public List<TripTransport> getTripsByPage(int pageNumber, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT t FROM TripTransport t " +
                            "LEFT JOIN FETCH t.transportCompany " +
                            "LEFT JOIN FETCH t.vehicleTransport " +
                            "LEFT JOIN FETCH t.driverTransport " +
                            "ORDER BY t.departureDate DESC, t.departureTime DESC",
                    TripTransport.class)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Tổng số Trip
    public int getTotalTripCount() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(t) FROM TripTransport t", Long.class)
                    .getSingleResult();
            return count.intValue();
        } finally {
            em.close();
        }
    }
}
