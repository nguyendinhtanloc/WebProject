package com.busbooking.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class chứa danh sách 63 tỉnh thành Việt Nam
 */
public class VietnamProvinces {
    
    private static final List<String> PROVINCES = Arrays.asList(
        "An Giang",
        "Bà Rịa - Vũng Tàu", 
        "Bắc Giang",
        "Bắc Kạn",
        "Bạc Liêu",
        "Bắc Ninh",
        "Bến Tre",
        "Bình Định",
        "Bình Dương",
        "Bình Phước",
        "Bình Thuận",
        "Cà Mau",
        "Cao Bằng",
        "Đắk Lắk",
        "Đắk Nông",
        "Điện Biên",
        "Đồng Nai",
        "Đồng Tháp",
        "Gia Lai",
        "Hà Giang",
        "Hà Nam",
        "Hà Tĩnh",
        "Hải Dương",
        "Hậu Giang",
        "Hòa Bình",
        "Hưng Yên",
        "Khánh Hòa",
        "Kiên Giang",
        "Kon Tum",
        "Lai Châu",
        "Lâm Đồng",
        "Lạng Sơn",
        "Lào Cai",
        "Long An",
        "Nam Định",
        "Nghệ An",
        "Ninh Bình",
        "Ninh Thuận",
        "Phú Thọ",
        "Quảng Bình",
        "Quảng Nam",
        "Quảng Ngãi",
        "Quảng Ninh",
        "Quảng Trị",
        "Sóc Trăng",
        "Sơn La",
        "Tây Ninh",
        "Thái Bình",
        "Thái Nguyên",
        "Thanh Hóa",
        "Thừa Thiên Huế",
        "Tiền Giang",
        "Trà Vinh",
        "Tuyên Quang",
        "Vĩnh Long",
        "Vĩnh Phúc",
        "Yên Bái",
        "Phú Yên",
        "Cần Thơ",
        "Đà Nẵng",
        "Hải Phòng",
        "Hà Nội",
        "TP. Hồ Chí Minh"
    );
    
    /**
     * Lấy danh sách tất cả các tỉnh thành Việt Nam
     * @return Danh sách 63 tỉnh thành được sắp xếp theo thứ tự alphabet
     */
    public static List<String> getAllProvinces() {
        return PROVINCES;
    }
    
    /**
     * Kiểm tra xem một tỉnh thành có tồn tại trong danh sách không
     * @param provinceName Tên tỉnh thành cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    public static boolean isValidProvince(String provinceName) {
        if (provinceName == null || provinceName.trim().isEmpty()) {
            return false;
        }
        return PROVINCES.contains(provinceName.trim());
    }
    
    /**
     * Tìm kiếm các tỉnh thành theo từ khóa
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách các tỉnh thành chứa từ khóa
     */
    public static List<String> searchProvinces(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProvinces();
        }
        
        String lowerKeyword = keyword.toLowerCase().trim();
        return PROVINCES.stream()
                .filter(province -> province.toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
}