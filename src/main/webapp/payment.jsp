<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thanh toán vé xe</title>
    <link rel="stylesheet" href="css/payment.css">
</head>
<body>
    <!-- Banner -->
    <header class="site-header">
        <div class="top-banner">
            <div class="brand-name">Bus Ticket</div>
            <div class="slogan">Dễ dàng – Nhanh chóng – An toàn</div>
        </div>
        <nav class="navbar">
            <div class="logo"><img src="images/logo.png" alt="Logo"></div>
            <ul class="nav-links">
                <li><a href="index.jsp">Trang chủ</a></li>
                <li><a href="schedule.jsp">Lịch trình</a></li>
                <li><a href="contact.jsp">Liên hệ</a></li>
            </ul>
            <%-- DÁN ĐOẠN CODE NÀY VÀO THAY THẾ --%>
            <div class="nav-auth">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <div class="user-menu">
                            <span class="user-name">
                                <i class="fas fa-user"></i>
                                ${sessionScope.user.email}
                            </span>
                            <%-- Thêm nút đăng xuất ở đây nếu cần --%>
                            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Đăng xuất</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline">
                            <i class="fas fa-sign-in-alt"></i>
                            Đăng nhập
                        </a>
                        <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-primary">
                            <i class="fas fa-user-plus"></i>
                            Đăng ký
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </nav>
    </header>

    <!-- Nội dung chính -->
    <main class="payment-bg">
        <div class="payment-container">
            <h1>🎫 Thanh toán vé xe</h1>

            <!-- Thông tin người dùng -->
            <p class="user-info">Người thanh toán: <strong><c:out value="${sessionScope.user.name}"/></strong></p>

            <form action="${pageContext.request.contextPath}/payment" method="GET">

                <%-- Luôn gửi kèm orderId, nhưng ẩn đi để người dùng không thấy --%>
                <input type="hidden" name="orderId" value="<c:out value='${order.id}'/>">

                <div class="form-group">
                    <label for="order_id_display">Mã vé:</label>
                    <input type="text" id="order_id_display" value="<c:out value='${order.id}'/>" disabled>
                </div>

                <div class="form-group">
                    <label for="amount_display">Giá vé (VNĐ):</label>
                    <input type="number" id="amount_display" value="<c:out value='${order.amount}'/>" disabled>
                </div>

                <div class="voucher-section">
                    <%-- Giữ lại giá trị voucher sau khi tải lại trang --%>
                    <input type="text" name="voucher_code" value="<c:out value='${param.voucher_code}'/>" placeholder="Nhập mã giảm giá (nếu có)">

                    <%-- Nút này sẽ submit form bằng GET về chính trang này (/payment) --%>
                    <button type="submit" class="btn-apply">Áp dụng</button>
                </div>

                <%-- Hiển thị thông báo áp dụng voucher --%>
                <p style="color: green; text-align: center; margin-bottom: 15px;">
                    <c:out value="${voucherMessage}"/>
                </p>

                <div class="amount-display">
                    <div class="amount-row">
                        <span>Giá vé gốc:</span>
                        <span><c:out value="${order.amount}"/> VNĐ</span>
                    </div>
                    <div class="amount-row">
                        <span>Giảm giá:</span>
                        <%-- Hiển thị số tiền được giảm từ Controller --%>
                        <span>- <c:out value="${discountAmount}" default="0"/> VNĐ</span>
                    </div>
                    <div class="amount-row total">
                        <span>Tổng thanh toán:</span>
                         <%-- Hiển thị tổng tiền cuối cùng từ Controller --%>
                        <span><c:out value="${finalAmount}" default="${order.amount}"/> VNĐ</span>
                    </div>
                </div>

                <%-- Nút này sẽ submit form bằng POST đến /payment/create --%>
                <button type="submit" class="btn-pay"
                        formaction="${pageContext.request.contextPath}/payment/create"
                        formmethod="POST">
                        Thanh toán với VNPay
                </button>

                <%-- Các trường cần gửi đi cho /payment/create nhưng ẩn đi --%>
                <input type="hidden" name="order_id" value="<c:out value='${order.id}'/>">
                <input type="hidden" name="amount" value="<c:out value='${order.amount}'/>">

            </form>
        </div>
    </main>
</body>
</html>
