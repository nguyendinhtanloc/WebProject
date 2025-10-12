<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%
    // Lấy URI hiện tại để xác định trang đang active
    String currentURI = request.getRequestURI();
    String contextPath = request.getContextPath();
    String currentPage = currentURI.substring(contextPath.length());
    
    // Xác định trang hiện tại
    String activePage = "";
    
    // Kiểm tra contentPage attribute (được set từ servlet)
    String contentPage = (String) request.getAttribute("contentPage");
    if (contentPage != null) {
        if (contentPage.contains("home")) {
            activePage = "home";
        } else if (contentPage.contains("drivers")) {
            activePage = "drivers";
        } else if (contentPage.contains("vehicles")) {
            activePage = "vehicles";
        } else if (contentPage.contains("trips-content") || contentPage.contains("trip-form-content")) {
            activePage = "trips";
        } else if (contentPage.contains("accounts")) {
            activePage = "accounts";
        } else if (contentPage.contains("datas")) {
            activePage = "datas";
        }
    }
    
    // Nếu không có contentPage, fallback về JSP path
    if (activePage.isEmpty()) {
        if (currentPage.contains("home")) {
            activePage = "home";
        } else if (currentPage.contains("drivers")) {
            activePage = "drivers";
        } else if (currentPage.contains("vehicles")) {
            activePage = "vehicles";
        } else if (currentPage.contains("trips")) {
            activePage = "trips";
        } else if (currentPage.contains("accounts")) {
            activePage = "accounts";
        } else if (currentPage.contains("datas")) {
            activePage = "datas";
        }
    }
%>
<aside class="sidebar">
    <ul class="sidebar-list">
        <!-- Dashboard -->
        <li class="sidebar-item <%= "home".equals(activePage) ? "active" : "" %>" data-page="home">
            <a href="${pageContext.request.contextPath}/home">
                <svg
                    class="sidebar-item__icon"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 640 640"
                >
                    <path
                        d="M304 70.1C313.1 61.9 326.9 61.9 336 70.1L568 278.1C577.9 286.9 578.7 302.1 569.8 312C560.9 321.9 545.8 322.7 535.9 313.8L527.9 306.6L527.9 511.9C527.9 547.2 499.2 575.9 463.9 575.9L175.9 575.9C140.6 575.9 111.9 547.2 111.9 511.9L111.9 306.6L103.9 313.8C94 322.6 78.9 321.8 70 312C61.1 302.2 62 287 71.8 278.1L304 70.1zM320 120.2L160 263.7L160 512C160 520.8 167.2 528 176 528L224 528L224 424C224 384.2 256.2 352 296 352L344 352C383.8 352 416 384.2 416 424L416 528L464 528C472.8 528 480 520.8 480 512L480 263.7L320 120.3zM272 528L368 528L368 424C368 410.7 357.3 400 344 400L296 400C282.7 400 272 410.7 272 424L272 528z"
                    />
                </svg>
                <span class="text">Dashboard</span>
            </a>
        </li>

        <!-- Quản lý tài xế -->
        <li class="sidebar-item <%= "drivers".equals(activePage) ? "active" : "" %>" data-page="drivers">
            <a href="${pageContext.request.contextPath}/drivers?action=list">
                <svg
                    class="sidebar-item__icon"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 640 640"
                >
                    <path
                        d="M376 88C376 57.1 350.9 32 320 32C289.1 32 264 57.1 264 88C264 118.9 289.1 144 320 144C350.9 144 376 118.9 376 88zM400 300.7L446.3 363.1C456.8 377.3 476.9 380.3 491.1 369.7C505.3 359.1 508.3 339.1 497.7 324.9L427.2 229.9C402 196 362.3 176 320 176C277.7 176 238 196 212.8 229.9L142.3 324.9C131.8 339.1 134.7 359.1 148.9 369.7C163.1 380.3 183.1 377.3 193.7 363.1L240 300.7L240 576C240 593.7 254.3 608 272 608C289.7 608 304 593.7 304 576L304 416C304 407.2 311.2 400 320 400C328.8 400 336 407.2 336 416L336 576C336 593.7 350.3 608 368 608C385.7 608 400 593.7 400 576L400 300.7z"
                    />
                </svg>
                <span class="text">Quản lý tài xế</span>
            </a>
        </li>

        <!-- Quản lý xe -->
        <li class="sidebar-item <%= "vehicles".equals(activePage) ? "active" : "" %>" data-page="vehicles">
            <a href="${pageContext.request.contextPath}/vehicles?action=list">
                <svg
                    class="sidebar-item__icon"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 640 640"
                >
                    <path
                        d="M192 64C139 64 96 107 96 160L96 448C96 477.8 116.4 502.9 144 510L144 544C144 561.7 158.3 576 176 576L192 576C209.7 576 224 561.7 224 544L224 512L416 512L416 544C416 561.7 430.3 576 448 576L464 576C481.7 576 496 561.7 496 544L496 510C523.6 502.9 544 477.8 544 448L544 160C544 107 501 64 448 64L192 64zM160 192C160 174.3 174.3 160 192 160L448 160C465.7 160 480 174.3 480 192L480 288C480 305.7 465.7 320 448 320L192 320C174.3 320 160 305.7 160 288L160 192zM192 384C209.7 384 224 398.3 224 416C224 433.7 209.7 448 192 448C174.3 448 160 433.7 160 416C160 398.3 174.3 384 192 384zM448 384C465.7 384 480 398.3 480 416C480 433.7 465.7 448 448 448C430.3 448 416 433.7 416 416C416 398.3 430.3 384 448 384z"
                    />
                </svg>
                <span class="text">Quản lý xe</span>
            </a>
        </li>

        <!-- Quản lý chuyến -->
        <li class="sidebar-item <%= "trips".equals(activePage) ? "active" : "" %>" data-page="trips">
            <a href="${pageContext.request.contextPath}/trips?action=list">
                <svg
                    class="sidebar-item__icon"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 640 640"
                >
                    <path
                        d="M576 112C576 103.7 571.7 96 564.7 91.6C557.7 87.2 548.8 86.8 541.4 90.5L416.5 152.1L244 93.4C230.3 88.7 215.3 89.6 202.1 95.7L77.8 154.3C69.4 158.2 64 166.7 64 176L64 528C64 536.2 68.2 543.9 75.1 548.3C82 552.7 90.7 553.2 98.2 549.7L225.5 489.8L396.2 546.7C409.9 551.3 424.7 550.4 437.8 544.2L562.2 485.7C570.6 481.7 576 473.3 576 464L576 112zM208 146.1L208 445.1L112 490.3L112 191.3L208 146.1zM256 449.4L256 148.3L384 191.8L384 492.1L256 449.4zM432 198L528 150.6L528 448.8L432 494L432 198z"
                    />
                </svg>
                <span class="text">Quản lý chuyến</span>
            </a>
        </li>

        <!-- Tài khoản -->
        <li class="sidebar-item <%= "accounts".equals(activePage) ? "active" : "" %>" data-page="accounts">
            <a href="${pageContext.request.contextPath}/accounts?action=list">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640" class="sidebar-item__icon">
                    <path d="M320 312C386.3 312 440 258.3 440 192C440 125.7 386.3 72 320 72C253.7 72 200 125.7 200 192C200 258.3 253.7 312 320 312zM290.3 368C191.8 368 112 447.8 112 546.3C112 562.7 125.3 576 141.7 576L498.3 576C514.7 576 528 562.7 528 546.3C528 447.8 448.2 368 349.7 368L290.3 368z"/>
                </svg>
                <span class="text">Tài khoản</span>
            </a>
        </li>

        <!-- Dữ liệu -->
        <li class="sidebar-item <%= "datas".equals(activePage) ? "active" : "" %>" data-page="datas">
            <a href="${pageContext.request.contextPath}/datas?action=list">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640" class="sidebar-item__icon">
                    <path d="M544 269.8C529.2 279.6 512.2 287.5 494.5 293.8C447.5 310.6 385.8 320 320 320C254.2 320 192.4 310.5 145.5 293.8C127.9 287.5 110.8 279.6 96 269.8L96 352C96 396.2 196.3 432 320 432C443.7 432 544 396.2 544 352L544 269.8zM544 192L544 144C544 99.8 443.7 64 320 64C196.3 64 96 99.8 96 144L96 192C96 236.2 196.3 272 320 272C443.7 272 544 236.2 544 192zM494.5 453.8C447.6 470.5 385.9 480 320 480C254.1 480 192.4 470.5 145.5 453.8C127.9 447.5 110.8 439.6 96 429.8L96 496C96 540.2 196.3 576 320 576C443.7 576 544 540.2 544 496L544 429.8C529.2 439.6 512.2 447.5 494.5 453.8z"/>
                </svg>
                <span class="text">Dữ liệu</span>
            </a>
        </li>

        <li class="sidebar-item">
            <a href="!#">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640" class="sidebar-item__icon">
                    <path d="M64 416L64 192C64 139 107 96 160 96L480 96C533 96 576 139 576 192L576 416C576 469 533 512 480 512L360 512C354.8 512 349.8 513.7 345.6 516.8L230.4 603.2C226.2 606.3 221.2 608 216 608C202.7 608 192 597.3 192 584L192 512L160 512C107 512 64 469 64 416z"/>
                </svg>
                <span class="text">Hỗ trợ</span>
            </a>
        </li>
    </ul>
</aside>
