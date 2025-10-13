package com.busbooking.servlet;

import com.busbooking.entity.AppUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import com.busbooking.dao.AppUserDAO;

@WebServlet(name = "UserServlet", urlPatterns = {"/user"})
public class UserServlet extends HttpServlet {
    private AppUserDAO appUserDAO;

    @Override
    public void init() throws ServletException {
        appUserDAO = new AppUserDAO();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        // TODO: Lấy lịch sử vé, thông tin user từ DB nếu cần
        request.getRequestDispatcher("/user.jsp").forward(request, response);
    }
     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // --- BỘ ĐIỀU HƯỚNG ACTION ---
        if ("updateInfo".equals(action)) {
            // Lấy đối tượng user từ session
            AppUser user = (AppUser) session.getAttribute("user");

            // Lấy thông tin mới từ form
            String fullName = request.getParameter("name");
            String phone = request.getParameter("phone");
            String birthDateStr = request.getParameter("birthDate");
            String address = request.getParameter("address");
            String gender = request.getParameter("gender");
            String password = request.getParameter("password");

            // Cập nhật vào đối tượng user
            if (fullName != null && !fullName.isEmpty()) user.setName(fullName);
            if (phone != null && !phone.isEmpty()) user.setPhone(phone);
            if (address != null && !address.isEmpty()) user.setAddress(address);
            if (gender != null && !gender.isEmpty()) user.setGender(gender);
            
            if (birthDateStr != null && !birthDateStr.isEmpty()) {
                try {
                     user.setBirthDate(LocalDate.parse(birthDateStr));
                } catch (Exception e) {
                    request.setAttribute("error", "Ngày sinh không hợp lệ!");
                }
            }
            
            if (password != null && !password.isEmpty()) {
                user.setPassword(password); // Nên mã hóa trong dự án thực tế
            }

            // Lưu vào DB thông qua DAO đã được cấu hình đúng
            AppUser updatedUser = appUserDAO.update(user);

            if (updatedUser != null) {
                session.setAttribute("user", updatedUser); // Cập nhật lại user trong session
                request.setAttribute("message", "Cập nhật thông tin thành công!");
            } else {
                request.setAttribute("error", "Cập nhật thông tin thất bại!");
            }
        } else {
            // Xử lý các action khác hoặc báo lỗi nếu cần
            request.setAttribute("error", "Hành động không hợp lệ.");
        }
        
        // Luôn chuyển về trang user để hiển thị kết quả
        request.getRequestDispatcher("/user.jsp").forward(request, response);
    }
}
