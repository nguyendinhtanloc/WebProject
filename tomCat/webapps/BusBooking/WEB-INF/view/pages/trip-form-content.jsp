<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<div class="content-container">
    <h2>${mode == 'create' ? 'Thêm chuyến xe' : 'Sửa chuyến xe'}</h2>
    
    <c:if test="${not empty errorMessage}">
        <div class="error-message">
            <p>${errorMessage}</p>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/trips" method="post" class="data-form">
        <input type="hidden" name="action" value="${mode}" />

        <c:choose>
            <c:when test="${mode == 'create'}">
                <div class="form-group">
                    <label for="companyId">Công ty:</label>
                    <select id="companyId" name="companyId" required>
                        <option value="">-- Chọn công ty --</option>
                        <c:forEach var="company" items="${companyList}">
                            <option value="${company.companyId}" ${trip.transportCompany != null && trip.transportCompany.companyId == company.companyId ? 'selected' : ''}>
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
                            <option value="${vehicle.vehicleId}" ${trip.vehicleTransport != null && trip.vehicleTransport.vehicleId == vehicle.vehicleId ? 'selected' : ''}>
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
                            <option value="${driver.driverId}" ${trip.driverTransport != null && trip.driverTransport.driverId == driver.driverId ? 'selected' : ''}>
                                ${driver.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </c:when>

            <c:otherwise>
                <div class="form-group">
                    <label>Công ty:</label>
                    <input type="text" value="${trip.transportCompany != null ? trip.transportCompany.name : ''}" readonly />
                    <input type="hidden" name="companyId" value="${trip.transportCompany != null ? trip.transportCompany.companyId : ''}" />
                </div>

                <div class="form-group">
                    <label>Xe (Biển số):</label>
                    <input type="text" value="${trip.vehicleTransport != null ? trip.vehicleTransport.licensePlate : ''}" readonly />
                    <input type="hidden" name="vehicleId" value="${trip.vehicleTransport != null ? trip.vehicleTransport.vehicleId : ''}" />
                </div>

                <div class="form-group">
                    <label>Tài xế:</label>
                    <input type="text" value="${trip.driverTransport != null ? trip.driverTransport.name : ''}" readonly />
                    <input type="hidden" name="driverId" value="${trip.driverTransport != null ? trip.driverTransport.driverId : ''}" />
                </div>
            </c:otherwise>
        </c:choose>

        <div class="form-group">
            <label for="departurePoint">Điểm đi:</label>
            <input type="text" id="departurePoint" name="departurePoint" value="${trip.departurePoint}" required/>
        </div>

        <div class="form-group">
            <label for="arrivalPoint">Điểm đến:</label>
            <input type="text" id="arrivalPoint" name="arrivalPoint" value="${trip.arrivalPoint}" required/>
        </div>

        <!-- Sửa phần formatter -->
        <c:set var="formatter" value="${T(java.time.format.DateTimeFormatter).ofPattern(\"yyyy-MM-dd'T'HH:mm\")}" />
        <div class="form-group">
            <label for="departureDatetime">Ngày & giờ đi:</label>
            <input type="datetime-local" id="departureDatetime" name="departureDatetime" 
                   value="${trip.departureDatetime != null ? trip.departureDatetime.format(formatter) : ''}" required/>
        </div>

        <div class="form-group">
            <label for="arrivalDatetime">Ngày & giờ đến:</label>
            <input type="datetime-local" id="arrivalDatetime" name="arrivalDatetime" 
                   value="${trip.arrivalDatetime != null ? trip.arrivalDatetime.format(formatter) : ''}" required/>
        </div>

        <div class="form-group">
            <label for="status">Trạng thái:</label>
            <select id="status" name="status">
                <option value="SCHEDULED" ${trip.status == 'SCHEDULED' ? 'selected' : ''}>Đã lên lịch</option>
                <option value="ON_GOING" ${trip.status == 'ON_GOING' ? 'selected' : ''}>Đang diễn ra</option>
                <option value="COMPLETED" ${trip.status == 'COMPLETED' ? 'selected' : ''}>Đã hoàn thành</option>
                <option value="CANCELLED" ${trip.status == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
            </select>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Lưu</button>
            <a href="${pageContext.request.contextPath}/trips">Quay lại danh sách</a>
        </div>
    </form>
</div>
