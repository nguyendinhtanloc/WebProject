<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>BusBooking Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/reset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/trip.css" /> 
</head>
<body>
    <jsp:include page="/WEB-INF/view/components/header.jsp" />
    <div class="container">
        <jsp:include page="/WEB-INF/view/components/sidebar.jsp" />
        <main class="main">
            <div class="main-content" id="mainContent">
                <jsp:include page="${contentPage}" />
            </div>
        </main>
    </div>
</body>
</html>
