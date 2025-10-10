package com.busbooking.dao;

import com.busbooking.entity.Seat;
import com.busbooking.entity.Trip;
import com.busbooking.util.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

public class SeatDAO {

    public Trip getTripSelected(String tripId) {
        if (tripId == null || tripId.trim().isEmpty()) {
            return null;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            UUID uuid = UUID.fromString(tripId);

            // Dùng JPQL với JOIN FETCH để tải Trip và Vehicle trong cùng 1 câu lệnh SQL
            String jpql = "SELECT t FROM Trip t JOIN FETCH t.vehicle WHERE t.tripId = :tripId";
            TypedQuery<Trip> query = em.createQuery(jpql, Trip.class);
            query.setParameter("tripId", uuid);

            // getSingleResult() sẽ ném Exception nếu không tìm thấy, nên ta dùng getResultList()
            return query.getResultList().stream().findFirst().orElse(null);

        } catch (IllegalArgumentException e) {
            System.err.println("Định dạng Trip ID không hợp lệ: " + tripId);
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Seat> getSeatsByVehicle(String vehicle_id) {
        // Lấy EntityManager từ factory
        EntityManager em = JPAUtil.getEntityManager();
        List<Seat> seats = null;

        try {
            em.getTransaction().begin();

            String jpql = "SELECT s FROM Seat s WHERE s.idVehicle = :vehicleId";

            // Tạo query
            TypedQuery<Seat> query = em.createQuery(jpql, Seat.class);
            query.setParameter("vehicleId", UUID.fromString(vehicle_id));

            // Thực thi query và lấy kết quả
            seats = query.getResultList();

            // Commit transaction
            em.getTransaction().commit();

        } catch (Exception e) {
            // Nếu có lỗi, rollback transaction
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            // Đóng EntityManager để giải phóng tài nguyên
            em.close();
        }

        if (seats != null) {
            seats.sort((s1, s2) -> {
                String prefix1 = s1.getNumSeat().replaceAll("\\d", "");
                String prefix2 = s2.getNumSeat().replaceAll("\\d", "");
                int cmp = prefix1.compareTo(prefix2);
                if (cmp != 0) return cmp;

                int num1 = Integer.parseInt(s1.getNumSeat().replaceAll("\\D", ""));
                int num2 = Integer.parseInt(s2.getNumSeat().replaceAll("\\D", ""));
                return Integer.compare(num1, num2);
            });
        }

        return seats;
    }
}