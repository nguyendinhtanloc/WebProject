package com.busbooking.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import com.busbooking.util.AuthUtils;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra session + role admin
        if (!AuthUtils.isAdmin(request, response)) {
            // Nếu không phải admin, AuthUtils đã redirect / message
            return;
        }

        // Nếu là admin, forward tới home.jsp
        request.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(request, response);
    }
}
