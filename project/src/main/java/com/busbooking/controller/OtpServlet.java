package com.busbooking.controller;

import com.busbooking.dao.AppUserDAO;
import com.busbooking.dao.LoginLogDAO;
import com.busbooking.model.AppUser;
import com.busbooking.model.AuthService;
import com.busbooking.model.LoginLog;
import com.busbooking.model.enums.LoginStatus;
import com.busbooking.util.AuthUtils;
import io.jsonwebtoken.Jwts;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/verify-otp")
public class OtpServlet extends HttpServlet {

    private AuthService authService;
    private static final long OTP_VALID_DURATION_MS = 30 * 1000; // 30 giây
    private static final int MAX_OTP_ATTEMPTS = 3;

    @Override
    public void init() {
        authService = new AuthService();
    }

    // hash OTP bằng SHA-256
    private String hashOtp(String otp) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(otp.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession oldSession = request.getSession(false);
        if (oldSession == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String userEmail = (String) oldSession.getAttribute("userEmail");
        Long otpTimestamp = (Long) oldSession.getAttribute("otpTimestamp");
        String otpHash = (String) oldSession.getAttribute("otpHash");
        Integer otpAttempts = (Integer) oldSession.getAttribute("otpAttempts");

        if (userEmail == null || otpTimestamp == null || otpHash == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (otpAttempts == null) otpAttempts = 0;

        long currentTime = System.currentTimeMillis();
        if (currentTime - otpTimestamp > OTP_VALID_DURATION_MS) {
            // OTP hết hạn → gửi OTP mới
            try {
                String newOtp = authService.generateAndSendOtp(userEmail, request);
                oldSession.setAttribute("otpHash", hashOtp(newOtp));
                oldSession.setAttribute("otpTimestamp", currentTime);
                oldSession.setAttribute("otpAttempts", 0);
                request.setAttribute("errorMessage", "Mã OTP đã hết hạn. Mã mới đã được gửi.");
                request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);
            } catch (Exception e) {
                throw new ServletException("Không thể gửi OTP mới", e);
            }
            return;
        }

        String enteredOtp = request.getParameter("otp");
        if (enteredOtp == null) enteredOtp = "";

        boolean validOtp;
        try {
            validOtp = hashOtp(enteredOtp.trim()).equals(otpHash);
        } catch (Exception e) {
            validOtp = false;
        }

        if (validOtp) {
            try {
                // Lấy thông tin user
                AppUserDAO userDAO = new AppUserDAO();
                AppUser user = userDAO.findByEmail(userEmail);

                // Tạo login log
                LoginLog log = new LoginLog();
                log.setUser(user);
                log.setLoginTime(LocalDateTime.now());
                log.setStatus(LoginStatus.success);
                log.setIpAddress(request.getRemoteAddr());
                log.setUserAgent(request.getHeader("User-Agent"));
                LoginLogDAO logDAO = new LoginLogDAO();

                // === SỬA LỖI: Hứng kết quả trả về từ hàm save() để lấy đối tượng có ID ===
                LoginLog savedLog = logDAO.save(log);

                // Tạo JWT
                String jwtToken = Jwts.builder()
                        .setSubject(user.getEmail())
                        .claim("role", user.getRole())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 giờ
                        .signWith(AuthUtils.JWT_SECRET_KEY)
                        .compact();

                // Tạo session mới (an toàn)
                Map<String, Object> tempData = new HashMap<>();
                tempData.put("user", user);

                // === SỬA LỖI: Dùng "savedLog" để lấy ID, không phải "log" ===
                tempData.put("logId", savedLog.getLogId());
                
                tempData.put("jwtToken", jwtToken);

                oldSession.invalidate();
                HttpSession newSession = request.getSession(true);
                tempData.forEach(newSession::setAttribute);

                authService.notifyAdmins(userEmail);
                response.sendRedirect(request.getContextPath() + "/home");

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Lỗi hệ thống khi đăng nhập: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);
            }
        } else {
            otpAttempts++;
            oldSession.setAttribute("otpAttempts", otpAttempts);
            if (otpAttempts >= MAX_OTP_ATTEMPTS) {
                authService.sendSecurityAlert(userEmail);
                try {
                    String newOtp = authService.generateAndSendOtp(userEmail, request);
                    oldSession.setAttribute("otpHash", hashOtp(newOtp));
                    oldSession.setAttribute("otpTimestamp", currentTime);
                    oldSession.setAttribute("otpAttempts", 0);
                } catch (Exception e) {
                    throw new ServletException("Không thể gửi OTP mới", e);
                }
                request.setAttribute("errorMessage", "Bạn đã nhập sai 3 lần. Mã OTP mới đã được gửi.");
            } else {
                request.setAttribute("errorMessage",
                        "Mã OTP không đúng. Còn " + (MAX_OTP_ATTEMPTS - otpAttempts) + " lần thử.");
            }
            request.getRequestDispatcher("/WEB-INF/view/otp.jsp").forward(request, response);
        }
    }
}