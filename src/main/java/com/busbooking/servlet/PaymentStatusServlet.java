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
import java.util.UUID;

@WebServlet("/payment/status")
public class PaymentStatusServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        JsonObject response = new JsonObject();

        try {
            String paymentIdStr = req.getParameter("payment_id");

            if (paymentIdStr == null) {
                response.addProperty("code", "01");
                response.addProperty("message", "Missing payment_id parameter");
                out.write(gson.toJson(response));
                return;
            }

            long paymentId = Long.parseLong(paymentIdStr);
            // Get payment from database
            Payment payment = paymentService.getPaymentById(paymentId);

            if (payment != null) {
                response.addProperty("code", "00");
                response.addProperty("message", "success");
                response.addProperty("payment_id", payment.getPaymentId().toString());
                response.addProperty("status", payment.getStatus());
                response.addProperty("amount", payment.getAmount());
                response.addProperty("final_amount", payment.getFinalAmount());
                response.addProperty("transaction_no", payment.getTransactionNo());
                response.addProperty("created_at", payment.getCreatedAt().toString());

                if (payment.getPaidAt() != null) {
                    response.addProperty("paid_at", payment.getPaidAt().toString());
                }
            } else {
                response.addProperty("code", "02");
                response.addProperty("message", "Payment not found");
            }

            out.write(gson.toJson(response));

        } catch (Exception e) {
            e.printStackTrace();
            response.addProperty("code", "99");
            response.addProperty("message", "Error: " + e.getMessage());
            out.write(gson.toJson(response));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        JsonObject response = new JsonObject();

        try {
            String action = req.getParameter("action");
            String orderId = req.getParameter("order_id");
            String transDate = req.getParameter("trans_date");

            if ("query".equals(action)) {
                // Query transaction from VNPay
                JsonObject vnpayResponse = vnPayService.queryTransaction(orderId, transDate);
                response.addProperty("code", "00");
                response.addProperty("message", "success");
                response.add("data", vnpayResponse);

            } else if ("refund".equals(action)) {
                // Process refund
                String amountStr = req.getParameter("amount");
                String user = req.getParameter("user");

                long amount = Long.parseLong(amountStr);
                JsonObject vnpayResponse = vnPayService.refundTransaction(orderId, amount, transDate, user);

                response.addProperty("code", "00");
                response.addProperty("message", "success");
                response.add("data", vnpayResponse);

            } else {
                response.addProperty("code", "03");
                response.addProperty("message", "Invalid action");
            }

            out.write(gson.toJson(response));

        } catch (Exception e) {
            e.printStackTrace();
            response.addProperty("code", "99");
            response.addProperty("message", "Error: " + e.getMessage());
            out.write(gson.toJson(response));
        }
    }
}