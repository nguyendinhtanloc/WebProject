package com.busbooking.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter này kiểm tra trạng thái đăng nhập trước khi truy cập các trang cần bảo vệ.
 * Các JSP giờ nằm trong /WEB-INF/view/ nên user không truy cập trực tiếp được.
 */
@WebFilter("/*") // Bắt tất cả request
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        // Kiểm tra login
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        // Kiểm tra trang công khai (chỉ dùng URL Servlet, không check JSP nữa)
        boolean isPublicPage = uri.equals(contextPath + "/login") ||
                               uri.equals(contextPath + "/verify-otp") ||
                               uri.equals(contextPath + "/resend-otp");

        if (isLoggedIn) {
            // Nếu đã login mà cố vào public page → redirect về dashboard/home
            if (isPublicPage) {
                response.sendRedirect(contextPath + "/dashboard");
            } else {
                chain.doFilter(req, res); // Cho phép truy cập trang bảo vệ
            }
        } else {
            // Chưa login
            if (isPublicPage) {
                chain.doFilter(req, res); // Cho phép truy cập login/verify-otp/resend-otp
            } else {
                // Truy cập bất kỳ trang bảo vệ nào → redirect login
                response.sendRedirect(contextPath + "/login");
            }
        }
    }

    @Override
    public void destroy() { }
}
