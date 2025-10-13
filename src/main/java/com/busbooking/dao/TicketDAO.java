package com.busbooking.dao;
import com.busbooking.dto.TicketTripDTO;
import com.busbooking.entity.Ticket;
// import com.busbooking.entity.Trip;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Optional;
import com.busbooking.entity.Order;
import com.busbooking.entity.Seat;
import com.busbooking.entity.Ticket;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;

public class TicketDAO {

    private EntityManager em;

    public TicketDAO(EntityManager em) {
        this.em = em;
    }

    public Optional<TicketTripDTO> findTicket(String email, int ticketId) {
        String jpql = "SELECT new com.busbooking.dto.TicketTripDTO(" +
                "t.ticketId, t.userId, t.seatId, t.bookingTime, t.status, t.price, " +
                "trip.tripId, trip.departureCity, trip.arrivalCity, trip.departureDate, trip.departureTime, " +
                "trip.distanceKm, trip.arrivalDateTime, trip.price) " +
                "FROM Ticket t " +
                "JOIN Trip trip ON t.tripId = trip.tripId " +
                "JOIN AppUser au ON t.userId = au.userId " +  //
                "WHERE au.email = :email AND t.ticketId = :ticketId";

        TypedQuery<TicketTripDTO> query = em.createQuery(jpql, TicketTripDTO.class);
        query.setParameter("email", email);
        query.setParameter("ticketId", ticketId);

        try {
            TicketTripDTO dto = query.getSingleResult();
            return Optional.of(dto);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // Lấy danh sách vé của user theo email
    public java.util.List<TicketTripDTO> findTicketsByEmail(String email) {
        String jpql = "SELECT new com.busbooking.dto.TicketTripDTO(" +
                "t.ticketId, t.userId, t.seatId, t.bookingTime, t.status, t.price, " +
                "trip.tripId, trip.departureCity, trip.arrivalCity, trip.departureDate, trip.departureTime, " +
                "trip.distanceKm, trip.arrivalDateTime, trip.price) " +
                "FROM Ticket t " +
                "JOIN Trip trip ON t.tripId = trip.tripId " +
                "JOIN AppUser au ON t.userId = au.userId " +
                "WHERE au.email = :email ORDER BY t.bookingTime DESC";

        return em.createQuery(jpql, TicketTripDTO.class)
                .setParameter("email", email)
                .getResultList();
    }

    // Hoàn vé: cập nhật trạng thái vé thành 'cancelled'
    public boolean refundTicket(int ticketId) {
        try {
            em.getTransaction().begin();
            Ticket ticket = em.find(Ticket.class, ticketId);
            if (ticket != null && "booked".equals(ticket.getStatus().name())) {
                ticket.setStatus(Ticket.TicketStatus.cancelled);
                em.merge(ticket);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    // Tạo ticket cho từng ghế đã đặt khi order đã thanh toán thành công
    public void createTicketsForOrder(Order order) {
        // 1. Việc kiểm tra order đã được Service thực hiện, nhưng kiểm tra lại vẫn tốt
        if (order == null || !"paid".equalsIgnoreCase(order.getStatus())) {
            return;
        }

        List<Seat> seats = order.getSeatBooked();
        if (seats == null || seats.isEmpty()) {
            return; // Không có ghế để tạo vé
        }
        
        // 2. Sử dụng BigDecimal để tính toán giá vé cho an toàn
        BigDecimal totalAmount = order.getAmount();
        BigDecimal numberOfSeats = new BigDecimal(seats.size());
        BigDecimal pricePerTicket = totalAmount.divide(numberOfSeats, 2, RoundingMode.HALF_UP);

        // 3. Vòng lặp tạo vé - KHÔNG CÓ QUẢN LÝ TRANSACTION Ở ĐÂY
        for (Seat seat : seats) {
            Ticket ticket = new Ticket();
            
            // 4. Thiết lập các mối quan hệ đối tượng trực tiếp
            ticket.setOrder(order);
            ticket.setUser(order.getUser());   // Giả định Order có getUser() trả về AppUser
            ticket.setTrip(order.getTrip());     // Giả định Order có getTrip() trả về Trip
            ticket.setSeat(seat);
            
            ticket.setBookingTime(LocalDateTime.now());
            ticket.setStatus("active"); // Nên dùng hằng số hoặc Enum
            ticket.setPrice(pricePerTicket); // Lưu dưới dạng BigDecimal
            
            em.persist(ticket);
        }
    }
}
