package com.busbooking.servlet;

import com.busbooking.entity.AppUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "UserServlet", urlPatterns = {"/user"})
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        // TODO: Lấy lịch sử vé, thông tin user từ DB nếu cần
        request.getRequestDispatcher("/user.jsp").forward(request, response);
    }

    private void handleUpdateInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ServletException {
        AppUser user = (AppUser) session.getAttribute("user");

        // Cập nhật thông tin từ form vào đối tượng user
        user.setName(request.getParameter("fullName"));
        user.setPhone(request.getParameter("phone"));
        user.setAddress(request.getParameter("address"));
        user.setGender(request.getParameter("gender"));
        try {
             user.setBirthDate(LocalDate.parse(request.getParameter("birthDate")));
        } catch (Exception e) { /* Bỏ qua nếu ngày sinh không hợp lệ */ }
        
        // Cập nhật mật khẩu nếu có
        String password = request.getParameter("password");
        if (password != null && !password.isEmpty()) {
            user.setPassword(password); // Nên mã hóa
        }

        if (fullName != null && !fullName.isEmpty()) {
                user.setName(fullName);
        }
        if (phone != null && !phone.isEmpty()) {
            user.setPhone(phone);
        }
        if (address != null && !address.isEmpty()) {
            user.setAddress(address);
        }
        if (gender != null && !gender.isEmpty()) {
            user.setGender(gender);
        }

        // Lưu vào DB thông qua DAO
        AppUser updatedUser = appUserDAO.update(user);

        if (updatedUser != null) {
            session.setAttribute("user", updatedUser);
            request.setAttribute("message", "Cập nhật thông tin thành công!");
        } else {
            request.setAttribute("error", "Cập nhật thông tin thất bại!");
        }
        request.getRequestDispatcher("/user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("updateInfo".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }
            handleUpdateInfo(request, response, session);
        } else if ("refundTicket".equals(action)) {
            // Xử lý hoàn vé
            String ticketIdStr = request.getParameter("ticketId");
            if (ticketIdStr == null) {
                request.setAttribute("error", "Thiếu mã vé!");
                request.getRequestDispatcher("/user.jsp").forward(request, response);
                return;
            }
            int ticketId = Integer.parseInt(ticketIdStr);
            javax.persistence.EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("busbookingPU");
            javax.persistence.EntityManager em = emf.createEntityManager();
            try {
                com.busbooking.entity.Ticket ticket = em.find(com.busbooking.entity.Ticket.class, ticketId);
                if (ticket == null) {
                    request.setAttribute("error", "Không tìm thấy vé!");
                    request.getRequestDispatcher("/user.jsp").forward(request, response);
                    return;
                }
                // Lấy giờ xuất phát từ Trip
                com.busbooking.entity.Trip trip = em.find(com.busbooking.entity.Trip.class, ticket.getTripId());
                java.time.LocalDateTime departureDateTime = java.time.LocalDateTime.of(trip.getDepartureDate(), trip.getDepartureTime());
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                long hoursDiff = java.time.Duration.between(now, departureDateTime).toHours();
                int refundPercent = 0;
                if (hoursDiff >= 72) {
                    refundPercent = 70;
                } else if (hoursDiff >= 24) {
                    refundPercent = 50;
                } else if (hoursDiff > 0) {
                    refundPercent = 20; // hoặc 0-30% tùy chính sách
                } else {
                    refundPercent = 0;
                }
                if (refundPercent == 0) {
                    ticket.setStatus(com.busbooking.entity.Ticket.TicketStatus.cancelled);
                    em.getTransaction().begin();
                    em.merge(ticket);
                    em.getTransaction().commit();
                    request.setAttribute("error", "Vé không đủ điều kiện hoàn tiền!");
                } else {
                    ticket.setStatus(com.busbooking.entity.Ticket.TicketStatus.cancelled);
                    em.getTransaction().begin();
                    em.merge(ticket);
                    em.getTransaction().commit();
                    request.setAttribute("message", "Hoàn vé thành công! Số tiền hoàn lại: " + (ticket.getPrice() * refundPercent / 100) + " VND (" + refundPercent + "%)");
                }
            } finally {
                em.close();
                emf.close();
            }
            request.getRequestDispatcher("/user.jsp").forward(request, response);
            return;
        }
        // TODO: Xử lý các action khác nếu cần
    }
}
