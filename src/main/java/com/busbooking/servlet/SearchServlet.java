package com.busbooking.servlet;

import com.busbooking.dao.TripDAO;
import com.busbooking.entity.Trip;
import com.busbooking.util.VietnamProvinces;

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
        
        // Hỗ trợ cả 2 loại parameter để tương thích với form khác nhau
        String departure = request.getParameter("departure");
        if (departure == null) {
            departure = request.getParameter("departureCity");
        }
        
        String arrival = request.getParameter("arrival");
        if (arrival == null) {
            arrival = request.getParameter("arrivalCity");
        }
        
        String dateStr = request.getParameter("date");
        if (dateStr == null) {
            dateStr = request.getParameter("departureDate");
        }
        
        String sessionEmail = request.getParameter("sessionName");
        
        // Luôn truyền danh sách tỉnh thành để hiển thị trong form tìm kiếm
        List<String> provinces = VietnamProvinces.getAllProvinces();
        request.setAttribute("departureCities", provinces);
        request.setAttribute("arrivalCities", provinces);
        
        if (departure != null && arrival != null && dateStr != null) {
            try {
                LocalDate departureDate = LocalDate.parse(dateStr);
                List<Trip> trips = tripDAO.searchTrips(departure, arrival, departureDate);
                
                request.setAttribute("trips", trips);
                request.setAttribute("departureCity", departure);
                request.setAttribute("arrivalCity", arrival);
                request.setAttribute("departureDate", dateStr);
                request.setAttribute("sessionName", sessionEmail);
                
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