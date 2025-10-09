package com.busbooking.controller;

import com.busbooking.dao.LoginStatusLogDAO;
import com.busbooking.model.LoginStatusLog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/accounts")
public class AccountsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final LoginStatusLogDAO loginStatusLogDAO = new LoginStatusLogDAO();
    private static final int LOGS_PER_PAGE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Ngăn cache để khi bấm back không bị load lại log cũ
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        int page = 1;
        String pageParam = request.getParameter("page");

        // Xử lý tham số page hợp lệ
        if (pageParam != null) {
            try {
                page = Math.max(1, Integer.parseInt(pageParam)); // Không cho nhỏ hơn 1
            } catch (NumberFormatException ignored) {
            }
        }

        int offset = (page - 1) * LOGS_PER_PAGE;

        try {
            // Lấy dữ liệu từ DAO
            List<LoginStatusLog> logs = loginStatusLogDAO.getLogs(offset, LOGS_PER_PAGE);
            int totalLogs = loginStatusLogDAO.countLogs();
            int totalPages = (int) Math.ceil((double) totalLogs / LOGS_PER_PAGE);

            // Gửi dữ liệu sang JSP
            request.setAttribute("logs", logs);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            // Forward tới layout chính
            request.setAttribute("contentPage", "/WEB-INF/view/pages/accounts.jsp");
            request.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi tải danh sách log.");
            request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
        }
    }
}
