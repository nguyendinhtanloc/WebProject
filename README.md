# Hệ thống Chat Nhân viên - Khách hàng

Hệ thống chat đơn giản cho phép nhân viên hỗ trợ khách hàng thông qua giao diện web real-time.

## Tính năng

- **Đăng ký/Đăng nhập**: Hỗ trợ hai loại tài khoản (Khách hàng và Nhân viên)
- **Tạo cuộc trò chuyện**: Khách hàng có thể tạo chat mới với chủ đề
- **Quản lý chat**: Nhân viên có thể xem và nhận các chat chờ xử lý
- **Tin nhắn real-time**: Sử dụng WebSocket để gửi/nhận tin nhắn tức thời
- **Trạng thái người dùng**: Hiển thị trạng thái online/offline
- **Thông báo typing**: Hiển thị khi ai đó đang nhập tin nhắn
- **Giao diện responsive**: Tương thích với mobile và desktop

## Công nghệ sử dụng

### Backend
- **Java Servlet**: API endpoints
- **JPA/Hibernate**: ORM cho database
- **WebSocket**: Real-time communication
- **Jackson**: JSON processing
- **Maven**: Dependency management

### Frontend
- **HTML5/CSS3**: Giao diện người dùng
- **JavaScript (ES6)**: Logic phía client
- **WebSocket API**: Kết nối real-time
- **Fetch API**: HTTP requests

### Database
- **Supabase (PostgreSQL)**: Cloud database

## Cài đặt và chạy

### 1. Cấu hình Supabase

1. Tạo project mới trên [Supabase](https://supabase.com)
2. Lấy thông tin kết nối database:
   - Database URL
   - Username (thường là `postgres`)
   - Password

### 2. Cấu hình ứng dụng

Chỉnh sửa file `src/main/resources/database.properties`:

```properties
# Thay thế bằng thông tin Supabase của bạn
db.url=jdbc:postgresql://db.xxxxxxxxxxxxxxxxxxxx.supabase.co:5432/postgres
db.username=postgres
db.password=YOUR_SUPABASE_PASSWORD
```

Chỉnh sửa file `src/main/resources/META-INF/persistence.xml`:

```xml
<!-- Cập nhật thông tin kết nối database -->
<property name="javax.persistence.jdbc.url" value="jdbc:postgresql://db.xxxxxxxxxxxxxxxxxxxx.supabase.co:5432/postgres"/>
<property name="javax.persistence.jdbc.user" value="postgres"/>
<property name="javax.persistence.jdbc.password" value="YOUR_SUPABASE_PASSWORD"/>
```

### 3. Build và chạy

```bash
# Build project
mvn clean compile

# Chạy với Tomcat Maven Plugin
mvn tomcat7:run
```

Hoặc deploy WAR file lên Tomcat server:

```bash
# Build WAR file
mvn clean package

# Copy file target/employee-customer-chat-1.0.0.war vào thư mục webapps của Tomcat
```

### 4. Truy cập ứng dụng

Mở trình duyệt và truy cập: `http://localhost:8080/chat`

## Cấu trúc Database

### Bảng `users`
- `id`: Primary key
- `username`: Tên đăng nhập (unique)
- `email`: Email (unique)
- `full_name`: Họ tên đầy đủ
- `user_type`: Loại người dùng (CUSTOMER/EMPLOYEE)
- `is_online`: Trạng thái online
- `created_at`: Thời gian tạo
- `last_seen`: Lần cuối hoạt động

### Bảng `chats`
- `id`: Primary key
- `customer_id`: Foreign key đến users (khách hàng)
- `employee_id`: Foreign key đến users (nhân viên)
- `status`: Trạng thái chat (OPEN/IN_PROGRESS/CLOSED)
- `subject`: Chủ đề chat
- `created_at`: Thời gian tạo
- `updated_at`: Thời gian cập nhật
- `closed_at`: Thời gian đóng

### Bảng `messages`
- `id`: Primary key
- `chat_id`: Foreign key đến chats
- `sender_id`: Foreign key đến users
- `content`: Nội dung tin nhắn
- `message_type`: Loại tin nhắn (TEXT/IMAGE/FILE/SYSTEM)
- `created_at`: Thời gian tạo
- `is_read`: Đã đọc chưa
- `read_at`: Thời gian đọc

## API Endpoints

### Authentication
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/register` - Đăng ký
- `POST /api/auth/logout` - Đăng xuất

### Chat Management
- `GET /api/chat/list` - Lấy danh sách chat
- `GET /api/chat/open` - Lấy chat chờ xử lý (chỉ nhân viên)
- `GET /api/chat/{id}` - Lấy thông tin chat cụ thể
- `POST /api/chat/create` - Tạo chat mới
- `POST /api/chat/assign` - Nhận chat (nhân viên)
- `POST /api/chat/close` - Đóng chat

### Messages
- `GET /api/message/chat/{chatId}` - Lấy tin nhắn trong chat
- `GET /api/message/unread` - Lấy tin nhắn chưa đọc
- `POST /api/message/send` - Gửi tin nhắn
- `POST /api/message/mark-read` - Đánh dấu đã đọc

### WebSocket
- `ws://localhost:8080/chat/ws/chat/{chatId}/{userId}` - Kết nối real-time

## Sử dụng

### Cho Khách hàng
1. Đăng ký tài khoản với user type "CUSTOMER"
2. Đăng nhập và tạo chat mới
3. Chờ nhân viên nhận chat và bắt đầu trò chuyện

### Cho Nhân viên
1. Đăng ký tài khoản với user type "EMPLOYEE"
2. Đăng nhập và xem danh sách chat chờ xử lý
3. Nhận chat và trả lời khách hàng

## Lưu ý

- Đây là phiên bản demo đơn giản, chưa có bảo mật mật khẩu (hash password)
- Cần cấu hình CORS nếu deploy trên domain khác
- Cần thêm SSL/HTTPS cho production
- Có thể mở rộng thêm tính năng upload file, emoji, etc.

## Troubleshooting

### Lỗi kết nối database
- Kiểm tra thông tin Supabase trong persistence.xml
- Đảm bảo database đã được tạo và accessible

### Lỗi WebSocket
- Kiểm tra port 8080 có bị block không
- Đảm bảo Tomcat hỗ trợ WebSocket

### Lỗi build
- Kiểm tra Java version (cần Java 11+)
- Chạy `mvn clean` trước khi build lại

