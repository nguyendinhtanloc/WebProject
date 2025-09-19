<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<aside class="sidebar">
    <ul class="sidebar-list">
        <!-- Dashboard -->
        <li class="sidebar-item" data-page="dashboard">
            <a href="${pageContext.request.contextPath}/page?view=dashboard">
                <svg class="sidebar-item__icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path d="M304 70.1C313.1..."/>
                </svg>
                <span class="text">Dashboard</span>
            </a>
        </li>

        <!-- Quản lý tài xế -->
        <li class="sidebar-item" data-page="drivers">
            <a href="${pageContext.request.contextPath}/drivers?action=list">
                <svg class="sidebar-item__icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path d="M376 88C376..."/>
                </svg>
                <span class="text">Quản lý tài xế</span>
            </a>
        </li>

        <!-- Quản lý xe -->
        <li class="sidebar-item" data-page="buses">
            <a href="${pageContext.request.contextPath}/buses?action=list">
                <svg class="sidebar-item__icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path d="M192 64C139..."/>
                </svg>
                <span class="text">Quản lý xe</span>
            </a>
        </li>

        <!-- Quản lý chuyến -->
        <li class="sidebar-item" data-page="trips">
            <a href="${pageContext.request.contextPath}/trips?action=list">
                <svg class="sidebar-item__icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path d="M576 112C576..."/>
                </svg>
                <span class="text">Quản lý chuyến</span>
            </a>
        </li>

        <!-- Tài khoản -->
        <li class="sidebar-item" data-page="accounts">
            <a href="${pageContext.request.contextPath}/accounts?action=list">
                <svg class="sidebar-item__icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path d="M576 112C576..."/>
                </svg>
                <span class="text">Tài khoản</span>
            </a>
        </li>

        <!-- Dữ liệu -->
        <li class="sidebar-item" data-page="datas">
            <a href="${pageContext.request.contextPath}/datas?action=list">
                <svg class="sidebar-item__icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path d="M576 112C576..."/>
                </svg>
                <span class="text">Dữ liệu</span>
            </a>
        </li>
    </ul>
</aside>
