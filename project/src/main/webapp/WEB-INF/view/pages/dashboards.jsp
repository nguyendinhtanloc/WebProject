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
                    <div class="dashboard">
                        <div class="grid">
                            <div class="infor-card">
                                <h2 class="infor-card__title">
                                    Tổng số lượng xe
                                </h2>
                                <p class="infor-card__value">
                                    ${totalVehicles}
                                </p>
                            </div>
                            <div class="infor-card">
                                <h2 class="infor-card__title">
                                    Tổng số lượng tài xế
                                </h2>
                                <p class="infor-card__value">${totalDrivers}</p>
                            </div>
                            <div class="infor-card">
                                <h2 class="infor-card__title">
                                    Tổng số tài khoản
                                </h2>
                                <p class="infor-card__value">${totalUsers}</p>
                            </div>
                            <div class="infor-card">
                                <h2 class="infor-card__title">
                                    Tổng doanh số tháng
                                </h2>
                                <p class="infor-card__value"></p>
                            </div>

                            <div class="big-chart"></div>
                            <div class="premium-chart"></div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </body>
</html>
