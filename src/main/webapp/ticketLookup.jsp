<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>BusGo - Tra cứu vé</title>
    <link rel="stylesheet" href="styles/styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>

<!-- Header -->
<header class="header">
    <div class="nav-container">
        <div class="logo">
            <img src="styles/logo.png" alt="BusGo Logo">
        </div>
        <nav>
            <ul class="nav-menu">
                <li><a href="index.jsp"><i class="fas fa-home"></i> Trang chủ</a></li>
                <li><a href="#"><i class="fas fa-route"></i> Chuyến xe</a></li>
                <li><a href="ticket-lookup.jsp"><i class="fas fa-search"></i> Tra cứu vé</a></li>
                <li><a href="#"><i class="fas fa-phone"></i> Liên hệ</a></li>
            </ul>
        </nav>
        <div class="user-actions">
            <a href="#" class="btn btn-outline"><i class="fas fa-sign-in-alt"></i> Đăng nhập</a>
            <a href="#" class="btn btn-primary"><i class="fas fa-user-plus"></i> Đăng ký</a>
        </div>
    </div>
</header>

<!-- Main Container -->
<div class="container">
    <!-- Tiêu đề -->
    <section class="search-section">
        <h2 class="search-title">Tra cứu vé</h2>
        <p class="search-subtitle">Nhập mã vé hoặc thông tin cá nhân để tra cứu thông tin đặt vé</p>

        <!-- Form tra cứu -->
        <form action="ticket-lookup" method="get">
            <div class="form-grid">
                <div class="form-group full-width">
                    <label for="ticketCode">Mã vé:</label>
                    <i class="form-icon fas fa-ticket-alt"></i>
                    <input type="text" id="ticketCode" name="ticketCode"
                           placeholder="Nhập mã vé (nếu có)" value="${param.ticketCode}">
                </div>

                <div class="form-divider">
                    <span>Hoặc</span>
                </div>

                <div class="form-group">
                    <label for="fullName">Họ tên:</label>
                    <i class="form-icon fas fa-user"></i>
                    <input type="text" id="fullName" name="fullName"
                           placeholder="Nhập họ tên" value="${param.fullName}">
                </div>

                <div class="form-group">
                    <label for="phoneNumber">Số điện thoại:</label>
                    <i class="form-icon fas fa-phone"></i>
                    <input type="tel" id="phoneNumber" name="phoneNumber"
                           placeholder="Nhập SĐT đã đặt vé" value="${param.phoneNumber}">
                </div>

                <button type="submit" class="search-button">
                    <i class="fas fa-search"></i> Tra cứu
                </button>
            </div>
        </form>
    </section>

    <!-- Hiển thị kết quả -->
    <c:if test="${not empty ticket}">
        <section class="results-section">
            <h3>Thông tin vé</h3>
            <table class="results-table">
                <tr>
                    <th><i class="fas fa-ticket-alt"></i> Mã vé</th>
                    <td>${ticket.code}</td>
                </tr>
                <tr>
                    <th><i class="fas fa-user"></i> Họ tên</th>
                    <td>${ticket.fullName}</td>
                </tr>
                <tr>
                    <th><i class="fas fa-phone"></i> SĐT</th>
                    <td>${ticket.phoneNumber}</td>
                </tr>
                <tr>
                    <th><i class="fas fa-route"></i> Tuyến</th>
                    <td>${ticket.fromPlace} → ${ticket.toPlace}</td>
                </tr>
                <tr>
                    <th><i class="fas fa-calendar-alt"></i> Ngày</th>
                    <td>${ticket.departureDate}</td>
                </tr>
                <tr>
                    <th><i class="fas fa-clock"></i> Giờ đi</th>
                    <td>${ticket.departureTime}</td>
                </tr>
                <tr>
                    <th><i class="fas fa-chair"></i> Ghế</th>
                    <td>${ticket.seatNumber}</td>
                </tr>
                <tr>
                    <th><i class="fas fa-money-bill"></i> Giá vé</th>
                    <td><i class="fas fa-dong-sign"></i> ${ticket.price}</td>
                </tr>
            </table>
        </section>
    </c:if>

    <!-- Nếu không tìm thấy -->
    <c:if test="${empty ticket && param.ticketCode != null}">
        <div class="empty-state">
            <i class="fas fa-exclamation-circle empty-icon"></i>
            <h3>Không tìm thấy vé</h3>
            <p>Vui lòng kiểm tra lại thông tin và thử lại.</p>
        </div>
    </c:if>
</div>

<!-- Script loading -->
<script>
    document.querySelector('form').addEventListener('submit', function () {
        const btn = document.querySelector('.search-button');
        btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang tra cứu...';
        btn.disabled = true;
    });
</script>
</body>
</html>
