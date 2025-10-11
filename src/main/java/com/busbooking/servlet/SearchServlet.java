package com.busbooking.servlet;

import com.busbooking.dao.TripDAO;
import com.busbooking.entity.Trip;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    
    private TripDAO tripDAO;
    
    @Override
    public void init() throws ServletException {
        try {
            tripDAO = new TripDAO();
            System.out.println("✅ TripDAO initialized successfully");
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize TripDAO: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Cannot initialize TripDAO", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String departure = request.getParameter("departure");
        String arrival = request.getParameter("arrival");
        String dateStr = request.getParameter("date");
        
        if (departure != null && arrival != null && dateStr != null) {
            try {
                LocalDate departureDate = LocalDate.parse(dateStr);
                List<Trip> trips = tripDAO.searchTrips(departure, arrival, departureDate);
                
                request.setAttribute("trips", trips);
                request.setAttribute("departureCity", departure);
                request.setAttribute("arrivalCity", arrival);
                request.setAttribute("departureDate", dateStr);
                
                request.getRequestDispatcher("/search-results.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Có lỗi xảy ra khi tìm kiếm chuyến xe: " + e.getMessage());
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}