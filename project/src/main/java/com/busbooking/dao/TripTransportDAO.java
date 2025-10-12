package com.busbooking.dao;

import com.busbooking.model.TripTransport;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class TripTransportDAO {

    private final TripTransportStatusLogDAO tripTransportStatusLogDAO = new TripTransportStatusLogDAO();

    // Thêm Trip mới
    public void insertTrip(TripTransport trip, String userEmail) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(trip);
            em.getTransaction().commit();

            // Ghi log kiểu insert
            tripTransportStatusLogDAO.logChange(trip, userEmail, "insert");
        } finally {
            em.close();
        }
    }

    // Cập nhật Trip
    public void updateTrip(TripTransport trip, String userEmail) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(trip);
            em.getTransaction().commit();

            // Ghi log kiểu update
            tripTransportStatusLogDAO.logChange(trip, userEmail, "update");
        } finally {
            em.close();
        }
    }

    // Xóa Trip
    public void deleteTrip(Integer tripId, String userEmail) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            TripTransport trip = em.find(TripTransport.class, tripId);
            if (trip != null) {
                // Ghi log kiểu delete
                tripTransportStatusLogDAO.logChange(trip, userEmail, "delete");
                em.remove(trip);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
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
