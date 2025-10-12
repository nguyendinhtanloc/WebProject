<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%-- Import hằng số từ CsrfTokenFilter để code sạch hơn --%>
<%@ page import="com.busbooking.filter.CsrfTokenFilter" %>

<header class="header">
    <div class="header-left">
        <img src="${pageContext.request.contextPath}/images/logo(1).png" alt="logo" class="header-left__logo" />

        <svg
            class="menu-header__icon"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 640 640"
        >
            <path
                d="M96 160C96 142.3 110.3 128 128 128L512 128C529.7 128 544 142.3 544 160C544 177.7 529.7 192 512 192L128 192C110.3 192 96 177.7 96 160zM96 320C96 302.3 110.3 288 128 288L512 288C529.7 288 544 302.3 544 320C544 337.7 529.7 352 512 352L128 352C110.3 352 96 337.7 96 320zM544 480C544 497.7 529.7 512 512 512L128 512C110.3 512 96 497.7 96 480C96 462.3 110.3 448 128 448L512 448C529.7 448 544 462.3 544 480z"
            />
        </svg>
    </div>

    <div class="header-right">
        <div class="main-content">
            <div class="header-account">
                <img src="${pageContext.request.contextPath}/images/IMG_4612.jpg" alt="avatar" class="header-account__avatar" />

                <label for="dropdown">
                    <svg
                        class="header-account__dropdown-icon"
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 640 640"
                    >
                        <path
                            d="M480 224C492.9 224 504.6 231.8 509.6 243.8C514.6 255.8 511.8 269.5 502.7 278.7L342.7 438.7C330.2 451.2 309.9 451.2 297.4 438.7L137.4 278.7C128.2 269.5 125.5 255.8 130.5 243.8C135.5 231.8 147.1 224 160 224L480 224z"
                        />
                    </svg>
                </label>
                <input
                    type="checkbox"
                    name="dropdown"
                    id="dropdown"
                    class="dropdown-checkbox"
                />
                <div class="dropdown">
                    <ul class="dropdown-menu">
                        <li class="dropdown-item">Profile</li>
                        <li class="dropdown-item">Settings</li>
                        
                        <!-- === NÂNG CẤP BẢO MẬT: Chuyển link logout thành form với confirm === -->
                        <li class="dropdown-item">
                            <form action="${pageContext.request.contextPath}/logout" method="post" style="display: inline;">
                                <!-- Thêm token bí mật vào form -->
                                <input type="hidden" name="<%= CsrfTokenFilter.CSRF_TOKEN_SESSION_ATTR %>" value="${sessionScope.csrfToken}">
                                <button type="submit" class="logout-button"
                                        onclick="return confirm('Bạn có chắc chắn muốn đăng xuất không?');">
                                    Logout
                                </button>
                            </form>
                        </li>
                        <!-- === KẾT THÚC NÂNG CẤP === -->

                    </ul>
                </div>
                <p class="header-account__name">Tien Dat</p>
            </div>
        </div>
    </div>
</header>

<%-- Thêm một chút CSS để nút logout trông giống như một link bình thường --%>
<style>
    .logout-button {
        background: none;
        border: none;
        padding: 0;
        margin: 0;
        font: inherit;
        color: inherit;
        cursor: pointer;
        text-align: left;
        width: 100%;
    }
    .logout-button:hover {
        text-decoration: underline;
    }
</style>
