<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="content-container">
    <h2>${mode == 'create' ? 'Thêm chuyến xe' : 'Sửa chuyến xe'}</h2>
    
    <c:if test="${not empty errorMessage}">
        <div class="error-message">
            <p>${errorMessage}</p>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/trips" method="post" class="data-form">
        <input type="hidden" name="action" value="${mode}" />
        
        <%-- ======================== PHẦN ĐƯỢC CHỈNH SỬA ======================== --%>
        
        <%-- Luôn gửi tripId khi Sửa, nhưng dưới dạng ẩn --%>
        <c:if test="${mode == 'edit'}">
            <input type="hidden" name="tripId" value="${trip.tripId}" />
        </c:if>

        <c:if test="${mode == 'create'}">
            <%-- Khi Thêm mới, hiển thị các ô chọn --%>
            <div class="form-group">
                <label for="companyId">Công ty:</label>
                <select id="companyId" name="companyId" required>
                    <option value="">-- Chọn công ty --</option>
                    <c:forEach var="company" items="${companyList}">
                        <option value="${company.companyId}" ${trip.companyId == company.companyId ? 'selected' : ''}>${company.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="vehicleId">Xe (Biển số):</label>
                <select id="vehicleId" name="vehicleId" required>
                    <option value="">-- Chọn xe --</option>
                    <c:forEach var="vehicle" items="${vehicleList}">
                        <option value="${vehicle.vehicleId}" ${trip.vehicleId == vehicle.vehicleId ? 'selected' : ''}>${vehicle.licensePlate}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="driverId">Tài xế:</label>
                <select id="driverId" name="driverId" required>
                    <option value="">-- Chọn tài xế --</option>
                    <c:forEach var="driver" items="${driverList}">
                        <option value="${driver.driverId}" ${trip.driverId == driver.driverId ? 'selected' : ''}>${driver.name}</option>
                    </c:forEach>
                </select>
            </div>
        </c:if>

        <c:if test="${mode == 'edit'}">
            <%-- Khi Sửa, hiển thị tên dưới dạng không thể sửa (readonly) --%>
            <div class="form-group">
                <label>Công ty:</label>
                <input type="text" value="${trip.companyName}" readonly />
            </div>
            <div class="form-group">
                <label>Xe (Biển số):</label>
                <input type="text" value="${trip.vehicleLicensePlate}" readonly />
            </div>
            <div class="form-group">
                <label>Tài xế:</label>
                <input type="text" value="${trip.driverName}" readonly />
            </div>
            <%-- Vẫn gửi các ID đi dưới dạng ẩn để logic update hoạt động --%>
            <input type="hidden" name="companyId" value="${trip.companyId}" />
            <input type="hidden" name="vehicleId" value="${trip.vehicleId}" />
            <input type="hidden" name="driverId" value="${trip.driverId}" />
        </c:if>
        <%-- ====================== KẾT THÚC PHẦN CHỈNH SỬA ====================== --%>

        <div class="form-group">
            <label for="departurePlace">Nơi đi:</label>
            <input type="text" id="departurePlace" name="departurePlace" value="${trip.departurePlace}" required/>
        </div>

        <div class="form-group">
            <label for="arrivalPlace">Nơi đến:</label>
            <input type="text" id="arrivalPlace" name="arrivalPlace" value="${trip.arrivalPlace}" required/>
        </div>

        <div class="form-group">
            <label for="departureDate">Ngày đi:</label>
            <input type="date" id="departureDate" name="departureDate" value="${trip.departureDate}" required/>
        </div>

        <div class="form-group">
            <label for="departureTime">Giờ đi:</label>
            <input type="time" id="departureTime" name="departureTime" value="${trip.departureTime}" required/>
        </div>

        <div class="form-group">
            <label for="price">Giá vé:</label>
            <input type="number" id="price" min="0" step="1" name="price" value="${trip.price}" required/>
        </div>

        <div class="form-group">
            <label for="status">Trạng thái:</label>
            <select id="status" name="status">
                <option value="scheduled" ${trip.status == 'scheduled' ? 'selected' : ''}>Đã lên lịch</option>
                <option value="on_going" ${trip.status == 'on_going' ? 'selected' : ''}>Đang diễn ra</option>
                <option value="completed" ${trip.status == 'completed' ? 'selected' : ''}>Đã hoàn thành</option>
                <option value="cancelled" ${trip.status == 'cancelled' ? 'selected' : ''}>Đã hủy</option>
            </select>
        </div>
        
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Lưu</button>
            <a href="${pageContext.request.contextPath}/trips">Quay lại danh sách</a>
        </div>
    </form>
</div>