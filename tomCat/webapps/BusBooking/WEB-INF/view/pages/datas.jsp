<!-- <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="content-container">
    <h2>Lịch sử thao tác dữ liệu chuyến</h2>

    <table class="content-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Hành động</th>
                <th>Bảng</th>
                <th>Dữ liệu cũ</th>
                <th>Dữ liệu mới</th>
                <th>Email</th>
                <th>Thời gian</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="log" items="${logs}">
                <tr>
                    <td>${log.id}</td>
                    <td><b>${log.action}</b></td>
                    <td>${log.tableName}</td>
                    <td><pre>${log.oldData}</pre></td>
                    <td><pre>${log.newData}</pre></td>
                    <td>${log.email}</td>
                    <td>${log.actionTimeFormatted}</td>
                </tr>
            </c:forEach>

            <c:if test="${empty logs}">
                <tr>
                    <td colspan="7" style="text-align:center; padding:20px;">
                        Không có dữ liệu log.
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table> -->

    <!-- Phân trang -->
    <!-- <c:if test="${totalPages > 1}">
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
</div> -->
