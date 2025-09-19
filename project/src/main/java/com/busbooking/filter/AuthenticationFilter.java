package com.busbooking.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter này hoạt động như một "người gác cổng" cho ứng dụng.
 * Nó chặn tất cả các yêu cầu đến các trang trong thư mục /view/
 * và kiểm tra xem người dùng đã đăng nhập hay chưa.
 */
@WebFilter("/view/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Phương thức này được gọi một lần khi filter được khởi tạo.
    }

    /**
     * Đây là phương thức quan trọng nhất, nơi xử lý logic kiểm tra.
     * Nó được gọi mỗi khi có một request khớp với urlPatterns ("/view/*").
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String requestURI = request.getRequestURI();

        // Kiểm tra xem người dùng đã đăng nhập ĐẦY ĐỦ hay chưa
        // (chỉ khi xác thực OTP thành công thì session mới có attribute "user")
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        // === PHẦN SỬA LỖI ===
        // Tạo một biến để kiểm tra xem trang được yêu cầu có phải là trang công khai không.
        // Trang công khai là trang mà người dùng chưa đăng nhập vẫn có thể truy cập.
        boolean isPublicPage = requestURI.endsWith("login.jsp") || requestURI.endsWith("otp.jsp");
        // === KẾT THÚC SỬA LỖI ===


        if (isLoggedIn) {
            // --- Nếu người dùng ĐÃ đăng nhập đầy đủ ---

            // Nếu họ đã đăng nhập mà lại cố vào trang login hoặc otp, chuyển họ về trang chủ.
            if (isPublicPage) {
                response.sendRedirect(request.getContextPath() + "/view/home.jsp");
            } else {
                // Nếu họ truy cập các trang khác cần bảo vệ (home.jsp, drivers.jsp,...), cho phép đi tiếp.
                chain.doFilter(req, res);
            }
        } else {
            // --- Nếu người dùng CHƯA đăng nhập đầy đủ ---

            // Nếu họ đang cố truy cập một trang công khai (login hoặc otp), cho phép đi tiếp.
            // Đây là logic quan trọng sửa lỗi của bạn.
            if (isPublicPage) {
                chain.doFilter(req, res);
            } else {
                // Nếu họ cố truy cập bất kỳ trang nào khác mà chưa đăng nhập,
                // "đá" họ về trang login.
                response.sendRedirect(request.getContextPath() + "/view/login.jsp");
            }
        }
    }

    @Override
    public void destroy() {
        // Được gọi khi filter bị hủy.
    }
}

