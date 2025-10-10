package com.busbooking.servlet;

import com.busbooking.dao.SeatDAO;
import com.busbooking.entity.Seat;
import com.busbooking.entity.Trip;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/book")
public class ChooseSeatServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String type = request.getParameter("vehicle_type");
            String ve_id = request.getParameter("vehicle_id");
            String tripId = request.getParameter("tripSelected");

            SeatDAO dao = new SeatDAO();
            Trip trip = dao.getTripSelected(tripId);

            List<Seat> seats = dao.getSeatsByVehicle(ve_id);

            request.setAttribute("type", type);
            request.setAttribute("seats", seats);
            request.setAttribute("trip", trip);

            request.getRequestDispatcher("/chooseSeat.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error fetching seats", e);
        }
    }
}