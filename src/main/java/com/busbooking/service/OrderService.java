package com.busbooking.service;

import com.busbooking.dao.OrderRepository;
import com.busbooking.dao.TicketDAO;
import com.busbooking.entity.Order;
import com.busbooking.util.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class OrderService {

    private OrderRepository orderRepository;
    
    public OrderService() {
        this.orderRepository = new OrderRepository();
    }

    /**
     * Xác nhận đơn hàng đã thanh toán và tạo vé.
     * Toàn bộ quá trình được bọc trong một transaction duy nhất.
     * @param orderId ID của đơn hàng cần xử lý.
     */
    public void confirmOrderAndCreateTickets(Long orderId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // 1. Lấy thông tin Order
            Order order = em.find(Order.class, orderId);
            if (order == null) {
                throw new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId);
            }

            // 2. Cập nhật trạng thái Order thành "paid"
            order.setStatus("paid");
            em.merge(order); // Dùng merge để chắc chắn entity được quản lý

            // 3. Tạo các vé tương ứng
            TicketDAO ticketDAO = new TicketDAO(em);
            ticketDAO.createTicketsForOrder(order);

            tx.commit(); // Hoàn tất giao dịch, lưu tất cả thay đổi vào DB
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Hủy bỏ mọi thay đổi nếu có lỗi
            }
            // Ghi log hoặc throw exception để tầng trên xử lý
            throw new RuntimeException("Lỗi khi xác nhận đơn hàng và tạo vé", e);
        } finally {
            em.close();
        }
    }
}
