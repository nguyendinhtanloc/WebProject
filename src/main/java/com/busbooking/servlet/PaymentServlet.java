package com.busbooking.servlet;

import com.busbooking.entity.Payment;
import com.busbooking.exception.SeatUnavailableException;
import com.busbooking.service.PaymentService;
import com.busbooking.service.VNPayService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet("/payment/create")
public class PaymentServlet extends HttpServlet {
    private PaymentService paymentService;
    private VNPayService vnPayService;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        super.init();
        paymentService = new PaymentService();
        vnPayService = new VNPayService(paymentService);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Get parameters from request
            String orderIdStr = req.getParameter("order_id");
            String voucherCode = req.getParameter("voucher_code");

            // Validate required parameters
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                resp.sendRedirect("/payment-error.jsp?message=Missing+required+parameters");
                return;
            }

            // Parse parameters
            Long orderId = Long.parseLong(orderIdStr);

            // THAY ĐỔI LỚN: Gọi phương thức service mới, thực hiện toàn bộ giao dịch
            // Phương thức này sẽ giữ ghế, tạo payment, và đặt hẹn giờ trong 1 transaction.
            Payment payment = paymentService.initiatePaymentTransaction(orderId, voucherCode);

            // Nếu không có lỗi, tạo URL thanh toán VNPay và chuyển hướng người dùng
            String paymentUrl = vnPayService.createPaymentUrl(payment, req);
            resp.sendRedirect(paymentUrl);

        } catch (SeatUnavailableException e) {

            Integer tripId = Integer.valueOf(req.getParameter("tripId"));
            Integer veId = Integer.valueOf(req.getParameter("veId"));
            String vehicleType = req.getParameter("vehicleType");

            // XỬ LÝ QUAN TRỌNG: Bắt lỗi nghiệp vụ khi ghế đã bị người khác đặt
            // Đặt một thông báo lỗi vào session để hiển thị trên trang tiếp theo
            req.getSession().setAttribute("errorMessage", "Rất tiếc, một hoặc nhiều ghế bạn chọn đã có người khác đặt. Vui lòng chọn lại.");
            String redirectUrl = req.getContextPath() + "/book?tripId=" + tripId
                    + "&veId=" + veId
                    + "&vehicleType=" + vehicleType;
            resp.sendRedirect(redirectUrl);

        }catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect("/payment-error.jsp?code=02");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/payment-error.jsp?code=99");
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void writeErrorResponse(PrintWriter out, String code, String message) {
        JsonObject errorResponse = new JsonObject();
        errorResponse.addProperty("code", code);
        errorResponse.addProperty("message", message);
        out.write(gson.toJson(errorResponse));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Forward to payment page
        req.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(req, resp);
    }
}