<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Đăng nhập - BusBooking Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css" />
</head>
<body>
    <div class="container">
        <form action="${pageContext.request.contextPath}/login" method="post" class="card">
            <a href="#" class="login">Đăng nhập</a>
            <div class="inputBox">
                <input type="text" name="email" required>
                <span>Tài khoản</span>
            </div>
            <div class="inputBox">
                <input type="password" name="password" required>
                <span>Mật khẩu</span>
            </div>
            <div class="button-group">
                <button type="submit" class="enter">Đăng nhập</button>
            </div>
        </form>
    </div>
</body>
</html>
