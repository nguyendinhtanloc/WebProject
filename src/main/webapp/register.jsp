<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký - BusBooking</title>
    <link rel="stylesheet" href="css/register.css">
    <style>
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
            margin-left: 10px;
        }
        .alert {
            padding: 12px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        .alert-danger {
            background-color: #f8d7da;
            border-color: #f5c6cb;
            color: #721c24;
        }
        .alert-success {
            background-color: #d4edda;
            border-color: #c3e6cb;
            color: #155724;
        }
        .alert-info {
            background-color: #cce7ff;
            border-color: #b3d7ff;
            color: #0c5460;
        }
        .otp-input {
            text-align: center;
            font-size: 20px;
            letter-spacing: 3px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="container" style="max-width:500px;margin:60px auto;padding:30px;border:1px solid #ddd;border-radius:30px;">
    <h2 style="text-align:center;margin-bottom:30px;">Đăng ký tài khoản</h2>

    <!-- Hiển thị thông báo lỗi -->
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <!-- Hiển thị thông báo thành công -->
    <% if (request.getAttribute("success") != null) { %>
        <div class="alert alert-success">
            <%= request.getAttribute("success") %>
        </div>
    <% } %>

    <!-- Hiển thị thông báo thông tin -->
    <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-info">
            <%= request.getAttribute("message") %>
        </div>
    <% } %>

    <!-- Form đăng ký chính -->
    <% if (request.getAttribute("showOTPForm") == null) { %>
        <form action="register" method="post" id="registerForm">
            <input type="hidden" name="action" value="sendOTP">

            <div class="form-group">
                <label for="name">Họ và tên:</label>
<input type="text" id="name" name="name" required
                       value="<%= request.getParameter("name") != null ? request.getParameter("name") : "" %>">
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required
                       value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>">
            </div>

            <div class="form-group">
                <label for="phone">Số điện thoại:</label>
                <input type="tel" id="phone" name="phone" required
                       value="<%= request.getParameter("phone") != null ? request.getParameter("phone") : "" %>">
            </div>

            <div class="form-group">
                <label for="password">Mật khẩu:</label>
                <input type="password" id="password" name="password" required>
            </div>

            <div class="form-group">
                <label for="confirmPassword">Xác nhận mật khẩu:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>

            <div style="text-align:center;margin-top:30px;">
                <button type="submit" class="btn btn-primary">Gửi mã xác thực</button>
            </div>
        </form>
    <% } else { %>
        <!-- Form nhập OTP -->
        <div style="text-align:center;margin-bottom:20px;">
            <h3>Nhập mã xác thực OTP</h3>
            <p>Mã OTP đã được gửi đến email của bạn. Mã có hiệu lực trong 5 phút.</p>
        </div>

        <form action="register" method="post" id="otpForm">
            <input type="hidden" name="action" value="verifyOTP">

            <div class="form-group">
                <label for="otp">Mã OTP (6 ký tự):</label>
                <input type="text" id="otp" name="otp" required maxlength="6"
                       class="otp-input" placeholder="Nhập mã OTP">
            </div>

            <div style="text-align:center;margin-top:30px;">
                <button type="submit" class="btn btn-primary">Xác thực</button>
                <button type="button" class="btn btn-secondary" onclick="location.href='register'">Quay lại</button>
            </div>
        </form>

        <!-- Gửi lại OTP -->
        <div style="text-align:center;margin-top:20px;">
            <p>Không nhận được mã?
                <a href="javascript:void(0)" onclick="resendOTP()">Gửi lại mã OTP</a>
            </p>
        </div>
    <% } %>

    <!-- Login link removed since we only have registration functionality -->
</div>

<script>
    function resendOTP() {
        // Tạo form ẩn để gửi lại OTP
        var form = document.createElement('form');
        form.method = 'post';
        form.action = 'register';

        var actionInput = document.createElement('input');
        actionInput.type = 'hidden';
actionInput.name = 'action';
        actionInput.value = 'sendOTP';
        form.appendChild(actionInput);

        // Lấy thông tin từ session (nếu có)
        <%
        HttpSession currentSession = request.getSession(false);
        if (currentSession != null) {
            String sessionName = (String) currentSession.getAttribute("registerName");
            String sessionEmail = (String) currentSession.getAttribute("registerEmail");
            String sessionPhone = (String) currentSession.getAttribute("registerPhone");
            if (sessionName != null && sessionEmail != null && sessionPhone != null) {
        %>
        var nameInput = document.createElement('input');
        nameInput.type = 'hidden';
        nameInput.name = 'name';
        nameInput.value = '<%= sessionName %>';
        form.appendChild(nameInput);

        var emailInput = document.createElement('input');
        emailInput.type = 'hidden';
        emailInput.name = 'email';
        emailInput.value = '<%= sessionEmail %>';
        form.appendChild(emailInput);

        var phoneInput = document.createElement('input');
        phoneInput.type = 'hidden';
        phoneInput.name = 'phone';
        phoneInput.value = '<%= sessionPhone %>';
        form.appendChild(phoneInput);

        var passwordInput = document.createElement('input');
        passwordInput.type = 'hidden';
        passwordInput.name = 'password';
        passwordInput.value = '<%= currentSession.getAttribute("registerPassword") %>';
        form.appendChild(passwordInput);

        var confirmPasswordInput = document.createElement('input');
        confirmPasswordInput.type = 'hidden';
        confirmPasswordInput.name = 'confirmPassword';
        confirmPasswordInput.value = '<%= currentSession.getAttribute("registerPassword") %>';
        form.appendChild(confirmPasswordInput);
        <%
            }
        }
        %>

        document.body.appendChild(form);
        form.submit();
    }

    // Auto focus vào input OTP nếu đang ở form OTP
    <% if (request.getAttribute("showOTPForm") != null) { %>
        document.addEventListener('DOMContentLoaded', function() {
            document.getElementById('otp').focus();
        });
    <% } %>
</script>
</body>
</html>