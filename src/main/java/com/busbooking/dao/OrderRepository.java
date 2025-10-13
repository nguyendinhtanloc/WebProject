package com.busbooking.dao;

import com.busbooking.entity.Order;
import com.busbooking.entity.Payment;
import com.busbooking.entity.PaymentTransactionLog;
import com.busbooking.entity.Seat;
import com.busbooking.util.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class OrderRepository {

    public Order findById(Long orderId, EntityManager em) {
        return em.find(Order.class, orderId);
    }

    public Order addOrder(Order order) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            order = em.merge(order);
            tx.commit();
            return order;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public Order update(Order order, EntityManager em) {
        return em.merge(order);
    }

    public boolean updateOrderStatus(Long orderId, String newStatus) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            // 1. Tìm Order trong DB
            Order orderToUpdate = em.find(Order.class, orderId);

            if (orderToUpdate != null) {
                // 2. Thay đổi trạng thái
                orderToUpdate.setStatus(newStatus); 
                
                tx.commit();
                return true;
            } else {
                // Không tìm thấy order, không cần làm gì cả
                return false;
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Trong class OrderRepository.java
    public int cleanupExpiredOrders() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // 1. TÌM các order hết hạn VÀ TẢI SẴN ghế của chúng
            String hql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.seatBooked " + // Dùng LEFT JOIN FETCH để lấy cả order không có ghế
                    "WHERE o.orderStatus = :status AND o.expiresAt < :now";
            TypedQuery<Order> query = em.createQuery(hql, Order.class);
            query.setParameter("status", "Đang Chờ Thanh Toán");
            query.setParameter("now", LocalDateTime.now());
            List<Order> expiredOrders = query.getResultList();

            if (expiredOrders.isEmpty()) {
                tx.commit();
                return 0;
            }

            System.out.println("REPOSITORY: Tìm thấy " + expiredOrders.size() + " đơn hàng. Bắt đầu xử lý...");

            // 2. LẶP QUA các đối tượng đang được quản lý (managed entities)
            for (Order order : expiredOrders) {
                System.out.println("--> Đang xử lý Order ID: " + order.getOrderId());
                // CẬP NHẬT ghế
                if (order.getSeatBooked() != null && !order.getSeatBooked().isEmpty()) {
                    System.out.println("    Giải phóng " + order.getSeatBooked().size() + " ghế.");
                    for (Seat seat : order.getSeatBooked()) {
                        seat.setStatusBook("available");
                    }
                }

                // 2b. XÓA order (sẽ tự động xóa Payment và Log nhờ CascadeType.ALL)
                System.out.println("    Đang xóa Order.");
                em.remove(order);
            }

            tx.commit();
            return expiredOrders.size();

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return 0; // Trả về 0 nếu có lỗi
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
