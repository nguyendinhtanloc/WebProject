package com.busbooking.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object logIdObj = session.getAttribute("logId");
            if (logIdObj != null) {
                try {
                    long logId = (long) logIdObj;
                    new com.busbooking.dao.LoginLogDAO().updateLogout(logId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Không cho logout bằng GET để tránh CSRF
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
