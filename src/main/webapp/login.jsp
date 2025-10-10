<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập - BusBooking</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container" style="max-width:400px;margin:60px auto;">
    <h2>Đăng nhập</h2>

    <!-- Hiển thị thông báo lỗi hoặc thành công -->
    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
        <p style="color:red;"><%= error %></p>
    <% } %>

    <form action="login" method="post" id="loginForm">
        <div>
            <label>Email:</label><br>
            <input type="email" name="email" required>
        </div>
        <div style="margin-top:10px;">
            <label>Mật khẩu:</label><br>
            <input type="password" name="password" required>
        </div>
        <div style="margin-top:20px;">
            <button type="submit" class="btn-primary">Đăng nhập</button>
        </div>
    </form>

    <p style="margin-top:20px;">
        Chưa có tài khoản? <a href="register.jsp">Đăng ký ngay</a>
    </p>
</div>
</body>
</html>