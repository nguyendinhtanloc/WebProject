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
import com.busbooking.service.EmailService;
import com.busbooking.service.OTPService;
import com.busbooking.service.SupabaseService;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        
        if ("sendOTP".equals(action)) {
            handleSendOTP(request, response);
        } else if ("verifyOTP".equals(action)) {
            handleVerifyOTP(request, response);
        } else {
            // Mặc định chuyển đến trang đăng ký
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
    
    private void handleSendOTP(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        
        // Validation
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập họ tên!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        if (phone == null || phone.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập số điện thoại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra trùng khớp mật khẩu
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không trùng khớp!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        AppUserDAO userDAO = new AppUserDAO();
        
        // Kiểm tra email đã tồn tại chưa
        if (userDAO.findByEmail(email) != null) {
            request.setAttribute("error", "Email đã tồn tại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra email trong Supabase
        if (SupabaseService.checkEmailExists(email)) {
            request.setAttribute("error", "Email đã được đăng ký!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Tạo và gửi OTP
        String otp = OTPService.generateOTP();
        OTPService.storeOTP(email, otp);
        
        boolean emailSent = EmailService.sendOTPEmail(email, otp);
        
        if (emailSent) {
            // Lưu thông tin vào session để sử dụng khi verify OTP
            HttpSession session = request.getSession();
            session.setAttribute("registerName", name);
            session.setAttribute("registerEmail", email);
            session.setAttribute("registerPassword", password);
            session.setAttribute("registerPhone", phone);
            
            request.setAttribute("message", "Mã OTP đã được gửi đến email của bạn. Vui lòng kiểm tra và nhập mã xác thực.");
            request.setAttribute("showOTPForm", true);
            request.getRequestDispatcher("register.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không thể gửi email xác thực. Vui lòng thử lại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
    
    private void handleVerifyOTP(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String inputOTP = request.getParameter("otp");
        HttpSession session = request.getSession();
        
        String name = (String) session.getAttribute("registerName");
        String email = (String) session.getAttribute("registerEmail");
        String password = (String) session.getAttribute("registerPassword");
        String phone = (String) session.getAttribute("registerPhone");
        
        if (email == null) {
            request.setAttribute("error", "Phiên đăng ký đã hết hạn. Vui lòng thử lại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Xác thực OTP
        if (OTPService.verifyOTP(email, inputOTP)) {
            // OTP đúng, tạo tài khoản
            try {
                // Tạo user trong database local
                AppUser user = new AppUser();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password); // Trong thực tế nên hash password
                user.setPhone(phone);
                user.setStatus("active");
                user.setRole("user");
                
                AppUserDAO userDAO = new AppUserDAO();
                userDAO.save(user);
                
                // Tạo user trong Supabase
                boolean supabaseSuccess = SupabaseService.createUserInSupabase(email, password, name, phone);
                
                if (supabaseSuccess) {
                    // Xóa thông tin khỏi session
                    session.removeAttribute("registerName");
                    session.removeAttribute("registerEmail");
                    session.removeAttribute("registerPassword");
                    session.removeAttribute("registerPhone");
                    
                    request.setAttribute("success", "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                } else {
                    // Nếu Supabase thất bại, có thể xóa user khỏi database local
                    userDAO.delete(user);
                    request.setAttribute("error", "Có lỗi xảy ra khi tạo tài khoản. Vui lòng thử lại!");
                    request.setAttribute("showOTPForm", true);
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Có lỗi xảy ra khi tạo tài khoản. Vui lòng thử lại!");
                request.setAttribute("showOTPForm", true);
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Mã OTP không đúng hoặc đã hết hạn!");
            request.setAttribute("showOTPForm", true);
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}