package com.busbooking.controller;

import com.busbooking.util.AuthUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthUtils.isAdmin(request, response)) return;

        request.getRequestDispatcher("/WEB-INF/view/pages/dashboards.jsp").forward(request, response);
    }
}
