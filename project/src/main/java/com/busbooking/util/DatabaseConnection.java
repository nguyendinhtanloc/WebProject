// Khai báo package
package com.busbooking.util;

// Import thư viện dotenv để đọc file .env trong resources
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp DatabaseConnection quản lý việc kết nối đến cơ sở dữ liệu.
 * Áp dụng Singleton để đảm bảo chỉ tạo một kết nối duy nhất trong suốt vòng đời ứng dụng.
 */
public class DatabaseConnection {
    // Biến static để lưu trữ đối tượng kết nối duy nhất
    private static Connection connection = null;

    /**
     * Phương thức static để lấy về đối tượng kết nối CSDL.
     *
     * @return Đối tượng Connection để tương tác với CSDL
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Load file .env từ classpath (resources/)
                Dotenv dotenv = Dotenv.load();

                // Lấy thông tin kết nối từ biến môi trường
                String url = dotenv.get("SUPABASE_DB_URL");
                String user = dotenv.get("SUPABASE_DB_USER");
                String pass = dotenv.get("SUPABASE_DB_PASS");

                // Đảm bảo driver PostgreSQL được load
                Class.forName("org.postgresql.Driver");

                // Tạo kết nối
                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("Kết nối thành công đến PostgreSQL!");

            } catch (SQLException ex) {
                System.out.println("Kết nối thất bại!");
                ex.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Không tìm thấy PostgreSQL Driver!");
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Phương thức static để đóng kết nối CSDL.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Ngắt kết nối thành công!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
