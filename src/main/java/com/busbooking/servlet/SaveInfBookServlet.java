package com.busbooking.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/saveInf")
public class SaveInfBookServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String customerName = request.getParameter("customerName");
        String customerPhone = request.getParameter("customerPhone");
        String customerEmail = request.getParameter("customerEmail");

        String pickupOption = request.getParameter("selected_pickup_option");
        String pickupLocation = request.getParameter("selected_pickup_location");
        String dropoffOption = request.getParameter("selected_dropoff_option");
        String dropoffLocation = request.getParameter("selected_dropoff_location");

        String selectedSeatsStr = request.getParameter("selected_seats");

        List<String> selectedSeatsList = null;
        if (selectedSeatsStr != null && !selectedSeatsStr.isEmpty()) {
            selectedSeatsList = Arrays.asList(selectedSeatsStr.split(","));
        }

        request.setAttribute("customerName", customerName);
        request.setAttribute("customerPhone", customerPhone);
        request.setAttribute("customerEmail", customerEmail);

        request.setAttribute("pickupOption", pickupOption);
        request.setAttribute("pickupLocation", pickupLocation);
        request.setAttribute("dropoffOption", dropoffOption);
        request.setAttribute("dropoffLocation", dropoffLocation);

        request.setAttribute("selectedSeatsList", selectedSeatsList);

        request.getRequestDispatcher("/payment.jsp").forward(request, response);
    }
}