package com.busbooking.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter này kiểm tra trạng thái đăng nhập trước khi truy cập các trang cần bảo
 * vệ.
 * Các JSP giờ nằm trong /WEB-INF/view/ nên user không truy cập trực tiếp được.
 */
@WebFilter("/*") // Bắt tất cả request
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        boolean isStaticResource = uri.startsWith(contextPath + "/styles/")
                || uri.startsWith(contextPath + "/images/")
                || uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.endsWith(".png")
                || uri.endsWith(".jpg")
                || uri.endsWith(".jpeg")
                || uri.endsWith(".gif")
                || uri.endsWith(".woff")
                || uri.endsWith(".woff2")
                || uri.endsWith(".ttf")
                || uri.endsWith(".ico");

        boolean isPublicPage = uri.equals(contextPath + "/login")
                || uri.equals(contextPath + "/verify-otp")
                || uri.equals(contextPath + "/resend-otp");
                // || uri.equals(contextPath + "/test-db")
                // || uri.equals(contextPath + "/test-user");

        if (isLoggedIn) {
            if (isPublicPage) {
                response.sendRedirect(contextPath + "/home");
            } else {
                chain.doFilter(req, res);
            }
        } else {
            if (isPublicPage || isStaticResource) {
                chain.doFilter(req, res);
            } else {
                response.sendRedirect(contextPath + "/login");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
