# Cập nhật Database Schema - Bus Booking System

## Tóm tắt thay đổi

Dự án đã được cập nhật để phù hợp với cấu trúc database mới với những thay đổi chính sau:

## 🗄️ Cấu trúc Database Mới

### 1. Bảng `transportcompany` (trước đây: `bus_company`)
- **companyid**: INTEGER IDENTITY (Primary Key)
- **name**: VARCHAR (NOT NULL)
- **address**: VARCHAR
- **contactinfo**: TEXT
- **contact**: TEXT  
- **status**: USER-DEFINED
- **createdat**: TIMESTAMP DEFAULT NOW()
- **updatedby**: INTEGER (Foreign Key → appuser.userid)

### 2. Bảng `vehicletransport` (trước đây: `vehicle`)
- **vehicleid**: INTEGER IDENTITY (Primary Key)
- **companyid**: INTEGER (Foreign Key → transportcompany.companyid)
- **type**: VARCHAR
- **licenseplate**: VARCHAR NOT NULL UNIQUE
- **capacity**: INTEGER CHECK (capacity > 0)
- **status**: USER-DEFINED
- **updatedby**: INTEGER (Foreign Key → appuser.userid)
- **amenities**: TEXT
- **seatlayout**: TEXT

### 3. Bảng `drivertransport` (trước đây: `driver`)
- **driverid**: INTEGER IDENTITY (Primary Key)
- **companyid**: INTEGER (Foreign Key → transportcompany.companyid)
- **name**: VARCHAR NOT NULL
- **phone**: VARCHAR
- **licenseno**: VARCHAR
- **hiredate**: DATE
- **enddate**: DATE
- **status**: USER-DEFINED
- **updatedby**: INTEGER (Foreign Key → appuser.userid)

### 4. Bảng `triptransport` (cập nhật quan trọng)
- **tripid**: INTEGER IDENTITY (Primary Key)
- **companyid**: INTEGER (Foreign Key → transportcompany.companyid)
- **vehicleid**: INTEGER (Foreign Key → vehicletransport.vehicleid)  
- **driverid**: INTEGER (Foreign Key → drivertransport.driverid)
- **departurepoint**: VARCHAR (điểm đi chi tiết)
- **departurecity**: VARCHAR (🎯 **thành phố đi - input chính**)
- **arrivalpoint**: VARCHAR (điểm đến chi tiết)
- **arrivalcity**: VARCHAR (🎯 **thành phố đến - input chính**)
- **arrivaladdress**: VARCHAR
- **distancekm**: DOUBLE PRECISION CHECK (distancekm >= 0)
- **departuretime**: TIME (thời gian khởi hành)
- **departuredate**: DATE (🎯 **ngày đi - input chính**)
- **arrivaldatetime**: TIMESTAMP
- **status**: USER-DEFINED
- **updatedby**: INTEGER (Foreign Key → appuser.userid)
- **price**: DOUBLE PRECISION

## 🔧 Cập nhật Code

### Entity Classes
1. **Trip.java** - Cập nhật mapping với cấu trúc mới:
   - Tách `departureDateTime` thành `departureDate` và `departureTime`
   - Thêm các trường mới: `price`, `arrivalAddress`, `distanceKm`
   - Mapping với bảng `triptransport`

2. **BusCompany.java** - Cập nhật:
   - Mapping với bảng `transportcompany`
   - ID type: Integer thay vì UUID
   - Thêm các trường: `contactInfo`, `createdAt`, `updatedBy`

3. **Vehicle.java** - Cập nhật:
   - Mapping với bảng `vehicletransport`
   - ID type: Integer thay vì UUID
   - Thêm các trường: `amenities`, `seatLayout`, `updatedBy`

4. **Driver.java** - Tạm thời bỏ qua do vấn đề encoding

### DAO Classes
1. **TripDAO.java** - Cập nhật logic tìm kiếm:
   - Method `searchTrips()`: Tìm theo `departureCity`, `arrivalCity`, `departureDate`
   - Method `getAllDepartureCities()`: Lấy danh sách thành phố đi
   - Method `getAllArrivalCities()`: Lấy danh sách thành phố đến
   - Thêm filter `status = 'active'`

### Servlet Classes
1. **HomeServlet.java**: Cập nhật để load `departureCities` và `arrivalCities`
2. **SearchServlet.java**: Cập nhật attributes cho JSP

### JSP Files
1. **index.jsp**: Cập nhật dropdown sử dụng `departureCities` và `arrivalCities`
2. **search-results.jsp**: 
   - Hiển thị `departureCity` và `arrivalCity`
   - Format thời gian hiển thị
   - Hiển thị thông tin bổ sung: distance, price, amenities

## 📊 Logic Tìm Kiếm Mới

### Input từ người dùng:
- **Điểm đi**: `departurecity` (ví dụ: "TP. Hồ Chí Minh", "Hà Nội")
- **Điểm đến**: `arrivalcity` (ví dụ: "Đà Lạt", "Hải Phòng") 
- **Ngày đi**: `departuredate` (ví dụ: "2025-10-11")

### Thông tin bổ sung hiển thị:
- **Điểm đi chi tiết**: `departurepoint` (ví dụ: "Bến xe Miền Đông")
- **Điểm đến chi tiết**: `arrivalpoint` (ví dụ: "Bến xe Đà Lạt")
- **Khoảng cách**: `distancekm` 
- **Giá vé**: `price`
- **Tiện ích**: `amenities` (WiFi, Điều hòa, Chăn gối, ...)

## 📝 Script Dữ Liệu Mẫu

File `sample-data-new.sql` đã được tạo với:
- 4 nhà xe mẫu
- Các tuyến phổ biến: TP.HCM → Đà Lạt, TP.HCM → Cần Thơ, Hà Nội → Hải Phòng
- Nhiều khung giờ khởi hành
- Thông tin chi tiết về xe và tiện ích

## ✅ Trạng thái Build

- ✅ Project compile thành công
- ✅ WAR file được tạo thành công
- ⚠️ Driver entity tạm thời bỏ qua do vấn đề encoding
- ⚠️ Cần cập nhật database với script mới trước khi test

## 🚀 Hướng dẫn Triển khai

1. **Cập nhật Database**:
   ```sql
   -- Chạy script sample-data-new.sql trong Supabase
   ```

2. **Deploy**:
   ```bash
   mvn clean package
   # Deploy file target/Web-project.war lên Tomcat
   ```

3. **Test**:
   - Truy cập trang chủ
   - Thử tìm kiếm: "TP. Hồ Chí Minh" → "Đà Lạt" 
   - Kiểm tra kết quả hiển thị

## 📋 Ghi chú

- Logic tìm kiếm bây giờ tập trung vào **thành phố** thay vì điểm cụ thể
- Thông tin chi tiết (bến xe, địa chỉ) được hiển thị như thông tin bổ sung
- Cấu trúc database mới linh hoạt hơn với nhiều trường thông tin
- Cần tạo lại Driver entity sau khi giải quyết vấn đề encoding