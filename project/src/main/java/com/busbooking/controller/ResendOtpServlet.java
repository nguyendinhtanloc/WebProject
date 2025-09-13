package com.busbooking.controller;

import com.busbooking.model.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet này để xử lý chức năng "Gửi lại mã OTP".
 * Cái này quan trọng để phòng trường hợp email gửi OTP bị trễ, vào spam,
 * hoặc người dùng lỡ tay xóa mất.
 */
@WebServlet("/resend-otp") // Map URL /resend-otp với servlet này. Người dùng bấm link "Gửi lại mã" là nó chạy vào đây.
public class ResendOtpServlet extends HttpServlet {
    private AuthService authService;
    // Đặt thời gian chờ giữa 2 lần gửi lại là 60 giây.
    // Cái này để chống spam email, không cho người dùng bấm liên tục gây quá tải hệ thống. Quan trọng lắm! 👮‍♂️
    private static final long RESEND_COOLDOWN = 60 * 1000; // 60 giây

    @Override
    public void init() {
        // Vẫn là khởi tạo AuthService quen thuộc.
        authService = new AuthService();
    }

    /**
     * Dùng doGet vì người dùng chỉ cần bấm một cái link "Gửi lại mã" là đủ,
     * không cần submit form gì phức tạp cả.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy session ra để thao tác.
        HttpSession session = request.getSession();
        // Lấy email để biết gửi cho ai, và lấy thời gian gửi lại lần cuối để check cooldown.
        String userEmail = (String) session.getAttribute("user_email");
        Long lastResendTime = (Long) session.getAttribute("last_resend_time");
        long currentTime = System.currentTimeMillis();

        // Kiểm tra xem có đúng là người dùng đang trong quá trình xác thực OTP không.
        // Nếu không có email trong session, tức là vào trang này bất hợp pháp -> "chuyển hộ khẩu" về login.
        if (userEmail == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        // === KIỂM TRA THỜI GIAN CHỜ (COOLDOWN) ===
        // Đây là logic quan trọng để chống spam.
        // Nếu thời gian gửi lại lần cuối khác null VÀ (thời gian hiện tại - lần cuối) < 60s...
        if (lastResendTime != null && (currentTime - lastResendTime < RESEND_COOLDOWN)) {
            // ...thì báo lỗi và không cho gửi.
            request.setAttribute("errorMessage", "Vui lòng đợi một chút trước khi gửi lại mã.");
            request.getRequestDispatcher("/jsp/otp.jsp").forward(request, response);
            return; // Dừng lại luôn.
        }

        // === GỬI LẠI OTP VÀ CẬP NHẬT SESSION ===
        // Nếu mọi thứ oke, không bị cooldown, thì tiến hành gửi lại.
        String newOtp = authService.generateAndSendOtp(userEmail, request); // Gọi service để tạo và gửi mã mới toanh.

        // Cập nhật lại toàn bộ thông tin OTP trong session để khớp với mã mới.
        session.setAttribute("otp_code", newOtp); // Mã mới.
        session.setAttribute("otp_timestamp", currentTime); // Thời gian tạo mới.
        session.setAttribute("otp_attempts", 0); // Reset số lần thử sai về 0.
        // Cái này quan trọng nè: cập nhật lại thời gian gửi lại lần cuối (last_resend_time) thành thời gian hiện tại.
        // Để lần bấm tiếp theo nó còn biết đường mà check cooldown.
        session.setAttribute("last_resend_time", currentTime);

        // Set một thông báo thành công để báo cho người dùng biết là mail đã được gửi đi.
        request.setAttribute("successMessage", "Một mã OTP mới đã được gửi đến email của bạn.");
        // Forward về lại trang OTP để hiển thị thông báo và cho người dùng nhập mã mới.
        request.getRequestDispatcher("/jsp/otp.jsp").forward(request, response);
    }
}