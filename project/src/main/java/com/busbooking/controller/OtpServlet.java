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
@WebServlet("/verify-otp") // Map servlet này với URL /verify-otp, nhận dữ liệu từ form nhập OTP.
public class OtpServlet extends HttpServlet {
    private AuthService authService;
    // Định nghĩa vài hằng số cho dễ quản lý và thay đổi sau này
    private static final long OTP_VALID_DURATION = 30 * 1000; // Thời gian hiệu lực của OTP, 30 giây cho bảo mật.
    private static final int MAX_ATTEMPTS = 3; // Cho phép người dùng nhập sai tối đa 3 lần.

    @Override
    public void init() {
        // Khởi tạo AuthService, y hệt như bên LoginServlet để dùng các hàm nghiệp vụ.
        authService = new AuthService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đầu tiên là phải lôi cái session ra để kiểm tra, vì mọi thông tin tạm thời đều nằm trong đó.
        HttpSession session = request.getSession();
        String userEmail = (String) session.getAttribute("user_email");
        Long otpTimestamp = (Long) session.getAttribute("otp_timestamp");

        // === 1. KIỂM TRA XEM CÓ SESSION HỢP LỆ KHÔNG ===
        // Check xem người dùng có "đi đường tắt" vào trang này không.
        // Nếu không có email hoặc timestamp trong session, nghĩa là chưa qua bước đăng nhập, đá về trang login ngay.
        if (userEmail == null || otpTimestamp == null) {
            response.sendRedirect(request.getContextPath() + "/view/login.jsp");
            return; // Dùng return để kết thúc hàm ngay lập tức.
        }

        // === 2. KIỂM TRA OTP CÓ HẾT HẠN KHÔNG ===
        long currentTime = System.currentTimeMillis(); // Lấy thời gian hiện tại của hệ thống.
        // Nếu (thời gian hiện tại - thời gian tạo OTP) > 30 giây thì hết hạn.
        if (currentTime - otpTimestamp > OTP_VALID_DURATION) {
            // Trường hợp OTP hết hạn:
            // Tạo mã mới và gửi lại cho người dùng.
            String newOtp = authService.generateAndSendOtp(userEmail, request); // <-- Chỗ này đã sửa, phải truyền request vào.

            // Reset lại hết thông tin OTP trong session.
            session.setAttribute("otp_code", newOtp);
            session.setAttribute("otp_timestamp", System.currentTimeMillis());
            session.setAttribute("otp_attempts", 0); // Reset bộ đếm số lần nhập sai.

            // Gửi một thông báo lỗi đặc biệt để người dùng biết là mã đã hết hạn.
            request.setAttribute("errorMessage", "Mã OTP đã hết hạn. Một mã mới đã được gửi đến email của bạn.");
            // Forward về lại trang otp.jsp để họ nhập mã mới.
            request.getRequestDispatcher("/view/otp.jsp").forward(request, response);
            return; // Kết thúc hàm.
        }

        // === 3. LẤY OTP TỪ FORM VÀ SO SÁNH ===
        String enteredOtp = request.getParameter("otp").trim(); // Lấy mã OTP người dùng nhập, trim() để xóa khoảng trắng thừa.
        String sessionOtp = (String) session.getAttribute("otp_code"); // Lấy mã OTP đúng được lưu trong session.
        Integer attempts = (Integer) session.getAttribute("otp_attempts"); // Lấy số lần đã thử sai.

        // So sánh mã người dùng nhập với mã trong session.
        if (sessionOtp != null && sessionOtp.equals(enteredOtp)) {
            // --- TRƯỜNG HỢP ĐÚNG OTP ---
            // Đăng nhập thành công! 

            // Tạo đối tượng User để lưu vào session, đánh dấu là người dùng này đã đăng nhập thành công.
            User user = new User(userEmail);
            session.setAttribute("user", user);

            // (Chức năng thêm) Gửi email thông báo cho admin là có người vừa đăng nhập thành công.
            authService.notifyAdmins(userEmail);
            
            // Dọn dẹp session: Xóa các thuộc tính OTP đi vì không cần nữa, giữ cho session gọn gàng.
            session.removeAttribute("otp_code");
            session.removeAttribute("user_email");
            session.removeAttribute("otp_timestamp");
            session.removeAttribute("otp_attempts");

            // Chuyển hướng tới trang dashboard của người dùng.
            response.sendRedirect(request.getContextPath() + "/view/home.jsp");
        } else {
            // --- TRƯỜNG HỢP SAI OTP ---
            attempts++; // Tăng bộ đếm số lần nhập sai lên 1.
            session.setAttribute("otp_attempts", attempts); // Cập nhật lại vào session.

            if (attempts >= MAX_ATTEMPTS) {
                // Nếu đã nhập sai quá giới hạn (3 lần)...
                authService.sendSecurityAlert(userEmail); // Gửi email cảnh báo bảo mật cho người dùng.
                
                // Tạo và gửi một mã OTP HOÀN TOÀN MỚI để tránh bị dò mã.
                String newOtp = authService.generateAndSendOtp(userEmail, request);
                
                // Reset lại mọi thứ trong session y như lúc OTP hết hạn.
                session.setAttribute("otp_code", newOtp);
                session.setAttribute("otp_timestamp", System.currentTimeMillis());
                session.setAttribute("otp_attempts", 0); 

                // Gửi thông báo cho người dùng biết là họ đã nhập sai quá 3 lần và mã mới đã được gửi.
                request.setAttribute("errorMessage", "Bạn đã nhập sai 3 lần. Một mã OTP mới đã được gửi để bảo mật.");
            } else {
                // Nếu vẫn còn lượt thử...
                int remainingAttempts = MAX_ATTEMPTS - attempts; // Tính số lần thử còn lại.
                request.setAttribute("errorMessage", "Mã OTP không chính xác. Bạn còn " + remainingAttempts + " lần thử.");
            }
            // Dù sai kiểu gì thì cuối cùng cũng forward về lại trang otp.jsp để người dùng nhập lại.
            request.getRequestDispatcher("/view/otp.jsp").forward(request, response);
        }
    }
}