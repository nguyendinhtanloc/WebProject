package com.busbooking.controller;

import com.busbooking.dao.LoginLogDAO;
import com.busbooking.model.LoginLog;
import com.busbooking.util.AuthUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/accounts")
public class AccountsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final LoginLogDAO loginLogDAO = new LoginLogDAO();
    private static final int LOGS_PER_PAGE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthUtils.isAdmin(request, response)) return;

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try { page = Math.max(1, Integer.parseInt(pageParam)); } catch (NumberFormatException ignored) {}
        }

        int offset = (page - 1) * LOGS_PER_PAGE;

        try {
            List<LoginLog> logs = loginLogDAO.getLogs(offset, LOGS_PER_PAGE);
            int totalLogs = loginLogDAO.countLogs();
            int totalPages = (int) Math.ceil((double) totalLogs / LOGS_PER_PAGE);

            request.setAttribute("logs", logs);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("contentPage", "/WEB-INF/view/pages/accounts.jsp");
            request.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi tải danh sách login.");
            request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
        }
    }
}
