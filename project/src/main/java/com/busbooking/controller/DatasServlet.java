package com.busbooking.controller;

import com.busbooking.dao.TripTransportStatusLogDAO;
import com.busbooking.model.TripTransportStatusLog;
import com.busbooking.util.AuthUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/datas")
public class DatasServlet extends HttpServlet {
    private TripTransportStatusLogDAO tripTransportStatusLogDAO;
    private static final int LOGS_PER_PAGE = 15;

    @Override
    public void init() {
        tripTransportStatusLogDAO = new TripTransportStatusLogDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!AuthUtils.isAdmin(req, resp)) return;

        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        int currentPage = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            try { currentPage = Integer.parseInt(pageParam); } catch (NumberFormatException ignored) {}
        }

        try {
            int totalLogs = tripTransportStatusLogDAO.getTotalLogCount();
            int totalPages = (int) Math.ceil((double) totalLogs / LOGS_PER_PAGE);

            if (currentPage < 1) currentPage = 1;
            if (currentPage > totalPages && totalPages > 0) currentPage = totalPages;

            List<TripTransportStatusLog> logList = tripTransportStatusLogDAO.getLogsByPage(currentPage, LOGS_PER_PAGE);

            for (TripTransportStatusLog log : logList) {
                if (log.getChangedBy() != null) {
                    log.getChangedBy().getName();
                }
            }

            req.setAttribute("logList", logList);
            req.setAttribute("currentPage", currentPage);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("contentPage", "/WEB-INF/view/pages/datas.jsp");
            req.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(req, resp);

        } catch (Exception e) {
            log("Lỗi khi tải dữ liệu lịch sử thay đổi trạng thái chuyến xe", e);
            throw new ServletException("Không thể tải lịch sử thay đổi chuyến xe", e);
        }
    }
}
