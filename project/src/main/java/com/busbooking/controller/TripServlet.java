package com.busbooking.controller;

import com.busbooking.dao.TripDAO;
import com.busbooking.model.Trips;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/trips")
public class TripServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TripDAO tripDAO = new TripDAO();
        List<Trips> tripList = tripDAO.getAllTrips();

        request.setAttribute("tripList", tripList);

        // Chỉnh đúng đường dẫn tới JSP trong WEB-INF
        request.getRequestDispatcher("/WEB-INF/view/trips.jsp").forward(request, response);
    }
}

