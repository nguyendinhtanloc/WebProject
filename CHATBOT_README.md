# Chatbot Gemini AI Integration

## Mô tả
Chatbot được tích hợp vào trang index.jsp sử dụng Gemini AI API để trả lời câu hỏi của người dùng về dịch vụ đặt vé xe buýt.

## Cài đặt

### 1. Cấu hình Gemini API Key
Trong file `ChatbotServlet.java`, thay đổi API key:

```java
private static final String GEMINI_API_KEY = "YOUR_GEMINI_API_KEY_HERE";
```

**Lưu ý quan trọng**: 
- Đăng ký API key tại: https://makersuite.google.com/app/apikey
- Không commit API key lên repository
- Nên sử dụng environment variable:

```java
private static final String GEMINI_API_KEY = System.getenv("GEMINI_API_KEY");
```

### 2. Cấu hình Environment Variable (Khuyến nghị)
Tạo environment variable:
```bash
# Windows
set GEMINI_API_KEY=your_actual_api_key_here

# Linux/Mac
export GEMINI_API_KEY=your_actual_api_key_here
```

## Cấu trúc Files

### Backend
- `ChatbotServlet.java` - Servlet xử lý API requests đến Gemini
- Endpoint: `/api/chatbot` (POST)

### Frontend
- `chatbot.css` - Styling cho chatbot UI
- `chatbot.js` - JavaScript logic xử lý chat
- `index.jsp` - Tích hợp chatbot vào trang chính

## Tính năng

### 1. UI/UX
- Chat bubble design đẹp mắt
- Typing indicator khi AI đang trả lời
- Responsive design cho mobile
- Animation mượt mà
- Toggle mở/đóng chat

### 2. AI Features
- Context-aware responses về bus booking
- Trả lời bằng tiếng Việt
- Safety settings để lọc nội dung không phù hợp
- Error handling đầy đủ

### 3. Technical Features
- Async/await API calls
- Proper error handling
- Auto-scroll to new messages
- Input validation
- CORS support

## Cách sử dụng

1. Người dùng click vào icon chat ở góc phải màn hình
2. Nhập câu hỏi về dịch vụ đặt vé xe
3. AI sẽ trả lời dựa trên context của bus booking
4. Chat history được lưu trong session

## Ví dụ câu hỏi
- "Làm sao để đặt vé xe?"
- "Giá vé từ Hà Nội đi Sài Gòn là bao nhiều?"
- "Tôi có thể hủy vé không?"
- "Cách thanh toán như thế nào?"

## Troubleshooting

### 1. Chatbot không hiển thị
- Kiểm tra console browser xem có lỗi JavaScript
- Đảm bảo file CSS và JS được load đúng
- Kiểm tra path trong index.jsp

### 2. API không hoạt động
- Kiểm tra Gemini API key có đúng không
- Xem logs trong server console
- Kiểm tra network tab trong browser

### 3. CORS errors
- Đảm bảo servlet có headers CORS đúng
- Kiểm tra URL mapping của servlet

## Security Notes

1. **API Key Security**:
   - Không hardcode API key trong code
   - Sử dụng environment variables
   - Rotate API key định kỳ

2. **Input Validation**:
   - Validate user input trước khi gửi API
   - Sanitize output từ AI
   - Rate limiting để tránh spam

3. **Error Handling**:
   - Không expose internal errors cho user
   - Log errors để debug
   - Graceful fallback messages

## Customization

### Thay đổi AI Context
Chỉnh sửa `contextPrompt` trong `ChatbotServlet.java`:

```java
String contextPrompt = "Bạn là trợ lý AI của hệ thống đặt vé xe buýt BusBooking. " +
        "Hãy trả lời các câu hỏi về dịch vụ đặt vé xe, giá vé, tuyến đường, " +
        "cách đặt vé, thanh toán và các thông tin liên quan đến việc đi lại bằng xe buýt. " +
        "Trả lời bằng tiếng Việt và ngắn gọn, thân thiện. " +
        "Câu hỏi: " + userMessage;
```

### Thay đổi Styling
Chỉnh sửa `chatbot.css` để customize giao diện:
- Colors: Thay đổi gradient colors
- Size: Điều chỉnh kích thước chat window
- Position: Thay đổi vị trí chatbot

### Thêm Features
- Chat history persistence
- User authentication integration
- Quick reply buttons
- File upload support
- Voice input/output

## Dependencies

Đảm bảo có các dependencies trong `pom.xml`:
- `gson` - JSON processing
- `javax.servlet-api` - Servlet support
- Java 11+ cho HttpClient

## Performance Tips

1. **Caching**: Cache responses cho câu hỏi thường gặp
2. **Rate Limiting**: Giới hạn số requests per user
3. **Async Processing**: Xử lý requests không đồng bộ
4. **Connection Pooling**: Reuse HTTP connections

## Monitoring

- Log all API calls và responses
- Monitor response times
- Track user engagement metrics
- Monitor API quota usage