package com.busbooking.service;

import com.busbooking.entity.*;
import com.busbooking.dao.OrderRepository;
import com.busbooking.dao.PaymentRepository;

import java.math.BigDecimal;

public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService() {
        this.paymentRepository = new PaymentRepository();
        this.orderRepository = new OrderRepository();
    }

    // Tạo payment mới
    public Payment createPayment(Long orderId, String voucherCode) throws Exception {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new Exception("Không tìm thấy đơn hàng với ID: " + orderId);
        }

        BigDecimal amount = order.getAmount();
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(amount);

        // Lấy phương thức thanh toán VNPay
        PaymentMethod vnpayMethod = paymentRepository.findPaymentMethodByName("VNPay");
        if (vnpayMethod == null) {
            throw new Exception("VNPay payment method not found");
        }
        payment.setPaymentMethod(vnpayMethod);

        // Áp dụng voucher
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (voucherCode != null && !voucherCode.isEmpty()) {
            Voucher voucher = paymentRepository.findVoucherByCode(voucherCode);
            if (voucher != null && voucher.isValid()) {
                discountAmount = voucher.calculateDiscount(amount);
                payment.setVoucher(voucher);
            }
        }
        payment.setDiscountAmount(discountAmount);


        // Tính tổng cuối cùng
        BigDecimal finalAmount = amount.subtract(discountAmount);
        payment.setFinalAmount(finalAmount);
        // Lưu vào DB
        paymentRepository.save(payment);

        return payment;
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