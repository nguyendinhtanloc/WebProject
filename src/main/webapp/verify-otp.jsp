<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xác thực OTP - BusBooking</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #333;
        }
        .form-group input {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 6px;
            box-sizing: border-box;
            font-size: 16px;
        }
        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        .btn-primary:hover {
            background-color: #0056b3;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
            margin-left: 10px;
        }
        .btn-secondary:hover {
            background-color: #545b62;
        }
        .btn-link {
            background: none;
            border: none;
            color: #007bff;
            text-decoration: underline;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-link:hover {
            color: #0056b3;
        }
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 6px;
            font-weight: 500;
        }
        .alert-danger {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
        }
        .alert-success {
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
        }
        .alert-info {
            background-color: #cce7ff;
            border: 1px solid #b3d7ff;
            color: #0c5460;
        }
        .otp-input {
            text-align: center;
            font-size: 24px;
            letter-spacing: 8px;
            font-weight: bold;
            border: 3px solid #007bff;
        }
        .otp-input:focus {
            outline: none;
            border-color: #0056b3;
            box-shadow: 0 0 10px rgba(0, 123, 255, 0.3);
        }
        .container {
            max-width: 500px;
            margin: 80px auto;
            padding: 40px;
            border: 1px solid #e0e0e0;
            border-radius: 12px;
            background: white;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .otp-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .otp-header h2 {
            color: #333;
            margin-bottom: 10px;
        }
        .otp-info {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 25px;
            text-align: center;
        }
        .email-info {
            font-weight: bold;
            color: #007bff;
        }
        .timer {
            color: #dc3545;
            font-weight: bold;
            font-size: 18px;
        }
        .resend-section {
            text-align: center;
            margin-top: 25px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="otp-header">
        <h2>🔐 Xác thực OTP</h2>
        <p>Vui lòng nhập mã xác thực để hoàn tất đăng ký</p>
    </div>

    <!-- Hiển thị thông báo lỗi -->
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger">
            <strong>❌ Lỗi!</strong> <%= request.getAttribute("error") %>
        </div>
    <% } %>
    
    <!-- Hiển thị thông báo thành công -->
    <% if (request.getAttribute("success") != null) { %>
        <div class="alert alert-success">
            <strong>✅ Thành công!</strong> <%= request.getAttribute("success") %>
        </div>
        
        <!-- Script để tự động chuyển trang -->
        <% if (request.getAttribute("redirectScript") != null) { %>
            <%= request.getAttribute("redirectScript") %>
        <% } %>
        
    <% } %>
    
    <!-- Hiển thị thông báo thông tin -->
    <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-info">
            <strong>ℹ️ Thông báo:</strong> <%= request.getAttribute("message") %>
        </div>
    <% } %>

    <!-- Thông tin về email đã gửi -->
    <div class="otp-info">
        <p>📧 Mã OTP đã được gửi đến email:</p>
        <div class="email-info">
            <% 
                HttpSession userSession = request.getSession();
                String email = (String) userSession.getAttribute("registerEmail");
                if (email != null) {
                    // Ẩn một phần email để bảo mật
                    String maskedEmail = email.replaceAll("(.{2})(.*)(@.*)", "$1***$3");
                    out.print(maskedEmail);
                } else {
                    out.print("Email không xác định");
                }
            %>
        </div>
        <p style="margin-top: 10px; font-size: 14px; color: #666;">
            ⏰ Mã có hiệu lực trong <span class="timer" id="countdown">5:00</span> phút
        </p>
        <p style="font-size: 12px; color: #888;">
            💡 Kiểm tra cả thư mục Spam nếu không thấy email
        </p>
    </div>

    <!-- Form nhập OTP -->
    <form action="${pageContext.request.contextPath}/register;jsessionid=<%= session.getId() %>" method="post" id="otpForm">
        <input type="hidden" name="action" value="verifyOTP">
        
        <div class="form-group">
            <label for="otp">Nhập mã OTP (6 ký tự):</label>
            <input type="text" id="otp" name="otp" required maxlength="6" 
                   class="otp-input" placeholder="000000" 
                   pattern="[0-9]{6}" title="Vui lòng nhập 6 số">
        </div>
        
        <div style="text-align:center;margin-top:30px;">
            <button type="submit" class="btn btn-primary">✅ Xác thực và hoàn tất đăng ký</button>
            <button type="button" class="btn btn-secondary" onclick="location.href='register'">◀️ Quay lại</button>
        </div>
    </form>

    <!-- Gửi lại OTP -->
    <div class="resend-section">
        <p style="margin-bottom: 15px;">Không nhận được mã OTP?</p>
        <button type="button" class="btn-link" id="resendBtn" onclick="resendOTP()">
            🔄 Gửi lại mã OTP
        </button>
        <p style="font-size: 12px; color: #888; margin-top: 10px;">
            * Bạn có thể gửi lại sau <span id="resendTimer">60</span> giây
        </p>
    </div>
</div>

<script type="text/javascript">
    // Tự động focus vào input OTP
    document.addEventListener('DOMContentLoaded', function() {
        var otpInput = document.getElementById('otp');
        if (otpInput) {
            otpInput.focus();
        }
        
        // Khởi động countdown timer
        startCountdown();
        startResendTimer();
    });

    // Countdown timer cho OTP (5 phút)
    function startCountdown() {
        var totalSeconds = 5 * 60; // 5 phút
        var countdownElement = document.getElementById('countdown');
        
        var interval = setInterval(function() {
            var minutes = Math.floor(totalSeconds / 60);
            var seconds = totalSeconds % 60;
            
            countdownElement.textContent = minutes + ':' + (seconds < 10 ? '0' : '') + seconds;
            
            if (totalSeconds <= 60) {
                countdownElement.style.color = '#dc3545'; // Đỏ khi còn < 1 phút
            }
            
            if (totalSeconds <= 0) {
                clearInterval(interval);
                countdownElement.textContent = 'Hết hạn';
                countdownElement.style.color = '#dc3545';
                
                // Thông báo hết hạn
                alert('⚠️ Mã OTP đã hết hạn! Vui lòng gửi lại mã mới.');
            }
            
            totalSeconds--;
        }, 1000);
    }

    // Timer cho nút gửi lại (60 giây)
    function startResendTimer() {
        var resendSeconds = 60;
        var resendBtn = document.getElementById('resendBtn');
        var resendTimer = document.getElementById('resendTimer');
        
        resendBtn.disabled = true;
        resendBtn.style.color = '#999';
        
        var interval = setInterval(function() {
            resendTimer.textContent = resendSeconds;
            
            if (resendSeconds <= 0) {
                clearInterval(interval);
                resendBtn.disabled = false;
                resendBtn.style.color = '#007bff';
                resendTimer.textContent = '0';
            }
            
            resendSeconds--;
        }, 1000);
    }

    // Gửi lại OTP
    function resendOTP() {
        if (confirm('🔄 Bạn có chắc muốn gửi lại mã OTP không?')) {
            // Tạo form để gửi lại OTP
            var form = document.createElement('form');
            form.method = 'post';
            form.action = 'register';
            
            var actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'resendOTP';
            form.appendChild(actionInput);
            
            document.body.appendChild(form);
            form.submit();
        }
    }

    // Chỉ cho phép nhập số
    document.getElementById('otp').addEventListener('input', function(e) {
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    // Auto submit khi nhập đủ 6 ký tự
    document.getElementById('otp').addEventListener('input', function(e) {
        if (this.value.length === 6) {
            setTimeout(function() {
                document.getElementById('otpForm').submit();
            }, 500);
        }
    });
</script>

</body>
</html>