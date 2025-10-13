// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package com.busbooking.servlet;

import com.busbooking.dao.UserRepository;
import com.busbooking.entity.AppUser;
import com.busbooking.service.AuthService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/login"})
public class LoginServlet extends HttpServlet {
    public LoginServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            AuthService authService = new AuthService();
            boolean isValidLogin = authService.signIn(email, password);
            if (isValidLogin) {
                UserRepository userRepo = new UserRepository();
                AppUser user = userRepo.findByEmail(email);
                if (user == null) {
                    request.setAttribute("error", "Không tìm thấy thông tin người dùng sau khi đăng nhập.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }

                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect("index.jsp");
            } else {
                request.setAttribute("error", "Sai email hoặc mật khẩu!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception var10) {
            var10.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + var10.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }
}