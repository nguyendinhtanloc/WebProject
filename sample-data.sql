-- Script để tạo dữ liệu mẫu cho testing
-- Chạy script này trong Supabase SQL Editor

-- 1. Tạo dữ liệu mẫu cho bảng bus_company
INSERT INTO bus_company (name, address, contact, status) VALUES
('Nhà xe Phương Trang', '272 Đề Thám, Quận 1, TP.HCM', '1900 6067', 'active'),
('Nhà xe Thành Bưởi', '24 Nguyễn Trãi, Quận 1, TP.HCM', '028 3838 5858', 'active'),
('Nhà xe Hoàng Long', '123 Giải Phóng, Hà Nội', '024 3633 4666', 'active'),
('Nhà xe Mai Linh Express', '18 Nguyễn Huệ, Quận 1, TP.HCM', '1900 6056', 'active');

-- 2. Tạo dữ liệu mẫu cho bảng vehicle
INSERT INTO vehicle (company_id, license_plate, capacity, type, status, lane) 
SELECT 
    c.company_id,
    CASE 
        WHEN c.name = 'Nhà xe Phương Trang' THEN '51B-12345'
        WHEN c.name = 'Nhà xe Thành Bưởi' THEN '51B-23456'
        WHEN c.name = 'Nhà xe Hoàng Long' THEN '30A-34567'
        ELSE '51B-45678'
    END,
    CASE 
        WHEN c.name = 'Nhà xe Phương Trang' THEN 45
        WHEN c.name = 'Nhà xe Thành Bưởi' THEN 40
        WHEN c.name = 'Nhà xe Hoàng Long' THEN 50
        ELSE 35
    END,
    CASE 
        WHEN c.name = 'Nhà xe Phương Trang' THEN 'Xe giường nằm'
        WHEN c.name = 'Nhà xe Thành Bưởi' THEN 'Xe ghế ngồi'
        WHEN c.name = 'Nhà xe Hoàng Long' THEN 'Xe limousine'
        ELSE 'Xe giường nằm'
    END,
    'available',
    3
FROM bus_company c;

-- 3. Tạo dữ liệu mẫu cho bảng driver
INSERT INTO driver (company_id, name, phone, license_no, experience_years, status)
SELECT 
    c.company_id,
    CASE 
        WHEN c.name = 'Nhà xe Phương Trang' THEN 'Nguyễn Văn A'
        WHEN c.name = 'Nhà xe Thành Bưởi' THEN 'Trần Văn B'
        WHEN c.name = 'Nhà xe Hoàng Long' THEN 'Lê Văn C'
        ELSE 'Phạm Văn D'
    END,
    CASE 
        WHEN c.name = 'Nhà xe Phương Trang' THEN '0901234567'
        WHEN c.name = 'Nhà xe Thành Bưởi' THEN '0912345678'
        WHEN c.name = 'Nhà xe Hoàng Long' THEN '0923456789'
        ELSE '0934567890'
    END,
    CASE 
        WHEN c.name = 'Nhà xe Phương Trang' THEN 'B1-123456'
        WHEN c.name = 'Nhà xe Thành Bưởi' THEN 'B2-234567'
        WHEN c.name = 'Nhà xe Hoàng Long' THEN 'B1-345678'
        ELSE 'B2-456789'
    END,
    CASE 
        WHEN c.name = 'Nhà xe Phương Trang' THEN 8
        WHEN c.name = 'Nhà xe Thành Bưởi' THEN 12
        WHEN c.name = 'Nhà xe Hoàng Long' THEN 5
        ELSE 10
    END,
    'active'
FROM bus_company c;

-- 4. Tạo dữ liệu mẫu cho bảng trips
INSERT INTO trips (company_id, vehicle_id, driver_id, departure_place, arrival_place, departure_date, departure_time, price, status)
SELECT 
    c.company_id,
    v.vehicle_id,
    d.driver_id,
    'Hồ Chí Minh',
    'Hà Nội',
    CURRENT_DATE + INTERVAL '1 day',
    '08:00:00',
    800000,
    'scheduled'
FROM bus_company c
JOIN vehicle v ON c.company_id = v.company_id
JOIN driver d ON c.company_id = d.company_id;

INSERT INTO trips (company_id, vehicle_id, driver_id, departure_place, arrival_place, departure_date, departure_time, price, status)
SELECT 
    c.company_id,
    v.vehicle_id,
    d.driver_id,
    'Hà Nội',
    'Hồ Chí Minh',
    CURRENT_DATE + INTERVAL '1 day',
    '20:00:00',
    750000,
    'scheduled'
FROM bus_company c
JOIN vehicle v ON c.company_id = v.company_id
JOIN driver d ON c.company_id = d.company_id;

INSERT INTO trips (company_id, vehicle_id, driver_id, departure_place, arrival_place, departure_date, departure_time, price, status)
SELECT 
    c.company_id,
    v.vehicle_id,
    d.driver_id,
    'Hồ Chí Minh',
    'Đà Nẵng',
    CURRENT_DATE + INTERVAL '2 days',
    '07:30:00',
    450000,
    'scheduled'
FROM bus_company c
JOIN vehicle v ON c.company_id = v.company_id
JOIN driver d ON c.company_id = d.company_id;

INSERT INTO trips (company_id, vehicle_id, driver_id, departure_place, arrival_place, departure_date, departure_time, price, status)
SELECT 
    c.company_id,
    v.vehicle_id,
    d.driver_id,
    'Đà Nẵng',
    'Hồ Chí Minh',
    CURRENT_DATE + INTERVAL '2 days',
    '14:00:00',
    500000,
    'scheduled'
FROM bus_company c
JOIN vehicle v ON c.company_id = v.company_id
JOIN driver d ON c.company_id = d.company_id;

-- Thêm một số chuyến xe khác để có nhiều lựa chọn
INSERT INTO trips (company_id, vehicle_id, driver_id, departure_place, arrival_place, departure_date, departure_time, price, status)
SELECT 
    c.company_id,
    v.vehicle_id,
    d.driver_id,
    'Hồ Chí Minh',
    'Nha Trang',
    CURRENT_DATE + INTERVAL '1 day',
    '22:00:00',
    350000,
    'scheduled'
FROM bus_company c
JOIN vehicle v ON c.company_id = v.company_id
JOIN driver d ON c.company_id = d.company_id
WHERE c.name = 'Nhà xe Phương Trang';

INSERT INTO trips (company_id, vehicle_id, driver_id, departure_place, arrival_place, departure_date, departure_time, price, status)
SELECT 
    c.company_id,
    v.vehicle_id,
    d.driver_id,
    'Hà Nội',
    'Đà Nẵng',
    CURRENT_DATE + INTERVAL '3 days',
    '21:30:00',
    600000,
    'scheduled'
FROM bus_company c
JOIN vehicle v ON c.company_id = v.company_id
JOIN driver d ON c.company_id = d.company_id
WHERE c.name = 'Nhà xe Hoàng Long';