<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 1. Import lớp helper với package chính xác --%>
<%@ page import="com.busbooking.util.AssetHasher" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <%-- 2. Gọi phương thức getHash --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css?v=<%= AssetHasher.getHash(application, "/styles/login.css") %>">
</head>
<body>
    <div class="container">
        <form action="${pageContext.request.contextPath}/login" method="post" class="card">

            <a href="#" class="login">Login</a>

            <c:if test="${not empty errorMessage}">
                <p style="color:red; font-size: 14px; text-align: center;">${errorMessage}</p>
            </c:if>

            <div class="inputBox">
                <input type="email" name="email" required>
                <span>Email</span>
            </div>

            <div class="inputBox">
                <input type="password" name="password" required>
                <span>Password</span>
            </div>

            <button type="submit" class="enter">Đăng nhập</button>

        </form>
    </div>
</body>
</html>