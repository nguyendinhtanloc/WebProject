package com.busbooking.servlet;

import com.busbooking.entity.Order;
import com.busbooking.entity.Voucher;
import com.busbooking.dao.OrderRepository;
import com.busbooking.dao.PaymentRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet("/payment") // Servlet này sẽ xử lý các request đến /payment
public class ShowPaymentPageServlet extends HttpServlet {

    private OrderRepository orderRepository;
    private PaymentRepository paymentRepository;

    @Override
    public void init() {
        orderRepository = new OrderRepository();
        paymentRepository = new PaymentRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF--8");
        resp.setCharacterEncoding("UTF-8");

        // Lấy session hiện tại
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            resp.getWriter().write("Bạn chưa đăng nhập. Vui lòng truy cập /login để đăng nhập.");
            return;
        }

        Map<String, Object> user = (Map<String, Object>) session.getAttribute("currentUser");

        try {
            // Lấy orderId từ URL, ví dụ: /payment?orderId=1
            String orderIdStr = req.getParameter("orderId");
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                resp.getWriter().write("Lỗi: Vui lòng cung cấp ID đơn hàng.");
                return;
            }

            Long orderId = Long.parseLong(orderIdStr);
            Order order = orderRepository.findById(orderId);

            if (order == null) {
                resp.getWriter().write("Lỗi: Không tìm thấy đơn hàng với ID = " + orderId);
                return;
            }

            //  Lấy voucherCode (tùy chọn)
            String voucherCode = req.getParameter("voucher_code");
            BigDecimal discountAmount = BigDecimal.ZERO;
            BigDecimal finalAmount = order.getAmount();

            if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                Voucher voucher = paymentRepository.findVoucherByCode(voucherCode);
                if (voucher != null && voucher.isValid()) {
                    discountAmount = voucher.calculateDiscount(order.getAmount());
                    finalAmount = order.getAmount().subtract(discountAmount);
                    req.setAttribute("voucherMessage", "Áp dụng mã giảm giá thành công!");
                } else {
                    req.setAttribute("voucherMessage", "Mã giảm giá không hợp lệ!");
                }
            }

            // Gửi tất cả dữ liệu sang JSP
            req.setAttribute("user", user);
            req.setAttribute("order", order);
            req.setAttribute("discountAmount", discountAmount);
            req.setAttribute("finalAmount", finalAmount);

            // Chuyển tiếp đến trang JSP để hiển thị
            req.getRequestDispatcher("/payment.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.getWriter().write("Lỗi: ID đơn hàng không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            // Chuyển đến trang lỗi chung
        }
    }
}