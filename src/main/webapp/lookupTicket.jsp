<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tra cứu vé - BusBooking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lookupTicket.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/includes/header.jsp" />
    <div class="content-wrapper">
        <h1>Tra cứu vé</h1>
        <form action="${pageContext.request.contextPath}/lookup" method="GET" style="display: flex; gap: 15px; align-items: flex-end;">
            <div style="flex-grow: 1;">
                <label for="email">Nhập email:</label>
                <input type="email" id="email" name="email" value="${param.email}" required class="form-control" style="width: 100%;">
            </div>
            <div style="flex-grow: 1;">
                <label for="code">Mã vé:</label>
                <input type="text" id="code" name="ticketCode" value="${param.ticketCode}" required class="form-control" style="width: 100%;">
            </div>
            <button type="submit" class="btn btn-primary" style="height: 40px; margin-bottom: 1px;">
                <i class="fas fa-search"></i> Tra cứu
            </button>
        </form>
        <c:if test="${not empty error}">
            <div class="error-message">
                <strong>Lỗi tra cứu:</strong> ${error}
            </div>
        </c:if>
        <c:if test="${not empty ticketDto}">
            <c:set var="dto" value="${ticketDto}" />
            <div class="ticket-result" id="ticketSection">
                <h2>Thông tin vé</h2>
                <p><strong>Mã vé:</strong> ${dto.ticketId}</p>
                <p><strong>Ghế:</strong> ${dto.seatId}</p>
                <p><strong>Trạng thái:</strong> ${dto.status}</p>
                <p><strong>Giá vé:</strong> <fmt:formatNumber value="${dto.price}" pattern="#,##0" /> VND</p>
                <h3>Thông tin chuyến đi</h3>
                <p><strong>Tuyến:</strong> ${dto.departureCity} → ${dto.arrivalCity}</p>
                <p><strong>Ngày đi:</strong> <fmt:formatDate value="${dto.departureDateAsDate}" pattern="dd/MM/yyyy" /></p>
                <p><strong>Giờ đi:</strong> ${dto.departureTime}</p>
                <p><strong>Khoảng cách:</strong> ${dto.distanceKm} km</p>
                <p><strong>Đến lúc:</strong> <fmt:formatDate value="${dto.arrivalDateTimeAsDate}" pattern="dd/MM/yyyy HH:mm" /></p>
                <p><strong>Giá gốc chuyến đi:</strong> <fmt:formatNumber value="${dto.tripPrice}" pattern="#,##0" /> VND</p>
                <div class="print-button">
<button onclick="printTicket()">In vé</button>
                </div>
            </div>
        </c:if>
    </div>
    <jsp:include page="/includes/footer.jsp" />
    <script>
        function printTicket() {
            const printContents = document.getElementById("ticketSection").innerHTML;
            const originalContents = document.body.innerHTML;
            document.body.innerHTML = printContents;
            window.print();
            document.body.innerHTML = originalContents;
            location.reload();
        }
    </script>
</body>
</html>