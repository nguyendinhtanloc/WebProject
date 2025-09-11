<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" href="styles/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <title>Đăng nhập</title>
</head>
<body>
<div id="wrapper">
    <!-- action phải trỏ tới servlet /login và method POST -->
    <form action="login" method="post" id="form-login">
        <h1 class="form-heading">Đăng Nhập</h1>

        <div class="form-group">
            <i class="fa fa-user"></i>
            <!-- thêm name="email" -->
            <input type="text" class="form-input" name="email" placeholder="Email">
        </div>

        <div class="form-group">
            <i class="fa fa-key"></i>
            <!-- thêm name="password" -->
            <input type="password" class="form-input" name="password" placeholder="Mật khẩu">
        </div>

        <input type="submit" value="Đăng nhập" class="form-submit">

        <!-- hiện thông báo lỗi nếu có -->
        <c:if test="${param.error == '1'}">
            <p style="color:red; text-align:center;">Sai email hoặc mật khẩu!</p>
        </c:if>
    </form>
</div>
</body>
</html>
