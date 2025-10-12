<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="content-container">
    <h2>Lịch sử đăng nhập hệ thống</h2>

    <table class="content-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Người dùng</th>
                <th>Thời gian đăng nhập</th>
                <th>Thời gian đăng xuất</th>
                <th>Địa chỉ IP</th>
                <th>Trình duyệt</th>
                <th>Trạng thái</th>
                <th>Ghi chú</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="log" items="${logs}">
                <tr>
                    <td>${log.logId}</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty log.user}">
                                ${log.user.name} (${log.user.email})
                            </c:when>
                            <c:otherwise>
                                <i>Không xác định</i>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${log.loginTime}</td>
                    <td>${log.logoutTime}</td>
                    <td>${log.ipAddress}</td>
                    <td>${log.userAgent}</td>
                    <td>
                        <c:choose>
                            <c:when test="${log.status eq 'success'}">
                                <span style="color: green; font-weight: bold;">${log.status}</span>
                            </c:when>
                            <c:when test="${log.status eq 'failed'}">
                                <span style="color: red; font-weight: bold;">${log.status}</span>
                            </c:when>
                            <c:otherwise>
                                ${log.status}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td style="max-width: 250px; word-wrap: break-word;">${log.description}</td>
                </tr>
            </c:forEach>

            <c:if test="${empty logs}">
                <tr>
                    <td colspan="8" style="text-align:center; padding:20px;">
                        Không có bản ghi đăng nhập nào.
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
