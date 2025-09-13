<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Kiểm tra nếu người dùng chưa đăng nhập thì chuyển về trang login --%>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="/jsp/login.jsp"/>
</c:if>

<html>
<head>
    <title>Trang quản trị</title>
</head>
<body>
    <h1>Chào mừng bạn đã quay trở lại!</h1>
    
    <p>
        Bạn đã đăng nhập với email: <strong>${sessionScope.user.email}</strong>
    </p>
    
    <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
</body>
</html>