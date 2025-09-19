package com.busbooking.controller;

import com.busbooking.model.AuthService;
import com.busbooking.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet này xử lý việc xác thực mã OTP mà người dùng nhập vào.
 * Nó là bước cuối cùng trước khi đăng nhập thành công, có check thời hạn, số lần nhập sai các kiểu.
 * Nói chung là logic chính nằm hết ở đây.
 */
@WebServlet("/verify-otp")
public class OtpServlet extends HttpServlet {
    private AuthService authService;
    private static final long OTP_VALID_DURATION = 30 * 1000;
    private static final int MAX_ATTEMPTS = 3;

    @Override
    public void init() { authService = new AuthService(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userEmail = (String) session.getAttribute("user_email");
        Long otpTimestamp = (Long) session.getAttribute("otp_timestamp");

        if (userEmail == null || otpTimestamp == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - otpTimestamp > OTP_VALID_DURATION) {
            String newOtp = authService.generateAndSendOtp(userEmail, request);
            session.setAttribute("otp_code", newOtp);
            session.setAttribute("otp_timestamp", currentTime);
            session.setAttribute("otp_attempts", 0);
            request.setAttribute("errorMessage", "Mã OTP đã hết hạn. Một mã mới đã được gửi đến email của bạn.");
            request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);
            return;
        }

        String enteredOtp = request.getParameter("otp").trim();
        String sessionOtp = (String) session.getAttribute("otp_code");
        Integer attempts = (Integer) session.getAttribute("otp_attempts");

        if (sessionOtp != null && sessionOtp.equals(enteredOtp)) {
            String authenticatedEmail = userEmail;
            session.invalidate();
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("user", new User(authenticatedEmail));
            authService.notifyAdmins(authenticatedEmail);
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            attempts++;
            session.setAttribute("otp_attempts", attempts);
            if (attempts >= MAX_ATTEMPTS) {
                authService.sendSecurityAlert(userEmail);
                String newOtp = authService.generateAndSendOtp(userEmail, request);
                session.setAttribute("otp_code", newOtp);
                session.setAttribute("otp_timestamp", currentTime);
                session.setAttribute("otp_attempts", 0);
                request.setAttribute("errorMessage", "Bạn đã nhập sai 3 lần. Một mã OTP mới đã được gửi để bảo mật.");
            } else {
                int remainingAttempts = MAX_ATTEMPTS - attempts;
                request.setAttribute("errorMessage", "Mã OTP không chính xác. Bạn còn " + remainingAttempts + " lần thử.");
            }
            request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);
        }
    }
}
