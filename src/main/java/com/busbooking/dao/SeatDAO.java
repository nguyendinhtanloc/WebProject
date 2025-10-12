package com.busbooking.dao;

import com.busbooking.entity.Payment;
import com.busbooking.entity.Seat;
import com.busbooking.entity.Trip;
import com.busbooking.exception.SeatUnavailableException;
import com.busbooking.util.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SeatDAO {

    public Trip getTripSelected(Integer tripId) {

        EntityManager em = JPAUtil.getEntityManager();
        try {

            // Dùng JPQL với JOIN FETCH để tải Trip và Vehicle trong cùng 1 câu lệnh SQL
            String jpql = "SELECT t FROM Trip t JOIN FETCH t.vehicle WHERE t.tripId = :tripId";
            TypedQuery<Trip> query = em.createQuery(jpql, Trip.class);
            query.setParameter("tripId", tripId);

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
    public List<Seat> findSeatsByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM Seat s WHERE s.id IN :ids", Seat.class)
                    .setParameter("ids", ids)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Seat> getSeatsByVehicle(Integer vehicleId) {
        // Lấy EntityManager từ factory
        EntityManager em = JPAUtil.getEntityManager();
        List<Seat> seats = null;

        try {
            em.getTransaction().begin();

            String jpql = "SELECT s FROM Seat s WHERE s.idVehicle.vehicleId = :vehicleId";

            // Tạo query
            TypedQuery<Seat> query = em.createQuery(jpql, Seat.class);
            query.setParameter("vehicleId", vehicleId);

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

    public void holdSeats(List<Integer> seatIds, EntityManager em) throws SeatUnavailableException {
        List<Seat> seatsToHold = em.createQuery("SELECT s FROM Seat s WHERE s.idSeat IN :ids", Seat.class)
                .setParameter("ids", seatIds)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();

        if (seatsToHold.size() != seatIds.size()) {
            throw new SeatUnavailableException("Một hoặc nhiều ghế bạn chọn không hợp lệ.");
        }

        for (Seat seat : seatsToHold) {
            // SỬA LỖI 1: Sửa lỗi chính tả và logic trạng thái
            if (!"available".equalsIgnoreCase(seat.getStatusBook())) {
                throw new SeatUnavailableException("Rất tiếc, ghế " + seat.getNumSeat() + " đã có người khác đặt.");
            }
        }

        for (Seat seat : seatsToHold) {
            seat.setStatusBook("booked");
            em.merge(seat);
        }
    }
}