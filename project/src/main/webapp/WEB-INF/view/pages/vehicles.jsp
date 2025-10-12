<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="com.busbooking.model.TransportCompany" %>
<%
    List<TransportCompany> companyList = (List<TransportCompany>) request.getAttribute("companyList");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <link
            rel="stylesheet"
            href="${pageContext.request.contextPath}/styles/reset.css"
        />
        <link
            rel="stylesheet"
            href="${pageContext.request.contextPath}/styles/style.css"
        />
        <title>admin</title>
    </head>
    <body>
        <jsp:include page="/WEB-INF/view/components/header.jsp" />
        <div class="container">
            <jsp:include page="/WEB-INF/view/components/sidebar.jsp" />
            <main class="main">
                <div class="main-content" id="mainContent">
                    <div class="vehicle">
                        <h1 class="vehicle-title title">Phương tiện</h1>
                        <div class="vehicle-top top">
                            <div class="search-container">
                                <input 
                                    type="text" 
                                    id="searchVehicleInput" 
                                    class="search-input" 
                                    placeholder="Tìm kiếm theo biển số, công ty, loại xe, trạng thái..." 
                                    onkeyup="searchVehicles()"
                                />
                                <svg class="search-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <circle cx="11" cy="11" r="8"></circle>
                                    <path d="m21 21-4.35-4.35"></path>
                                </svg>
                            </div>
                            <label
                                for="addform-checkbox"
                                class="vehicle-top__addbutton addbutton"
                            >
                                Them
                            </label>
                            <input
                                type="checkbox"
                                name="addform"
                                id="addform-checkbox"
                                class="addform-checkbox form-checkbox"
                            />
                            <div class="addform-block form-block">
                                <div class="main-content">
                                    <h2 class="addform__title form__title">Thêm Phương tiện</h2>
                                    <form
                                        class="addform__form form"
                                        action="${pageContext.request.contextPath}/vehicles?action=insert"
                                        method="POST"
                                    >
                                        <div class="form-group">
                                            <label for="company_id">Công ty:</label>
											<select name="company_id" id="company_id" class="form-select" required>
											    <option value="">-- Chọn công ty --</option>
											    <c:forEach var="c" items="${companyList}">
											        <option value="${c.companyId}">${c.name}</option>
											    </c:forEach>
											</select>
                                        </div>

                                        <div class="form-row">
                                            <div class="form-group">
                                                <label for="license_plate">Biển số xe</label>
                                                <input
                                                    type="text"
                                                    name="license_plate"
                                                    id="license_plate"
                                                    class="form-input"
                                                    placeholder="VD: 29A-12345"
                                                    required
                                                />
                                            </div>
                                            <div class="form-group">
                                                <label for="vehicle_capacity">Sức chứa</label>
                                                <input
                                                    type="number"
                                                    name="capacity"
                                                    id="vehicle_capacity"
                                                    class="form-input"
                                                    placeholder="VD: 45"
                                                    min="1"
                                                    required
                                                />
                                            </div>
                                        </div>

                                        <div class="form-row">
                                            <div class="form-group">
                                                <label for="vehicle_type">Loại xe</label>
                                                <input
                                                    type="text"
                                                    name="type"
                                                    id="vehicle_type"
                                                    class="form-input"
                                                    placeholder="VD: Xe giường nằm"
                                                    required
                                                />
                                            </div>
                                            <div class="form-group">
                                                <label for="vehicle_status">Trạng thái</label>
                                                <select name="status" id="vehicle_status" class="form-select" required>
                                                	<option value="">-- Chọn trạng thái --</option>
                                                	<option value="available">Hoạt động</option>
                                                	<option value="maintenance">Bảo trì</option>
                                                	<option value="onTrip">Đang chạy</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="seat_layout">Bố trí ghế</label>
                                            <textarea 
                                                name="seat_layout" 
                                                id="seat_layout" 
                                                class="form-textarea"
                                                placeholder="Mô tả bố trí ghế ngồi (VD: 2-2, ghế giường nằm...)"
                                                rows="3"></textarea>
                                        </div>

                                        <div class="form-group">
                                            <label for="amenities">Tiện nghi</label>
                                            <textarea 
                                                name="amenities" 
                                                id="amenities" 
                                                class="form-textarea"
                                                placeholder="Mô tả các tiện nghi (VD: WiFi, điều hòa, TV...)"
                                                rows="3"></textarea>
                                        </div>

                                        <div class="form-actions">
                                            <input type="submit" value="Thêm mới" class="form-btn" />
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <!-- overlay -->
                            <label
                                for="addform-checkbox"
                                class="addform-overlay overlay"
                            ></label>
                        </div>

                        <table class="vehicle-table table">
                            <thead class="vehicle-table__head table-head">
                                <tr class="vehicle-table__row table-row">
                                    <th>ID</th>
                                    <th>Công ty</th>
                                    <th>Biển số xe</th>
                                    <th>Sức chứa</th>
                                    <th>Loại</th>
                                    <th>Trạng thái</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody class="vehicle-table__body table-body">
                                <c:forEach var="v" items="${vehicleList}">
                                    <tr>
                                        <td>${v.vehicleId}</td>
                                        <td>${v.transportCompany.name}</td>
                                        <td>${v.licensePlate}</td>
                                        <td>${v.capacity}</td>
                                        <td>${v.type}</td>
                                        <td>${v.status}</td>
                                        <td>
                                            <a
                                                onclick="return confirm('Xác nhận xóa phương tiện này?')"
                                                href="${pageContext.request.contextPath}/vehicles?action=delete&vehicle_id=${v.vehicleId}"
                                            >
                                                <button
                                                    class="vehicle-table__deletebutton deletebutton"
                                                >
                                                    Xóa
                                                </button>
                                            </a>
                                            <button type="button"
											        class="vehicle-table__editbutton editbutton"
											        onclick="openEditForm('${v.vehicleId}')">
											    Sửa
											</button>
				                            <input
				                                type="checkbox"
				                                name="editform"
				                                id="editform-checkbox-${v.vehicleId}"
				                                class="editform-checkbox form-checkbox"
				                                style="display: none;"
				                            />
				                            <div class="editform-block form-block" id="editform-block-${v.vehicleId}">
												<div class="main-content">
											    <h2 class="editform__title form__title">Sửa phương tiện</h2>
												    <form id="editForm-${v.vehicleId}"
												          class="editform__form form"
												          action="${pageContext.request.contextPath}/vehicles?action=update"
												          method="POST">
												        <input type="hidden" name="vehicle_id" id="vehicle_id-${v.vehicleId}">
														
														<div class="form-group">
															<label for="company_id-${v.vehicleId}">Công ty:</label>
													    	<select name="company_id" id="company_id-${v.vehicleId}" class="form-select" required>
															    <option value="">-- Chọn công ty --</option>
															    <c:forEach var="c" items="${companyList}">
															        <option value="${c.companyId}"
															        	<c:if test="${v.transportCompany.companyId == c.companyId}">selected</c:if>>
	            														${c.name} 
															        </option>
															    </c:forEach>
															</select>
														</div>

														<div class="form-row">
															<div class="form-group">
												        		<label for="license_plate-${v.vehicleId}">Biển số xe</label>
												        		<input type="text" name="license_plate" id="license_plate-${v.vehicleId}" 
												        			   class="form-input" placeholder="VD: 29A-12345" required>
															</div>
															<div class="form-group">
												        		<label for="capacity-${v.vehicleId}">Sức chứa</label>
												        		<input type="number" name="capacity" id="capacity-${v.vehicleId}" 
												        			   class="form-input" placeholder="VD: 45" min="1" required>
															</div>
														</div>

														<div class="form-row">
															<div class="form-group">
												        		<label for="type-${v.vehicleId}">Loại xe</label>
												        		<input type="text" name="type" id="type-${v.vehicleId}" 
												        			   class="form-input" placeholder="VD: Xe giường nằm" required>
															</div>
															<div class="form-group">
												        		<label for="status-${v.vehicleId}">Trạng thái</label>
												        		<select name="status" id="status-${v.vehicleId}" class="form-select" required>
												        			<option value="available">Hoạt động</option>
												        			<option value="maintenance">Bảo trì</option>
												        			<option value="onTrip">Đang di chuyển</option>
												        		</select>
															</div>
														</div>

														<div class="form-group">
												        	<label for="seat_layout-${v.vehicleId}">Bố trí ghế</label>
												        	<textarea name="seat_layout" id="seat_layout-${v.vehicleId}" 
												        		      class="form-textarea" 
												        		      placeholder="Mô tả bố trí ghế ngồi (VD: 2-2, ghế giường nằm...)"
												        		      rows="3"></textarea>
														</div>

														<div class="form-group">
												        	<label for="amenities-${v.vehicleId}">Tiện nghi</label>
												        	<textarea name="amenities" id="amenities-${v.vehicleId}" 
												        		      class="form-textarea" 
												        		      placeholder="Mô tả các tiện nghi (VD: WiFi, điều hòa, TV...)"
												        		      rows="3"></textarea>
														</div>

														<div class="form-actions">
												        	<input type="submit" value="Cập nhật" class="form-btn">
												        	<button type="button" onclick="closeEditForm('${v.vehicleId}')" 
												        		    class="form-btn cancel">Hủy</button>
														</div>
												    </form>
												</div>
											</div>
				                            <!-- overlay -->
				                            <label
				                                for="editform-checkbox-${v.vehicleId}"
				                                class="editform-overlay overlay"
				                                id="editform-overlay-${v.vehicleId}"
				                            ></label>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <!-- Phân trang -->
                        <c:if test="${totalPages > 1}">
                            <div class="pagination-container">
                                <a href="?action=list&page=1" class="pagination-btn ${currentPage == 1 ? 'disabled' : ''}">&laquo;</a>
                                <a href="?action=list&page=${currentPage - 1}" class="pagination-btn ${currentPage == 1 ? 'disabled' : ''}">&lsaquo;</a>

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
                                    <a href="?action=list&page=${i}" class="pagination-btn ${currentPage == i ? 'active' : ''}">${i}</a>
                                </c:forEach>

                                <a href="?action=list&page=${currentPage + 1}" class="pagination-btn ${currentPage == totalPages ? 'disabled' : ''}">&rsaquo;</a>
                                <a href="?action=list&page=${totalPages}" class="pagination-btn ${currentPage == totalPages ? 'disabled' : ''}">&raquo;</a>

                                <form class="pagination-goto" action="${pageContext.request.contextPath}/vehicles" method="get">
                                    <input type="hidden" name="action" value="list">
                                    <input type="number" name="page" min="1" max="${totalPages}" 
                                           placeholder="${currentPage}/${totalPages}" required
                                           title="Nhập số trang rồi nhấn Enter để đi tới">
                                </form>
                            </div>
                        </c:if>
                    </div>
                </div>
            </main>
        </div>
    </body>
    <script>
	function openEditForm(vehicleId) {
	    // bật checkbox để hiện form
	    const checkbox = document.getElementById('editform-checkbox-' + vehicleId);
	    if (!checkbox) {
	        alert('Không tìm thấy checkbox');
	        return;
	    }
	    checkbox.checked = true;
	
	    // gọi API lấy dữ liệu
	    const apiUrl = '${pageContext.request.contextPath}/vehicles?action=getVehicle&vehicle_id=' + vehicleId;
	    fetch(apiUrl, {headers:{'Accept':'application/json'}})
	      .then(res => res.json())
	      .then(vehicle => {
	          // đổ dữ liệu vào các input
	          document.getElementById('vehicle_id-' + vehicleId).value = vehicle.vehicleId || '';
	          document.getElementById('company_id-' + vehicleId).value = vehicle.transportCompany?.companyId || '';
	          document.getElementById('license_plate-' + vehicleId).value = vehicle.licensePlate || '';
	          document.getElementById('capacity-' + vehicleId).value = vehicle.capacity || '';
	          document.getElementById('type-' + vehicleId).value = vehicle.type || '';
	          document.getElementById('seat_layout-' + vehicleId).value = vehicle.seatLayout || '';
	          document.getElementById('amenities-' + vehicleId).value = vehicle.amenities || '';
	          document.getElementById('status-' + vehicleId).value = vehicle.status || '';
	      })
	      .catch(err => {
	          alert('Lỗi lấy dữ liệu: ' + err.message);
	      });
	}

	function closeEditForm(vehicleId) {
	    // ẩn form = uncheck checkbox
	    const checkbox = document.getElementById('editform-checkbox-' + vehicleId);
	    if (checkbox) checkbox.checked = false;
	}

	// Hàm tìm kiếm phương tiện
	function searchVehicles() {
	    const searchTerm = document.getElementById('searchVehicleInput').value.toLowerCase()
	        .normalize('NFD').replace(/[\u0300-\u036f]/g, ''); // Loại bỏ dấu
	    const tableRows = document.querySelectorAll('.vehicle-table__body tr');
	    
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
	    updateVehicleNoResultsMessage(searchTerm);
	}

	// Hàm hiển thị thông báo không có kết quả
	function updateVehicleNoResultsMessage(searchTerm) {
	    const tableBody = document.querySelector('.vehicle-table__body');
	    const visibleRows = tableBody.querySelectorAll('tr:not([style*="display: none"])');
	    
	    // Xóa thông báo cũ nếu có
	    const existingMessage = document.getElementById('no-vehicle-results-message');
	    if (existingMessage) {
	        existingMessage.remove();
	    }
	    
	    // Nếu không có kết quả và có từ khóa tìm kiếm
	    if (visibleRows.length === 0 && searchTerm !== '') {
	        const noResultsRow = document.createElement('tr');
	        noResultsRow.id = 'no-vehicle-results-message';
	        noResultsRow.innerHTML = `
	            <td colspan="7" style="text-align: center; padding: 20px; color: #666; font-style: italic;">
	                Không tìm thấy phương tiện nào phù hợp với từ khóa "${document.getElementById('searchVehicleInput').value}"
	            </td>
	        `;
	        tableBody.appendChild(noResultsRow);
	    }
	}
	</script>

</html>
