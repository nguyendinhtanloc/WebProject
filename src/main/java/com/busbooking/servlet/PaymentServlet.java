package com.busbooking.servlet;

import com.busbooking.entity.Payment;
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

            // Create payment record
            Payment payment = paymentService.createPayment(orderId, voucherCode);

            // Generate VNPay payment URL
            String paymentUrl = vnPayService.createPaymentUrl(payment, req);

            resp.sendRedirect(paymentUrl);
        } catch (NumberFormatException e) {
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