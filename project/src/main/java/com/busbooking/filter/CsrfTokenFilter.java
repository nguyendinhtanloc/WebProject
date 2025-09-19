package com.busbooking.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

/**
 * Filter này chịu trách nhiệm tạo và quản lý Anti-CSRF token.
 * Nó sẽ chạy cho mọi request, đảm bảo rằng mỗi session của người dùng
 * luôn có một token bí mật để xác thực các yêu cầu nhạy cảm.
 */
@WebFilter("/*") // Áp dụng cho tất cả các request
public class CsrfTokenFilter implements Filter {

    public static final String CSRF_TOKEN_SESSION_ATTR = "csrfToken";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        // Kiểm tra xem session đã có CSRF token chưa
        if (session.getAttribute(CSRF_TOKEN_SESSION_ATTR) == null) {
            // Nếu chưa có, tạo một token mới (sử dụng UUID cho tính ngẫu nhiên cao)
            String token = UUID.randomUUID().toString();
            session.setAttribute(CSRF_TOKEN_SESSION_ATTR, token);
        }

        // Cho phép request đi tiếp trong chuỗi filter
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No-op
    }

    @Override
    public void destroy() {
        // No-op
    }
}
