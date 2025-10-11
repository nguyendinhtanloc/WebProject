# Bus Booking Web Application

Ứng dụng web đặt vé xe khách trực tuyến được xây dựng bằng Java Servlet và JPA.

## Tính năng chính

- 🏠 **Trang chủ hiện đại**: Giao diện responsive với thiết kế chuyên nghiệp
- 🔍 **Tìm kiếm chuyến xe**: Tìm kiếm theo điểm đi, điểm đến và ngày đi
- 📱 **Responsive Design**: Tương thích với tất cả các thiết bị
- 🎨 **UI/UX hiện đại**: Sử dụng CSS3 và Font Awesome icons

## Công nghệ sử dụng

- **Backend**: Java Servlet, JPA/Hibernate
- **Database**: PostgreSQL (Supabase)
- **Frontend**: JSP, HTML5, CSS3, JavaScript
- **Build Tool**: Maven
- **Server**: Apache Tomcat

## Cấu trúc dự án

```
src/
├── main/
│   ├── java/
│   │   └── com/busbooking/
│   │       ├── entity/          # JPA Entities
│   │       ├── dao/             # Data Access Objects  
│   │       ├── servlet/         # Servlets
│   │       └── util/            # Utility classes
│   ├── resources/
│   │   └── META-INF/
│   │       └── persistence.xml  # JPA Configuration
│   └── webapp/
│       ├── css/                 # Stylesheets
│       ├── js/                  # JavaScript files
│       ├── index.jsp            # Trang chủ
│       ├── search-results.jsp   # Trang kết quả tìm kiếm
│       └── WEB-INF/
│           └── web.xml          # Web configuration
```

## Hướng dẫn cài đặt

### 1. Yêu cầu hệ thống

- Java 11+
- Maven 3.6+
- Apache Tomcat 9+
- PostgreSQL (hoặc Supabase)

### 2. Cấu hình cơ sở dữ liệu

1. Mở file `src/main/resources/META-INF/persistence.xml`
2. Cập nhật thông tin kết nối Supabase:

```xml
<property name="javax.persistence.jdbc.url" 
         value="jdbc:postgresql://YOUR_SUPABASE_URL:5432/postgres"/>
<property name="javax.persistence.jdbc.user" 
         value="YOUR_USERNAME"/>
<property name="javax.persistence.jdbc.password" 
         value="YOUR_PASSWORD"/>
```

### 3. Build và Deploy

1. Build project:
```bash
mvn clean compile
```

2. Package WAR file:
```bash
mvn package
```

3. Deploy to Tomcat:
   - Copy file `target/Web-project.war` vào thư mục `webapps` của Tomcat
   - Hoặc sử dụng Tomcat Manager để deploy

### 4. Chạy ứng dụng

1. Start Tomcat server
2. Truy cập: `http://localhost:8080/Web-project`

## Cấu trúc cơ sở dữ liệu

Ứng dụng sử dụng các bảng chính:

- `trips`: Thông tin chuyến xe
- `bus_company`: Thông tin nhà xe
- `vehicle`: Thông tin xe
- `driver`: Thông tin tài xế

## API Endpoints

- `GET /`: Trang chủ
- `GET|POST /search`: Tìm kiếm chuyến xe

## Tính năng sẽ phát triển

- [ ] Đặt vé trực tuyến
- [ ] Quản lý người dùng
- [ ] Thanh toán trực tuyến
- [ ] Quản lý vé đã đặt
- [ ] Hệ thống đánh giá
- [ ] Thông báo real-time

## Thông tin liên hệ

Nếu có bất kỳ câu hỏi nào về dự án, vui lòng liên hệ:

- Email: [email của bạn]
- GitHub: [github của bạn]

## License

MIT License