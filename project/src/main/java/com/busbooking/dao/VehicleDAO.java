package com.busbooking.dao;

import com.busbooking.model.Vehicle; // Import model
import com.busbooking.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    public boolean vehicleExists(String vehicleId) throws SQLException {
        String sql = "SELECT 1 FROM vehicle WHERE vehicle_id = ?::uuid";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicleId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // BỔ SUNG PHƯƠNG THỨC NÀY
    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT vehicle_id, license_plate FROM vehicle ORDER BY license_plate";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vehicle v = new Vehicle();
                v.setVehicleId(rs.getString("vehicle_id"));
                v.setLicensePlate(rs.getString("license_plate"));
                list.add(v);
            }
        }
        return list;
    }
}