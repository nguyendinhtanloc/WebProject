<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Xác thực OTP - BusBooking Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css" />
</head>
<body>
    <div class="container">
        <form action="${pageContext.request.contextPath}/verify-otp" method="post" class="card">
            <a href="#" class="login">Xác thực OTP</a>
            
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
                <button type="submit" class="resend-btn" 
                        formaction="${pageContext.request.contextPath}/resend-otp" 
                        formnovalidate>
                    Gửi lại OTP
                </button>
            </div>
        </form>
    </div>
</body>
</html>
