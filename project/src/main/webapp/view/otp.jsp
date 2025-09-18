<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Xác thực</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css?v=2">
        </head>

        <body>
            <div class="container">
                <form action="${pageContext.request.contextPath}/verify-otp" method="post" class="card">

                    <a href="#" class="login">OTP</a>

                    <p class="otp-info">
                        Mã OTP đã được gửi đến email của bạn.<br>
                        Vui lòng nhập mã bên dưới.
                    </p>

                    <c:if test="${not empty errorMessage}">
                        <p style="color:red; font-size: 14px; text-align: center;">${errorMessage}</p>
                    </c:if>
                    <c:if test="${not empty successMessage}">
                        <p style="color:green; font-size: 14px; text-align: center;">${successMessage}</p>
                    </c:if>

                    <div class="inputBox">
                        <input type="text" id="otp" name="otp" required maxlength="8">
                        <span>OTP Code</span>
                    </div>

                    <button type="submit" class="enter">Xác thực</button>

                    <button type="submit" class="resend-btn" id="resendBtn"
                        formaction="${pageContext.request.contextPath}/resend-otp">
                        Gửi lại OTP (<span id="countdown">60</span>s)
                    </button>

                </form>
            </div>
        </body>

        </html>