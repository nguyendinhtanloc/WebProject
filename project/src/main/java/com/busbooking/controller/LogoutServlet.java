package com.busbooking.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet này siêu đơn giản, chỉ có một nhiệm vụ là xử lý việc đăng xuất của người dùng.
 * Cứ gọi đến nó là nó "đá" người dùng về trang login. 😂
 */
@WebServlet("/logout") // Đăng ký servlet này với URL "/logout". Khi người dùng bấm vào link/nút Đăng xuất thì sẽ trỏ vào đây.
public class LogoutServlet extends HttpServlet {
    
    /**
     * Xử lý yêu cầu GET.
     * Thường thì chức năng đăng xuất chỉ cần một cái link (thẻ <a>),
     * mà bấm vào link là tạo ra GET request nên mình dùng doGet là hợp lý.
     * @param request  Đối tượng request từ client.
     * @param response Đối tượng response để gửi về client.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Bước 1: Hủy session hiện tại của người dùng.
        // Dòng này là quan trọng nhất: nó sẽ xóa sạch session và mọi thứ lưu trong đó (như user_email, is_logged_in,...).
        request.getSession().invalidate();
        
        // Bước 2: Chuyển hướng người dùng quay trở lại trang đăng nhập.
        // Sau khi đăng xuất thành công thì phải cho họ về trang login để đăng nhập lại.
        // Dùng getContextPath() để đường dẫn luôn đúng dù mình có đổi tên project sau này.
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
    }
}