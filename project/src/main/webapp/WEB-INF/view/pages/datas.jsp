<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="data">
    <h1 class="data-title title">Lịch sử Thay đổi Chuyến xe</h1>
    <div class="data-top top">
        <div class="search-container">
            <input
                type="text"
                id="searchDataInput"
                class="search-input"
                placeholder="Tìm kiếm theo hành động, người thực hiện, nội dung..."
                onkeyup="searchData()"
            />
            <svg
                class="search-icon"
                width="20"
                height="20"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
            >
                <circle cx="11" cy="11" r="8"></circle>
                <path d="m21 21-4.35-4.35"></path>
            </svg>
        </div>
    </div>
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

<script>
    // Hàm tìm kiếm lịch sử thay đổi
    function searchData() {
        const searchTerm = document
            .getElementById("searchDataInput")
            .value.toLowerCase()
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, ""); // Loại bỏ dấu
        const tableRows = document.querySelectorAll(".data-table__body tr");

        tableRows.forEach((row) => {
            // Bỏ qua dòng "Không có dữ liệu" nếu có
            if (row.querySelector("td[colspan]")) {
                return;
            }

            const cells = row.querySelectorAll("td");
            let found = false;

            // Tìm kiếm trong tất cả các cột
            for (let i = 0; i < cells.length; i++) {
                const cellText = cells[i].textContent
                    .toLowerCase()
                    .normalize("NFD")
                    .replace(/[\u0300-\u036f]/g, ""); // Loại bỏ dấu

                if (cellText.includes(searchTerm)) {
                    found = true;
                    break;
                }
            }

            // Hiển thị hoặc ẩn dòng
            if (found || searchTerm === "") {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });

        // Ẩn/hiện thông báo không tìm thấy kết quả
        updateDataNoResultsMessage(searchTerm);
    }

    // Hàm hiển thị thông báo không có kết quả
    function updateDataNoResultsMessage(searchTerm) {
        const tableBody = document.querySelector(".data-table__body");
        const visibleRows = tableBody.querySelectorAll(
            'tr:not([style*="display: none"]):not([id="no-data-results-message"])'
        );
        const emptyRow = tableBody.querySelector("td[colspan]")?.parentElement;

        // Xóa thông báo cũ nếu có
        const existingMessage = document.getElementById(
            "no-data-results-message"
        );
        if (existingMessage) {
            existingMessage.remove();
        }

        // Nếu không có kết quả và có từ khóa tìm kiếm
        if (visibleRows.length === 0 && searchTerm !== "") {
            // Ẩn dòng "Không có dữ liệu" ban đầu nếu có
            if (emptyRow) {
                emptyRow.style.display = "none";
            }

            const noResultsRow = document.createElement("tr");
            noResultsRow.id = "no-data-results-message";
            noResultsRow.innerHTML = `
                <td colspan="6" style="text-align: center; padding: 20px; color: #666; font-style: italic;">
                    Không tìm thấy bản ghi thay đổi nào phù hợp với từ khóa "${
                        document.getElementById("searchDataInput").value
                    }"
                </td>
            `;
            tableBody.appendChild(noResultsRow);
        } else {
            // Hiện lại dòng "Không có dữ liệu" ban đầu nếu cần
            if (emptyRow && searchTerm === "") {
                emptyRow.style.display = "";
            }
        }
    }
</script>
