<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %> <%-- Báo cho JSP biết đây là trang lỗi --%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Đã có lỗi xảy ra</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        h1 { color: #d9534f; }
        pre { background-color: #f8f9fa; border: 1px solid #ccc; padding: 15px; white-space: pre-wrap; }
    </style>
</head>
<body>
    <h1>Rất tiếc, đã có lỗi xảy ra!</h1>
    <p>Chi tiết lỗi:</p>

    <%-- Đoạn code này sẽ in ra chi tiết lỗi và vị trí xảy ra --%>
    <pre>
    <%
        // In ra thông báo lỗi
        out.println(exception.getMessage());
        out.println("\n----------------------------\n");
        // In ra toàn bộ stack trace
        exception.printStackTrace(new java.io.PrintWriter(out));
    %>
    </pre>
</body>
</html>