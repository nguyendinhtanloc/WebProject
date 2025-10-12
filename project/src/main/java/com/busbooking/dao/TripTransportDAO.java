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
    
    /**
     * Lấy thống kê top 4 tuyến đường phổ biến nhất (theo số lượng chuyến)
     * Trả về chuỗi JSON để sử dụng trong biểu đồ tròn
     */
    public static String getRouteStatistics() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Lấy top 4 tuyến đường có nhiều chuyến nhất
            String jpql = "SELECT CONCAT(t.departureCity, ' - ', t.arrivalCity) as route, COUNT(t) as tripCount " +
                         "FROM TripTransport t " +
                         "WHERE t.departureCity IS NOT NULL AND t.arrivalCity IS NOT NULL " +
                         "GROUP BY t.departureCity, t.arrivalCity " +
                         "ORDER BY COUNT(t) DESC";
            
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createQuery(jpql)
                                      .setMaxResults(4)
                                      .getResultList();
            
            if (results.isEmpty()) {
                return "{\"labels\":[\"Chưa có dữ liệu\"],\"data\":[1]}";
            }
            
            // Tính tổng số chuyến của top 4
            long top4Total = 0;
            for (Object[] result : results) {
                top4Total += ((Number) result[1]).longValue();
            }
            
            // Lấy tổng số chuyến của tất cả tuyến đường
            String totalJpql = "SELECT COUNT(t) FROM TripTransport t " +
                              "WHERE t.departureCity IS NOT NULL AND t.arrivalCity IS NOT NULL";
            Long totalTrips = em.createQuery(totalJpql, Long.class).getSingleResult();
            
            // Tính số chuyến của các tuyến còn lại
            long othersCount = totalTrips - top4Total;
            
            // Xây dựng JSON
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{\"labels\":[");
            
            StringBuilder labels = new StringBuilder();
            StringBuilder data = new StringBuilder();
            
            // Thêm top 4 tuyến đường
            for (int i = 0; i < results.size(); i++) {
                Object[] result = results.get(i);
                String route = (String) result[0];
                Long count = ((Number) result[1]).longValue();
                
                if (i > 0) {
                    labels.append(",");
                    data.append(",");
                }
                
                labels.append("\"").append(route).append("\"");
                data.append(count);
            }
            
            // Thêm "Khác" nếu có tuyến đường khác
            if (othersCount > 0) {
                if (results.size() > 0) {
                    labels.append(",");
                    data.append(",");
                }
                labels.append("\"Khác\"");
                data.append(othersCount);
            }
            
            jsonBuilder.append(labels.toString());
            jsonBuilder.append("],\"data\":[");
            jsonBuilder.append(data.toString());
            jsonBuilder.append("]}");
            
            return jsonBuilder.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"labels\":[\"Lỗi dữ liệu\"],\"data\":[1]}";
        } finally {
            em.close();
        }
    }
}
