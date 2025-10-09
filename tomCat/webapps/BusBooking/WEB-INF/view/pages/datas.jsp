<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="content-container">
    <h2>Lịch sử Thay đổi Chuyến xe</h2>

    <table class="content-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Hành động</th>
                <th>Người thực hiện</th>
                <th>Thời gian</th>
                <th>Trạng thái cũ</th>
                <th>Trạng thái mới</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="log" items="${logList}">
                <tr>
                    <td>${log.logId}</td>
                    <td>
                        <c:choose>
                            <c:when test="${log.oldStatus == null}">INSERT</c:when>
                            <c:otherwise>UPDATE</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:out value="${log.changedBy != null ? log.changedBy.name : 'Unknown'}"/>
                    </td>
                    <td>
                        <fmt:formatDate value="${log.changedAt}" pattern="HH:mm:ss dd/MM/yyyy"/>
                    </td>
                    <td>${log.oldStatus != null ? log.oldStatus : '-'}</td>
                    <td>${log.newStatus != null ? log.newStatus : '-'}</td>
                </tr>
            </c:forEach>

            <c:if test="${empty logList}">
                <tr>
                    <td colspan="6" style="text-align:center; padding:20px;">Không có dữ liệu.</td>
                </tr>
            </c:if>
        </tbody>
    </table>

    <c:if test="${totalPages > 1}">
        <div class="pagination-container">
            <a href="?page=1" class="pagination-btn ${currentPage == 1 ? 'disabled' : ''}">&laquo;</a>
            <a href="?page=${currentPage - 1}" class="pagination-btn ${currentPage == 1 ? 'disabled' : ''}">&lsaquo;</a>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="?page=${i}" class="pagination-btn ${currentPage == i ? 'active' : ''}">${i}</a>
            </c:forEach>

            <a href="?page=${currentPage + 1}" class="pagination-btn ${currentPage == totalPages ? 'disabled' : ''}">&rsaquo;</a>
            <a href="?page=${totalPages}" class="pagination-btn ${currentPage == totalPages ? 'disabled' : ''}">&raquo;</a>
        </div>
    </c:if>
</div>
