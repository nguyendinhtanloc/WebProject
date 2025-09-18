// Gói (package) đã được cập nhật chính xác theo cấu trúc project của bạn
package com.busbooking.util;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;

/**
 * Lớp tiện ích để tạo mã hash (băm) cho các file tài nguyên tĩnh (assets) như CSS, JS.
 * Mục đích chính là để thực hiện "cache busting", buộc trình duyệt tải lại file
 * khi nội dung của nó thay đổi, thay vì sử dụng phiên bản cũ từ cache.
 */
public class AssetHasher {

    /**
     * Một bộ đệm (cache) để lưu trữ các mã hash đã được tính toán.
     * Key là đường dẫn của asset (ví dụ: "/css/style.css"), Value là mã hash tương ứng.
     * Sử dụng ConcurrentHashMap để đảm bảo an toàn khi nhiều luồng (thread) truy cập cùng lúc
     * trong môi trường web server.
     */
    private static final Map<String, String> HASH_CACHE = new ConcurrentHashMap<>();

    /**
     * Lấy mã hash MD5 của một file asset.
     * Nếu hash đã có trong cache, nó sẽ được trả về ngay lập tức để tăng hiệu suất.
     * Nếu không, phương thức sẽ đọc file, tính toán hash, lưu vào cache và trả về.
     *
     * @param context   Đối tượng ServletContext để lấy đường dẫn vật lý của file trên server.
     * @param assetPath Đường dẫn tương đối của file asset (ví dụ: "/css/main.css").
     * @return Một chuỗi hash MD5 (32 ký tự hex) của nội dung file, hoặc một chuỗi timestamp
     * nếu có lỗi xảy ra hoặc không tìm thấy file.
     */
    public static String getHash(ServletContext context, String assetPath) {
        // BƯỚC 1: Kiểm tra xem hash đã có trong cache chưa. Nếu có, trả về ngay.
        if (HASH_CACHE.containsKey(assetPath)) {
            return HASH_CACHE.get(assetPath);
        }

        // BƯỚC 2: Nếu chưa có trong cache, tiến hành đọc file và tạo hash.
        try {
            // Lấy đường dẫn vật lý tuyệt đối của file trên ổ đĩa của server.
            // Ví dụ: "/css/style.css" -> "C:\tomcat\webapps\my-app\css\style.css"
            String realPath = context.getRealPath(assetPath);

            // Cơ chế dự phòng (fallback): Nếu không tìm thấy file (realPath là null),
            // trả về timestamp hiện tại dưới dạng chuỗi. Điều này đảm bảo trình duyệt
            // sẽ luôn yêu cầu một phiên bản mới vì URL luôn thay đổi.
            if (realPath == null) {
                return String.valueOf(System.currentTimeMillis());
            }

            // Đọc toàn bộ nội dung của file thành một mảng byte.
            byte[] fileBytes = Files.readAllBytes(Paths.get(realPath));

            // Khởi tạo đối tượng để thực hiện băm bằng thuật toán MD5.
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Thực hiện băm nội dung file. Kết quả là một mảng byte.
            byte[] messageDigest = md.digest(fileBytes);

            // Chuyển mảng byte của hash thành một số BigInteger dương.
            BigInteger no = new BigInteger(1, messageDigest);

            // Chuyển số BigInteger thành chuỗi Hexadecimal (hệ 16).
            String hashtext = no.toString(16);

            // Một mã hash MD5 chuẩn luôn có 32 ký tự. Vòng lặp này đảm bảo
            // chuỗi hash luôn đủ 32 ký tự bằng cách thêm các số "0" vào đầu nếu cần.
            // (Ví dụ: nếu hash bắt đầu bằng byte 0, chuỗi hex có thể bị ngắn hơn).
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // Lưu mã hash vừa tạo vào cache để các lần gọi tiếp theo không cần tính toán lại.
            HASH_CACHE.put(assetPath, hashtext);
            return hashtext;

        } catch (Exception e) {
            // Xử lý ngoại lệ: Nếu có bất kỳ lỗi nào xảy ra (ví dụ: không đọc được file,
            // thuật toán hash không tồn tại), in ra lỗi và trả về timestamp hiện tại
            // như một cơ chế dự phòng an toàn.
            e.printStackTrace();
            return String.valueOf(System.currentTimeMillis());
        }
    }
}