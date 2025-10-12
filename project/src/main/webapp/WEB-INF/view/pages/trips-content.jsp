<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="trip">
    <h1 class="trip-title title">Danh sách chuyến xe</h1>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="trip-top top">
        <div class="search-container">
            <input 
                type="text" 
                id="searchTripInput" 
                class="search-input" 
                placeholder="Tìm kiếm theo công ty, biển số, tài xế, điểm đi/đến, trạng thái..." 
                onkeyup="searchTrips()"
            />
            <svg class="search-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="11" cy="11" r="8"></circle>
                <path d="m21 21-4.35-4.35"></path>
            </svg>
        </div>
        <a href="trips?action=new" class="trip-top__addbutton addbutton">Thêm chuyến xe mới</a>
    </div>

    <table class="trip-table table">
        <thead class="trip-table__head table-head">
            <tr class="trip-table__row table-row">
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
        <tbody class="trip-table__body table-body">
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
                        <form action="trips" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="tripId" value="${t.tripId}">
                            <button type="submit" class="trip-table__deletebutton deletebutton"
                                    onclick="return confirm('Bạn có chắc muốn xóa?');">
                                Xóa
                            </button>
                        </form>
                        <a href="trips?action=edit&id=${t.tripId}" class="trip-table__editbutton editbutton">Sửa</a>
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

<script>
    // Hàm tìm kiếm chuyến xe
    function searchTrips() {
        const searchTerm = document.getElementById('searchTripInput').value.toLowerCase()
            .normalize('NFD').replace(/[\u0300-\u036f]/g, ''); // Loại bỏ dấu
        const tableRows = document.querySelectorAll('.trip-table__body tr');
        
        tableRows.forEach(row => {
            const cells = row.querySelectorAll('td');
            let found = false;
            
            // Tìm kiếm trong tất cả các cột (trừ cột cuối là hành động)
            for (let i = 0; i < cells.length - 1; i++) {
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
        updateTripNoResultsMessage(searchTerm);
    }

    // Hàm hiển thị thông báo không có kết quả
    function updateTripNoResultsMessage(searchTerm) {
        const tableBody = document.querySelector('.trip-table__body');
        const visibleRows = tableBody.querySelectorAll('tr:not([style*="display: none"])');
        
        // Xóa thông báo cũ nếu có
        const existingMessage = document.getElementById('no-trip-results-message');
        if (existingMessage) {
            existingMessage.remove();
        }
        
        // Nếu không có kết quả và có từ khóa tìm kiếm
        if (visibleRows.length === 0 && searchTerm !== '') {
            const noResultsRow = document.createElement('tr');
            noResultsRow.id = 'no-trip-results-message';
            noResultsRow.innerHTML = `
                <td colspan="11" style="text-align: center; padding: 20px; color: #666; font-style: italic;">
                    Không tìm thấy chuyến xe nào phù hợp với từ khóa "${document.getElementById('searchTripInput').value}"
                </td>
            `;
            tableBody.appendChild(noResultsRow);
        }
    }
</script>
