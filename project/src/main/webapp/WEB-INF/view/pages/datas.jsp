<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="data">
    <h1 class="data-title title">Lịch sử Thay đổi Chuyến xe</h1>
    <div class="data top"></div>
    <table class="data-table table">
        <thead class="data-table__head table-head">
            <tr class="data-table__row table-row">
                <th>ID</th>
                <th>Hành động</th>
                <th>Người thực hiện</th>
                <th>Thời gian</th>
                <th>Nội dung cũ</th>
                <th>Nội dung mới</th>
            </tr>
        </thead>
        <tbody class="data-table__body table-body">
            <c:forEach var="log" items="${logList}">
                <tr>
                    <td>${log.logId}</td>
                    <td>
                        <c:choose>
                            <c:when test="${log.type == 'insert'}"
                                >INSERT</c:when
                            >
                            <c:when test="${log.type == 'update'}"
                                >UPDATE</c:when
                            >
                            <c:when test="${log.type == 'delete'}"
                                >DELETE</c:when
                            >
                            <c:otherwise>UNKNOWN</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:out
                            value="${log.changedBy != null ? log.changedBy.name : 'Unknown'}"
                        />
                    </td>
                    <td>
                        <c:if test="${log.changedAt != null}">
                            ${log.changedAt.toString().substring(0,16).replace('T','
                            ')}
                        </c:if>
                    </td>
                    <td>
                        <c:out
                            value="${log.oldContent != null ? log.oldContent : '-'}"
                        />
                    </td>
                    <td>
                        <c:out
                            value="${log.newContent != null ? log.newContent : '-'}"
                        />
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty logList}">
                <tr>
                    <td colspan="5" style="text-align: center; padding: 20px">
                        Không có dữ liệu.
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>

    <c:if test="${totalPages > 1}">
        <div class="pagination-container">
            <a
                href="?page=1"
                class="pagination-btn ${currentPage == 1 ? 'disabled' : ''}"
                >&laquo;</a
            >
            <a
                href="?page=${currentPage - 1}"
                class="pagination-btn ${currentPage == 1 ? 'disabled' : ''}"
                >&lsaquo;</a
            >

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a
                    href="?page=${i}"
                    class="pagination-btn ${currentPage == i ? 'active' : ''}"
                    >${i}</a
                >
            </c:forEach>

            <a
                href="?page=${currentPage + 1}"
                class="pagination-btn ${currentPage == totalPages ? 'disabled' : ''}"
                >&rsaquo;</a
            >
            <a
                href="?page=${totalPages}"
                class="pagination-btn ${currentPage == totalPages ? 'disabled' : ''}"
                >&raquo;</a
            >
        </div>
    </c:if>
</div>
