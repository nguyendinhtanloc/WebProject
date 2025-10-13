# Hướng dẫn cài đặt và chạy hệ thống Chat

## Yêu cầu hệ thống

- Java 11 hoặc cao hơn
- Maven 3.6 hoặc cao hơn
- Tài khoản Supabase (miễn phí)

## Bước 1: Cài đặt Supabase

1. Truy cập [https://supabase.com](https://supabase.com)
2. Đăng ký tài khoản miễn phí
3. Tạo project mới
4. Lấy thông tin kết nối database từ Settings > Database

## Bước 2: Cấu hình Database

### Thông tin cần lấy từ Supabase:
- **Database URL**: `postgresql://postgres:[password]@db.[project-ref].supabase.co:5432/postgres`
- **Password**: Mật khẩu database
- **Username**: `postgres` (mặc định)

### Cập nhật file cấu hình:

**File: `src/main/resources/database.properties`**
```properties
# Thay YOUR_SUPABASE_PASSWORD bằng mật khẩu thực tế
db.url=jdbc:postgresql://db.xxxxxxxxxxxxxxxxxxxx.supabase.co:5432/postgres
db.username=postgres
db.password=YOUR_SUPABASE_PASSWORD
```

**File: `src/main/resources/META-INF/persistence.xml`**
```xml
<!-- Thay thế các giá trị này bằng thông tin thực tế -->
<property name="javax.persistence.jdbc.url" value="jdbc:postgresql://db.xxxxxxxxxxxxxxxxxxxx.supabase.co:5432/postgres"/>
<property name="javax.persistence.jdbc.user" value="postgres"/>
<property name="javax.persistence.jdbc.password" value="YOUR_SUPABASE_PASSWORD"/>
```

## Bước 3: Chạy ứng dụng

### Trên Windows:
```cmd
run.bat
```

### Trên Linux/Mac:
```bash
chmod +x run.sh
./run.sh
```

### Hoặc chạy thủ công:
```bash
mvn clean compile
mvn tomcat7:run
```

## Bước 4: Truy cập ứng dụng

1. Mở trình duyệt
2. Truy cập: `http://localhost:8080/chat`
3. Đăng ký tài khoản mới hoặc đăng nhập

## Bước 5: Test hệ thống

### Tạo tài khoản khách hàng:
1. Đăng ký với User Type: "Khách hàng"
2. Tạo chat mới với chủ đề
3. Chờ nhân viên nhận chat

### Tạo tài khoản nhân viên:
1. Đăng ký với User Type: "Nhân viên" 
2. Xem danh sách chat chờ xử lý
3. Nhận chat và trả lời khách hàng

## Troubleshooting

### Lỗi kết nối database:
```
ERROR: Could not connect to database
```
**Giải pháp:**
- Kiểm tra thông tin Supabase trong persistence.xml
- Đảm bảo mật khẩu chính xác
- Kiểm tra firewall/network

### Lỗi build Maven:
```
ERROR: Failed to build project
```
**Giải pháp:**
- Kiểm tra Java version: `java -version`
- Kiểm tra Maven: `mvn -version`
- Chạy `mvn clean` trước khi build lại

### Lỗi WebSocket:
```
WebSocket connection failed
```
**Giải pháp:**
- Đảm bảo port 8080 không bị block
- Kiểm tra Tomcat có hỗ trợ WebSocket không
- Thử refresh trang

### Lỗi CORS (nếu deploy khác domain):
```
Access to fetch at '...' has been blocked by CORS policy
```
**Giải pháp:**
- Thêm CORS headers trong servlet
- Hoặc deploy trên cùng domain

## Cấu trúc thư mục

```
new_chat/
├── src/main/java/com/chat/
│   ├── model/          # JPA Entities
│   ├── dao/            # Data Access Objects
│   ├── servlet/        # API Endpoints
│   └── websocket/      # WebSocket handlers
├── src/main/resources/
│   ├── META-INF/persistence.xml  # JPA config
│   └── database.properties       # DB config
├── src/main/webapp/
│   ├── css/            # Stylesheets
│   ├── js/             # JavaScript
│   ├── error/          # Error pages
│   └── index.html      # Main page
├── pom.xml             # Maven config
├── run.bat            # Windows runner
├── run.sh             # Linux/Mac runner
└── README.md          # Documentation
```

## Tính năng chính

✅ **Đăng ký/Đăng nhập** - Hỗ trợ 2 loại tài khoản  
✅ **Tạo chat** - Khách hàng tạo cuộc trò chuyện mới  
✅ **Quản lý chat** - Nhân viên nhận và xử lý chat  
✅ **Real-time messaging** - Tin nhắn tức thời qua WebSocket  
✅ **Typing indicator** - Hiển thị đang nhập  
✅ **Online status** - Trạng thái online/offline  
✅ **Responsive UI** - Giao diện đẹp, tương thích mobile  
✅ **Error handling** - Xử lý lỗi và thông báo  

## Mở rộng trong tương lai

- 🔐 Bảo mật mật khẩu (bcrypt)
- 📁 Upload file/ảnh
- 😊 Emoji support
- 🔔 Push notifications
- 📊 Analytics dashboard
- 👥 Group chat
- 🎨 Custom themes
- 📱 Mobile app (React Native)

