package com.busbooking.dao;

import com.busbooking.model.Driver; // Import model
import com.busbooking.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {
    public boolean driverExists(String driverId) throws SQLException {
        String sql = "SELECT 1 FROM driver WHERE driver_id = ?::uuid";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, driverId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // BỔ SUNG PHƯƠNG THỨC NÀY
    public List<Driver> getAllDrivers() throws SQLException {
        List<Driver> list = new ArrayList<>();
        String sql = "SELECT driver_id, name FROM driver ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Driver d = new Driver();
                d.setDriverId(rs.getString("driver_id"));
                d.setName(rs.getString("name"));
                list.add(d);
            }
        }
        return list;
    }
}