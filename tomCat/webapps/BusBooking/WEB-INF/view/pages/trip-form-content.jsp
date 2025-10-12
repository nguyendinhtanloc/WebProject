<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="content-container">
    <h2>
        <c:choose>
            <c:when test="${mode == 'create'}">Thêm chuyến xe</c:when>
            <c:otherwise>Sửa chuyến xe</c:otherwise>
        </c:choose>
    </h2>

    <c:if test="${not empty errorMessage}">
        <div class="error-message">
            <p>${errorMessage}</p>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/trips" method="post" class="data-form">
        <input type="hidden" name="action" value="${mode}" />

        <c:if test="${mode == 'edit'}">
            <input type="hidden" name="tripId" value="${trip.tripId}" />
        </c:if>

        <!-- Công ty, xe, tài xế -->
        <div class="form-group">
            <label for="companyId">Công ty:</label>
            <select id="companyId" name="companyId" required>
                <option value="">-- Chọn công ty --</option>
                <c:forEach var="company" items="${companyList}">
                    <option value="${company.companyId}"
                        <c:if test="${trip.transportCompany != null && trip.transportCompany.companyId == company.companyId}">
                            selected
                        </c:if>>
                        ${company.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="vehicleId">Xe (Biển số):</label>
            <select id="vehicleId" name="vehicleId" required>
                <option value="">-- Chọn xe --</option>
                <c:forEach var="vehicle" items="${vehicleList}">
                    <option value="${vehicle.vehicleId}"
                        <c:if test="${trip.vehicleTransport != null && trip.vehicleTransport.vehicleId == vehicle.vehicleId}">
                            selected
                        </c:if>>
                        ${vehicle.licensePlate}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="driverId">Tài xế:</label>
            <select id="driverId" name="driverId" required>
                <option value="">-- Chọn tài xế --</option>
                <c:forEach var="driver" items="${driverList}">
                    <option value="${driver.driverId}"
                        <c:if test="${trip.driverTransport != null && trip.driverTransport.driverId == driver.driverId}">
                            selected
                        </c:if>>
                        ${driver.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Thông tin điểm đi/đến -->
        <div class="form-group">
            <label for="departurePoint">Điểm đi:</label>
            <input type="text" id="departurePoint" name="departurePoint"
                value="${trip.departurePoint != null ? trip.departurePoint : ''}" required />
        </div>

        <div class="form-group">
            <label for="departureCity">Thành phố đi:</label>
            <input type="text" id="departureCity" name="departureCity"
                value="${trip.departureCity != null ? trip.departureCity : ''}" />
        </div>

        <div class="form-group">
            <label for="departureAddress">Địa chỉ đi:</label>
            <input type="text" id="departureAddress" name="departureAddress"
                value="${trip.departureAddress != null ? trip.departureAddress : ''}" />
        </div>

        <div class="form-group">
            <label for="arrivalPoint">Điểm đến:</label>
            <input type="text" id="arrivalPoint" name="arrivalPoint"
                value="${trip.arrivalPoint != null ? trip.arrivalPoint : ''}" required />
        </div>

        <div class="form-group">
            <label for="arrivalCity">Thành phố đến:</label>
            <input type="text" id="arrivalCity" name="arrivalCity"
                value="${trip.arrivalCity != null ? trip.arrivalCity : ''}" />
        </div>

        <div class="form-group">
            <label for="arrivalAddress">Địa chỉ đến:</label>
            <input type="text" id="arrivalAddress" name="arrivalAddress"
                value="${trip.arrivalAddress != null ? trip.arrivalAddress : ''}" />
        </div>

        <div class="form-group">
            <label for="distanceKm">Khoảng cách (Km):</label>
            <input type="number" step="0.1" min="0" name="distanceKm" id="distanceKm"
                value="${trip.distanceKm != null ? trip.distanceKm : ''}" required />
        </div>

        <div class="form-group">
            <label for="departureDate">Ngày đi:</label>
            <input type="date" id="departureDate" name="departureDate"
                value="${trip.departureDate != null ? trip.departureDate : ''}" required />
        </div>

        <div class="form-group">
            <label for="departureTime">Giờ đi:</label>
            <input type="time" id="departureTime" name="departureTime"
                value="${trip.departureTime != null ? trip.departureTime : ''}" required />
        </div>

        <div class="form-group">
            <label for="arrivalDate">Ngày đến:</label>
            <input type="date" id="arrivalDate" name="arrivalDate"
                value="${trip.arrivalDate != null ? trip.arrivalDate : ''}" required />
        </div>

        <div class="form-group">
            <label for="arrivalTime">Giờ đến:</label>
            <input type="time" id="arrivalTime" name="arrivalTime"
                value="${trip.arrivalTime != null ? trip.arrivalTime : ''}" required />
        </div>

        <div class="form-group">
            <label for="price">Giá chuyến (VND):</label>
            <input type="number" step="1000" min="0" id="price" name="price"
                value="${trip.price != null ? trip.price : ''}" required />
        </div>

        <div class="form-group">
            <label for="status">Trạng thái:</label>
            <select id="status" name="status" required>
                <option value="scheduled" <c:if test="${trip.status != null && trip.status.name() == 'scheduled'}">selected</c:if>>Đã lên lịch</option>
                <option value="ongoing" <c:if test="${trip.status != null && trip.status.name() == 'ongoing'}">selected</c:if>>Đang diễn ra</option>
                <option value="completed" <c:if test="${trip.status != null && trip.status.name() == 'completed'}">selected</c:if>>Đã hoàn thành</option>
                <option value="cancelled" <c:if test="${trip.status != null && trip.status.name() == 'cancelled'}">selected</c:if>>Đã hủy</option>
                <option value="delayed" <c:if test="${trip.status != null && trip.status.name() == 'delayed'}">selected</c:if>>Đã trì hoãn</option>
            </select>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Lưu</button>
            <a href="${pageContext.request.contextPath}/trips">Quay lại danh sách</a>
        </div>
    </form>
</div>
