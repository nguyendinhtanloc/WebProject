<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="trip">
    <h1 class="trip-title title">Danh sách chuyến xe</h1>
    <div class="trip-top top">
        <a href="${pageContext.request.contextPath}/trips?action=new" class="trip-top__addbutton addbutton">Thêm chuyến</a>
    </div>

    <table class="trip-table table">
        <thead class="trip-table__head table-head">
            <tr class="trip-table__row table-row">
                <th>Công ty</th>
                <th>Biển số xe</th>
                <th>Tài xế</th>
                <th>Nơi đi</th>
                <th>Nơi đến</th>
                <th>Ngày đi</th>
                <th>Giờ đi</th>
                <th>Giá</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody class="trip-table__body table-body">
            <c:forEach var="t" items="${tripList}">
                <tr>
                    <td>${t.transportCompany.name}</td>
                    <td>${t.vehicleTransport.licensePlate}</td>
                    <td>${t.driverTransport.name}</td>
                    <td>${t.departurePoint}</td>
                    <td>${t.arrivalPoint}</td>
                    <td>${t.departureDatetime != null ? t.departureDatetime.toLocalDate() : ''}</td>
                    <td>${t.departureDatetime != null ? t.departureDatetime.toLocalTime() : ''}</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty t.seats}">
                                <c:set var="minPrice" value="${t.seats[0].price}" />
                                <c:forEach var="s" items="${t.seats}">
                                    <c:if test="${s.price < minPrice}">
                                        <c:set var="minPrice" value="${s.price}" />
                                    </c:if>
                                </c:forEach>
                                ${minPrice}
                            </c:when>
                            <c:otherwise>
                                Chưa có
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${t.status}</td>
                    <td style="display: flex; gap: 5px; align-items: center;">
                        <form action="${pageContext.request.contextPath}/trips" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete"/>
                            <input type="hidden" name="tripId" value="${t.tripId}"/>
                            <button type="submit" class="trip-table__deletebutton deletebutton" onclick="return confirm('Xóa chuyến này?')">Xóa</button>
                        </form>
                        <a href="${pageContext.request.contextPath}/trips?action=edit&id=${t.tripId}" class="trip-table__editbutton editbutton">Sửa</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty tripList}">
                <tr>
                    <td colspan="10" style="text-align:center; padding: 20px;">Không có dữ liệu để hiển thị.</td>
                </tr>
            </c:if>
        </tbody>
    </table>

    <c:if test="${totalPages > 1}">
        <div class="pagination-container">
            <a href="?page=1" class="pagination-btn ${currentPage == 1 ? 'disabled' : ''}">&laquo;</a>
            <a href="?page=${currentPage - 1}" class="pagination-btn ${currentPage == 1 ? 'disabled' : ''}">&lsaquo;</a>

            <c:set var="startPage" value="${currentPage - 2}" />
            <c:set var="endPage" value="${currentPage + 2}" />
            <c:if test="${startPage < 1}">
                <c:set var="endPage" value="${endPage + (1 - startPage)}" />
                <c:set var="startPage" value="1" />
            </c:if>
            <c:if test="${endPage > totalPages}">
                <c:set var="startPage" value="${startPage - (endPage - totalPages)}" />
                <c:set var="endPage" value="${totalPages}" />
            </c:if>
            <c:if test="${startPage < 1}"><c:set var="startPage" value="1" /></c:if>

            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                <a href="?page=${i}" class="pagination-btn ${currentPage == i ? 'active' : ''}">${i}</a>
            </c:forEach>

            <a href="?page=${currentPage + 1}" class="pagination-btn ${currentPage == totalPages ? 'disabled' : ''}">&rsaquo;</a>
            <a href="?page=${totalPages}" class="pagination-btn ${currentPage == totalPages ? 'disabled' : ''}">&raquo;</a>

            <form class="pagination-goto" action="${pageContext.request.contextPath}/trips" method="get">
                <input type="number" name="page" min="1" max="${totalPages}" 
                       placeholder="${currentPage}/${totalPages}" required
                       title="Nhập số trang rồi nhấn Enter để đi tới">
            </form>
        </div>
    </c:if>
</div>
