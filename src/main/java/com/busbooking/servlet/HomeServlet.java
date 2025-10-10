package com.busbooking.servlet;

import com.busbooking.dao.TripDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class HomeServlet extends HttpServlet {
    
    private TripDAO tripDAO;
    
    @Override
    public void init() throws ServletException {
        tripDAO = new TripDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Lấy danh sách thành phố đi và thành phố đến để hiển thị trong dropdown
            List<String> departureCities = tripDAO.getAllDepartureCities();
            List<String> arrivalCities = tripDAO.getAllArrivalCities();
            
            request.setAttribute("departureCities", departureCities);
            request.setAttribute("arrivalCities", arrivalCities);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải dữ liệu: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}