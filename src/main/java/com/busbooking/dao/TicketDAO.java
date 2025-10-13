package com.busbooking.dao;
import com.busbooking.dto.TicketTripDTO;
import com.busbooking.entity.Ticket;
// import com.busbooking.entity.Trip;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Optional;

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
    public void createTicketsForOrder(com.busbooking.entity.Order order) {
        if (order == null || order.getOrderStatus() == null || !"paid".equalsIgnoreCase(order.getOrderStatus())) {
            return; // Chỉ tạo ticket khi order đã thanh toán
        }
        java.util.List<com.busbooking.entity.Seat> seats = order.getSeatBooked();
        long userId = order.getUserId();
        int tripId = order.getTripId();
        double pricePerTicket = order.getAmount().doubleValue() / (seats != null && seats.size() > 0 ? seats.size() : 1);
        em.getTransaction().begin();
        for (com.busbooking.entity.Seat seat : seats) {
            com.busbooking.entity.Ticket ticket = new com.busbooking.entity.Ticket();
            ticket.setUserId(userId);
            ticket.setTripId(tripId);
            ticket.setSeatId(String.valueOf(seat.getIdSeat()));
            ticket.setBookingTime(java.time.LocalDateTime.now());
            ticket.setStatus(com.busbooking.entity.Ticket.TicketStatus.booked);
            ticket.setPrice(pricePerTicket);
            em.persist(ticket);
        }
        em.getTransaction().commit();
    }
}
