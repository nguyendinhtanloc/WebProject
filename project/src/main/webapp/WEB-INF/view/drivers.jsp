<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <link
            rel="stylesheet"
            href="../styles/reset.css"
        />
        <link
            rel="stylesheet"
            href="../styles/style.css"
        />
        <title>admin</title>
    </head>
    <body>
        <jsp:include page="/components/header.jsp" />
        <div class="container">
            <jsp:include page="/components/sidebar.jsp" />
            <main class="main">
                <div class="main-content" id="mainContent">
                    <div class="driver">
                        <h1 class="driver-title title">Tài xế</h1>
                        <div class="driver-top top">
                            <label
                                for="addform-checkbox"
                                class="driver-top__addbutton addbutton"
                            >
                                ThÃªm
                            </label>
                            <input
                                type="checkbox"
                                name="addform"
                                id="addform-checkbox"
                                class="addform-checkbox"
                            />
                            <div class="addform">
                                <div class="main-content">
                                    <h2 class="addform__title">Thêm tài xế</h2>
                                    <form
                                        class="addform__form"
                                        action="${pageContext.request.contextPath}/drivers?action=insert"
                                        method="POST"
                                    >
                                        <!-- Äáº£m báº£o method="POST" -->
                                        <label>company_id (UUID format)</label>
                                        <input
                                            type="text"
                                            name="company_id"
                                            required
                                            pattern="[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
                                            placeholder="vÃ­ dá»¥: 123e4567-e89b-12d3-a456-426614174000"
                                        />
                                        <br />

                                        <label>name</label>
                                        <input
                                            type="text"
                                            name="name"
                                            required
                                        />
                                        <br />

                                        <label>phone</label>
                                        <input
                                            type="text"
                                            name="phone"
                                            required
                                        />
                                        <br />

                                        <label>license_no</label>
                                        <input
                                            type="text"
                                            name="license_no"
                                            required
                                        />
                                        <br />

                                        <label>experience_years</label>
                                        <input
                                            type="number"
                                            name="experience_years"
                                            required
                                        />
                                        <br />

                                        <label>status</label>
                                        <input
                                            type="text"
                                            name="status"
                                            required
                                        />
                                        <br />

                                        <input type="submit" value="submit" />
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
                                    <th>ID Công ty</th>
                                    <th>Tên</th>
                                    <th>Số điện thoại</th>
                                    <th>Căn cước</th>
                                    <th>Kinh nghiệm (năm)</th>
                                    <th>Trạng thái</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody class="driver-table__body table-body">
                                <c:forEach var="driver" items="${driverList}">
                                    <tr>
                                        <td>${driver.driver_id}</td>
                                        <td>${driver.company_id}</td>
                                        <td>${driver.name}</td>
                                        <td>${driver.phone}</td>
                                        <td>${driver.license_no}</td>
                                        <td>${driver.experience_years}</td>
                                        <td>${driver.status}</td>
                                        <td>
                                            <a
                                                href="${pageContext.request.contextPath}/drivers?action=update&driver_id=${driver.driver_id}"
                                            >
                                                <button
                                                    class="driver-table__editbutton editbutton"
                                                >
                                                    Sá»­a
                                                </button>
                                            </a>
                                            <a
                                                onclick="return confirm ('XÃ¡c nháº­n xÃ³a')"
                                                href="${pageContext.request.contextPath}/drivers?action=delete&driver_id=${driver.driver_id}"
                                            >
                                                <button
                                                    class="driver-table__deletebutton deletebutton"
                                                >
                                                    XÃ³a
                                                </button>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </main>
        </div>
    </body>
</html>
