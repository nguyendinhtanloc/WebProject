package com.busbooking.controller;

import com.busbooking.dao.LoginLogDAO;
import com.busbooking.model.LoginLog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/accounts")
public class AccountsServlet extends HttpServlet {
    private final LoginLogDAO loginLogDAO = new LoginLogDAO();
    private static final int LOGS_PER_PAGE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {}
        }

        int offset = (page - 1) * LOGS_PER_PAGE;

        try {
            List<LoginLog> logs = loginLogDAO.getLogs(offset, LOGS_PER_PAGE);
            int totalLogs = loginLogDAO.countLogs();
            int totalPages = (int) Math.ceil((double) totalLogs / LOGS_PER_PAGE);

            request.setAttribute("logs", logs);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            // giống TripServlet: nhúng contentPage vào home.jsp
            request.setAttribute("contentPage", "/WEB-INF/view/pages/accounts.jsp");
            request.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Không thể load lịch sử đăng nhập", e);
        }
    }
}

