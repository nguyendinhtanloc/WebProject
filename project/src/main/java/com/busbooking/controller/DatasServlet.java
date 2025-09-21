package com.busbooking.controller;

import com.busbooking.dao.TripLogDAO;
import com.busbooking.model.TripLog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/datas") // Giữ nguyên URL /datas
public class DatasServlet extends HttpServlet {
    private TripLogDAO tripLogDAO;
    private static final int LOGS_PER_PAGE = 15;

    @Override
    public void init() {
        tripLogDAO = new TripLogDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        int currentPage = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                // Giữ nguyên trang 1 nếu tham số không hợp lệ
            }
        }

        try {
            List<TripLog> logList = tripLogDAO.getLogsByPage(currentPage, LOGS_PER_PAGE);
            int totalLogs = tripLogDAO.getTotalLogCount();
            int totalPages = (int) Math.ceil((double) totalLogs / LOGS_PER_PAGE);

            if (currentPage < 1) currentPage = 1;
            if (currentPage > totalPages && totalPages > 0) currentPage = totalPages;

            req.setAttribute("logList", logList);
            req.setAttribute("currentPage", currentPage);
            req.setAttribute("totalPages", totalPages);

            // Forward tới home.jsp và nhúng nội dung của datas.jsp vào
            req.setAttribute("contentPage", "/WEB-INF/view/pages/datas.jsp");
            req.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Không thể tải lịch sử thay đổi chuyến xe", e);
        }
    }
}