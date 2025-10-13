package com.busbooking.service;

import com.busbooking.dao.SeatDAO;
import com.busbooking.entity.*;
import com.busbooking.dao.OrderRepository;
import com.busbooking.dao.PaymentRepository;
import com.busbooking.exception.SeatUnavailableException;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final SeatDAO seatDAO; // THÊM MỚI

    public PaymentService() {
        this.paymentRepository = new PaymentRepository();
        this.orderRepository = new OrderRepository();
        this.seatDAO = new SeatDAO(); // THÊM MỚI
    }

    public Payment initiatePaymentTransaction(Long orderId, String voucherCode) throws SeatUnavailableException {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin(); // BẮT ĐẦU MỘT TRANSACTION DUY NHẤT

            // 1. Lấy và kiểm tra Order
            Order order = orderRepository.findById(orderId, em);
            if (order == null || !"Đang Chờ Thanh Toán".equals(order.getOrderStatus())) {
                throw new IllegalStateException("Đơn hàng không hợp lệ hoặc đã được xử lý.");
            }

            // 2. Lấy danh sách Seat ID và giữ ghế
            List<Integer> seatIds = order.getSeatBooked().stream()
                    .map(Seat::getIdSeat)
                    .collect(Collectors.toList());

            // THAY ĐỔI: Gọi phương thức instance và truyền `em` vào
            seatDAO.holdSeats(seatIds, em);

            // 3. Cập nhật Order với thời gian hết hạn
            order.setOrderStatus("Đang Chờ Thanh Toán"); // Cập nhật trạng thái
            order.setExpiresAt(LocalDateTime.now().plusMinutes(3));
            orderRepository.update(order, em); // Dùng phương thức update mới

            // 4. Tạo Payment (Logic từ hàm createPayment được đưa trực tiếp vào đây)
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getAmount());
            payment.setStatus("pending");
            PaymentMethod vnpayMethod = paymentRepository.findPaymentMethodByName("VNPay");
            if (vnpayMethod == null) {
                throw new Exception("VNPay payment method not found");
            }
            payment.setPaymentMethod(vnpayMethod);

            // Xử lý voucher
            BigDecimal discountAmount = BigDecimal.ZERO;
            if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                Voucher voucher = paymentRepository.findVoucherByCode(voucherCode);
                if (voucher != null && voucher.isValid()) {
                    discountAmount = voucher.calculateDiscount(order.getAmount());
                    payment.setVoucher(voucher);
                }
            }
            payment.setDiscountAmount(discountAmount);
            payment.setFinalAmount(order.getAmount().subtract(discountAmount));

            em.persist(payment); // Lưu payment trong transaction hiện tại

            tx.commit(); // KẾT THÚC TRANSACTION - Tất cả thay đổi được lưu
            return payment;

        } catch (SeatUnavailableException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Lỗi hệ thống khi khởi tạo thanh toán.", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public void updatePayment(Payment payment) throws Exception {
        paymentRepository.update(payment);
    }

    public Payment getPaymentById(Long paymentId) throws Exception {
        return paymentRepository.findPaymentById(paymentId);
    }

    public Payment getPaymentByTransactionNo(String transactionNo) throws Exception {
        return paymentRepository.findPaymentByTransactionNo(transactionNo);
    }

    public PaymentMethod getPaymentMethodByName(String name) throws Exception {
        return paymentRepository.findPaymentMethodByName(name);
    }

    public Voucher getVoucherByCode(String code) throws Exception {
        return paymentRepository.findVoucherByCode(code);
    }

    public void saveTransactionLog(PaymentTransactionLog log) throws Exception {
        paymentRepository.save(log);
    }
}
