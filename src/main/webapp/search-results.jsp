<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết quả tìm kiếm - BusBooking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
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
                        <a href="#" class="nav-link">Tra cứu vé</a>
                    </li>
                    <li class="nav-item">
                        <a href="#" class="nav-link">Liên hệ</a>
                    </li>
                    <li class="nav-item">
                        <a href="#" class="nav-link">Đăng nhập</a>
                    </li>
                </ul>
            </div>
        </nav>
    </header>

    <!-- Search Results -->
    <section class="search-results">
        <div class="container">
            <!-- Search Summary -->
            <div class="search-summary">
                <h1 class="page-title">Kết quả tìm kiếm</h1>
                <div class="search-info">
                    <div class="route-info">
                        <i class="fas fa-route"></i>
                        <span class="route">${departure} → ${arrival}</span>
                    </div>
                    <div class="date-info">
                        <i class="fas fa-calendar-alt"></i>
                        <span class="date">${date}</span>
                    </div>
                    <div class="count-info">
                        <i class="fas fa-bus"></i>
                        <span class="count">Tìm thấy ${trips.size()} chuyến xe</span>
                    </div>
                </div>
            </div>

            <!-- Modify Search -->
            <div class="modify-search">
                <button class="btn btn-outline" onclick="toggleSearchForm()">
                    <i class="fas fa-edit"></i>
                    Thay đổi tìm kiếm
                </button>
            </div>

            <!-- Hidden Search Form -->
            <div class="search-form-container" id="searchFormContainer" style="display: none;">
                <div class="search-card">
                    <form action="${pageContext.request.contextPath}/search" method="POST" class="search-form">
                        <div class="form-row">
                            <div class="form-group">
                                <label for="departure" class="form-label">
                                    <i class="fas fa-map-marker-alt"></i>
                                    Điểm đi
                                </label>
                                <input type="text" 
                                       id="departure" 
                                       name="departure" 
                                       class="form-control" 
                                       value="${departure}"
                                       required>
                            </div>
                            
                            <div class="form-group">
                                <label for="arrival" class="form-label">
                                    <i class="fas fa-map-marker-alt"></i>
                                    Điểm đến
                                </label>
                                <input type="text" 
                                       id="arrival" 
                                       name="arrival" 
                                       class="form-control" 
                                       value="${arrival}"
                                       required>
                            </div>
                            
                            <div class="form-group">
                                <label for="date" class="form-label">
                                    <i class="fas fa-calendar-alt"></i>
                                    Ngày đi
                                </label>
                                <input type="date" 
                                       id="date" 
                                       name="date" 
                                       class="form-control"
                                       value="${date}"
                                       min="<%= java.time.LocalDate.now() %>"
                                       required>
                            </div>
                            
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search"></i>
                                    Tìm lại
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Trip Results -->
            <div class="trip-results">
                <c:choose>
                    <c:when test="${empty trips}">
                        <div class="no-results">
                            <div class="no-results-icon">
                                <i class="fas fa-bus"></i>
                            </div>
                            <h2>Không tìm thấy chuyến xe phù hợp</h2>
                            <p>Xin lỗi, không có chuyến xe nào phù hợp với yêu cầu của bạn.</p>
                            <div class="suggestions">
                                <h3>Gợi ý:</h3>
                                <ul>
                                    <li>Thử thay đổi ngày đi</li>
                                    <li>Kiểm tra lại điểm đi và điểm đến</li>
                                    <li>Tìm kiếm các tuyến đường gần đó</li>
                                </ul>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="trip-list">
                            <c:forEach var="trip" items="${trips}">
                                <div class="trip-card">
                                    <div class="trip-header">
                                        <div class="company-info">
                                            <div class="company-logo">
                                                <i class="fas fa-building"></i>
                                            </div>
                                            <div class="company-details">
                                                <h3 class="company-name">${trip.busCompany.name}</h3>
                                                <p class="vehicle-info">
                                                    <span class="vehicle-type">${trip.vehicle.type}</span>
                                                    <span class="separator">•</span>
                                                    <span class="capacity">${trip.vehicle.capacity} chỗ</span>
                                                </p>
                                            </div>
                                        </div>
                                        <div class="trip-price">
                                            <span class="price">
                                                <fmt:formatNumber value="${trip.price}" type="currency" 
                                                                currencySymbol="" pattern="#,##0"/> VNĐ
                                            </span>
                                        </div>
                                    </div>
                                    
                                    <div class="trip-body">
                                        <div class="trip-route">
                                            <div class="departure-info">
                                                <div class="time">${trip.departureTime}</div>
                                                <div class="place">${trip.departurePlace}</div>
                                            </div>
                                            
                                            <div class="route-visual">
                                                <div class="route-line">
                                                    <div class="departure-dot"></div>
                                                    <div class="line"></div>
                                                    <div class="arrival-dot"></div>
                                                </div>
                                                <div class="vehicle-icon">
                                                    <i class="fas fa-bus"></i>
                                                </div>
                                            </div>
                                            
                                            <div class="arrival-info">
                                                <div class="time">--:--</div>
                                                <div class="place">${trip.arrivalPlace}</div>
                                            </div>
                                        </div>
                                        
                                        <div class="trip-details">
                                            <div class="detail-item">
                                                <i class="fas fa-id-card"></i>
                                                <span>Tài xế: ${trip.driver.name}</span>
                                            </div>
                                            <div class="detail-item">
                                                <i class="fas fa-car"></i>
                                                <span>Biển số: ${trip.vehicle.licensePlate}</span>
                                            </div>
                                            <div class="detail-item">
                                                <i class="fas fa-phone"></i>
                                                <span>Liên hệ: ${trip.driver.phone}</span>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="trip-footer">
                                        <div class="trip-status">
                                            <span class="status-badge status-scheduled">
                                                Còn chỗ
                                            </span>
                                        </div>
                                        <div class="trip-actions">
                                            <button class="btn btn-outline btn-sm" onclick="viewDetails('${trip.tripId}')">
                                                <i class="fas fa-info-circle"></i>
                                                Chi tiết
                                            </button>
                                            <button class="btn btn-primary btn-sm" onclick="bookTicket('${trip.tripId}')">
                                                <i class="fas fa-ticket-alt"></i>
                                                Đặt vé
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
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
                
                <div class="footer-section">
                    <h3 class="footer-title">Liên kết</h3>
                    <ul class="footer-links">
                        <li><a href="#">Về chúng tôi</a></li>
                        <li><a href="#">Điều khoản sử dụng</a></li>
                        <li><a href="#">Chính sách bảo mật</a></li>
                        <li><a href="#">Liên hệ</a></li>
                    </ul>
                </div>
                
                <div class="footer-section">
                    <h3 class="footer-title">Liên hệ</h3>
                    <ul class="footer-contact">
                        <li><i class="fas fa-phone"></i> 1900 xxxx</li>
                        <li><i class="fas fa-envelope"></i> info@busbooking.vn</li>
                        <li><i class="fas fa-map-marker-alt"></i> Hà Nội, Việt Nam</li>
                    </ul>
                </div>
            </div>
            
            <div class="footer-bottom">
                <p>&copy; 2025 BusBooking. Tất cả quyền được bảo lưu.</p>
            </div>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>