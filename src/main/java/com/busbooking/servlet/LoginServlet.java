package com.busbooking.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.busbooking.dao.AppUserDAO;
import com.busbooking.entity.AppUser;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to login.jsp
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        AppUserDAO userDAO = new AppUserDAO();
        AppUser user = userDAO.login(email, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("index.jsp"); // redirect về trang chủ
        } else {
            request.setAttribute("error", "Sai email hoặc mật khẩu!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}