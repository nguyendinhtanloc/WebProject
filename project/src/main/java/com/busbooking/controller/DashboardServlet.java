package com.busbooking.controller;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.*;
import com.busbooking.dao.*;
import com.busbooking.util.AuthUtils;

import javax.servlet.ServletException;

@WebServlet(urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long totalDrivers = DriverTransportDAO.countAllDrivers();
        long totalVehicles = VehicleTransportDAO.countAllVehicles();
        long totalUsers = AppUserDAO.countUsersByRole();

        request.setAttribute("totalDrivers", totalDrivers);
        request.setAttribute("totalVehicles", totalVehicles);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("contentPage", "/WEB-INF/view/pages/dashboards.jsp");

        request.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(request, response);
        if (!AuthUtils.isAdmin(request, response)) return;

        request.getRequestDispatcher("/WEB-INF/view/pages/dashboards.jsp").forward(request, response);
    }
}
