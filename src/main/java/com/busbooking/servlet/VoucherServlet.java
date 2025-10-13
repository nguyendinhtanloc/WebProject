package com.busbooking.servlet;

import com.busbooking.dao.OrderRepository;
import com.busbooking.dao.PaymentRepository;
import com.busbooking.entity.Order; // Đảm bảo Order entity dùng 'long' cho orderId
import com.busbooking.entity.Voucher;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/voucher")
public class VoucherServlet extends HttpServlet {

    private OrderRepository orderRepository;
    private PaymentRepository paymentRepository;

    @Override
    public void init() {
        orderRepository = new OrderRepository();
        paymentRepository = new PaymentRepository();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager(); // Mở kết nối
        try {
            String orderIdStr = request.getParameter("orderId");
            String voucherCode = request.getParameter("voucher_code");

            // ... (Toàn bộ logic xử lý của bạn giữ nguyên ở đây) ...
            Order currentOrder = null;
            try {
                long orderId = Long.parseLong(orderIdStr);
                currentOrder = orderRepository.findById(orderId, em);
            } catch (NumberFormatException e) {
                request.setAttribute("voucherMessage", "Lỗi: Mã đơn hàng không hợp lệ.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/payment.jsp");
                dispatcher.forward(request, response);
                return;
            }

            if (currentOrder == null) {
                request.setAttribute("voucherMessage", "Lỗi: Không tìm thấy đơn hàng!");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/payment.jsp");
                dispatcher.forward(request, response);
                return;
            }

            BigDecimal originalAmount = currentOrder.getAmount();
            BigDecimal discountAmount = BigDecimal.ZERO;
            BigDecimal finalAmount = originalAmount;

            if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                Voucher voucher = paymentRepository.findVoucherByCode(voucherCode); // Nên truyền em vào đây

                if (voucher != null && voucher.isValid()) {
                    discountAmount = voucher.calculateDiscount(originalAmount);
                    finalAmount = originalAmount.subtract(discountAmount);
                    request.setAttribute("voucherMessage", "Áp dụng mã giảm giá thành công!");
                    request.setAttribute("appliedVoucher", true);
                } else {
                    request.setAttribute("voucherMessage", "Mã giảm giá không hợp lệ hoặc đã hết hạn!");
                    request.setAttribute("appliedVoucher", false);
                }
            } else {
                // Trường hợp người dùng xóa mã voucher đi và submit lại
                request.setAttribute("appliedVoucher", false);
            }

            request.setAttribute("currentOrder", currentOrder);
            request.setAttribute("discountAmount", discountAmount);
            request.setAttribute("finalAmount", finalAmount);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/payment.jsp");
            dispatcher.forward(request, response);

        } finally {
            if (em != null && em.isOpen()) {
                em.close(); // ✅ ĐÓNG KẾT NỐI AN TOÀN
            }
        }
    }
}