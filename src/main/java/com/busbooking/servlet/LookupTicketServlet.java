package com.busbooking.servlet;

import com.busbooking.dao.TicketDAO;
import com.busbooking.dto.TicketTripDTO;
import com.busbooking.util.JPAUtil;
import java.io.IOException;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lookup")
public class LookupTicketServlet extends HttpServlet {

    public LookupTicketServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        // Để trống
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String ticketIdStr = req.getParameter("ticketCode");
        
        // --- Bắt đầu luồng xử lý ---
        
        // 1. Kiểm tra các tham số đầu vào
        if (email == null && ticketIdStr == null) {
            // Lần đầu vào trang, chưa có gì để xử lý
            req.getRequestDispatcher("/lookupTicket.jsp").forward(req, resp);
            return;
        }

        if (email == null || ticketIdStr == null || email.isEmpty() || ticketIdStr.isEmpty()) {
            req.setAttribute("error", "Email và mã vé không được để trống");
            req.getRequestDispatcher("/lookupTicket.jsp").forward(req, resp);
            return;
        }

        EntityManager em = null; // Khai báo EntityManager ở ngoài để finally có thể truy cập

        try {
            // 2. Chuyển đổi và xác thực mã vé
            int ticketId;
            try {
                ticketId = Integer.parseInt(ticketIdStr);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Mã vé không hợp lệ");
                req.getRequestDispatcher("/lookupTicket.jsp").forward(req, resp);
                return; // Dừng xử lý nếu mã vé sai
            }

            // 3. Lấy EntityManager và truy vấn DB
            em = JPAUtil.getEntityManager(); // Lấy kết nối từ lớp tiện ích đã cấu hình đúng
            TicketDAO ticketDAO = new TicketDAO(em);
            Optional<TicketTripDTO> ticketOpt = ticketDAO.findTicket(email, ticketId);
            
            // 4. Xử lý kết quả
            if (ticketOpt.isPresent()) {
                req.setAttribute("ticketDto", ticketOpt.get());
            } else {
                req.setAttribute("error", "Không tìm thấy vé với email và mã vé đã nhập");
            }

        } catch (Exception e) {
            // Bắt các lỗi hệ thống khác
            e.printStackTrace();
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        } finally {
            // 5. Luôn đóng EntityManager để tránh rò rỉ kết nối
            if (em != null) {
                em.close();
            }
            // 6. Luôn chuyển tiếp về trang kết quả
            req.getRequestDispatcher("/lookupTicket.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    public void destroy() {
        // Để trống
    }
}
