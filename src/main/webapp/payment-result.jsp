<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Kết quả thanh toán</title>
    <link rel="stylesheet" href="css/payment.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <header class="site-header">
        <div class="top-banner">
            <div class="brand-name">Bus Ticket</div>
            <div class="slogan">Kết quả giao dịch</div>
        </div>
        <nav class="navbar">
            <div class="logo"><img src="images/logo.png" alt="Logo"></div>
            <ul class="nav-links">
                <li><a href="index.jsp">Trang chủ</a></li>
                <li><a href="schedule.jsp">Lịch trình</a></li>
                <li><a href="contact.jsp">Liên hệ</a></li>
            </ul>
            <div class="auth-buttons">
                 <span class="welcome">Xin chào, <c:out value="${user.fullName}" default="Guest"/>!</span>
                 <a href="#" class="btn-logout">Đăng xuất</a>
            </div>
        </nav>
    </header>

    <main class="payment-bg">
        <div class="result-container">
            <div class="icon ${status}">
                <c:choose>
                    <c:when test="${status == 'success'}">
                        <i class="fas fa-check-circle"></i>
                    </c:when>
                    <c:when test="${status == 'failed'}">
                        <i class="fas fa-times-circle"></i>
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-exclamation-triangle"></i>
                    </c:otherwise>
                </c:choose>
            </div>
            <h1>
                <c:choose>
                    <c:when test="${status == 'success'}">Giao dịch thành công</c:when>
                    <c:when test="${status == 'failed'}">Giao dịch thất bại</c:when>
                    <c:otherwise>Có lỗi xảy ra</c:otherwise>
                </c:choose>
            </h1>
            <c:if test="${status == 'success'}">
                <div class="details">
                    <div class="detail-row">
                        <span class="detail-label">Mã giao dịch (VNPay):</span>
                        <span class="detail-value"><c:out value="${vnpayData['vnp_TransactionNo']}" default="N/A"/></span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Mã đơn hàng:</span>
                        <span class="detail-value"><c:out value="${payment.order.id}"/></span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Số tiền:</span>
                        <span class="detail-value">
                            <%-- Số tiền VNPay trả về là * 100, nên cần chia lại và định dạng --%>
                            <fmt:formatNumber value="${vnpayData['vnp_Amount'] / 100}" type="number" maxFractionDigits="0"/> VNĐ
                        </span>
                    </div>
                     <div class="detail-row">
                        <span class="detail-label">Ngân hàng:</span>
                        <span class="detail-value"><c:out value="${vnpayData['vnp_BankCode']}"/></span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Thời gian thanh toán:</span>
                        <span class="detail-value">
                            <fmt:parseDate value="${vnpayData['vnp_PayDate']}" pattern="yyyyMMddHHmmss" var="paidDate" type="both" />
                            <fmt:formatDate value="${paidDate}" pattern="HH:mm:ss dd/MM/yyyy" />
                        </span>
                    </div>
                </div>
            </c:if>

            <div class="btn-group">
                <a href="index.jsp" class="btn btn-primary">Về trang chủ</a>
            </div>
        </div>
    </main>
</body>
</html>