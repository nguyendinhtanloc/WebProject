<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BusGo - Kết quả tìm kiếm</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="styles/styles.css">
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
    <!-- Search Form Mini -->
    <section class="search-section search-mini">
        <h3 class="search-title-mini">Tìm chuyến xe khác</h3>

        <form action="home" method="get">
            <div class="form-grid">
                <div class="form-group">
                    <label for="fromPlace">Điểm đi:</label>
                    <i class="form-icon fas fa-map-marker-alt"></i>
                    <input type="text" id="fromPlace" name="fromPlace" required
                           placeholder="Nhập điểm đi"
                           value="${fromPlace != null ? fromPlace : ''}">
                </div>

                <div class="arrow-separator">
                    <i class="fas fa-exchange-alt"></i>
                </div>

                <div class="form-group">
                    <label for="toPlace">Điểm đến:</label>
                    <i class="form-icon fas fa-flag-checkered"></i>
                    <input type="text" id="toPlace" name="toPlace" required
                           placeholder="Nhập điểm đến"
                           value="${toPlace != null ? toPlace : ''}">
                </div>

                <div class="form-group">
                    <label for="departureDate">Ngày đi:</label>
                    <i class="form-icon fas fa-calendar-alt"></i>
                    <input type="date" id="departureDate" name="departureDate" required
                           value="${departureDate != null ? departureDate : ''}">
                </div>

                <button type="submit" class="search-button search-button-mini">
                    <i class="fas fa-search"></i>
                </button>
            </div>

            <div class="search-actions">
                <a href="index.jsp" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i>
                    Quay lại
                </a>
            </div>
        </form>
    </section>

    <!-- Breadcrumb -->
    <div class="breadcrumb">
        <a href="index.jsp"><i class="fas fa-home"></i> Trang chủ</a>
        <span><i class="fas fa-chevron-right"></i></span>
        <span>Kết quả tìm kiếm</span>
    </div>

    <!-- Results Section -->
    <c:if test="${not empty trips}">
        <section class="results-section">
            <div class="results-header">
                <i class="fas fa-list"></i>
                <h3>Kết quả tìm kiếm - ${trips.size()} chuyến xe</h3>
                <div class="search-info">
                    <span><i class="fas fa-map-marker-alt"></i> ${fromPlace}</span>
                    <i class="fas fa-arrow-right"></i>
                    <span><i class="fas fa-flag-checkered"></i> ${toPlace}</span>
                    <span><i class="fas fa-calendar"></i> ${departureDate}</span>
                </div>
            </div>

            <table class="results-table">
                <thead>
                <tr>
                    <th><i class="fas fa-route"></i> Tuyến đường</th>
                    <th><i class="fas fa-calendar"></i> Ngày giờ</th>
                    <th><i class="fas fa-money-bill"></i> Giá vé</th>
                    <th><i class="fas fa-bus"></i> Loại xe</th>
                    <th><i class="fas fa-cog"></i> Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="trip" items="${trips}">
                    <tr>
                        <td data-label="Tuyến đường">
                            <div class="route-info">
                                <i class="fas fa-arrow-right"></i>
                                    ${trip.departure_place} - ${trip.arrival_place}
                            </div>
                        </td>
                        <td data-label="Ngày giờ">
                            <div class="date-time">
                                <div><i class="fas fa-calendar"></i> ${trip.departure_date}</div>
                                <div><i class="fas fa-clock"></i> ${trip.departure_time}</div>
                            </div>
                        </td>
                        <td data-label="Giá vé">
                            <span class="price-tag">
                                <i class="fas fa-dong-sign"></i> ${trip.price}
                            </span>
                        </td>
                        <td data-label="Loại xe">
                            <span class="vehicle-badge">
                                <i class="fas fa-bus"></i>
                                    ${trip.vehicle.type}
                            </span>
                        </td>
                        <td data-label="Thao tác">
                            <div class="action-buttons">
                                <form action="book" method="post">
                                    <input type="hidden" name="vehicle_type" value="${trip.vehicle.type}">
                                    <input type="hidden" name="vehicle_id" value="${trip.vehicle.vehicle_id}">
                                    <input type="hidden" name="vehicle_lane" value="${trip.vehicle.lane}">
                                    <button type="submit" class="btn-book">
                                        <i class="fas fa-ticket-alt"></i> Đặt vé
                                    </button>
                                </form>
                                <button class="btn-info">
                                    <i class="fas fa-info"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <!-- Pagination (optional) -->
            <div class="pagination">
                <button class="btn btn-outline" disabled>
                    <i class="fas fa-chevron-left"></i> Trước
                </button>
                <span class="page-info">Trang 1 của 1</span>
                <button class="btn btn-outline" disabled>
                    Sau <i class="fas fa-chevron-right"></i>
                </button>
            </div>
        </section>
    </c:if>

    <!-- Empty state when no results -->
    <c:if test="${empty trips}">
        <section class="results-section">
            <div class="empty-state">
                <div class="empty-icon">
                    <i class="fas fa-search-minus"></i>
                </div>
                <h3>Không tìm thấy chuyến xe phù hợp</h3>
                <p>Vui lòng thử lại với điều kiện tìm kiếm khác</p>
                <div class="empty-actions">
                    <a href="index.jsp" class="btn btn-primary">
                        <i class="fas fa-search"></i>
                        Tìm kiếm mới
                    </a>
                </div>
            </div>
        </section>
    </c:if>
</div>

<script>
    // Set minimum date to today
    document.getElementById('departureDate').min = new Date().toISOString().split('T')[0];

    // Add loading animation to search button
    document.querySelector('form').addEventListener('submit', function(e) {
        const btn = document.querySelector('.search-button-mini');
        btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
        btn.disabled = true;
    });
</script>
</body>
</html>