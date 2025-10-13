# Demo Hệ thống Chat Nhân viên - Khách hàng

## Kịch bản demo

### Bước 1: Chuẩn bị
1. Chạy ứng dụng: `./run.sh` (Linux/Mac) hoặc `run.bat` (Windows)
2. Mở 2 trình duyệt khác nhau:
   - Trình duyệt 1: Đăng ký tài khoản **Khách hàng**
   - Trình duyệt 2: Đăng ký tài khoản **Nhân viên**

### Bước 2: Đăng ký tài khoản Khách hàng
1. Truy cập: `http://localhost:8080/chat`
2. Click tab "Đăng ký"
3. Nhập thông tin:
   ```
   Tên đăng nhập: customer01
   Email: customer@example.com
   Họ tên: Nguyễn Văn A
   Loại tài khoản: Khách hàng
   ```
4. Click "Đăng ký"
5. Chuyển sang tab "Đăng nhập" và đăng nhập

### Bước 3: Đăng ký tài khoản Nhân viên
1. Mở trình duyệt thứ 2: `http://localhost:8080/chat`
2. Click tab "Đăng ký"
3. Nhập thông tin:
   ```
   Tên đăng nhập: employee01
   Email: employee@example.com
   Họ tên: Trần Thị B
   Loại tài khoản: Nhân viên
   ```
4. Click "Đăng ký"
5. Chuyển sang tab "Đăng nhập" và đăng nhập

### Bước 4: Khách hàng tạo chat
1. Ở trình duyệt Khách hàng:
2. Click "Tạo chat mới"
3. Nhập chủ đề: "Hỗ trợ sản phẩm ABC"
4. Click "Tạo chat"
5. Chat sẽ hiển thị trạng thái "Mở" và chờ nhân viên

### Bước 5: Nhân viên nhận chat
1. Ở trình duyệt Nhân viên:
2. Xem phần "Chat chờ xử lý"
3. Click vào chat "Hỗ trợ sản phẩm ABC"
4. Click "Nhận chat" (nếu có button)
5. Chat sẽ chuyển sang trạng thái "Đang xử lý"

### Bước 6: Trò chuyện
1. **Khách hàng gửi tin nhắn:**
   - "Xin chào, tôi cần hỗ trợ về sản phẩm ABC"
   
2. **Nhân viên trả lời:**
   - "Xin chào! Tôi sẽ giúp bạn. Bạn gặp vấn đề gì với sản phẩm ABC?"
   
3. **Khách hàng:**
   - "Sản phẩm không hoạt động như mong đợi"
   
4. **Nhân viên:**
   - "Để tôi kiểm tra và hỗ trợ bạn. Bạn có thể mô tả chi tiết hơn không?"

### Bước 7: Test tính năng real-time
1. **Typing indicator:**
   - Khi một người đang nhập, người kia sẽ thấy "Đang nhập..."
   
2. **Tin nhắn tức thời:**
   - Tin nhắn xuất hiện ngay lập tức mà không cần refresh

### Bước 8: Đóng chat
1. Một trong hai người có thể click "Đóng chat"
2. Chat sẽ chuyển sang trạng thái "Đã đóng"
3. Không thể gửi tin nhắn mới

## Kiểm tra tính năng

### ✅ Authentication
- [ ] Đăng ký tài khoản mới
- [ ] Đăng nhập với thông tin đúng
- [ ] Đăng xuất
- [ ] Phân biệt user type (Customer/Employee)

### ✅ Chat Management
- [ ] Khách hàng tạo chat mới
- [ ] Nhân viên xem chat chờ xử lý
- [ ] Nhân viên nhận chat
- [ ] Đóng chat

### ✅ Messaging
- [ ] Gửi tin nhắn text
- [ ] Nhận tin nhắn real-time
- [ ] Typing indicator
- [ ] Hiển thị thời gian tin nhắn
- [ ] Phân biệt tin nhắn gửi/nhận

### ✅ UI/UX
- [ ] Giao diện responsive
- [ ] Loading states
- [ ] Error messages
- [ ] Success notifications
- [ ] Chat status indicators

### ✅ Database
- [ ] Dữ liệu được lưu vào Supabase
- [ ] Quan hệ giữa User, Chat, Message
- [ ] Trạng thái online/offline

## Screenshots mong đợi

### Trang đăng nhập:
- Modal với 2 tabs: Đăng nhập/Đăng ký
- Form validation
- Thông báo lỗi/thành công

### Giao diện chính:
- Header với thông tin user
- Sidebar với danh sách chat
- Chat area với tin nhắn
- Input box để gửi tin nhắn

### Mobile responsive:
- Layout thích ứng với màn hình nhỏ
- Touch-friendly buttons
- Readable text

## Lỗi thường gặp và cách fix

### 1. "Authentication required"
- **Nguyên nhân:** Session hết hạn
- **Fix:** Đăng nhập lại

### 2. "WebSocket connection failed"
- **Nguyên nhân:** Port bị block hoặc server chưa start
- **Fix:** Kiểm tra server đang chạy, restart nếu cần

### 3. "Database connection error"
- **Nguyên nhân:** Thông tin Supabase sai
- **Fix:** Kiểm tra lại persistence.xml và database.properties

### 4. "Chat not found"
- **Nguyên nhân:** Chat đã bị xóa hoặc không có quyền truy cập
- **Fix:** Refresh trang, kiểm tra lại quyền user

## Kết luận

Hệ thống chat này cung cấp đầy đủ tính năng cơ bản cho việc hỗ trợ khách hàng:

- **Real-time communication** qua WebSocket
- **User management** với 2 loại tài khoản
- **Chat workflow** từ tạo → nhận → xử lý → đóng
- **Modern UI** responsive và user-friendly
- **Database integration** với Supabase

Có thể mở rộng thêm nhiều tính năng như upload file, emoji, push notification, analytics, etc.

