// Khai báo package, giúp nhóm các lớp tiện ích lại với nhau
package com.busbooking.util;

// Import các thư viện cần thiết
import io.github.cdimascio.dotenv.Dotenv; // Thư viện để đọc biến môi trường từ file .env

import java.sql.Connection;       // Lớp đại diện cho một kết nối tới cơ sở dữ liệu (CSDL)
import java.sql.DriverManager;    // Lớp quản lý danh sách các driver CSDL
import java.sql.SQLException;      // Lớp xử lý các lỗi liên quan đến CSDL

/**
 * Lớp DatabaseConnection quản lý việc kết nối đến cơ sở dữ liệu.
 * Lớp này áp dụng mẫu thiết kế Singleton để đảm bảo rằng chỉ có một kết nối duy nhất
 * được tạo và sử dụng trong suốt vòng đời của ứng dụng.
 */
public class DatabaseConnection {
    // Biến static để lưu trữ đối tượng kết nối duy nhất.
    // 'static' nghĩa là biến này thuộc về lớp chứ không thuộc về một đối tượng cụ thể nào.
    private static Connection connection = null;

    /**
     * Phương thức static để lấy về đối tượng kết nối CSDL.
     * Nếu kết nối chưa được tạo, nó sẽ tạo một kết nối mới.
     * Nếu đã có rồi, nó sẽ trả về kết nối hiện tại.
     *
     * @return Đối tượng Connection để tương tác với CSDL.
     */
    public static Connection getConnection() {
        // Chỉ tạo kết nối mới nếu biến 'connection' đang là null
        if (connection == null) {
            try {
                // Cấu hình và tải các biến môi trường từ file .env.
                // Việc này giúp bảo mật thông tin nhạy cảm (URL, user, pass) thay vì viết trực tiếp vào code.
                Dotenv dotenv = Dotenv.configure()
                        .directory("./") // Chỉ định thư mục chứa file .env (ở đây là thư mục gốc của dự án)
                        .ignoreIfMissing() // Bỏ qua nếu không tìm thấy file .env
                        .load();

                // Đọc các thông tin cần thiết để kết nối từ biến môi trường
                String url = dotenv.get("SUPABASE_DB_URL");
                String user = dotenv.get("SUPABASE_DB_USER");
                String pass = dotenv.get("SUPABASE_DB_PASS");

                // Sử dụng DriverManager để thiết lập kết nối tới CSDL với các thông tin đã lấy được
                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("Kết nối thành công!");

            } catch (SQLException ex) {
                // Nếu có lỗi xảy ra trong quá trình kết nối (sai URL, user, pass, ...),
                // in thông báo lỗi và chi tiết về lỗi đó.
                System.out.println("Kết nối thất bại!");
                ex.printStackTrace();
            }
        }
        // Trả về đối tượng kết nối (hoặc là cái mới tạo, hoặc là cái đã tồn tại)
        return connection;
    }

    /**
     * Phương thức static để đóng kết nối CSDL.
     * Việc đóng kết nối khi không sử dụng nữa là rất quan trọng để giải phóng tài nguyên.
     */
    public static void closeConnection() {
        try {
            // Kiểm tra để chắc chắn rằng có một kết nối đang tồn tại và nó chưa được đóng
            // trước khi thực hiện việc đóng nó.
            if (connection != null && !connection.isClosed()) {
                connection.close(); // Đóng kết nối
                System.out.println("Ngắt kết nối thành công!");
            }
        } catch (SQLException ex) {
            // Bắt và in ra lỗi nếu có sự cố trong quá trình đóng kết nối
            ex.printStackTrace();
        }
    }
}