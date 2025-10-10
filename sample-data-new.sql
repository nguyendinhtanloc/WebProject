-- Script tạo dữ liệu mẫu cho cấu trúc database mới
-- Chạy script này trong Supabase SQL Editor

-- 1. Tạo dữ liệu mẫu cho bảng transportcompany
INSERT INTO transportcompany (name, address, contactinfo, contact, status, createdat) VALUES
('Nhà xe Phương Trang', '272 Đề Thám, Quận 1, TP.HCM', 'Website: futabus.vn', '1900 6067', 'active', NOW()),
('Nhà xe Thành Bưởi', '24 Nguyễn Trãi, Quận 1, TP.HCM', 'Website: thanhbuoi.vn', '028 3838 5858', 'active', NOW()),
('Nhà xe Hoàng Long', '123 Giải Phóng, Hà Nội', 'Website: hoanglong.vn', '024 3633 4666', 'active', NOW()),
('Nhà xe Mai Linh Express', '18 Nguyễn Huệ, Quận 1, TP.HCM', 'Website: mailinh.vn', '1900 6056', 'active', NOW());

-- 2. Tạo dữ liệu mẫu cho bảng vehicletransport
INSERT INTO vehicletransport (companyid, type, licenseplate, capacity, status, amenities, seatlayout) 
SELECT 
    tc.companyid,
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN 'Xe giường nằm'
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN 'Xe ghế ngồi'
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN 'Xe limousine'
        ELSE 'Xe giường nằm'
    END,
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN '51B-12345'
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN '51B-23456'
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN '30A-34567'
        ELSE '51B-45678'
    END,
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN 45
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN 40
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN 50
        ELSE 35
    END,
    'available',
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN 'WiFi, Điều hòa, Chăn gối'
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN 'WiFi, Điều hòa'
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN 'WiFi, Điều hòa, Massage, Giải trí'
        ELSE 'WiFi, Điều hòa, Chăn gối'
    END,
    '2-2'
FROM transportcompany tc;

-- 3. Tạo dữ liệu mẫu cho bảng drivertransport
INSERT INTO drivertransport (companyid, name, phone, licenseno, hiredate, status)
SELECT 
    tc.companyid,
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN 'Nguyễn Văn A'
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN 'Trần Văn B'
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN 'Lê Văn C'
        ELSE 'Phạm Văn D'
    END,
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN '0901234567'
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN '0912345678'
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN '0923456789'
        ELSE '0934567890'
    END,
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN 'B1-123456'
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN 'B2-234567'
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN 'B1-345678'
        ELSE 'B2-456789'
    END,
    '2020-01-01',
    'active'
FROM transportcompany tc;

-- 4. Tạo dữ liệu mẫu cho bảng triptransport
INSERT INTO triptransport (
    companyid, vehicleid, driverid, 
    departurepoint, departurecity, 
    arrivalpoint, arrivalcity, 
    distancekm, departuretime, departuredate, 
    arrivaldatetime, status, price
)
SELECT 
    tc.companyid,
    vt.vehicleid,
    dt.driverid,
    -- Departure info
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN 'Bến xe Miền Đông'
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN 'Bến xe Miền Tây'
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN 'Bến xe Mỹ Đình'
        ELSE 'Bến xe Giáp Bát'
    END,
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN 'TP. Hồ Chí Minh'
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN 'TP. Hồ Chí Minh'
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN 'Hà Nội'
        ELSE 'Hà Nội'
    END,
    -- Arrival info
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN 'Bến xe Đà Lạt'
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN 'Bến xe Cần Thơ'
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN 'Bến xe Hải Phòng'
        ELSE 'Bến xe Vinh'
    END,
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN 'Đà Lạt'
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN 'Cần Thơ'
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN 'Hải Phòng'
        ELSE 'Vinh'
    END,
    -- Distance and time
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN 300.0
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN 169.0
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN 102.0
        ELSE 291.0
    END,
    '08:00:00'::time,
    CURRENT_DATE + INTERVAL '1 day',
    CURRENT_DATE + INTERVAL '1 day' + INTERVAL '6 hours',
    'active',
    CASE 
        WHEN tc.name = 'Nhà xe Phương Trang' THEN 250000.0
        WHEN tc.name = 'Nhà xe Thành Bưởi' THEN 150000.0
        WHEN tc.name = 'Nhà xe Hoàng Long' THEN 120000.0
        ELSE 200000.0
    END
FROM transportcompany tc
JOIN vehicletransport vt ON tc.companyid = vt.companyid
JOIN drivertransport dt ON tc.companyid = dt.companyid;

-- 5. Thêm một số chuyến xe khác với thời gian khác nhau
INSERT INTO triptransport (
    companyid, vehicleid, driverid,
    departurepoint, departurecity,
    arrivalpoint, arrivalcity,
    distancekm, departuretime, departuredate,
    arrivaldatetime, status, price
) VALUES
-- Chuyến TP.HCM -> Đà Lạt 14:00
((SELECT companyid FROM transportcompany WHERE name = 'Nhà xe Phương Trang'),
 (SELECT vehicleid FROM vehicletransport WHERE licenseplate = '51B-12345'),
 (SELECT driverid FROM drivertransport WHERE name = 'Nguyễn Văn A'),
 'Bến xe Miền Đông', 'TP. Hồ Chí Minh',
 'Bến xe Đà Lạt', 'Đà Lạt',
 300.0, '14:00:00', CURRENT_DATE + INTERVAL '1 day',
 CURRENT_DATE + INTERVAL '1 day' + INTERVAL '20:00:00',
 'active', 250000.0),

-- Chuyến TP.HCM -> Cần Thơ 15:30
((SELECT companyid FROM transportcompany WHERE name = 'Nhà xe Thành Bưởi'),
 (SELECT vehicleid FROM vehicletransport WHERE licenseplate = '51B-23456'),
 (SELECT driverid FROM drivertransport WHERE name = 'Trần Văn B'),
 'Bến xe Miền Tây', 'TP. Hồ Chí Minh',
 'Bến xe Cần Thơ', 'Cần Thơ',
 169.0, '15:30:00', CURRENT_DATE + INTERVAL '1 day',
 CURRENT_DATE + INTERVAL '1 day' + INTERVAL '18:30:00',
 'active', 150000.0),

-- Chuyến Hà Nội -> Hải Phòng 09:30
((SELECT companyid FROM transportcompany WHERE name = 'Nhà xe Hoàng Long'),
 (SELECT vehicleid FROM vehicletransport WHERE licenseplate = '30A-34567'),
 (SELECT driverid FROM drivertransport WHERE name = 'Lê Văn C'),
 'Bến xe Mỹ Đình', 'Hà Nội',
 'Bến xe Hải Phòng', 'Hải Phòng',
 102.0, '09:30:00', CURRENT_DATE + INTERVAL '1 day',
 CURRENT_DATE + INTERVAL '1 day' + INTERVAL '11:30:00',
 'active', 120000.0);