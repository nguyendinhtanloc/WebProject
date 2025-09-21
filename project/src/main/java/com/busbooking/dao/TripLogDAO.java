// package com.busbooking.dao;

// import com.busbooking.model.TripLog;
// import com.busbooking.util.DatabaseConnection;

// import java.sql.*;
// import java.text.SimpleDateFormat;
// import java.util.ArrayList;
// import java.util.List;

// public class TripLogDAO {
//     private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

//     // Ghi log
//     public void createLog(String action, String tableName, String oldData, String newData, String email) throws SQLException {
//         String sql = "INSERT INTO trip_logs (action, table_name, old_data, new_data, email) VALUES (?, ?, ?::jsonb, ?::jsonb, ?)";
//         try (Connection conn = DatabaseConnection.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {
//             stmt.setString(1, action);
//             stmt.setString(2, tableName);
//             stmt.setString(3, oldData);
//             stmt.setString(4, newData);
//             stmt.setString(5, email);
//             stmt.executeUpdate();
//         }
//     }

//     // Lấy danh sách log
//     public List<TripLog> getLogs(int offset, int limit) throws SQLException {
//         String sql = "SELECT * FROM trip_logs ORDER BY action_time DESC LIMIT ? OFFSET ?";
//         List<TripLog> logs = new ArrayList<>();

//         try (Connection conn = DatabaseConnection.getConnection();
//              PreparedStatement stmt = conn.prepareStatement(sql)) {
//             stmt.setInt(1, limit);
//             stmt.setInt(2, offset);

//             ResultSet rs = stmt.executeQuery();
//             while (rs.next()) {
//                 TripLog log = new TripLog();
//                 log.setId(rs.getLong("id"));
//                 log.setAction(rs.getString("action"));
//                 log.setTableName(rs.getString("table_name"));
//                 log.setOldData(rs.getString("old_data"));
//                 log.setNewData(rs.getString("new_data"));
//                 log.setEmail(rs.getString("email"));

//                 Timestamp ts = rs.getTimestamp("action_time");
//                 if (ts != null) {
//                     log.setActionTimeFormatted(DATE_FORMAT.format(ts));
//                 }

//                 logs.add(log);
//             }
//         }
//         return logs;
//     }

//     // Đếm tổng số log
//     public int countLogs() throws SQLException {
//         String sql = "SELECT COUNT(*) FROM trip_logs";
//         try (Connection conn = DatabaseConnection.getConnection();
//              Statement stmt = conn.createStatement();
//              ResultSet rs = stmt.executeQuery(sql)) {
//             if (rs.next()) return rs.getInt(1);
//         }
//         return 0;
//     }
// }
