<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Lấy mã lỗi từ URL parameter --%>
<c:set var="errorCode" value="${param.code}"/>

<!DOCTYPE html>
<html>
<head>
    <title>Lỗi Thanh Toán</title>
    <link rel="stylesheet" href="css/payment.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <header class="site-header">
        <div class="top-banner">
            <div class="brand-name">Bus Ticket</div>
            <div class="slogan">Đã có lỗi xảy ra</div>
        </div>
        <nav class="navbar">
            <div class="logo"><img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo"></div>
            <ul class="nav-links">
                <li><a href="#">Trang chủ</a></li>
                <li><a href="#">Lịch trình</a></li>
            </ul>
        </nav>
    </header>

    <main class="payment-bg">
        <div class="result-container">
            <div class="icon error">
                <i class="fas fa-exclamation-triangle"></i>
            </div>

            <h1>Thanh toán không thành công</h1>

            <p class="message">
                <c:choose>
                    <c:when test="${errorCode == '01'}">
                        Đã xảy ra lỗi: Vui lòng cung cấp đầy đủ thông tin đơn hàng và số tiền.
                    </c:when>
                    <c:when test="${errorCode == '02'}">
                        Đã xảy ra lỗi: Định dạng số tiền không hợp lệ.
                    </c:when>
                    <c:when test="${errorCode == '99'}">
                        Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau hoặc liên hệ bộ phận hỗ trợ.
                    </c:when>
                    <c:otherwise>
                        Đã có lỗi không xác định xảy ra trong quá trình thanh toán.
                    </c:otherwise>
                </c:choose>
            </p>

            <div class="details">
                 <div class="detail-row">
                    <span class="detail-label">Mã lỗi:</span>
                    <span class="detail-value">PAYMENT_ERR_<c:out value="${errorCode}" default="UNKNOWN"/></span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Thời gian:</span>
                    <span class="detail-value"><%= new java.util.Date() %></span>
                </div>
            </div>

            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/payment" class="btn btn-primary">Thử lại</a>
                <a href="#" class="btn btn-secondary">Về trang chủ</a>
            </div>
        </div>
    </main>
</body>
</html>