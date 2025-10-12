package com.busbooking.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.busbooking.dao.UserRepository;
import com.busbooking.entity.AppUser;
import com.busbooking.entity.Payment;
import com.busbooking.entity.PaymentTransactionLog;
import com.busbooking.service.PaymentService;
import com.busbooking.service.VNPayService;
import com.google.gson.Gson;

@WebServlet("/payment/vnpay-callback")
public class VNPayCallbackServlet extends HttpServlet {
    private PaymentService paymentService;
    private VNPayService vnPayService;
    private final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(VNPayCallbackServlet.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
        paymentService = new PaymentService();
        vnPayService = new VNPayService(paymentService);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        logger.info("CALLBACK: Checking session...");
        logger.info("CALLBACK: Session object is: " + session);
        if (session != null) {
            logger.info("CALLBACK: currentUser attribute is: " + session.getAttribute("currentUser"));
        }

        UserRepository userRepository = new UserRepository();
        Map<String, String> fields = new HashMap<>();
        PaymentTransactionLog log = new PaymentTransactionLog();
        AppUser user = null;

        try {
            // Lấy toàn bộ param từ callback
            for (Enumeration<String> params = req.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = req.getParameter(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    fields.put(fieldName, fieldValue);
                }
            }

            // Ghi log request
            log.setRequestPayload(gson.toJson(fields));
            logger.info("VNPay callback received: " + gson.toJson(fields));

            // Kiểm tra tham số bắt buộc
            String transactionNo = fields.get("vnp_TxnRef");
            String responseCode = fields.get("vnp_ResponseCode");

            if (transactionNo == null || responseCode == null) {
                req.setAttribute("message", "Thiếu tham số bắt buộc trong callback");
                req.setAttribute("status", "error");
                req.getRequestDispatcher("/payment-result.jsp").forward(req, resp);
                return;
            }

            // Xác minh chữ ký
            boolean valid = vnPayService.verifyCallback(fields);
            if (!valid) {
                req.setAttribute("message", "Chữ ký không hợp lệ");
                req.setAttribute("status", "error");
                log.setResponsePayload("Invalid signature");
                paymentService.saveTransactionLog(log);
                req.getRequestDispatcher("/payment-result.jsp").forward(req, resp);
                return;
            }

            // Lấy payment theo transactionNo
            Payment payment = paymentService.getPaymentByTransactionNo(transactionNo);
            if (payment == null) {
                req.setAttribute("message", "Không tìm thấy thông tin thanh toán");
                req.setAttribute("status", "error");
                log.setResponsePayload("Payment not found");
                paymentService.saveTransactionLog(log);
                req.getRequestDispatcher("/payment-result.jsp").forward(req, resp);
                return;
            }

            log.setPayment(payment);

            // Xử lý kết quả thanh toán
            if ("00".equals(responseCode)) {
                // Thành công
                payment.setStatus("success");
                payment.setPaidAt(LocalDateTime.now());
                payment.setTransactionNo(fields.get("vnp_TransactionNo"));
                updateOrderStatus(payment.getOrder().getOrderId(), "paid");
                req.setAttribute("message", "Thanh toán thành công!");
                req.setAttribute("status", "success");
            } else {
                // Thất bại
                payment.setStatus("failed");
                req.setAttribute("message", "Thanh toán thất bại. Mã lỗi: " + responseCode);
                req.setAttribute("status", "failed");
            }

            // Cập nhật DB
            paymentService.updatePayment(payment);

            // Lưu transaction log
            log.setResponsePayload(gson.toJson(fields));
            paymentService.saveTransactionLog(log);

            // Gửi sang JSP
            req.setAttribute("user", user);
            req.setAttribute("payment", payment);
            req.setAttribute("vnpayData", fields);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing VNPay callback", e);
            req.setAttribute("message", "Lỗi xử lý: " + e.getMessage());
            req.setAttribute("status", "error");

            // log lỗi vào transaction log
            log.setResponsePayload("Exception: " + e.getMessage());
            try {
                paymentService.saveTransactionLog(log);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Failed to save transaction log", ex);
            }
        }

        // Trả về kết quả
        req.getRequestDispatcher("/payment-result.jsp").forward(req, resp);
    }

    private void updateOrderStatus(Long orderId, String status) {
        // TODO: gọi TicketService hoặc TicketDAO để update status trong DB
        logger.info("Updating ticket " + orderId + " to status " + status);
    }
}