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

    <!-- ================== SCRIPT LOGOUT KHI ĐÓNG TAB ================== -->
    <script>
        let isInternalNavigation = false;

        // Bắt tất cả link trong sidebar và header → đánh dấu là điều hướng nội bộ
        document.querySelectorAll("a").forEach(a => {
            a.addEventListener("click", function () {
                isInternalNavigation = true;
            });
        });

        // Trước khi rời trang
        window.addEventListener("beforeunload", function (e) {
            if (!isInternalNavigation) {
                var confirmationMessage = "Bạn có chắc chắn muốn thoát? Nếu thoát, bạn sẽ bị đăng xuất.";
                e.returnValue = confirmationMessage;
                return confirmationMessage;
            }
        });

        // Khi unload (thật sự rời trang / đóng tab)
        window.addEventListener("unload", function () {
            if (!isInternalNavigation) {
                const formData = new FormData();
                formData.append("<%= com.busbooking.filter.CsrfTokenFilter.CSRF_TOKEN_SESSION_ATTR %>", "${sessionScope.csrfToken}");
                navigator.sendBeacon("${pageContext.request.contextPath}/logout", formData);
            }
        });
    </script>
    <!-- =============================================================== -->
</body>
</html>
