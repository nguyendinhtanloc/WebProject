package com.busbooking.servlet;

import com.busbooking.dao.TicketDAO;
import com.busbooking.dto.TicketTripDTO;
import java.io.IOException;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lookup")
public class LookupTicketServlet extends HttpServlet {

    public LookupTicketServlet() {
    }

    @Override
    public void init() throws ServletException {

    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String email = req.getParameter("email");
            String ticketIdStr = req.getParameter("ticketCode");
            if (email == null && ticketIdStr == null) {
                req.getRequestDispatcher("/lookupTicket.jsp").forward(req, resp);
                return;
            }

            if (email == null || ticketIdStr == null || email.isEmpty() || ticketIdStr.isEmpty()) {
                req.setAttribute("error", "Email và mã vé không được để trống");
                req.getRequestDispatcher("/lookupTicket.jsp").forward(req, resp);
                return;
            }

            int ticketId;
            try {
                ticketId = Integer.parseInt(ticketIdStr);
            } catch (NumberFormatException var13) {
                req.setAttribute("error", "Mã vé không hợp lệ");
                req.getRequestDispatcher("/lookupTicket.jsp").forward(req, resp);
                return;
            }

            EntityManager em = null;

            try {
                em = this.emf.createEntityManager();
                TicketDAO ticketDAO = new TicketDAO(em);
                Optional<TicketTripDTO> ticketOpt = ticketDAO.findTicket(email, ticketId);
                if (ticketOpt.isPresent()) {
                    req.setAttribute("ticketDto", ticketOpt.get());
                } else {
                    req.setAttribute("error", "Không tìm thấy vé với email và mã vé đã nhập");
                }

                req.getRequestDispatcher("/lookupTicket.jsp").forward(req, resp);
            } finally {
                if (em != null) {
                    em.close();
                }

            }
        } catch (Exception var15) {
            var15.printStackTrace();
            req.setAttribute("error", "Lỗi hệ thống: " + var15.getMessage());
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

    }
}
