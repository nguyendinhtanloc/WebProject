package com.busbooking.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import com.busbooking.dao.*;
import com.busbooking.util.AuthUtils;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra session + role admin
        if (!AuthUtils.isAdmin(request, response)) {
            // Nếu không phải admin, AuthUtils đã redirect / message
            return;
        }
        
        // Load dữ liệu dashboard giống DashboardServlet
        long totalDrivers = DriverTransportDAO.countAllDrivers();
        long totalVehicles = VehicleTransportDAO.countAllVehicles();
        long totalUsers = AppUserDAO.countUsersByRole();
        
        // Load dữ liệu doanh thu từ PaymentDAO
        BigDecimal monthlyRevenue = com.busbooking.dao.PaymentDAO.getMonthlyRevenue();
        
        // Load dữ liệu 7 ngày gần nhất cho biểu đồ
        String chartData = com.busbooking.dao.PaymentDAO.getLast7DaysData();
        System.out.println("DEBUG HomeServlet - Chart Data: " + chartData);
        
        // Load dữ liệu thống kê tuyến đường cho biểu đồ tròn
        String routeData = com.busbooking.dao.TripTransportDAO.getRouteStatistics();
        System.out.println("DEBUG HomeServlet - Route Data: " + routeData);
        
        // Format tiền tệ VND
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);
        String formattedRevenue = formatter.format(monthlyRevenue);
        
        request.setAttribute("totalDrivers", totalDrivers);
        request.setAttribute("totalVehicles", totalVehicles);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("monthlyRevenue", formattedRevenue);
        request.setAttribute("chartData", chartData);
        request.setAttribute("routeData", routeData);
        // Không set contentPage để tránh vòng lặp, sẽ hiển thị dashboard mặc định trong home.jsp
        
        request.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(request, response);
    }
}
