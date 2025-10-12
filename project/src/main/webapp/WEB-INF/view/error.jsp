<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Lỗi</title>
    </head>
    <body>
        <h1>Đã xảy ra lỗi</h1>
        <p>${errorMessage}</p>
        <a href="${pageContext.request.contextPath}/accounts"
            >Quay lại trang danh sách</a
        >
    </body>
</html>
