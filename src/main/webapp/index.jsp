<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BusBooking - Đặt vé xe khách trực tuyến</title>
    
    <!-- Try multiple CSS loading methods -->
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    
    <!-- External libraries -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <!-- Fallback inline styles -->
    <style>
        body { 
            font-family: 'Inter', Arial, sans-serif; 
            margin: 0; 
            padding: 0; 
            line-height: 1.6;
            color: #334155;
        }
        .header { 
            background: rgba(255, 255, 255, 0.95); 
            padding: 1rem 0; 
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .container { 
            max-width: 1200px; 
            margin: 0 auto; 
            padding: 0 20px; 
        }
        .hero {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 60vh;
            display: flex;
            align-items: center;
            color: white;
            text-align: center;
        }
        .search-card {
            background: white;
            border-radius: 20px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            margin: -100px auto 2rem;
            position: relative;
            z-index: 10;
            max-width: 800px;
        }
        .btn-primary {
            background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
            color: white;
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 16px;
        }
    </style>
</head>
<body>
    <!-- Header -->
    <header class="header">
        <nav class="navbar">
            <div class="nav-container">
                <div class="nav-logo">
                    <i class="fas fa-bus"></i>
                    <span>BusBooking</span>
                </div>
                <ul class="nav-menu">
                    <li class="nav-item">
                        <a href="#" class="nav-link">Trang chủ</a>
                    </li>
                    <li class="nav-item">
                        <a href="#" class="nav-link">Tra cứu vé</a>
                    </li>
                    <li class="nav-item">
                        <a href="#" class="nav-link">Liên hệ</a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/login.jsp" class="nav-link">Đăng nhập</a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/register.jsp" class="nav-link">Đăng ký</a>
                    </li>

                </ul>
                <div class="hamburger">
                    <span class="bar"></span>
                    <span class="bar"></span>
                    <span class="bar"></span>
                </div>
            </div>
        </nav>
    </header>

    <!-- Hero Section -->
    <section class="hero">
        <div class="hero-overlay">
            <div class="container">
                <div class="hero-content">
                    <h1 class="hero-title">
                        Đặt vé xe khách <span class="highlight">nhanh chóng</span> và 
                        <span class="highlight">tiện lợi</span>
                    </h1>
                    <p class="hero-description">
                        Tìm kiếm và đặt vé xe khách trực tuyến với hàng nghìn chuyến xe khắp Việt Nam. 
                        An toàn, nhanh chóng và uy tín.
                    </p>
                </div>
            </div>
        </div>
    </section>

    <!-- Search Form -->
    <section class="search-section">
        <div class="container">
            <div class="search-card">
                <h2 class="search-title">Tìm chuyến xe của bạn</h2>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-error">
                        <i class="fas fa-exclamation-triangle"></i>
                        ${error}
                    </div>
                </c:if>
                
                <form action="search" method="POST" class="search-form">
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
                                   placeholder="Nhập điểm đi..."
                                   list="departure-list"
                                   required>
                            <datalist id="departure-list">
                                <c:forEach var="place" items="${departurePlaces}">
                                    <option value="${place}">
                                </c:forEach>
                            </datalist>
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
                                   placeholder="Nhập điểm đến..."
                                   list="arrival-list"
                                   required>
                            <datalist id="arrival-list">
                                <c:forEach var="place" items="${arrivalPlaces}">
                                    <option value="${place}">
                                </c:forEach>
                            </datalist>
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
                                   min="<%= java.time.LocalDate.now() %>"
                                   required>
                        </div>
                        
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-search"></i>
                                Tìm chuyến xe
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </section>

    <!-- Features Section -->
    <section class="features">
        <div class="container">
            <h2 class="section-title">Tại sao chọn BusBooking?</h2>
            <div class="features-grid">
                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-clock"></i>
                    </div>
                    <h3 class="feature-title">Nhanh chóng</h3>
                    <p class="feature-description">
                        Tìm kiếm và đặt vé chỉ trong vài phút với giao diện thân thiện
                    </p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-shield-alt"></i>
                    </div>
                    <h3 class="feature-title">An toàn</h3>
                    <p class="feature-description">
                        Hệ thống thanh toán bảo mật và dữ liệu khách hàng được mã hóa
                    </p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-star"></i>
                    </div>
                    <h3 class="feature-title">Uy tín</h3>
                    <p class="feature-description">
                        Hợp tác với các nhà xe uy tín hàng đầu Việt Nam
                    </p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-headset"></i>
                    </div>
                    <h3 class="feature-title">Hỗ trợ 24/7</h3>
                    <p class="feature-description">
                        Đội ngũ chăm sóc khách hàng sẵn sàng hỗ trợ mọi lúc
                    </p>
                </div>
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

    <script>
        console.log('🔧 Form debugging enabled');
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.querySelector('.search-form');
            if (form) {
                form.addEventListener('submit', function(e) {
                    console.log('🔧 Form submitted!');
                    console.log('🔧 Action:', this.action);
                    console.log('🔧 Method:', this.method);
                    
                    const formData = new FormData(this);
                    console.log('🔧 Form data:');
                    for (let [key, value] of formData.entries()) {
                        console.log(`  ${key}: ${value}`);
                    }
                    
                    // Let form submit normally
                });
            }
        });
    </script>
</body>
</html>