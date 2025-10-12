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
                    <div class="driver">
                        <h1 class="driver-title title">Tài xế</h1>
                        <div class="driver-top top">
                            <div class="search-container">
                                <input 
                                    type="text" 
                                    id="searchInput" 
                                    class="search-input" 
                                    placeholder="Tìm kiếm theo tên, công ty, SĐT, bằng lái..." 
                                    onkeyup="searchDrivers()"
                                />
                                <svg class="search-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <circle cx="11" cy="11" r="8"></circle>
                                    <path d="m21 21-4.35-4.35"></path>
                                </svg>
                            </div>
                            <label
                                for="addform-checkbox"
                                class="driver-top__addbutton addbutton"
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
                                    <h2 class="addform__title form__title">Thêm Tài xế</h2>
                                    <form
                                        class="addform__form form"
                                        action="${pageContext.request.contextPath}/drivers?action=insert"
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
                                                <label for="driver_name">Tên tài xế</label>
                                                <input
                                                    type="text"
                                                    name="name"
                                                    id="driver_name"
                                                    class="form-input"
                                                    required
                                                />
                                            </div>
                                            <div class="form-group">
                                                <label for="driver_phone">Số điện thoại</label>
                                                <input
                                                    type="text"
                                                    name="phone"
                                                    id="driver_phone"
                                                    class="form-input"
                                                />
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="license_no">Số bằng lái</label>
                                            <input
                                                type="text"
                                                name="license_no"
                                                id="license_no"
                                                class="form-input"
                                            />
                                        </div>

                                        <div class="form-row">
                                            <div class="form-group">
                                                <label for="hire_date">Ngày thuê</label>
                                                <input type="date" name="hire_date" id="hire_date" class="form-input" />
                                            </div>
                                            <div class="form-group">
                                                <label for="end_date">Ngày kết thúc</label>
                                                <input type="date" name="end_date" id="end_date" class="form-input" />
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="driver_status">Trạng thái</label>
                                            <select name="status" id="driver_status" class="form-select" required>
                                            	<option value="">-- Chọn trạng thái --</option>
                                            	<option value="active">Hoạt động</option>
                                            	<option value="inactive">Không hoạt động</option>
                                            	<option value="suspended">Tạm ngưng</option>
                                            </select>
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

                        <table class="driver-table table">
                            <thead class="driver-table__head table-head">
                                <tr class="driver-table__row table-row">
                                    <th>ID</th>
                                    <th>Công ty</th>
                                    <th>Tên tài xế</th>
                                    <th>Số điện thoại</th>
                                    <th>Số bằng lái</th>
                                    <th>Ngày thuê</th>
                                    <th>Trạng thái</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody class="driver-table__body table-body">
                                <c:forEach var="d" items="${driverList}">
                                    <tr>
                                        <td>${d.driverId}</td>
                                        <td>${d.transportCompany.name}</td>
                                        <td>${d.name}</td>
                                        <td>${d.phone}</td>
                                        <td>${d.licenseNo}</td>
                                        <td>${d.hireDate}</td>
                                        <td>${d.status}</td>
                                        <td>
                                            <a
                                                onclick="return confirm('Xác nhận xóa tài xế này?')"
                                                href="${pageContext.request.contextPath}/drivers?action=delete&driver_id=${d.driverId}"
                                            >
                                                <button
                                                    class="driver-table__deletebutton deletebutton"
                                                >
                                                    Xóa
                                                </button>
                                            </a>
                                            <button type="button"
											        class="driver-table__editbutton editbutton"
											        onclick="openEditForm('${d.driverId}')">
											    Sửa
											</button>
				                            <input
				                                type="checkbox"
				                                name="editform"
				                                id="editform-checkbox-${d.driverId}"
				                                class="editform-checkbox form-checkbox"
				                                style="display: none;"
				                            />
				                            <div class="editform-block form-block" id="editform-block-${d.driverId}">
												<div class="main-content">
											    <h2 class="editform__title form__title">Sửa tài xế</h2>
												    <form id="editForm-${d.driverId}"
												          class="editform__form form"
												          action="${pageContext.request.contextPath}/drivers?action=update"
												          method="POST">
												        <input type="hidden" name="driver_id" id="driver_id-${d.driverId}">
														
														<div class="form-group">
															<label for="company_id-${d.driverId}">Công ty:</label>
													    	<select name="company_id" id="company_id-${d.driverId}" class="form-select" required>
															    <option value="">-- Chọn công ty --</option>
															    <c:forEach var="c" items="${companyList}">
															        <option value="${c.companyId}"
															        	<c:if test="${d.transportCompany.companyId == c.companyId}">selected</c:if>>
	            														${c.name} 
															        </option>
															    </c:forEach>
															</select>
														</div>

														<div class="form-row">
															<div class="form-group">
												        		<label for="name-${d.driverId}">Tên tài xế</label>
												        		<input type="text" name="name" id="name-${d.driverId}" class="form-input" required>
															</div>
															<div class="form-group">
												        		<label for="phone-${d.driverId}">Số điện thoại</label>
												        		<input type="text" name="phone" id="phone-${d.driverId}" class="form-input">
															</div>
														</div>

														<div class="form-group">
												        	<label for="license_no-${d.driverId}">Số bằng lái</label>
												        	<input type="text" name="license_no" id="license_no-${d.driverId}" class="form-input">
														</div>

														<div class="form-row">
															<div class="form-group">
												        		<label for="hire_date-${d.driverId}">Ngày thuê</label>
												        		<input type="date" name="hire_date" id="hire_date-${d.driverId}" class="form-input">
															</div>
															<div class="form-group">
												        		<label for="end_date-${d.driverId}">Ngày kết thúc</label>
												        		<input type="date" name="end_date" id="end_date-${d.driverId}" class="form-input">
															</div>
														</div>

														<div class="form-group">
												        	<label for="status-${d.driverId}">Trạng thái</label>
												        	<select name="status" id="status-${d.driverId}" class="form-select" required>
												        		<option value="active">Hoạt động</option>
												        		<option value="inactive">Không hoạt động</option>
												        		<option value="suspended">Tạm ngưng</option>
												        	</select>
														</div>

														<div class="form-actions">
												        	<input type="submit" value="Cập nhật" class="form-btn">
												        	<button type="button" onclick="closeEditForm('${d.driverId}')" class="form-btn cancel">Hủy</button>
														</div>
												    </form>
												</div>
											</div>
				                            <!-- overlay -->
				                            <label
				                                for="editform-checkbox-${d.driverId}"
				                                class="editform-overlay overlay"
				                                id="editform-overlay-${d.driverId}"
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

                                <form class="pagination-goto" action="${pageContext.request.contextPath}/drivers" method="get">
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
	function openEditForm(driverId) {
	    // bật checkbox để hiện form
	    const checkbox = document.getElementById('editform-checkbox-' + driverId);
	    if (!checkbox) {
	        alert('Không tìm thấy checkbox');
	        return;
	    }
	    checkbox.checked = true;
	
	    // gọi API lấy dữ liệu
	    const apiUrl = '${pageContext.request.contextPath}/drivers?action=getDriver&driver_id=' + driverId;
	    fetch(apiUrl, {headers:{'Accept':'application/json'}})
	      .then(res => res.json())
	      .then(driver => {
	          // đổ dữ liệu vào các input
	          document.getElementById('driver_id-' + driverId).value = driver.driverId || '';
	          document.getElementById('company_id-' + driverId).value = driver.transportCompany?.companyId || '';
	          document.getElementById('name-' + driverId).value = driver.name || '';
	          document.getElementById('phone-' + driverId).value = driver.phone || '';
	          document.getElementById('license_no-' + driverId).value = driver.licenseNo || '';
	          document.getElementById('hire_date-' + driverId).value = driver.hireDate || '';
	          document.getElementById('end_date-' + driverId).value = driver.endDate || '';
	          document.getElementById('status-' + driverId).value = driver.status || '';
	      })
	      .catch(err => {
	          alert('Lỗi lấy dữ liệu: ' + err.message);
	      });
	}

	function closeEditForm(driverId) {
	    // ẩn form = uncheck checkbox
	    const checkbox = document.getElementById('editform-checkbox-' + driverId);
	    if (checkbox) checkbox.checked = false;
	}

	// Hàm tìm kiếm tài xế
	function searchDrivers() {
	    const searchTerm = document.getElementById('searchInput').value.toLowerCase()
	        .normalize('NFD').replace(/[\u0300-\u036f]/g, ''); // Loại bỏ dấu
	    const tableRows = document.querySelectorAll('.driver-table__body tr');
	    
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
	    updateNoResultsMessage(searchTerm);
	}

	// Hàm hiển thị thông báo không có kết quả
	function updateNoResultsMessage(searchTerm) {
	    const tableBody = document.querySelector('.driver-table__body');
	    const visibleRows = tableBody.querySelectorAll('tr:not([style*="display: none"])');
	    
	    // Xóa thông báo cũ nếu có
	    const existingMessage = document.getElementById('no-results-message');
	    if (existingMessage) {
	        existingMessage.remove();
	    }
	    
	    // Nếu không có kết quả và có từ khóa tìm kiếm
	    if (visibleRows.length === 0 && searchTerm !== '') {
	        const noResultsRow = document.createElement('tr');
	        noResultsRow.id = 'no-results-message';
	        noResultsRow.innerHTML = `
	            <td colspan="8" style="text-align: center; padding: 20px; color: #666; font-style: italic;">
	                Không tìm thấy tài xế nào phù hợp với từ khóa "${document.getElementById('searchInput').value}"
	            </td>
	        `;
	        tableBody.appendChild(noResultsRow);
	    }
	}
	</script>

</html>
