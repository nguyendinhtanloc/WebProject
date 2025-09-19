package com.busbooking.controller;

import com.busbooking.model.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet xử lý chức năng "Gửi lại mã OTP" theo chuẩn POST an toàn.
 */
@WebServlet("/resend-otp")
public class ResendOtpServlet extends HttpServlet {

    private AuthService authService;
    private static final long RESEND_COOLDOWN = 60 * 1000; // 60 giây

    @Override
    public void init() {
        authService = new AuthService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String userEmail = (String) session.getAttribute("user_email");
        Long lastResendTime = (Long) session.getAttribute("last_resend_time");
        long currentTime = System.currentTimeMillis();

        if (userEmail == null) {
            // Nếu chưa login, quay về trang login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (lastResendTime != null && (currentTime - lastResendTime < RESEND_COOLDOWN)) {
            request.setAttribute("errorMessage", "Vui lòng đợi một chút trước khi gửi lại mã.");
            request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);
            return;
        }

        // Tạo OTP mới và gửi email
        String newOtp = authService.generateAndSendOtp(userEmail, request);
        session.setAttribute("otp_code", newOtp);
        session.setAttribute("otp_timestamp", currentTime);
        session.setAttribute("otp_attempts", 0);
        session.setAttribute("last_resend_time", currentTime);

        request.setAttribute("successMessage", "Một mã OTP mới đã được gửi đến email của bạn.");
        request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);
    }

    // Không cho phép GET để tránh lạm dụng
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET không được phép cho URL này.");
    }
}
