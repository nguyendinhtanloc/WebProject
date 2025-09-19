package com.busbooking.controller;

import com.busbooking.model.AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private AuthService authService;

    // --- NÂNG CẤP BẢO MẬT: Chống Brute-Force ---
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION_MS = 15 * 60 * 1000; // 15 phút
    private Map<String, LoginAttempt> loginAttempts;

    // Lớp nội bộ để lưu thông tin về các lần đăng nhập sai
    private static class LoginAttempt {
        int count = 0;
        long lockoutTime = 0;
    }

    @Override
    public void init() {
        authService = new AuthService();
        // Sử dụng ServletContext để lưu trữ map, giúp nó tồn tại xuyên suốt ứng dụng
        this.loginAttempts = new ConcurrentHashMap<>();
        getServletContext().setAttribute("loginAttemptsMap", this.loginAttempts);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // --- KIỂM TRA BRUTE-FORCE ---
        LoginAttempt attempt = loginAttempts.computeIfAbsent(email, k -> new LoginAttempt());

        if (System.currentTimeMillis() - attempt.lockoutTime < LOCKOUT_DURATION_MS) {
            request.setAttribute("errorMessage", "Tài khoản đang bị tạm khóa. Vui lòng thử lại sau 15 phút.");
            request.getRequestDispatcher("/view/login.jsp").forward(request, response);
            return;
        }

        try {
            JsonNode authResponse = authService.authenticateUser(email, password);

            if (authResponse != null) {
                // Đăng nhập thành công, xóa lịch sử đăng nhập sai
                loginAttempts.remove(email);

                HttpSession session = request.getSession();
                String otp = authService.generateAndSendOtp(email, request);
                session.setAttribute("otp_code", otp);
                session.setAttribute("user_email", email);
                session.setAttribute("otp_timestamp", System.currentTimeMillis());
                session.setAttribute("otp_attempts", 0);
                response.sendRedirect(request.getContextPath() + "/view/otp.jsp");
            } else {
                // Đăng nhập thất bại, xử lý bộ đếm
                attempt.count++;
                if (attempt.count >= MAX_FAILED_ATTEMPTS) {
                    attempt.lockoutTime = System.currentTimeMillis();
                    request.setAttribute("errorMessage", "Bạn đã nhập sai quá nhiều lần. Tài khoản bị tạm khóa 15 phút.");
                } else {
                    request.setAttribute("errorMessage", "Email hoặc mật khẩu không đúng.");
                }
                loginAttempts.put(email, attempt);
                request.getRequestDispatcher("/view/login.jsp").forward(request, response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại.");
            request.getRequestDispatcher("/view/login.jsp").forward(request, response);
        }
    }
}
