package com.busbooking.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp DatabaseConnection cung cấp phương thức để tạo kết nối mới đến CSDL.
 * Lớp này không còn áp dụng Singleton cho đối tượng Connection để tương thích
 * với try-with-resources trong DAO.
 */
public class DatabaseConnection {

    // Không cần biến static để lưu trữ connection nữa.

    /**
     * Phương thức static để lấy về một đối tượng kết nối CSDL MỚI.
     *
     * @return Một đối tượng Connection mới để tương tác với CSDL.
     * @throws SQLException nếu có lỗi khi kết nối.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load file .env từ classpath (resources/)
            Dotenv dotenv = Dotenv.load();

            // Lấy thông tin kết nối từ biến môi trường
            String url = dotenv.get("SUPABASE_DB_URL");
            String user = dotenv.get("SUPABASE_DB_USER");
            String pass = dotenv.get("SUPABASE_DB_PASS");

            // Đảm bảo driver PostgreSQL được load
            Class.forName("org.postgresql.Driver");

            // Tạo và trả về một kết nối MỚI mỗi lần được gọi
            return DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException e) {
            // Ném ra một SQLException để lớp gọi có thể xử lý
            throw new SQLException("Không tìm thấy PostgreSQL Driver!", e);
        }
    }

    // Phương thức closeConnection() không còn cần thiết và nên được xóa đi,
    // vì try-with-resources đã tự động quản lý việc đóng kết nối.
}