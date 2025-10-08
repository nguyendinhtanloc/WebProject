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
            // Lấy danh sách điểm đi và điểm đến để hiển thị trong dropdown
            List<String> departurePlaces = tripDAO.getAllDeparturePlaces();
            List<String> arrivalPlaces = tripDAO.getAllArrivalPlaces();
            
            request.setAttribute("departurePlaces", departurePlaces);
            request.setAttribute("arrivalPlaces", arrivalPlaces);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải dữ liệu: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}