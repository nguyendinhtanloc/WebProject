package com.busbooking.dao;

import com.busbooking.model.LoginLog;
import com.busbooking.util.DatabaseConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LoginLogDAO {
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    // Tạo log đăng nhập
    public long createLog(String email, String ip, String userAgent) throws SQLException {
        String sql = "INSERT INTO login_logs (email, ip_address, user_agent) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, ip);
            stmt.setString(3, userAgent);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            }
        }
        return -1;
    }

    // Cập nhật logout_time
    public void updateLogout(long logId) throws SQLException {
        String sql = "UPDATE login_logs SET logout_time = now() WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, logId);
            stmt.executeUpdate();
        }
    }

    // Lấy danh sách log
    public List<LoginLog> getLogs(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM login_logs ORDER BY login_time DESC LIMIT ? OFFSET ?";
        List<LoginLog> logs = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LoginLog log = new LoginLog();
                log.setId(rs.getLong("id"));
                log.setEmail(rs.getString("email"));

                Timestamp loginTs = rs.getTimestamp("login_time");
                if (loginTs != null) {
                    log.setLoginTimeFormatted(DATE_FORMAT.format(loginTs));
                }

                Timestamp logoutTs = rs.getTimestamp("logout_time");
                if (logoutTs != null) {
                    log.setLogoutTimeFormatted(DATE_FORMAT.format(logoutTs));
                } else {
                    log.setLogoutTimeFormatted("Chưa đăng xuất");
                }

                log.setIpAddress(rs.getString("ip_address"));
                log.setUserAgent(rs.getString("user_agent"));
                logs.add(log);
            }
        }
        return logs;
    }

    // Đếm tổng số log
    public int countLogs() throws SQLException {
        String sql = "SELECT COUNT(*) FROM login_logs";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
