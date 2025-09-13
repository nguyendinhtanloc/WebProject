<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Xác thực OTP</title>
</head>
<body>
    <h1>Xác thực OTP</h1>
    <p>Một mã OTP đã được gửi đến email của bạn. Vui lòng kiểm tra và nhập vào ô bên dưới.</p>

    <c:if test="${not empty errorMessage}">
        <p style="color:red;">${errorMessage}</p>
    </c:if>
    <c:if test="${not empty successMessage}">
        <p style="color:green;">${successMessage}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/verify-otp" method="post">
        <label for="otp">Mã OTP:</label><br>
        <input type="text" id="otp" name="otp" required maxlength="8"><br><br> <%-- Tăng maxlength lên 8 --%>
        
        <input type="submit" value="Xác thực">
    </form>
    
    <br>
    
    <form action="${pageContext.request.contextPath}/resend-otp" method="get">
         <button type="submit" id="resendBtn">Gửi lại OTP (<span id="countdown">60</span>s)</button>
    </form>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const resendBtn = document.getElementById('resendBtn');
            const countdownSpan = document.getElementById('countdown');
            let countdown = 60;

            function startCountdown() {
                resendBtn.disabled = true;
                countdownSpan.textContent = countdown;
                
                const interval = setInterval(() => {
                    countdown--;
                    countdownSpan.textContent = countdown;
                    if (countdown <= 0) {
                        clearInterval(interval);
                        resendBtn.disabled = false;
                        resendBtn.textContent = 'Gửi lại OTP';
                    }
                }, 1000);
            }

            startCountdown();
        });
    </script>
</body>
</html>