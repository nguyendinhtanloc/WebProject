package com.busbooking.controller;

import com.busbooking.filter.CsrfTokenFilter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Chuyển sang doPost để xử lý yêu cầu từ form, an toàn hơn doGet.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // --- NÂNG CẤP BẢO MẬT: KIỂM TRA CSRF TOKEN ---
        String sessionToken = (String) session.getAttribute(CsrfTokenFilter.CSRF_TOKEN_SESSION_ATTR);
        String requestToken = request.getParameter(CsrfTokenFilter.CSRF_TOKEN_SESSION_ATTR);

        if (sessionToken == null || !sessionToken.equals(requestToken)) {
            // Nếu token không khớp, đây có thể là một cuộc tấn công CSRF.
            // Hủy session và chuyển hướng về trang login.
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/view/login.jsp?error=csrf");
            return;
        }
        // --- KẾT THÚC KIỂM TRA ---

        // Nếu token hợp lệ, tiến hành đăng xuất bình thường.
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/view/login.jsp");
    }

    /**
     * Vẫn giữ lại doGet để xử lý các bookmark cũ, nhưng chuyển hướng nó sang trang lỗi
     * hoặc trang login để buộc người dùng phải logout đúng cách.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chuyển hướng về trang đăng nhập để tránh logout bằng phương thức GET.
        response.sendRedirect(request.getContextPath() + "/view/login.jsp");
    }
}
