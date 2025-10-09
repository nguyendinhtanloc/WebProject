<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="content-container">
    <h2>Lịch sử thay đổi trạng thái đăng nhập</h2>

    <table class="content-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Người thay đổi</th>
                <th>Trạng thái cũ</th>
                <th>Trạng thái mới</th>
                <th>Thời gian thay đổi</th>
                <th>Ghi chú</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="log" items="${logs}">
                <tr>
                    <td>${log.id}</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty log.changedBy}">
                                ${log.changedBy.name} (${log.changedBy.email})
                            </c:when>
                            <c:otherwise>
                                <i>Hệ thống</i>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${log.oldStatus}</td>
                    <td>
                        <c:choose>
                            <c:when test="${log.newStatus eq 'success'}">
                                <span style="color: green; font-weight: bold;">${log.newStatus}</span>
                            </c:when>
                            <c:when test="${log.newStatus eq 'failed'}">
                                <span style="color: red; font-weight: bold;">${log.newStatus}</span>
                            </c:when>
                            <c:otherwise>
                                ${log.newStatus}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${log.changedAt}</td>
                    <td style="max-width: 250px; word-wrap: break-word;">${log.note}</td>
                </tr>
            </c:forEach>

            <c:if test="${empty logs}">
                <tr>
                    <td colspan="6" style="text-align:center; padding:20px;">
                        Không có bản ghi thay đổi trạng thái nào.
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>

    <!-- Phân trang -->
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

            <form class="pagination-goto" action="${pageContext.request.contextPath}/accounts" method="get">
                <input type="number" name="page" min="1" max="${totalPages}" 
                       placeholder="${currentPage}/${totalPages}" required
                       title="Nhập số trang rồi nhấn Enter để đi tới">
            </form>
        </div>
    </c:if>
</div>
