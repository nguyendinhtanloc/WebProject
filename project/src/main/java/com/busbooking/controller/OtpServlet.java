package com.busbooking.controller;

import com.busbooking.dao.AppUserDAO;
import com.busbooking.dao.LoginLogDAO;
import com.busbooking.model.AppUser;
import com.busbooking.model.AuthService;
import com.busbooking.model.LoginLog;
import com.busbooking.model.enums.LoginStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/verify-otp")
public class OtpServlet extends HttpServlet {

    private AuthService authService;
    private static final long OTP_VALID_DURATION = 30 * 1000; // 30 giây
    private static final int MAX_ATTEMPTS = 3;

    @Override
    public void init() {
        authService = new AuthService();
    }

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
            try {
                String ip = request.getRemoteAddr();
                String userAgent = request.getHeader("User-Agent");

                // Load user từ DB
                AppUserDAO userDAO = new AppUserDAO();
                AppUser user = userDAO.findByEmail(userEmail);
                if (user == null || user.getUserId() == null) {
                    throw new IllegalStateException("Không tìm thấy user hợp lệ với email: " + userEmail);
                }

                // Tạo LoginLog
                LoginLog loginLog = new LoginLog();
                loginLog.setUser(user);
                loginLog.setLoginTime(LocalDateTime.now());
                loginLog.setCreatedAt(LocalDateTime.now()); // ✅ bắt buộc
                loginLog.setStatus(LoginStatus.success);     // ✅ bắt buộc
                loginLog.setIpAddress(ip);
                loginLog.setUserAgent(userAgent);

                LoginLogDAO loginLogDAO = new LoginLogDAO();
                loginLogDAO.save(loginLog);

                // Lưu logId và user vào session mới
                session.invalidate();
                HttpSession newSession = request.getSession(true);
                newSession.setAttribute("user", user);
                newSession.setAttribute("logId", loginLog.getLogId());

                authService.notifyAdmins(userEmail);
                response.sendRedirect(request.getContextPath() + "/home");

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Chi tiết lỗi ghi log: " + e.getMessage());
                request.setAttribute("errorMessage", "Lỗi hệ thống khi ghi log đăng nhập: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);
            }

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
                int remaining = MAX_ATTEMPTS - attempts;
                request.setAttribute("errorMessage",
                        "Mã OTP không chính xác. Bạn còn " + remaining + " lần thử.");
            }
            request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);
        }
    }
}
