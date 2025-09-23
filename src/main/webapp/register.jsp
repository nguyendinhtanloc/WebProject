<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký</title>
    <link rel="stylesheet" href="styles/register.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div id="register-wrapper">
    <h1>Đăng ký tài khoản</h1>
    <!-- gửi dữ liệu lên servlet RegisterServlet -->
    <form id="register-form" action="RegisterServlet" method="post">
        <div class="form-group">
            <i class="fa fa-user"></i>
            <input type="text" name="name" placeholder="Họ tên" required>
        </div>
        <div class="form-group">
            <i class="fa fa-envelope"></i>
            <input type="email" name="email" placeholder="Email" required>
        </div>
        <div class="form-group">
            <i class="fa fa-key"></i>
            <input type="password" name="password" placeholder="Mật khẩu" required>
        </div>
        <div class="form-group">
            <i class="fa fa-phone"></i>
            <input type="text" name="phone" placeholder="Số điện thoại" required>
        </div>
        <button type="submit">Đăng ký</button>
    </form>
</div>
</body>
</html>
