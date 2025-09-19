package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    // Thông tin kết nối Supabase (Postgres) - Direct Connection
	private static final String URL = "jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:6543/postgres";
	private static final String USER = "postgres.kognqhcifxbpihjrwocg";
	private static final String PASSWORD = "c4tVS1Zmr1O1hhRR";
	private static Connection connection; 


    // Mở kết nối
    public static Connection openConnection() {
        try {
            // Load driver PostgreSQL
            Class.forName("org.postgresql.Driver");
            // Mở kết nối
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối đến Supabase thành công!");
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy driver PostgreSQL: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối database: " + e.getMessage());
        }
        return connection;
    }

    // Đóng kết nối
    public static void closeConnection() {
        try {
        	if (connection != null && !connection.isClosed()) {
        		connection.close();
        	}
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        System.out.println(ConnectionDB.openConnection());
//    }
}