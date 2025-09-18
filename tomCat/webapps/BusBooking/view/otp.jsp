<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 1. Import lớp helper với package chính xác --%>
<%@ page import="com.busbooking.util.AssetHasher" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Xác thực OTP</title>
     <%-- 2. Gọi phương thức getHash --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css?v=<%= AssetHasher.getHash(application, "/styles/login.css") %>">
</head>
<body>
    <%-- Phần body của trang OTP của bạn sẽ ở đây --%>
    <%-- Tôi sẽ lấy lại nội dung từ lần trước cho bạn tham khảo --%>
    <div class="container">
        <form action="${pageContext.request.contextPath}/verify-otp" method="post" class="card">
            <a href="#" class="login">OTP</a>
            <p class="otp-info">
                Mã OTP đã được gửi đến email của bạn. Vui lòng nhập mã bên dưới.
            </p>
            <c:if test="${not empty errorMessage}">
                <p style="color:red; font-size: 14px; text-align: center;">${errorMessage}</p>
            </c:if>
            <div class="inputBox">
                <input type="text" name="otp" required maxlength="8">
                <span>OTP Code</span>
            </div>
            <div class="button-group">
                <button type="submit" class="enter">Xác thực</button>
                <button type="submit" class="resend-btn" formaction="${pageContext.request.contextPath}/resend-otp" formnovalidate>Gửi lại OTP</button>
            </div>
        </form>
    </div>
</body>
</html>