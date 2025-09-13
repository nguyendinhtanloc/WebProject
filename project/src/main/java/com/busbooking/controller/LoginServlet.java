package com.busbooking.controller;

import com.busbooking.model.AuthService;
import com.fasterxml.jackson.databind.JsonNode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet này xử lý logic đăng nhập cho người dùng.
 * Nó nhận email và password từ form đăng nhập, gọi AuthService để xác thực,
 * và nếu thành công thì chuyển sang bước xác thực OTP.
 */
@WebServlet("/login") // Annotation này để map cái servlet này với URL "/login". Khách submit form là nó chạy vào đây.
public class LoginServlet extends HttpServlet {
    // Khai báo một đối tượng AuthService để xử lý logic nghiệp vụ (check pass, gửi mail,...)
    private AuthService authService;

    // LỖI SỐ 2: THÊM LẠI PHƯƠNG THỨC NÀY
    /**
     * Phương thức init() được servlet container gọi một lần duy nhất khi servlet được khởi tạo.
     * Dùng nó để khởi tạo các đối tượng cần thiết.
     * >> FIX LỖI: Phải có hàm này để new cái authService ra, không là lúc gọi ở doPost nó sẽ bị NullPointerException.
     */
    @Override
    public void init() {
        authService = new AuthService();
    }
    // KẾT THÚC SỬA LỖI SỐ 2

    /**
     * Hàm này xử lý yêu cầu POST gửi từ form đăng nhập của người dùng.
     * @param request  Đối tượng chứa thông tin yêu cầu từ client (chứa email, password).
     * @param response Đối tượng để gửi phản hồi về cho client.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy email và password người dùng nhập từ form bên trang login.jsp
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Dùng try-catch để bắt các lỗi ngoại lệ có thể xảy ra, ví dụ mất kết nối mạng khi gọi API
        try {
            // Gọi đến AuthService để xác thực thông tin đăng nhập.
            // Hàm này sẽ trả về thông tin user (dạng JsonNode) nếu thành công, ngược lại trả về null.
            JsonNode authResponse = authService.authenticateUser(email, password);

            // Kiểm tra xem xác thực có thành công không
            if (authResponse != null) {
                // Nếu đăng nhập thành công...

                // Lấy hoặc tạo mới một session cho người dùng. Session để lưu trạng thái đăng nhập.
                HttpSession session = request.getSession();

                // LỖI SỐ 1: SỬA LẠI DÒNG NÀY
                // Code cũ: String otp = authService.generateAndSendOtp(email);
                // >> FIX LỖI: Phải truyền thêm 'request' vào để trong service có thể lấy được context path,
                // cần thiết để tạo link trong email chẳng hạn.
                String otp = authService.generateAndSendOtp(email, request);
                // KẾT THÚC SỬA LỖI SỐ 1

                // Lưu các thông tin cần thiết vào session để dùng ở trang OTP
                session.setAttribute("otp_code", otp); // Lưu mã OTP đúng vào session để lát nữa so sánh.
                session.setAttribute("user_email", email); // Lưu email để biết ai đang xác thực.
                session.setAttribute("otp_timestamp", System.currentTimeMillis()); // Lưu thời gian tạo OTP để check hết hạn (ví dụ cho 5 phút).
                session.setAttribute("otp_attempts", 0); // Khởi tạo số lần nhập sai OTP là 0.

                // Đăng nhập thành công, chuyển hướng người dùng sang trang nhập OTP (otp.jsp).
                // Dùng sendRedirect để URL trên trình duyệt thay đổi.
                response.sendRedirect(request.getContextPath() + "/jsp/otp.jsp");
            } else {
                // Nếu đăng nhập thất bại (email hoặc mật khẩu không đúng)...

                // Gắn một thông báo lỗi vào request attribute.
                request.setAttribute("errorMessage", "Email hoặc mật khẩu không đúng.");
                // Dùng RequestDispatcher để "forward" (chuyển tiếp) request đến trang login.jsp.
                // Forward sẽ giữ lại request và response hiện tại, nên trang login.jsp có thể đọc được cái "errorMessage".
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }
        } catch (IOException e) {
            // Bắt các lỗi hệ thống, ví dụ như không gọi được API xác thực...
            e.printStackTrace(); // In lỗi ra console để debug.
            // Set một thông báo lỗi chung chung cho người dùng.
            request.setAttribute("errorMessage", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại.");
            // Chuyển về lại trang login để hiển thị lỗi.
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}