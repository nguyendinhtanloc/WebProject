package com.busbooking.dao;

import com.busbooking.model.TripLog;
import com.busbooking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripLogDAO {

    /**
     * Ghi một thay đổi vào bảng trip_logs.
     * Phương thức này nên được gọi bên trong một transaction.
     */
    public void logChange(String action, String oldData, String newData, String email, Connection conn) throws SQLException {
        String sql = "INSERT INTO trip_logs (action, old_data, new_data, email) VALUES (?, ?::jsonb, ?::jsonb, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, action);
            ps.setString(2, oldData);
            ps.setString(3, newData);
            ps.setString(4, email);
            ps.executeUpdate();
        }
    }

    /**
     * Lấy danh sách log theo trang.
     */
    public List<TripLog> getLogsByPage(int pageNumber, int pageSize) throws SQLException {
        List<TripLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM trip_logs ORDER BY action_time DESC LIMIT ? OFFSET ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pageSize);
            ps.setInt(2, (pageNumber - 1) * pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TripLog log = new TripLog();
                    log.setId(rs.getLong("id"));
                    log.setAction(rs.getString("action"));
                    log.setTableName(rs.getString("table_name"));
                    log.setOldData(rs.getString("old_data"));
                    log.setNewData(rs.getString("new_data"));
                    log.setEmail(rs.getString("email"));
                    log.setActionTime(rs.getTimestamp("action_time").toLocalDateTime());
                    logs.add(log);
                }
            }
        }
        return logs;
    }

    /**
     * Lấy tổng số lượng log.
     */
    public int getTotalLogCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM trip_logs";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}