<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="/error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết quả tìm kiếm - BusBooking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/searchResults.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
</head>
<body>
    <!-- Header -->
    <header class="header">
        <nav class="navbar">
            <div class="nav-container">
                <div class="nav-logo">
                    <a href="${pageContext.request.contextPath}/">
                        <i class="fas fa-bus"></i>
                        <span>BusBooking</span>
                    </a>
                </div>
                <ul class="nav-menu">
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/" class="nav-link">Trang chủ</a>
                    </li>
                    <li class="nav-item">
                        <a href="#" class="nav-link">Lịch trình</a>
                    </li>
                    <li class="nav-item">
                        <a href="#" class="nav-link">Tra cứu vé</a>
                    </li>
                    <li class="nav-item">
                        <a href="#" class="nav-link">Liên hệ</a>
                    </li>
                </ul>
                <div class="nav-auth">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <div class="user-menu">
                                <span class="user-name">
                                    <i class="fas fa-user"></i>
                                    ${sessionScope.user.fullName}
                                </span>
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
            </div>
        </nav>
    </header>

    <!-- Search Form Section -->
    <section class="search-section">
        <div class="container">
            <div class="search-form-container">
                <h2 class="search-title">🔍 Tìm chuyến xe phù hợp</h2>
                <form class="search-form" action="${pageContext.request.contextPath}/search" method="GET">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="departureCity">
                                <i class="fas fa-map-marker-alt departure-icon"></i>
                                Điểm đi
                            </label>
                            <input type="text" id="departureCity" name="departureCity"
                                   value="${param.departureCity}" placeholder="Chọn điểm đi" required>
                        </div>

                        <div class="form-group">
                            <label for="arrivalCity">
                                <i class="fas fa-map-marker-alt arrival-icon"></i>
                                Điểm đến
                            </label>
                            <input type="text" id="arrivalCity" name="arrivalCity"
                                   value="${param.arrivalCity}" placeholder="Chọn điểm đến" required>
                        </div>

                        <div class="form-group">
                            <label for="departureDate">
                                <i class="fas fa-calendar-alt"></i>
                                Ngày đi
                            </label>
                            <input type="date" id="departureDate" name="departureDate"
                                   value="${param.departureDate}" required>
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-search">
                                <i class="fas fa-search"></i>
                                Tìm kiếm
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </section>

    <!-- Search Results Section -->
    <section class="results-section">
        <div class="container">
            <div class="results-header">
                <div class="search-summary">
                    <h2 class="results-title">
                        <i class="fas fa-search"></i>
                        <c:choose>
                            <c:when test="${not empty trips}">
                                Tìm thấy ${trips.size()} chuyến xe
                            </c:when>
                            <c:otherwise>
                                Không tìm thấy chuyến xe nào
                            </c:otherwise>
                        </c:choose>
                    </h2>
                    <c:if test="${not empty param.departureCity and not empty param.arrivalCity}">
                        <div class="search-info-card">
                            <div class="route-summary">
                                <div class="route-item">
                                    <i class="fas fa-map-marker-alt departure-marker"></i>
                                    <span class="route-label">Từ:</span>
                                    <strong class="route-value">${param.departureCity}</strong>
                                </div>
                                <div class="route-arrow">
                                    <i class="fas fa-arrow-right"></i>
                                </div>
                                <div class="route-item">
                                    <i class="fas fa-map-marker-alt arrival-marker"></i>
                                    <span class="route-label">Đến:</span>
                                    <strong class="route-value">${param.arrivalCity}</strong>
                                </div>
                                <c:if test="${not empty param.departureDate}">
                                    <div class="date-item">
                                        <i class="fas fa-calendar-alt"></i>
                                        <span class="route-label">Ngày:</span>
                                        <strong class="route-value">${param.departureDate}</strong>
                                    </div>
                                </c:if>
                            </div>
                            <div class="search-actions">
                                <button class="btn btn-outline btn-sm" onclick="showSearchForm()">
                                    <i class="fas fa-edit"></i>
                                    Thay đổi tìm kiếm
                                </button>
                            </div>
                        </div>
                    </c:if>
                </div>

                <c:if test="${not empty trips}">
                    <div class="results-filters">
                        <div class="filter-options">
                            <span class="filter-label">Sắp xếp theo:</span>
                            <select class="filter-select" onchange="sortTrips(this.value)">
                                <option value="time">Giờ khởi hành</option>
                                <option value="price">Giá vé</option>
                                <option value="company">Nhà xe</option>
                            </select>
                        </div>
                        <div class="view-options">
                            <span class="view-label">Hiển thị:</span>
                            <button class="btn btn-sm btn-outline active" onclick="setView('list')">
                                <i class="fas fa-list"></i>
                            </button>
                            <button class="btn btn-sm btn-outline" onclick="setView('grid')">
                                <i class="fas fa-th"></i>
                            </button>
                        </div>
                    </div>
                </c:if>
            </div>

            <div class="results-content">
                <c:choose>
                    <c:when test="${not empty trips}">
                        <div class="trip-list">
                            <c:forEach var="trip" items="${trips}">
                                <div class="trip-card">
                                    <div class="trip-header">
                                        <div class="company-info">
                                            <div class="company-logo">
                                                <i class="fas fa-building"></i>
                                            </div>
                                            <div class="company-details">
                                                <h3 class="company-name">
                                                    <c:choose>
                                                        <c:when test="${not empty trip.busCompany}">
                                                            ${trip.busCompany.name}
                                                        </c:when>
                                                        <c:otherwise>
                                                            Nhà xe (ID: ${trip.companyId})
                                                        </c:otherwise>
                                                    </c:choose>
                                                </h3>
                                                <p class="vehicle-info">
                                                    <c:choose>
                                                        <c:when test="${not empty trip.vehicle}">
                                                            <span class="vehicle-type">${trip.vehicle.type}</span>
                                                            <span class="separator">•</span>
                                                            <span class="capacity">${trip.vehicle.capacity} chỗ</span>
                                                            <c:if test="${not empty trip.vehicle.licensePlate}">
                                                                <span class="separator">•</span>
                                                                <span class="license">${trip.vehicle.licensePlate}</span>
                                                            </c:if>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="vehicle-type">Xe khách</span>
                                                            <span class="separator">•</span>
                                                            <span class="capacity">ID: ${trip.vehicleId}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                            </div>
                                        </div>
                                        <div class="trip-price">
                                            <span class="price">
                                                <c:choose>
                                                    <c:when test="${not empty trip.price}">
                                                        <fmt:formatNumber value="${trip.price}" type="currency"
                                                                        currencySymbol="" pattern="#,##0"/> VNĐ
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="contact-price">Liên hệ</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                            <span class="price-label">/ người</span>
                                        </div>
                                    </div>

                                    <div class="trip-body">
                                        <div class="trip-route">
                                            <div class="departure-info">
                                                <div class="time">
                                                    <c:choose>
                                                        <c:when test="${not empty trip.departureTime}">
                                                            ${trip.departureTime}
                                                        </c:when>
                                                        <c:otherwise>--:--</c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="place">${trip.departureCity}</div>
                                                <c:if test="${not empty trip.departurePoint}">
                                                    <div class="address">${trip.departurePoint}</div>
                                                </c:if>
                                                <div class="date">
                                                    <i class="fas fa-calendar-alt"></i>
                                                    ${trip.departureDate}
                                                </div>
                                            </div>

                                            <div class="route-line">
                                                <div class="route-info">
                                                    <c:if test="${not empty trip.distanceKm}">
                                                        <div class="distance">
                                                            <i class="fas fa-route"></i>
                                                            ${trip.distanceKm} km
                                                        </div>
                                                    </c:if>
                                                    <div class="duration">
                                                        <i class="fas fa-clock"></i>
                                                        <c:choose>
                                                            <c:when test="${not empty trip.arrivalDateTime and not empty trip.departureTime}">
                                                                ~4h 30m
                                                            </c:when>
                                                            <c:otherwise>
                                                                ~4h
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                                <div class="route-arrow">
                                                    <i class="fas fa-long-arrow-alt-right"></i>
                                                </div>
                                            </div>

                                            <div class="arrival-info">
                                                <div class="time">
                                                    <c:choose>
                                                        <c:when test="${not empty trip.arrivalDateTime}">
                                                            ${trip.arrivalDateTime.toLocalTime()}
                                                        </c:when>
                                                        <c:otherwise>--:--</c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="place">${trip.arrivalCity}</div>
                                                <c:if test="${not empty trip.arrivalPoint}">
                                                    <div class="address">${trip.arrivalPoint}</div>
                                                </c:if>
                                                <c:if test="${not empty trip.arrivalDateTime}">
                                                    <div class="date">
                                                        <i class="fas fa-calendar-alt"></i>
                                                        ${trip.arrivalDateTime.toLocalDate()}
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>

                                        <div class="trip-details">
                                            <div class="details-row">
                                                <c:if test="${not empty trip.vehicle and not empty trip.vehicle.amenities}">
                                                    <div class="amenities">
                                                        <i class="fas fa-star"></i>
                                                        <span class="feature-label">Tiện ích:</span>
                                                        <span class="amenities-list">${trip.vehicle.amenities}</span>
                                                    </div>
                                                </c:if>

                                                <div class="trip-status">
                                                    <i class="fas fa-check-circle status-icon"></i>
                                                    <span class="status-text">Còn chỗ trống</span>
                                                </div>

                                                <c:if test="${not empty trip.status}">
                                                    <div class="trip-state">
                                                        <i class="fas fa-info-circle"></i>
                                                        <span>Trạng thái: ${trip.status}</span>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="trip-footer">
                                        <div class="footer-info">
                                            <div class="booking-note">
                                                <i class="fas fa-exclamation-circle"></i>
                                                <span>Đặt vé ngay để đảm bảo chỗ ngồi</span>
                                            </div>
                                        </div>
                                        <div class="trip-actions">
                                            <button class="btn btn-outline view-details"
                                                    onclick="toggleTripDetails('${trip.tripId}')">
                                                <i class="fas fa-info-circle"></i>
                                                Chi tiết
                                            </button>
                                            <form action="book" method="post">
                                                <input type="hidden" name="tripSelected" value="${trip.tripId}">
                                                <input type="hidden" name="vehicleType" value="${trip.vehicle.type}">
                                                <input type="hidden" name="vehicleId" value="${trip.vehicle.vehicleId}">
                                                <button type="submit" class="btn btn-primary btn-book">
                                                    <i class="fas fa-ticket-alt"></i>
                                                    Đặt vé ngay
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="no-results">
                            <div class="no-results-content">
                                <div class="no-results-icon">
                                    <i class="fas fa-bus"></i>
                                    <i class="fas fa-search"></i>
                                </div>
                                <h3>Oops! Không tìm thấy chuyến xe phù hợp</h3>
                                <p class="no-results-description">
                                    Rất tiếc, chúng tôi không tìm thấy chuyến xe nào cho tuyến đường
                                    <strong>${param.departureCity} → ${param.arrivalCity}</strong>
                                    <c:if test="${not empty param.departureDate}">
                                        vào ngày <strong>${param.departureDate}</strong>
                                    </c:if>
                                </p>

                                <div class="suggestions">
                                    <h4><i class="fas fa-lightbulb"></i> Gợi ý cho bạn</h4>
                                    <ul class="suggestion-list">
                                        <li>
                                            <i class="fas fa-calendar-alt"></i>
                                            Thử thay đổi ngày khởi hành (±1-2 ngày)
                                        </li>
                                        <li>
                                            <i class="fas fa-map-marker-alt"></i>
                                            Kiểm tra lại chính tả điểm đi và điểm đến
                                        </li>
                                        <li>
                                            <i class="fas fa-route"></i>
                                            Tìm kiếm các tuyến đường gần khu vực của bạn
                                        </li>
                                        <li>
                                            <i class="fas fa-clock"></i>
                                            Thử tìm kiếm vào các thời điểm khác trong ngày
                                        </li>
                                    </ul>
                                </div>

                                <div class="no-results-actions">
                                    <button class="btn btn-primary" onclick="showSearchForm()">
                                        <i class="fas fa-search"></i>
                                        Tìm kiếm lại
                                    </button>
                                    <a href="${pageContext.request.contextPath}/" class="btn btn-outline">
                                        <i class="fas fa-home"></i>
                                        Về trang chủ
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="footer">
        <div class="container">
            <div class="footer-content">
                <div class="footer-section">
                    <div class="footer-logo">
                        <i class="fas fa-bus"></i>
                        <span>BusBooking</span>
                    </div>
                    <p class="footer-description">
                        Nền tảng đặt vé xe khách trực tuyến hàng đầu Việt Nam
                    </p>
                </div>
            </div>

            <div class="footer-bottom">
                <p>&copy; 2024 BusBooking. Tất cả quyền được bảo lưu.</p>
            </div>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/searchResults.js"></script>
</body>
</html>