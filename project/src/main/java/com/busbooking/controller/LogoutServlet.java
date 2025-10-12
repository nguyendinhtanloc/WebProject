package com.busbooking.controller;

import com.busbooking.dao.LoginLogDAO;
import com.busbooking.model.LoginLog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Logout qua GET cho nút logout
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object logIdObj = session.getAttribute("logId");
            if (logIdObj instanceof Number) {
                int logId = ((Number) logIdObj).intValue();
                try {
                    LoginLogDAO dao = new LoginLogDAO();
                    LoginLog log = dao.findById(logId);
                    if (log != null) {
                        log.setLogoutTime(LocalDateTime.now());
                        dao.save(log); // merge nếu đã có ID
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Có thể gọi POST tương tự GET nếu muốn bảo mật hơn
        doGet(request, response);
    }
}
