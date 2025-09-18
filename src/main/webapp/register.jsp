<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
  <title>Đăng ký</title>
  <link rel="stylesheet" href="styles/register.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
  <script src="https://cdn.jsdelivr.net/npm/@supabase/supabase-js@2"></script>
</head>
<body>
  <div id="register-wrapper">
  <h1>Đăng ký tài khoản</h1>
  <form id="register-form">
  <div class="form-group">
    <i class="fa fa-user"></i>
    <input type="text" id="name" placeholder="Họ tên" required>
  </div>
  <div class="form-group">
    <i class="fa fa-envelope"></i>
    <input type="email" id="email" placeholder="Email" required>
  </div>
  <div class="form-group">
    <i class="fa fa-key"></i>
    <input type="password" id="password" placeholder="Mật khẩu" required>
  </div>
  <div class="form-group">
    <i class="fa fa-phone"></i>
    <input type="text" id="phone" placeholder="Số điện thoại" required>
  </div>
  <button type="submit">Đăng ký</button>
</form>

</div>


  <script>
    // URL và ANON KEY của bạn
    const SUPABASE_URL = 'https://kognqhcifxbpihjrwocg.supabase.co';
    const SUPABASE_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZ25xaGNpZnhicGloanJ3b2NnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTY2MDkzNDEsImV4cCI6MjA3MjE4NTM0MX0.P2thkCIF98_bnuJdcy6Nwxp3-9uOSvbebx1rfkU2j04';

    // Khởi tạo client Supabase
    const supabaseClient = supabase.createClient(SUPABASE_URL, SUPABASE_KEY);

    // Xử lý submit form
    document.getElementById('register-form').addEventListener('submit', async (e) => {
      e.preventDefault();

      const name = document.getElementById('name').value.trim();
      const email = document.getElementById('email').value.trim();
      const password = document.getElementById('password').value.trim();
      const phone = document.getElementById('phone').value.trim();

      // Insert vào bảng User
      const { data, error } = await supabaseClient
        .from('users')
        .insert([{ name, email, password, phone }]);

      console.log('data:', data);
      console.log('error:', error);

      if (error) {
        alert('Đăng ký lỗi: ' + error.message);
      } else {
        alert('Đăng ký thành công!');
        window.location.href = 'login.jsp';
      }
    });
  </script>
</body>
</html>
