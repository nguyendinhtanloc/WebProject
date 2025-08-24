# Dự án Hệ thống Đặt Vé Xe

## Giới thiệu
Dự án xây dựng hệ thống web đặt vé xe buýt sử dụng **Java Servlet/JSP** kết hợp **Maven**.  
Hệ thống hỗ trợ:
- Người dùng: tìm chuyến, đặt vé, thanh toán và quản lý vé.
- Nhà xe: quản lý chuyến xe, tài xế, phương tiện, thông tin người dùng (đã đạt vé).
- Quản trị viên: quản lý toàn hệ thống và thống kê dữ liệu.

---

## Cấu trúc dự án

BusBooking/
│
├─ src/
│ ├─ main/
│ │ ├─ java/
│ │ │ ├─ controller/ # Các Servlet điều khiển
│ │ │ ├─ model/ # Các lớp mô hình
│ │ │ ├─ dao/ # Truy xuất dữ liệu
│ │ │ └─ util/ # Tiện ích (DB, Email, QRCode)
│ │ │
│ │ └─ webapp/
│ │ ├─ WEB-INF/
│ │ │ ├─ jsp/ # Giao diện JSP (auth, user, company, admin)
│ │ │ └─ web.xml # Cấu hình Servlet
│ │ └─ static/ # CSS, JS, Images
│ │
├─ pom.xml # Cấu hình Maven
├─ README.md
├─ report/
│ ├─ report.docx
│ ├─ report.ppt
└─ .gitignore

---

## Yêu cầu môi trường

- Java (>= 17)
- Apache Maven (>= 3.9)
- Apache Tomcat (>= 9)
- PostgreSQL

---

## Cách chạy dự án

1. **Truy cập folder dự án**
   ```bash
   cd <đường_dẫn_folder>
    ```

2. **Truy cập folder dự án**
    ```bash
    git clone <url>
    ```

3. **Build dự án và tạo file .war**
    ```bash
    cd BusBooking
    mvn clean package
    ```

4. **Deploy vào Tomcat**: Copy file .war vào thư mục Tomcat/webapps/

5. **Khởi động Tomcat**
   - MacOs/Linux: `./startup.sh`
   - Windows: `startup.bat`

6. **Dừng Tomcat**
   - MacOs/Linux: `./startup.sh`
   - Windows: `startup.bat`

---

## Quy tắc phát triển
### Quy tắc đặt tên nhánh (gitflow):
- `main` → code đã ổn định, sẵn sàng chạy production.
- `develop` → nhánh tích hợp tính năng
- `feature/*` → nhánh tính năng mới (vd: feature/backend).
- `release/*` → chuẩn bị phát hành (vd: release/v1.0).
- `hotfix/*` → sửa lỗi khẩn cấp trên production (vd: hotfix/payment-bug)
- `support/*` → hỗ trợ các phiên bản cũ.


### Quy tắc đặt tên code
- Tên package: chữ thường (vd: controller, dao).
- Tên class: PascalCase (vd: BookingServlet).
- Tên biến/hàm: camelCase (vd: getUserById()).

### Quy tắc commit:
- [feat] Thêm chức năng đặt vé.
- [fix] Sửa lỗi chọn ghế.
- [refactor] Cấu trúc lại DAO.
- [docs] Cập nhật README [test] Viết unit test cho TripDAO.

---

# Thành viên nhóm

| Họ và tên              | MSSV   |
|-------------------------|--------|
| Mai Quốc Thái           | 23133071       |
| Nguyễn Vũ Minh          | 23133044       |
| Nguyễn Tiến Đạt         | 23133016       |
| Đinh Hoàng Phương       | 23133059       |
| Nguyễn Đình Tấn Lộc     | 23133041       |
| Đoàn Cao Thái           | 23133070       |
| Nguyễn Lê Hoàng Kiệt    | 23133040       |
| Nguyễn Đức Thịnh        | 23133073       |

---

# Liên hệ

Nếu có bất kỳ thắc mắc nào vui lòng liên hệ qua eamil: `23133041@student.hcmute.edu.vn`
