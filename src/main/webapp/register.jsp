<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký - BusBooking</title>
    <link rel="stylesheet" href="css/style.css">
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
        .container {
            max-width: 500px;
            margin: 60px auto;
            padding: 30px;
            border: 1px solid #ddd;
            border-radius: 8px;
        }
        h2, h3 {
            text-align: center;
        }
        p {
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Đăng ký tài khoản</h2>

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
    <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm">
        <input type="hidden" name="action" value="sendOTP">
        
        <div class="form-group">
            <label for="name">Họ và tên:</label>
            <input type="text" id="name" name="name" required 
                   value='<%= request.getParameter("name") != null ? request.getParameter("name") : "" %>'>
        </div>
        
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required 
                   value='<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>'>
        </div>
        
        <div class="form-group">
            <label for="phone">Số điện thoại:</label>
            <input type="tel" id="phone" name="phone" required 
                   value='<%= request.getParameter("phone") != null ? request.getParameter("phone") : "" %>'>
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
            <button type="submit" class="btn btn-primary">🚀 Gửi mã xác thực</button>
        </div>
        
        <div style="text-align:center;margin-top:20px;">
            <p>Đã có tài khoản? <a href="login.jsp">Đăng nhập ngay</a></p>
        </div>
    </form>
</div>


</body>
</html>