<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="/error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.busbooking.entity.Seat, java.util.List" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết quả tìm kiếm - BusBooking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/seat.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
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
                                    ${sessionScope.user.email}
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

    <section class="search-results">
        <div class="container">
            <%-- Scriptlet để chuẩn bị dữ liệu ghế --%>
            <%
                List<Seat> seats = (List<Seat>) request.getAttribute("seats");

                float price = 0;
                if (seats != null && !seats.isEmpty()) {
                    price = seats.get(0).getPrice();
                }
                request.setAttribute("ticketPrice", price);

                String type = (String) request.getAttribute("type");

                int veLane = 3;
                if (!"Limousine".equals(type)) {
                    veLane = 4;
                }
            %>

            <div class="booking-container">
                <%-- Cột bên trái: Sơ đồ ghế --%>
                <div class="seat-selection-wrapper">
                    <h2 class="section-title">Sơ đồ ghế</h2>

                    <div class="seat-legend">
                        <div class="legend-item"><img src="css/seat.png" class="legend-icon"><span>Còn trống</span></div>
                        <div class="legend-item"><img src="css/seat.png" class="legend-icon selected"><span>Đang chọn</span></div>
                        <div class="legend-item"><img src="css/seat.png" class="legend-icon booked"><span>Đã đặt</span></div>
                    </div>

                    <c:if test="${not empty seats}">
                        <%-- ===== HIỂN THỊ CHO XE GIƯỜNG NẰM (Limousine) ===== --%>
                        <c:if test="${type == 'Limousine'}">
                            <div class="seat-container sleeper">
                                <div class="sleeper-floors-container">

                                    <%-- Tầng Dưới --%>
                                    <div class="floor-wrapper">
                                        <h3>Tầng Dưới</h3>
                                        <table class="seat-table">
                                            <%
                                                int seatsPerFloor = seats.size() / 2;
                                                int startFloor1 = 0;
                                                for (int row = 0; row < 5 && startFloor1 < seatsPerFloor; row++) {
                                            %>
                                            <tr>
                                                <% for (int lane = 1; lane <= veLane; lane++) {
                                                    if (lane == 2 && (startFloor1 == 1 || startFloor1 == 16)) { %>
                                                <td class="aisle"></td>
                                                <% } else if (startFloor1 < seatsPerFloor) {
                                                    Seat seat = seats.get(startFloor1);
                                                    startFloor1++;
                                                %>
                                                <td class="seat-cell">
                                                    <input type="checkbox" id="seat-<%= seat.getNumSeat() %>" name="seat" value="<%= seat.getNumSeat() %>" <%= "booked".equals(seat.getStatusBook()) ? "disabled" : "" %>>
                                                    <label for="seat-<%= seat.getNumSeat() %>">
                                                        <img src="${pageContext.request.contextPath}/css/seat.png" class="seat-icon">
                                                        <span><%= seat.getNumSeat() %></span>
                                                    </label>
                                                </td>
                                                <% } else { %>
                                                <td></td>
                                                <% }
                                                } %>
                                            </tr>
                                            <% } %>
                                        </table>
                                    </div>

                                    <%-- Tầng Trên --%>
                                    <div class="floor-wrapper">
                                        <h3>Tầng Trên</h3>
                                        <table class="seat-table">
                                            <%
                                                int startFloor2 = seatsPerFloor;
                                                for (int row = 0; row < 5 && startFloor2 < seats.size(); row++) {
                                            %>
                                            <tr>
                                                <% for (int lane = 1; lane <= veLane; lane++) {
                                                    if (lane == 2 && (startFloor2 == 1 || startFloor2 == 16)) { %>
                                                <td class="aisle"></td>
                                                <% } else if (startFloor2 < seats.size()) {
                                                    Seat seat = seats.get(startFloor2);
                                                    startFloor2++;
                                                %>
                                                <td class="seat-cell">
                                                    <input type="checkbox" id="seat-<%= seat.getNumSeat() %>" name="seat" value="<%= seat.getNumSeat() %>" <%= "booked".equals(seat.getStatusBook()) ? "disabled" : "" %>>
                                                    <label for="seat-<%= seat.getNumSeat() %>">
                                                        <img src="${pageContext.request.contextPath}/css/seat.png" class="seat-icon">
                                                        <span><%= seat.getNumSeat() %></span>
                                                    </label>
                                                </td>
                                                <% } else { %>
                                                <td></td>
                                                <% }
                                                } %>
                                            </tr>
                                            <% } %>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <%-- ===== HIỂN THỊ CHO XE GHẾ NGỒI ===== --%>
                        <c:if test="${type != 'Limousine'}">
                            <div class="seat-container seating">
                                <table class="seat-table">
                                    <%
                                        int seatIndex = 0;
                                        int totalColumns = veLane + 1;
                                        int aisleColumn = 3;
                                        while (seatIndex < seats.size()) {
                                    %>
                                    <tr>
                                        <% for (int col = 1; col <= totalColumns; col++) {
                                            if (col == aisleColumn) { %>
                                        <td class="aisle"></td>
                                        <% } else {
                                            if (seatIndex < seats.size()) {
                                                Seat seat = seats.get(seatIndex);
                                                seatIndex++;
                                        %>
                                        <td class="seat-cell">
                                            <input type="checkbox" id="seat-<%= seat.getNumSeat() %>" name="seat" value="<%= seat.getNumSeat() %>" <%= "booked".equals(seat.getStatusBook()) ? "disabled" : "" %>>
                                            <label for="seat-<%= seat.getNumSeat() %>">
                                                <img src="css/seat.png" class="seat-icon">
                                                <span><%= seat.getNumSeat() %></span>
                                            </label>
                                        </td>
                                        <% } else { %>
                                        <td></td>
                                        <% }
                                        }
                                        } %>
                                    </tr>
                                    <% } %>
                                </table>
                            </div>
                        </c:if>
                    </c:if>
                </div>

                <%-- Cột bên phải: Thông tin vé --%>
                <div class="ticket-info-wrapper">
                    <h2 class="section-title">Thông tin vé</h2>
                    <div class="info-line">
                        <span>Tuyến xe:</span>
                        <strong>${trip.departureCity} → ${trip.arrivalCity}</strong>
                    </div>
                    <div class="info-line">
                        <span>Ngày đi:</span>
                        <strong>${trip.departureDate}</strong>
                    </div>
                    <div class="info-line">
                        <span>Giờ khởi hành:</span>
                        <strong>${trip.departureTime}</strong>
                    </div>
                    <hr>
                    <div id="selected-seats-info">
                        <div class="info-line">
                            <span>Ghế đã chọn:</span>
                            <strong id="seat-numbers">Chưa chọn</strong>
                        </div>
                        <div class="info-line">
                            <span>Số lượng:</span>
                            <strong id="seat-count">0</strong>
                        </div>
                        <div class="info-line total">
                            <span>Tổng tiền:</span>
                            <strong id="total-price">0 VND</strong>
                        </div>
                    </div>

                    <hr>
                    <div class="pickup-dropoff-info">
                        <h3 class="customer-info-title">Thông tin đón trả</h3>
                        <div class="pickup-dropoff-container">
                            <div class="location-point-section">
                                <h4>ĐIỂM ĐÓN</h4>
                                <div class="radio-group">
                                    <div>
                                        <input type="radio" id="pickup-station" name="pickup_option" value="station" checked>
                                        <label for="pickup-station">Bến xe/VP</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="pickup-transfer" name="pickup_option" value="transfer">
                                        <label for="pickup-transfer">Trung chuyển</label>
                                    </div>
                                </div>
                                <select name="pickup_location" id="pickup-location" class="location-select">
                                    <option value="${trip.departurePoint}" selected>${trip.departurePoint}</option>
                                    <option value="another_place_1">Một điểm đón khác 1</option>
                                </select>
                                <p class="location-note">
                                    Quý khách vui lòng có mặt tại Bến xe/Văn phòng <strong>${trip.departurePoint}</strong>
                                    trước <strong>${trip.departureTime} ${trip.departureDate}</strong>
                                    để được trung chuyển hoặc kiểm tra thông tin trước khi lên xe.
                                </p>
                            </div>

                            <div class="location-point-section">
                                <h4>ĐIỂM TRẢ</h4>
                                <div class="radio-group">
                                    <div>
                                        <input type="radio" id="dropoff-station" name="dropoff_option" value="station" checked>
                                        <label for="dropoff-station">Bến xe/VP</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="dropoff-transfer" name="dropoff_option" value="transfer">
                                        <label for="dropoff-transfer">Trung chuyển</label>
                                    </div>
                                </div>
                                <select name="dropoff_location" id="dropoff-location" class="location-select">
                                    <option value="${trip.arrivalPoint}" selected>${trip.arrivalPoint}</option>
                                    <option value="another_place_2">Một điểm trả khác 2</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="customer-details-section">
                <form action="saveInf" method="post">
                    <div class="details-content-wrapper">
                        <div class="customer-info-form">
                            <h3 class="customer-info-title">Thông tin khách hàng</h3>
                            <div class="form-group-vertical">
                                <label for="customerName">Họ và tên *</label>
                                <input type="text" id="customerName" name="customerName" placeholder="Nhập họ và tên" required>
                            </div>
                            <div class="form-group-vertical">
                                <label for="customerPhone">Số điện thoại *</label>
                                <input type="tel" id="customerPhone" name="customerPhone" placeholder="Nhập số điện thoại" required>
                            </div>
                            <div class="form-group-vertical">
                                <label for="customerEmail">Email *</label>
                                <input type="email" id="customerEmail" name="customerEmail" placeholder="Nhập email để nhận vé" required>
                            </div>
                        </div>

                        <div class="terms-and-notes-section">
                            <h3 class="terms-title">ĐIỀU KHOẢN & LƯU Ý</h3>
                            <ul class="terms-list">
                                <li>Quý khách vui lòng <strong class="text-danger">Đăng ký/Đăng nhập tài khoản</strong> để nhận chương trình khuyến mãi.</li>
                                <li>(*) Quý khách vui lòng có mặt tại bến xuất phát của xe trước ít nhất 30 phút giờ xe khởi hành, mang theo thông báo đã thanh toán vé thành công có chứa mã vé được gửi từ hệ thống BUSBOOKING. Vui lòng liên hệ Trung tâm tổng đài <strong class="text-danger">1900 6067</strong> để được hỗ trợ.</li>
                                <li>(*) Nếu quý khách có nhu cầu trung chuyển, vui lòng liên hệ Tổng đài trung chuyển <strong class="text-danger">1900 6918</strong> trước khi đặt vé. Chúng tôi không đón/trung chuyển tại những điểm xe trung chuyển không thể tới được.</li>
                            </ul>
                        </div>
                    </div>

                    <div class="payment-bar">
                        <div class="payment-total">
                            <span class="payment-label">Tổng cộng</span>
                            <span class="payment-total-price" id="final-total-price">0đ</span>
                        </div>

                        <input type="hidden" name="selected_seats" id="hidden_selected_seats">

                        <input type="hidden" name="selected_pickup_option" id="hidden_pickup_option">
                        <input type="hidden" name="selected_pickup_location" id="hidden_pickup_location">
                        <input type="hidden" name="selected_dropoff_option" id="hidden_dropoff_option">
                        <input type="hidden" name="selected_dropoff_location" id="hidden_dropoff_location">

                        <input type="hidden" name="vehicle_type" value="${trip.vehicle.type}">
                        <button type="submit" class="btn-submit">Tiếp tục</button>
                    </div>
                </form>
            </div>
        </div>
    </section>

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
        const ticketPrice = ${ticketPrice};
    </script>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
    <script src="${pageContext.request.contextPath}/js/getInfSeat.js"></script>

</body>
</html>