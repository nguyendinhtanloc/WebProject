package com.busbooking.servlet;

import com.busbooking.dao.OrderRepository;
import com.busbooking.dao.PaymentRepository;
import com.busbooking.dao.SeatDAO;
import com.busbooking.entity.Order;
import com.busbooking.entity.Seat; // Import Seat entity
import com.busbooking.entity.Voucher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import com.busbooking.entity.AppUser;

@WebServlet("/saveInf")
public class SaveInfBookServlet extends HttpServlet {

    private PaymentRepository paymentRepository;

    @Override
    public void init() {
        paymentRepository = new PaymentRepository();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        AppUser loggedInUser = (AppUser) session.getAttribute("user");


        // Lấy thông tin khách hàng và tổng tiền
        String customerName = request.getParameter("customerName");
        String customerPhone = request.getParameter("customerPhone");
        String customerEmail = request.getParameter("customerEmail");
        String totalPriceStr = request.getParameter("totalPrice");
        BigDecimal total = new BigDecimal(totalPriceStr);

        String selectedSeatIdsStr = request.getParameter("selected_seats");
        List<Integer> selectedSeatIds = Collections.emptyList();
        if (selectedSeatIdsStr != null && !selectedSeatIdsStr.isEmpty()) {
            selectedSeatIds = Arrays.stream(selectedSeatIdsStr.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }


        SeatDAO seatRepo = new SeatDAO();
        List<Seat> selectedSeatsList = seatRepo.findSeatsByIds(selectedSeatIds);

        OrderRepository orderRepo = new OrderRepository();
        Order newOrder = new Order();
        newOrder.setCustomerName(customerName);
        newOrder.setCustomerPhone(customerPhone);
        newOrder.setCustomerEmail(customerEmail);
        newOrder.setOrderStatus("Đang Chờ Thanh Toán");
        newOrder.setAmount(total);
        newOrder.setSeatBooked(selectedSeatsList);

        //newOrder.setUserId(loggedInUser.getUserUuid());

        Order currentOrder = orderRepo.addOrder(newOrder);

        //  Lấy voucherCode (tùy chọn)
        String voucherCode = request.getParameter("voucher_code");
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal finalAmount = currentOrder.getAmount();

        if (voucherCode != null && !voucherCode.trim().isEmpty()) {
            Voucher voucher = paymentRepository.findVoucherByCode(voucherCode);
            if (voucher != null && voucher.isValid()) {
                discountAmount = voucher.calculateDiscount(currentOrder.getAmount());
                finalAmount = currentOrder.getAmount().subtract(discountAmount);
                request.setAttribute("voucherMessage", "Áp dụng mã giảm giá thành công!");
            } else {
                request.setAttribute("voucherMessage", "Mã giảm giá không hợp lệ!");
            }
        }
        request.setAttribute("currentOrder", currentOrder);
        request.setAttribute("discountAmount", discountAmount);
        request.setAttribute("finalAmount", finalAmount);
        request.getRequestDispatcher("/payment.jsp").forward(request, response);
    }
}