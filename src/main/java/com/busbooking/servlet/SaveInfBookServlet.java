package com.busbooking.servlet;

import com.busbooking.dao.OrderRepository;
import com.busbooking.dao.SeatDAO;
import com.busbooking.entity.Order;
import com.busbooking.entity.Seat; // Import Seat entity

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/saveInf")
public class SaveInfBookServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Lấy thông tin khách hàng và tổng tiền
        String customerName = request.getParameter("customerName");
        String customerPhone = request.getParameter("customerPhone");
        String customerEmail = request.getParameter("customerEmail");
        String totalPriceStr = request.getParameter("totalPrice");
        Integer tripId = Integer.valueOf(request.getParameter("tripId"));
        String vehicleType = request.getParameter("vehicle_type");
        Integer veId = Integer.valueOf(request.getParameter("veId"));
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

        Order currentOrder = orderRepo.addOrder(newOrder);
        request.setAttribute("currentOrder", currentOrder);
        request.setAttribute("tripId", tripId);
        request.setAttribute("veId", veId);
        request.setAttribute("vehicleType", vehicleType);
        request.getRequestDispatcher("/payment.jsp").forward(request, response);
    }
}