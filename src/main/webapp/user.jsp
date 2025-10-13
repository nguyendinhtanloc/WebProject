
<%@ page contentType="text/html; charset=UTF-8" language="java" %><%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="includes/header.jsp" /><!DOCTYPE html>

<html lang="vi">

<div class="container" style="max-width: 800px; margin: 40px auto; background: #fff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); padding: 32px 28px;"><head>
    <meta charset="UTF-8">
    <title>Thông tin người dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
</head>
<body>
    <jsp:include page="/includes/header.jsp" />
    <div class="user-container">
        <div class="user-tabs">
            <button id="info-tab" class="user-tab" onclick="showTab('info')">Thông tin cá nhân</button>
            <button id="tickets-tab" class="user-tab" onclick="showTab('tickets')">Lịch sử vé</button>
            <button id="contact-tab" class="user-tab" onclick="showTab('contact')">Liên hệ/Báo cáo</button>
        </div>
        <div id="info-section" class="user-section active">
            <h2>Thông tin cá nhân</h2>
            <!-- Form hiển thị và chỉnh sửa thông tin user -->
            <form method="post" action="${pageContext.request.contextPath}/user">
                <input type="hidden" name="action" value="updateInfo">
                <label>Email:</label><input type="email" value="${user.email}" readonly><br>
                <label>Họ tên:</label><input type="text" name="fullName" value="${user.name}"><br>
                <label>Số điện thoại:</label><input type="text" name="phone" value="${user.phone}"><br>
                <label>Ngày sinh:</label><input type="date" name="birthDate" value="${user.birthDate}"><br>
                <label>Địa chỉ:</label><input type="text" name="address" value="${user.address}"><br>
                <label>Giới tính:</label>
                <select name="gender">
                    <option value="" ${empty user.gender ? 'selected' : ''}>Chọn</option>
                    <option value="male" ${user.gender eq 'male' ? 'selected' : ''}>Nam</option>
                    <option value="female" ${user.gender eq 'female' ? 'selected' : ''}>Nữ</option>
                    <option value="other" ${user.gender eq 'other' ? 'selected' : ''}>Khác</option>
                </select><br>
                <label>Mật khẩu mới:</label><input type="password" name="password" placeholder="Nhập mật khẩu mới nếu muốn đổi"><br>
                <button type="submit">Cập nhật</button>
            </form>
        </div>
        <div id="tickets-section" class="user-section">
            <h2>Lịch sử đặt vé</h2>
            <!-- Hiển thị danh sách vé, nút hoàn vé -->
<c:forEach var="ticket" items="${userTickets}">
                <div class="ticket-item">
                    <span>Mã vé: ${ticket.ticketId}</span> - <span>Chuyến: ${ticket.departureCity} → ${ticket.arrivalCity}</span> - <span>Ngày đi: ${ticket.departureDate}</span>
                    <c:if test="${ticket.status eq 'booked'}">
                        <button>Hoàn vé</button>
                    </c:if>
                </div>
            </c:forEach>
        </div>
        <div id="contact-section" class="user-section">
            <h2>Liên hệ & Báo cáo</h2>
            <form>
                <label>Nội dung:</label><br>
                <textarea rows="4" style="width:100%"></textarea><br>
                <button type="submit">Gửi phản hồi</button>
            </form>
        </div>
    </div>
    <jsp:include page="/includes/footer.jsp" />
    <script>
        function showTab(tab) {
            document.querySelectorAll('.user-tab').forEach(e => e.classList.remove('active'));
            document.querySelectorAll('.user-section').forEach(e => e.classList.remove('active'));
            document.getElementById(tab+'-tab').classList.add('active');
            document.getElementById(tab+'-section').classList.add('active');
        }
        window.onload = function() { showTab('info'); };
    </script>
</body>
</html>