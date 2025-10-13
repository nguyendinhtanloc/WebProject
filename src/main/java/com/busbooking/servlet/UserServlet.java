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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if ("updateInfo".equals(action)) {
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("user") == null) {
				response.sendRedirect(request.getContextPath() + "/login.jsp");
				return;
			}
			AppUser user = (AppUser) session.getAttribute("user");
			String fullName = request.getParameter("fullName");
			String phone = request.getParameter("phone");
			String birthDate = request.getParameter("birthDate");
			String address = request.getParameter("address");
			String gender = request.getParameter("gender");
			String password = request.getParameter("password");

			if (fullName != null && !fullName.isEmpty()) {
				user.setName(fullName);
			}
			if (phone != null && !phone.isEmpty()) {
				user.setPhone(phone);
			}
			if (birthDate != null && !birthDate.isEmpty()) {
				try {
					user.setBirthDate(java.time.LocalDate.parse(birthDate));
				} catch (Exception e) {
					request.setAttribute("error", "Ngày sinh không hợp lệ!");
				}
			}
			if (address != null && !address.isEmpty()) {
				user.setAddress(address);
			}
			if (gender != null && !gender.isEmpty()) {
				user.setGender(gender);
			}
			if (password != null && !password.isEmpty()) {
				user.setPassword(password); // Nên mã hóa mật khẩu ở thực tế
			}
			// Lưu cập nhật vào DB
			new com.busbooking.dao.AppUserDAO().save(user);
			// Cập nhật lại session
			session.setAttribute("user", user);
			request.setAttribute("message", "Cập nhật thông tin thành công!");
			request.getRequestDispatcher("/user.jsp").forward(request, response);
			return;
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
