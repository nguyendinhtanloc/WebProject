package com.busbooking.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Utility class để kiểm tra session và role admin,
 * cũng như quản lý JWT an toàn với HS256.
 */
public class AuthUtils {

    // SecretKey dùng cho JWT HS256
    // Có thể lấy từ config/env, nếu null thì tự sinh
    public static SecretKey JWT_SECRET_KEY;

    static {
        if (JWT_SECRET_KEY == null) {
            // Sinh key HS256 an toàn
            JWT_SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        }
    }

    /**
     * Kiểm tra session + role admin.
     * Nếu không hợp lệ, sẽ redirect về login hoặc hiện message.
     */
    public static boolean isAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || session.getAttribute("jwtToken") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        String token = (String) session.getAttribute("jwtToken");
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String role = claims.get("role", String.class);
            if (!"admin".equals(role)) {
                response.getWriter().write("Bạn không có quyền truy cập trang này!");
                return false;
            }

        } catch (Exception e) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }
}
