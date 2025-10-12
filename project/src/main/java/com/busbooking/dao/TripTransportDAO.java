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
