package com.busbooking.servlet;

import com.busbooking.util.VietnamProvinces;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class HomeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Sử dụng danh sách 63 tỉnh thành Việt Nam cố định
            List<String> provinces = VietnamProvinces.getAllProvinces();
            
            // Đặt cùng một danh sách cho cả điểm đi và điểm đến
            request.setAttribute("departureCities", provinces);
            request.setAttribute("arrivalCities", provinces);
            
            System.out.println("✅ Loaded " + provinces.size() + " provinces for dropdown selection");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải dữ liệu tỉnh thành: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}