<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="header">
    <nav class="navbar">
        <div class="nav-container">
            <div class="nav-logo">
                <i class="fas fa-bus"></i>
                <span>BusBooking</span>
            </div>
            <ul class="nav-menu">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/index.jsp" class="nav-link">Trang chủ</a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/lookup" class="nav-link">Tra cứu vé</a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">Liên hệ</a>
                </li>
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/user.jsp" class="nav-link" title="Trang cá nhân" style="color: #2563eb; font-weight: 700;">
                                <i class="fas fa-user-circle" style="font-size: 1.3em; vertical-align: middle;"></i>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/logout" class="nav-link">Đăng xuất</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/login.jsp" class="nav-link">Đăng nhập</a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/register.jsp" class="nav-link">Đăng ký</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
            <div class="hamburger">
                <span class="bar"></span>
                <span class="bar"></span>
                <span class="bar"></span>
            </div>
        </div>
    </nav>
</header>