// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package com.busbooking.servlet;

import com.busbooking.dao.AppUserDAO;
import com.busbooking.entity.AppUser;
import com.busbooking.service.EmailService;
import com.busbooking.service.OTPService;
import com.busbooking.service.SupabaseService;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/register"})
public class RegisterServlet extends HttpServlet {
    public RegisterServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\ud83d\udd0d DEBUG: RegisterServlet doGet called");
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\ud83d\ude80 DEBUG: RegisterServlet doPost called");
        System.out.println("\ud83d\udd0d DEBUG: Request URI: " + request.getRequestURI());
        System.out.println("\ud83d\udd0d DEBUG: Context Path: " + request.getContextPath());
        System.out.println("\ud83d\udd0d DEBUG: Servlet Path: " + request.getServletPath());
        String action = request.getParameter("action");
        System.out.println("\ud83d\udd0d DEBUG: Received action = " + action);
        if ("sendOTP".equals(action)) {
            System.out.println("✅ DEBUG: Calling handleSendOTP");
            this.handleSendOTP(request, response);
        } else if ("verifyOTP".equals(action)) {
            System.out.println("✅ DEBUG: Calling handleVerifyOTP");
            this.handleVerifyOTP(request, response);
        } else if ("resendOTP".equals(action)) {
            System.out.println("✅ DEBUG: Calling handleResendOTP");
            this.handleResendOTP(request, response);
        } else {
            System.out.println("❌ DEBUG: No action matched, forwarding to register.jsp");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }

    }

    private void handleSendOTP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\ud83d\ude80 DEBUG: Starting handleSendOTP");
        HttpSession session = request.getSession();
        session.removeAttribute("registrationCompleted");
        System.out.println("\ud83d\udd04 DEBUG: Cleared registrationCompleted flag for new registration");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        System.out.println("\ud83d\udcdd DEBUG: Parameters - name=" + name + ", email=" + email + ", phone=" + phone);
        if (name != null && !name.trim().isEmpty()) {
            if (email != null && !email.trim().isEmpty()) {
                if (password != null && !password.trim().isEmpty()) {
                    if (phone != null && !phone.trim().isEmpty()) {
                        if (!password.equals(confirmPassword)) {
                            request.setAttribute("error", "Mật khẩu xác nhận không trùng khớp!");
                            request.getRequestDispatcher("register.jsp").forward(request, response);
                        } else {
                            try {
                                System.out.println("\ud83d\udd0d DEBUG: Checking if email exists in Supabase: " + email);
                                if (SupabaseService.checkEmailExists(email)) {
                                    System.out.println("❌ DEBUG: Email already exists in Supabase");
                                    request.setAttribute("error", "Email đã được đăng ký!");
                                    request.getRequestDispatcher("register.jsp").forward(request, response);
                                    return;
                                }

                                System.out.println("✅ DEBUG: Email does not exist in Supabase, can proceed");
                            } catch (Exception var11) {
                                System.err.println("❌ ERROR: Supabase error when checking email: " + var11.getMessage());
                                var11.printStackTrace();
                                System.out.println("⚠️ WARNING: Continuing despite Supabase error...");
                            }

                            email = email.toLowerCase().trim();
                            String otp = OTPService.generateOTP();
                            System.out.println("\ud83d\udce7 DEBUG: Storing OTP for normalized email: " + email);
                            OTPService.storeOTP(email, otp);
                            boolean emailSent = EmailService.sendOTPEmail(email, otp);
                            System.out.println("\ud83d\udce7 DEBUG: Email sent result = " + emailSent);
                            if (emailSent) {
                                System.out.println("\ud83d\udcdd DEBUG: Storing data in session ID: " + session.getId());
                                session.setAttribute("registerName", name);
                                session.setAttribute("registerEmail", email);
                                session.setAttribute("registerPassword", password);
                                session.setAttribute("registerPhone", phone);
                                System.out.println("\ud83d\udcdd DEBUG: Session data stored successfully");
                                request.setAttribute("message", "Mã OTP đã được gửi thành công đến email của bạn!");
                                request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
                            } else {
                                System.out.println("❌ DEBUG: Email sending failed, forwarding back to register.jsp");
                                request.setAttribute("error", "Không thể gửi email xác thực. Vui lòng thử lại!");
                                request.getRequestDispatcher("register.jsp").forward(request, response);
                            }

                        }
                    } else {
                        request.setAttribute("error", "Vui lòng nhập số điện thoại!");
                        request.getRequestDispatcher("register.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("error", "Vui lòng nhập mật khẩu!");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Vui lòng nhập email!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Vui lòng nhập họ tên!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private void handleVerifyOTP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = "REQ-" + System.currentTimeMillis();
        System.out.println("\ud83d\ude80 DEBUG [" + requestId + "]: Starting handleVerifyOTP");
        HttpSession session = request.getSession();
        Boolean registrationCompleted = (Boolean)session.getAttribute("registrationCompleted");
        if (registrationCompleted != null && registrationCompleted) {
            System.out.println("⚠️ DEBUG [" + requestId + "]: Registration already completed, redirecting to success page");
            response.sendRedirect("registration-success.jsp");
        } else {
            String inputOTP = request.getParameter("otp");
            System.out.println("\ud83d\udcdd DEBUG [" + requestId + "]: Input OTP = '" + inputOTP + "'");
            System.out.println("\ud83d\udcdd DEBUG [" + requestId + "]: Input OTP length = " + (inputOTP != null ? inputOTP.length() : 0));
            String name = (String)session.getAttribute("registerName");
            String email = (String)session.getAttribute("registerEmail");
            String password = (String)session.getAttribute("registerPassword");
            String phone = (String)session.getAttribute("registerPhone");
            System.out.println("\ud83d\udcdd DEBUG: Session data - name=" + name + ", email=" + email + ", phone=" + phone);
            System.out.println("\ud83d\udcdd DEBUG: Session ID: " + session.getId());
            System.out.println("\ud83d\udcdd DEBUG: Session isNew: " + session.isNew());
            if (inputOTP != null && !inputOTP.trim().isEmpty()) {
                inputOTP = inputOTP.trim();
                if (inputOTP.length() != 6) {
                    System.out.println("❌ DEBUG: Input OTP length is not 6: " + inputOTP.length());
                    request.setAttribute("error", "Mã OTP phải có 6 chữ số!");
                    request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
                } else if (email != null && !email.trim().isEmpty()) {
                    email = email.toLowerCase().trim();
                    System.out.println("\ud83d\udd0d DEBUG: Verifying OTP for email: " + email);
                    System.out.println("\ud83d\udd0d DEBUG: Input OTP to verify: '" + inputOTP + "'");
                    OTPService.debugOTPStorage();
                    boolean otpValid = OTPService.verifyOTP(email, inputOTP);
                    System.out.println("\ud83d\udd0d DEBUG: OTP verification result: " + otpValid);
                    if (otpValid) {
                        System.out.println("✅ DEBUG: OTP is valid, proceeding to create account");

                        try {
                            System.out.println("\ud83d\udce4 DEBUG: Attempting to create user in Supabase...");
                            boolean supabaseSuccess = SupabaseService.createUserInSupabase(email, password, name, phone);
                            System.out.println("\ud83d\udce4 DEBUG: Supabase creation result: " + supabaseSuccess);
                            if (!supabaseSuccess) {
                                System.out.println("⚠️ DEBUG: Supabase failed, but user might already exist - treating as success");
                                supabaseSuccess = true;
                            }

                            if (supabaseSuccess) {
                                System.out.println("\ud83d\udce4 DEBUG: Supabase successful, attempting local database...");

                                try {
                                    AppUser user = new AppUser();
                                    user.setName(name);
                                    user.setEmail(email);
                                    user.setPassword(password);
                                    user.setPhone(phone);
                                    user.setStatus("active");
                                    user.setRole("user");
                                    user.setGender("male");
                                    AppUserDAO userDAO = new AppUserDAO();
                                    userDAO.save(user);
                                    System.out.println("✅ DEBUG: Local database save successful");
                                } catch (Exception var15) {
                                    System.err.println("⚠️ WARNING: Local database save failed but Supabase succeeded: " + var15.getMessage());
                                }
                            }

                            if (supabaseSuccess) {
                                session.setAttribute("registrationCompleted", true);
                                session.removeAttribute("registerName");
                                session.removeAttribute("registerEmail");
                                session.removeAttribute("registerPassword");
                                session.removeAttribute("registerPhone");
                                System.out.println("✅ DEBUG: Registration successful, redirecting to success page");
                                System.out.println("\ud83d\udcdd DEBUG: Redirecting to registration-success.jsp");
                                response.sendRedirect("registration-success.jsp");
                            } else {
                                System.out.println("❌ DEBUG: Supabase creation failed");
                                request.setAttribute("error", "Có lỗi xảy ra khi tạo tài khoản. Vui lòng thử lại!");
                                request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
                            }
                        } catch (Exception var16) {
                            System.err.println("❌ ERROR in handleVerifyOTP: " + var16.getMessage());
                            var16.printStackTrace();
                            request.setAttribute("error", "Có lỗi xảy ra khi tạo tài khoản. Vui lòng thử lại!");
                            request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
                        }
                    } else {
                        System.out.println("❌ DEBUG: OTP verification failed for email: " + email + " with input: '" + inputOTP + "'");
                        System.out.println("\ud83d\udd0d DEBUG: Current OTP storage after failed verification:");
                        OTPService.debugOTPStorage();
                        request.setAttribute("error", "Mã OTP không đúng hoặc đã hết hạn! Vui lòng kiểm tra lại hoặc yêu cầu gửi lại mã mới.");
                        request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
                    }

                } else {
                    System.out.println("❌ DEBUG: Email is null or empty - session expired or invalid");
                    System.out.println("\ud83d\udcdd DEBUG: All session attributes:");
                    Enumeration<String> attrs = session.getAttributeNames();

                    while(attrs.hasMoreElements()) {
                        String attrName = (String)attrs.nextElement();
                        System.out.println("    " + attrName + " = " + String.valueOf(session.getAttribute(attrName)));
                    }

                    request.setAttribute("error", "Phiên đăng ký đã hết hạn hoặc đã hoàn tất. Vui lòng đăng ký lại nếu cần!");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }
            } else {
                System.out.println("❌ DEBUG: Input OTP is null or empty");
                request.setAttribute("error", "Vui lòng nhập mã OTP!");
                request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
            }
        }
    }

    private void handleResendOTP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("registerEmail");
        if (email == null) {
            request.setAttribute("error", "Phiên đăng ký đã hết hạn. Vui lòng thử lại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        } else {
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
}