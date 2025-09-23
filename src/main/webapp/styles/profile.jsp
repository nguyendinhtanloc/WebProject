<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thông tin cá nhân</title>
    <link rel="stylesheet" href="styles/profile.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div id="profile-card">
    <h1>Thông tin cá nhân</h1>

    <form action="updateProfile" method="post">
        <div class="form-group">
            <i class="fas fa-user"></i>
            <input type="text" name="fullName" placeholder="Họ tên" value="${user.fullName}">
        </div>

        <div class="form-group">
            <i class="fas fa-phone"></i>
            <input type="text" name="phone" placeholder="Số điện thoại" value="${user.phone}">
        </div>

        <div class="form-group">
            <i class="fas fa-calendar-alt"></i>
            <input type="date" name="dob" value="${user.dob}">
        </div>

        <div class="form-group">
            <i class="fas fa-map-marker-alt"></i>
            <input type="text" name="address" placeholder="Địa chỉ" value="${user.address}">
        </div>

        <div class="form-group">
            <i class="fas fa-venus-mars"></i>
            <select name="gender">
                <option value="Nam" ${user.gender == 'Nam' ? 'selected' : ''}>Nam</option>
                <option value="Nữ" ${user.gender == 'Nữ' ? 'selected' : ''}>Nữ</option>
            </select>
        </div>

        <button type="submit">Cập nhật</button>
    </form>
</div>
</body>
</html>
