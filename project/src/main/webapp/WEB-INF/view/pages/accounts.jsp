<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="account">
    <h1 class="driver-title title">Lịch sử đăng nhập hệ thống</h1>
    <div class="account-top top">
        <div class="search-container">
            <input 
                type="text" 
                id="searchAccountInput" 
                class="search-input" 
                placeholder="Tìm kiếm theo tên, email, IP, trình duyệt, trạng thái..." 
                onkeyup="searchAccounts()"
            />
            <svg class="search-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="11" cy="11" r="8"></circle>
                <path d="m21 21-4.35-4.35"></path>
            </svg>
        </div>
    </div>

    <table class="account-table table">
        <thead class="account-table__head table-head">
            <tr class="account-table__row table-row">
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
        <tbody class="account-table__body table-body">
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

<script>
    // Hàm tìm kiếm lịch sử đăng nhập
    function searchAccounts() {
        const searchTerm = document.getElementById('searchAccountInput').value.toLowerCase()
            .normalize('NFD').replace(/[\u0300-\u036f]/g, ''); // Loại bỏ dấu
        const tableRows = document.querySelectorAll('.account-table__body tr');
        
        tableRows.forEach(row => {
            // Bỏ qua dòng "Không có bản ghi" nếu có
            if (row.querySelector('td[colspan]')) {
                return;
            }
            
            const cells = row.querySelectorAll('td');
            let found = false;
            
            // Tìm kiếm trong tất cả các cột
            for (let i = 0; i < cells.length; i++) {
                const cellText = cells[i].textContent.toLowerCase()
                    .normalize('NFD').replace(/[\u0300-\u036f]/g, ''); // Loại bỏ dấu
                
                if (cellText.includes(searchTerm)) {
                    found = true;
                    break;
                }
            }
            
            // Hiển thị hoặc ẩn dòng
            if (found || searchTerm === '') {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
        
        // Ẩn/hiện thông báo không tìm thấy kết quả
        updateAccountNoResultsMessage(searchTerm);
    }

    // Hàm hiển thị thông báo không có kết quả
    function updateAccountNoResultsMessage(searchTerm) {
        const tableBody = document.querySelector('.account-table__body');
        const visibleRows = tableBody.querySelectorAll('tr:not([style*="display: none"]):not([id="no-account-results-message"])');
        const emptyRow = tableBody.querySelector('td[colspan]')?.parentElement;
        
        // Xóa thông báo cũ nếu có
        const existingMessage = document.getElementById('no-account-results-message');
        if (existingMessage) {
            existingMessage.remove();
        }
        
        // Nếu không có kết quả và có từ khóa tìm kiếm
        if (visibleRows.length === 0 && searchTerm !== '') {
            // Ẩn dòng "Không có bản ghi" ban đầu nếu có
            if (emptyRow) {
                emptyRow.style.display = 'none';
            }
            
            const noResultsRow = document.createElement('tr');
            noResultsRow.id = 'no-account-results-message';
            noResultsRow.innerHTML = `
                <td colspan="8" style="text-align: center; padding: 20px; color: #666; font-style: italic;">
                    Không tìm thấy bản ghi đăng nhập nào phù hợp với từ khóa "${document.getElementById('searchAccountInput').value}"
                </td>
            `;
            tableBody.appendChild(noResultsRow);
        } else {
            // Hiện lại dòng "Không có bản ghi" ban đầu nếu cần
            if (emptyRow && searchTerm === '') {
                emptyRow.style.display = '';
            }
        }
    }
</script>
