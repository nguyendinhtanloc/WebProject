<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BusGo - Đặt vé</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="styles/styles.css">
    <link rel="stylesheet" href="styles/seat.css">
</head>
<body>
<div class="intro-banner">
    <h1>Biển Xanh Travel</h1>
    <p>Hành trình an toàn - Vươn xa mỗi ngày</p>

    <!-- Icon floating xung quanh -->
    <i class="fas fa-bus floating-icon" style="top: 10%; left: 5%;"></i>
    <i class="fas fa-anchor floating-icon" style="top: 20%; right: 10%;"></i>
    <i class="fas fa-ship floating-icon" style="bottom: 10%; left: 15%;"></i>
    <i class="fas fa-water floating-icon" style="top: 30%; left: 60%;"></i>
    <i class="fas fa-compass floating-icon" style="bottom: 20%; right: 15%;"></i>
    <i class="fas fa-route floating-icon" style="top: 5%; right: 25%;"></i>
    <i class="fas fa-map floating-icon" style="top: 40%; left: 30%;"></i>
    <i class="fas fa-map-marker-alt floating-icon" style="bottom: 30%; right: 5%;"></i>
    <i class="fas fa-plane-departure floating-icon" style="top: 15%; left: 40%;"></i>
    <i class="fas fa-umbrella-beach floating-icon" style="bottom: 5%; left: 50%;"></i>
    <i class="fas fa-traffic-light floating-icon" style="top: 25%; right: 40%;"></i>
    <i class="fas fa-ferry floating-icon" style="bottom: 15%; right: 25%;"></i>
</div>



<!-- Header -->
<header class="header">
    <div class="nav-container">
        <div class="logo">
            <img src="styles/logo.png" alt="BusGo Logo">
        </div>
        <nav>
            <ul class="nav-menu">
                <li><a href="#"><i class="fas fa-home"></i> Trang chủ</a></li>
                <li><a href="#"><i class="fas fa-route"></i> Chuyến xe</a></li>
                <li><a href="#"><i class="fas fa-info-circle"></i> Thông tin</a></li>
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
    <!-- Error Message (if any) -->
    <c:if test="${not empty errorMessage}">
        <div class="error-message">
            <i class="fas fa-exclamation-triangle"></i>
                ${errorMessage}
        </div>
    </c:if>

    <!-- Ticket Booking Feature -->
    <section class="features-booking">
        <h1>Chọn Ghế</h1>
        <%
            int seatCount = (Integer) request.getAttribute("seatCount");
            boolean isSleeper = (Boolean) request.getAttribute("isSleeper");
        %>
        <%
            if (isSleeper) {
                int half = seatCount / 2;
        %>
            <h2>Tầng dưới</h2>
            <div class="view-seatDwn">
                <%
                    for (int i = 1; i <= half; i++) {
                %>
                    <button class="seat"><img src="styles/seat.png" alt="Ghế <%= i %>"><%= i %></button>
                <%
                     }
                %>
            </div>

            <h2>Tầng Trên</h2>
            <div class="view-seatUp">
                <%
                    for (int i = 1; i <= half; i++) {
                %>
                    <button class="seat"><img src="styles/seat.png" alt="Ghế <%= i %>"><%= i %></button>
                <%
                     }
                %>
            </div>
        <%
            }
        %>
        <div class="ticket-inf">
        </div>
    </section>

    <!-- Features Section -->
    <section class="features-section">
        <div class="features-grid">
            <div class="feature-card">
                <div class="feature-icon">
                    <i class="fas fa-clock"></i>
                </div>
                <h3>Đặt vé 24/7</h3>
                <p>Dịch vụ hoạt động 24/7, đặt vé bất cứ lúc nào</p>
            </div>

            <div class="feature-card">
                <div class="feature-icon">
                    <i class="fas fa-shield-alt"></i>
                </div>
                <h3>An toàn & Bảo mật</h3>
                <p>Thông tin được bảo mật tuyệt đối</p>
            </div>

            <div class="feature-card">
                <div class="feature-icon">
                    <i class="fas fa-headset"></i>
                </div>
                <h3>Hỗ trợ 24/7</h3>
                <p>Đội ngũ hỗ trợ sẵn sàng phục vụ</p>
            </div>

            <div class="feature-card">
                <div class="feature-icon">
                    <i class="fas fa-mobile-alt"></i>
                </div>
                <h3>Vé điện tử</h3>
                <p>Vé điện tử tiện lợi trên mobile</p>
            </div>
        </div>
    </section>
</div>

<script>
    // Set minimum date to today
    document.getElementById('departureDate').min = new Date().toISOString().split('T')[0];

    // Add loading animation to search button
    document.querySelector('form').addEventListener('submit', function(e) {
        const btn = document.querySelector('.search-button');
        btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
        btn.disabled = true;
    });
</script>

<!-- Add error message styles -->
<style>
    .error-message {
        background: #f8d7da;
        color: #721c24;
        padding: 1rem 1.5rem;
        border-radius: 10px;
        margin-bottom: 2rem;
        display: flex;
        align-items: center;
        gap: 0.5rem;
        border: 1px solid #f5c6cb;
    }

    .error-message i {
        color: #dc3545;
    }
</style>
</body>
</html>