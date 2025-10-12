<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="content-container">
    <h2>Danh sách chuyến xe</h2>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <a href="trips?action=new" class="btn btn-primary mb-3">Thêm chuyến xe mới</a>

    <table class="content-table">
        <thead>
            <tr>
                <th>Công ty</th>
                <th>Xe</th>
                <th>Tài xế</th>
                <th>Điểm đi</th>
                <th>Điểm đến</th>
                <th>Khoảng cách (km)</th>
                <th>Ngày giờ đi</th>
                <th>Ngày giờ đến</th>
                <th>Trạng thái</th>
                <th>Giá chuyến</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="t" items="${tripList}">
                <tr>
                    <td>${t.transportCompany.name}</td>
                    <td>${t.vehicleTransport.licensePlate}</td>
                    <td>${t.driverTransport.name}</td>
                    <td>${t.departurePoint}</td>
                    <td>${t.arrivalPoint}</td>
                    <td>${t.distanceKm}</td>
                    <td>
                        <c:if test="${t.departureDate != null && t.departureTime != null}">
                            ${t.departureDate} ${t.departureTime}
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${t.arrivalDate != null && t.arrivalTime != null}">
                            ${t.arrivalDate} ${t.arrivalTime}
                        </c:if>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${t.status != null}">
                                ${t.status}
                            </c:when>
                            <c:otherwise>
                                Chưa cập nhật
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${t.price != null}">
                            ${t.price}
                        </c:if>
                    </td>
                    <td>
                        <a href="trips?action=edit&id=${t.tripId}" class="btn btn-edit">Sửa</a>
                        <form action="trips" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="tripId" value="${t.tripId}">
                            <button type="submit" class="btn btn-delete"
                                    onclick="return confirm('Bạn có chắc muốn xóa?');">
                                Xóa
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- Pagination -->
    <div class="pagination-container">
        <c:forEach var="i" begin="1" end="${totalPages}">
            <a href="trips?page=${i}" 
               class="pagination-btn ${i == currentPage ? 'active' : ''}">
               ${i}
            </a>
        </c:forEach>
    </div>
</div>
