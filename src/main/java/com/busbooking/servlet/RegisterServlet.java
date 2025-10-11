package com.busbooking.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.busbooking.dao.AppUserDAO;
import com.busbooking.entity.AppUser;
import com.busbooking.service.EmailService;
import com.busbooking.service.OTPService;
import com.busbooking.service.SupabaseService;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        System.out.println("🔍 DEBUG: Received action = " + action);
        
        if ("sendOTP".equals(action)) {
            System.out.println("✅ DEBUG: Calling handleSendOTP");
            handleSendOTP(request, response);
        } else if ("verifyOTP".equals(action)) {
            System.out.println("✅ DEBUG: Calling handleVerifyOTP");
            handleVerifyOTP(request, response);
        } else if ("resendOTP".equals(action)) {
            System.out.println("✅ DEBUG: Calling handleResendOTP");
            handleResendOTP(request, response);
        } else {
            System.out.println("❌ DEBUG: No action matched, forwarding to register.jsp");
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
        
        System.out.println("🚀 DEBUG: Starting handleSendOTP");
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        
        System.out.println("📝 DEBUG: Parameters - name=" + name + ", email=" + email + ", phone=" + phone);
        
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
        
        // AppUserDAO userDAO = new AppUserDAO();
        
        // TODO: Database connection issue - commenting out for now
        /*
        AppUserDAO userDAO = new AppUserDAO();
        // Test kết nối database trước
        if (!userDAO.testConnection()) {
            System.err.println("❌ ERROR: Cannot connect to database");
            request.setAttribute("error", "Không thể kết nối cơ sở dữ liệu. Vui lòng thử lại sau!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra email đã tồn tại trong database chưa
        try {
            System.out.println("🔍 DEBUG: Checking if email exists in database: " + email);
            AppUser existingUser = userDAO.findByEmail(email);
            if (existingUser != null) {
                System.out.println("❌ DEBUG: Email already exists in database");
                request.setAttribute("error", "Email đã tồn tại!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            System.out.println("✅ DEBUG: Email does not exist in database, can proceed");
        } catch (Exception e) {
            System.err.println("❌ ERROR: Database error when checking email: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Lỗi kết nối cơ sở dữ liệu. Vui lòng thử lại sau!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        */
        
        // Kiểm tra email trên Supabase
        try {
            System.out.println("🔍 DEBUG: Checking if email exists in Supabase: " + email);
            if (SupabaseService.checkEmailExists(email)) {
                System.out.println("❌ DEBUG: Email already exists in Supabase");
                request.setAttribute("error", "Email đã được đăng ký!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            System.out.println("✅ DEBUG: Email does not exist in Supabase, can proceed");
        } catch (Exception e) {
            System.err.println("❌ ERROR: Supabase error when checking email: " + e.getMessage());
            e.printStackTrace();
            // Không return ở đây, vì có thể Supabase tạm thời lỗi nhưng vẫn có thể tiếp tục
            System.out.println("⚠️ WARNING: Continuing despite Supabase error...");
        }
       
        // Tạo và gửi OTP
        String otp = OTPService.generateOTP();
        OTPService.storeOTP(email, otp);
        
        boolean emailSent = EmailService.sendOTPEmail(email, otp);
        
        System.out.println("📧 DEBUG: Email sent result = " + emailSent);
        
        if (emailSent) {
            HttpSession session = request.getSession();
            session.setAttribute("registerName", name);
            session.setAttribute("registerEmail", email);
            session.setAttribute("registerPassword", password);
            session.setAttribute("registerPhone", phone);
            
            // Chuyển hướng đến trang verify-otp.jsp
            request.setAttribute("message", "Mã OTP đã được gửi thành công đến email của bạn!");
            request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
        } else {
            System.out.println("❌ DEBUG: Email sending failed, forwarding back to register.jsp");
            request.setAttribute("error", "Không thể gửi email xác thực. Vui lòng thử lại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
    
    private void handleVerifyOTP(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("🚀 DEBUG: Starting handleVerifyOTP");
        
        String inputOTP = request.getParameter("otp");
        HttpSession session = request.getSession();
        
        System.out.println("📝 DEBUG: Input OTP = '" + inputOTP + "'");
        System.out.println("📝 DEBUG: Input OTP length = " + (inputOTP != null ? inputOTP.length() : 0));
        
        String name = (String) session.getAttribute("registerName");
        String email = (String) session.getAttribute("registerEmail");
        String password = (String) session.getAttribute("registerPassword");
        String phone = (String) session.getAttribute("registerPhone");
        
        System.out.println("📝 DEBUG: Session data - name=" + name + ", email=" + email + ", phone=" + phone);
        
        // Validate input OTP
        if (inputOTP == null || inputOTP.trim().isEmpty()) {
            System.out.println("❌ DEBUG: Input OTP is null or empty");
            request.setAttribute("error", "Vui lòng nhập mã OTP!");
            request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
            return;
        }
        
        inputOTP = inputOTP.trim();
        
        if (inputOTP.length() != 6) {
            System.out.println("❌ DEBUG: Input OTP length is not 6: " + inputOTP.length());
            request.setAttribute("error", "Mã OTP phải có 6 chữ số!");
            request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
            return;
        }
        
        if (email == null) {
            System.out.println("❌ DEBUG: Email is null - session expired");
            request.setAttribute("error", "Phiên đăng ký đã hết hạn hoặc đã hoàn tất. Vui lòng đăng ký lại nếu cần!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Xác thực OTP
        System.out.println("🔍 DEBUG: Verifying OTP for email: " + email);
        System.out.println("🔍 DEBUG: Input OTP to verify: '" + inputOTP + "'");
        boolean otpValid = OTPService.verifyOTP(email, inputOTP);
        System.out.println("🔍 DEBUG: OTP verification result: " + otpValid);
        
        if (otpValid) {
            // OTP đúng, tạo tài khoản
            System.out.println("✅ DEBUG: OTP is valid, proceeding to create account");
            
            try {
                // Tạo user trong Supabase trước
                System.out.println("📤 DEBUG: Attempting to create user in Supabase...");
                boolean supabaseSuccess = SupabaseService.createUserInSupabase(email, password, name, phone);
                System.out.println("📤 DEBUG: Supabase creation result: " + supabaseSuccess);
                
                // Nếu Supabase thành công, thử tạo trong database local (optional)
                if (supabaseSuccess) {
                    System.out.println("📤 DEBUG: Supabase successful, attempting local database...");
                    try {
                        AppUser user = new AppUser();
                        user.setName(name);
                        user.setEmail(email);
                        user.setPassword(password); // Trong thực tế nên hash password
                        user.setPhone(phone);
                        user.setStatus("active");
                        user.setRole("user");
                        
                        AppUserDAO userDAO = new AppUserDAO();
                        userDAO.save(user);
                        System.out.println("✅ DEBUG: Local database save successful");
                    } catch (Exception dbError) {
                        System.err.println("⚠️ WARNING: Local database save failed but Supabase succeeded: " + dbError.getMessage());
                        // Continue anyway since Supabase succeeded
                    }
                }
                
                if (supabaseSuccess) {
                    // Xóa thông tin khỏi session
                    session.removeAttribute("registerName");
                    session.removeAttribute("registerEmail");
                    session.removeAttribute("registerPassword");
                    session.removeAttribute("registerPhone");
                    
                    System.out.println("✅ DEBUG: Registration successful, redirecting to login");
                    
                    // Hiển thị thông báo thành công trước khi chuyển trang
                    request.setAttribute("success", "🎉 Bạn đăng ký tài khoản thành công! Đang chuyển đến trang đăng nhập...");
                    
                    // Thêm script để tự động chuyển trang sau 2 giây
                    request.setAttribute("redirectScript", 
                        "<script>" +
                        "setTimeout(function() {" +
                        "    window.location.href = 'login.jsp?success=" + 
                        java.net.URLEncoder.encode("Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.", "UTF-8") + "';" +
                        "}, 2000);" +
                        "</script>");
                    
                    request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
                } else {
                    System.out.println("❌ DEBUG: Supabase creation failed");
                    request.setAttribute("error", "Có lỗi xảy ra khi tạo tài khoản. Vui lòng thử lại!");
                    request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
                }
                
            } catch (Exception e) {
                System.err.println("❌ ERROR in handleVerifyOTP: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "Có lỗi xảy ra khi tạo tài khoản. Vui lòng thử lại!");
                request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
            }
        } else {
            System.out.println("❌ DEBUG: OTP verification failed for email: " + email + " with input: '" + inputOTP + "'");
            
            // Kiểm tra xem còn OTP trong storage không để thông báo chi tiết hơn
            // (tạm thời comment vì không có method public để kiểm tra)
            request.setAttribute("error", "Mã OTP không đúng hoặc đã hết hạn! Vui lòng kiểm tra lại hoặc yêu cầu gửi lại mã mới.");
            request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
        }
    }
    
    private void handleResendOTP(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("registerEmail");
        
        if (email == null) {
            request.setAttribute("error", "Phiên đăng ký đã hết hạn. Vui lòng thử lại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Tạo và gửi OTP mới
        String otp = OTPService.generateOTP();
        OTPService.storeOTP(email, otp);
        
        boolean emailSent = EmailService.sendOTPEmail(email, otp);
        
        if (emailSent) {
            request.setAttribute("message", "Mã OTP mới đã được gửi đến email của bạn!");
            request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không thể gửi lại mã OTP. Vui lòng thử lại!");
            request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
        }
    }
}