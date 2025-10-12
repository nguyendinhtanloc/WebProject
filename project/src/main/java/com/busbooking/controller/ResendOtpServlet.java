package com.busbooking.controller;

import com.busbooking.model.AuthService;
import com.busbooking.model.AppUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/resend-otp")
public class ResendOtpServlet extends HttpServlet {

    private AuthService authService;
    private static final long RESEND_COOLDOWN_MS = 60 * 1000; // 60 giây

    @Override
    public void init() {
        authService = new AuthService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        AppUser user = (AppUser) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long lastResendTime = (Long) session.getAttribute("lastResendTime");
        long now = System.currentTimeMillis();
        if (lastResendTime != null && now - lastResendTime < RESEND_COOLDOWN_MS) {
            request.setAttribute("errorMessage", "Vui lòng đợi một chút trước khi gửi lại mã.");
            request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);
            return;
        }

        try {
            String newOtp = authService.generateAndSendOtp(user.getEmail(), request);
            session.setAttribute("otp_code", newOtp);
            session.setAttribute("otp_timestamp", now);
            session.setAttribute("otp_attempts", 0);
            session.setAttribute("lastResendTime", now);

            request.setAttribute("successMessage", "Mã OTP mới đã được gửi đến email của bạn.");
            request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Không thể gửi mã OTP mới", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET không được phép cho URL này.");
    }
}
