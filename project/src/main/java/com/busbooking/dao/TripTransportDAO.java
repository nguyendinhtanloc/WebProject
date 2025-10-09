package com.busbooking.dao;

import com.busbooking.model.TripTransport;
import com.busbooking.model.enums.TripStatus;
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

            tripTransportStatusLogDAO.logChange(trip, null, trip.getStatus(), userEmail);
        } finally {
            em.close();
        }
    }

    // Cập nhật Trip
    public void updateTrip(TripTransport trip, String userEmail) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            TripTransport oldTrip = em.find(TripTransport.class, trip.getTripId());
            TripStatus oldStatus = oldTrip != null ? oldTrip.getStatus() : null;

            em.merge(trip);
            em.getTransaction().commit();

            tripTransportStatusLogDAO.logChange(trip, oldStatus, trip.getStatus(), userEmail);
        } finally {
            em.close();
        }
    }

    // Xóa Trip theo tripId (Integer)
    public void deleteTrip(Integer tripId, String userEmail) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            TripTransport trip = em.find(TripTransport.class, tripId);
            if (trip != null) {
                em.remove(trip);
                em.getTransaction().commit();

                tripTransportStatusLogDAO.logChange(trip, trip.getStatus(), null, userEmail);
            }
        } finally {
            em.close();
        }
    }

    // Lấy Trip theo tripId (Integer)
    public TripTransport getTripById(Integer tripId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(TripTransport.class, tripId);
        } finally {
            em.close();
        }
    }

    // Lấy danh sách Trip theo trang
    public List<TripTransport> getTripsByPage(int pageNumber, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<TripTransport> trips = em.createQuery(
                    "SELECT t FROM TripTransport t " +
                            "JOIN FETCH t.transportCompany " +
                            "JOIN FETCH t.vehicleTransport " +
                            "JOIN FETCH t.driverTransport " +
                            "ORDER BY t.departureDatetime DESC",
                    TripTransport.class)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

            SeatTransportDAO seatDAO = new SeatTransportDAO();
            for (TripTransport t : trips) {
                t.setSeats(seatDAO.getSeatsByVehicle(t.getVehicleTransport().getVehicleId()));
            }

            return trips;
        } finally {
            em.close();
        }
    }

    // Lấy tổng số Trip
    public int getTotalTripCount() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(t) FROM TripTransport t", Long.class)
                    .getSingleResult();
            return count.intValue(); // ép Long -> int
        } finally {
            em.close();
        }
    }
}
