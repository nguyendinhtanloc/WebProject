package com.busbooking.controller;

import com.busbooking.dao.LoginLogDAO;
import com.busbooking.model.LoginLog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object logIdObj = session.getAttribute("logId");
            if (logIdObj != null && logIdObj instanceof Number) {
                try {
                    // Chuyển sang int vì DAO findById nhận Integer
                    int logId = ((Number) logIdObj).intValue();
                    LoginLogDAO dao = new LoginLogDAO();
                    LoginLog log = dao.findById(logId);
                    if (log != null) {
                        log.setLogoutTime(LocalDateTime.now()); // ghi logoutTime
                        dao.save(log); // hoặc merge nếu cần
                    }
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
