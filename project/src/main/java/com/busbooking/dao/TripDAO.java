// File: src/main/java/com/busbooking/dao/TripDAO.java

package com.busbooking.dao;

import com.busbooking.model.TripDetail;
import com.busbooking.model.Trips;
import com.busbooking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {

    // (Phương thức insertTrip, updateTrip, deleteTrip không thay đổi)
    public void insertTrip(Trips trip) throws SQLException {
        String sql = "INSERT INTO trips (company_id, vehicle_id, driver_id, " +
                     "departure_place, arrival_place, departure_date, departure_time, price, status) " +
                     "VALUES (?::uuid, ?::uuid, ?::uuid, ?, ?, ?, ?, ?, ?::trip_status)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trip.getCompanyId());
            ps.setString(2, trip.getVehicleId());
            ps.setString(3, trip.getDriverId());
            ps.setString(4, trip.getDeparturePlace());
            ps.setString(5, trip.getArrivalPlace());
            ps.setDate(6, trip.getDepartureDate());
            ps.setTime(7, trip.getDepartureTime());
            ps.setFloat(8, trip.getPrice());
            ps.setString(9, trip.getStatus());
            ps.executeUpdate();
        }
    }

    // ======================== PHẦN ĐƯỢC CHỈNH SỬA 1 ========================
    /**
     * Lấy danh sách các chuyến đi chi tiết cho một trang cụ thể.
     * @param pageNumber Số của trang cần lấy (bắt đầu từ 1).
     * @param pageSize Số lượng chuyến đi trên mỗi trang.
     * @return Danh sách các chuyến đi chi tiết.
     */
    public List<TripDetail> getTripsByPage(int pageNumber, int pageSize) throws SQLException {
        List<TripDetail> list = new ArrayList<>();
        // Thêm ORDER BY để đảm bảo thứ tự nhất quán và LIMIT/OFFSET để phân trang
        String sql = "SELECT t.*, c.name as company_name, v.license_plate, d.name as driver_name " +
                     "FROM trips t " +
                     "JOIN bus_company c ON t.company_id = c.company_id " +
                     "JOIN vehicle v ON t.vehicle_id = v.vehicle_id " +
                     "JOIN driver d ON t.driver_id = d.driver_id " +
                     "ORDER BY t.departure_date DESC, t.departure_time DESC " + // Sắp xếp để có thứ tự ổn định
                     "LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, pageSize); // LIMIT
            ps.setInt(2, (pageNumber - 1) * pageSize); // OFFSET

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TripDetail td = new TripDetail();
                    td.setTripId(rs.getString("trip_id"));
                    td.setCompanyId(rs.getString("company_id"));
                    td.setVehicleId(rs.getString("vehicle_id"));
                    td.setDriverId(rs.getString("driver_id"));
                    td.setDeparturePlace(rs.getString("departure_place"));
                    td.setArrivalPlace(rs.getString("arrival_place"));
                    td.setDepartureDate(rs.getDate("departure_date"));
                    td.setDepartureTime(rs.getTime("departure_time"));
                    td.setPrice(rs.getFloat("price"));
                    td.setStatus(rs.getString("status"));
                    td.setCompanyName(rs.getString("company_name"));
                    td.setVehicleLicensePlate(rs.getString("license_plate"));
                    td.setDriverName(rs.getString("driver_name"));
                    list.add(td);
                }
            }
        }
        return list;
    }

    // ======================== PHẦN ĐƯỢC BỔ SUNG ========================
    /**
     * Lấy tổng số lượng chuyến đi trong cơ sở dữ liệu.
     * @return Tổng số chuyến đi.
     */
    public int getTotalTripCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM trips";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public TripDetail getTripById(String tripId) throws SQLException {
        String sql = "SELECT t.*, c.name as company_name, v.license_plate, d.name as driver_name " +
                     "FROM trips t " +
                     "JOIN bus_company c ON t.company_id = c.company_id " +
                     "JOIN vehicle v ON t.vehicle_id = v.vehicle_id " +
                     "JOIN driver d ON t.driver_id = d.driver_id " +
                     "WHERE t.trip_id = ?::uuid";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tripId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TripDetail td = new TripDetail();
                    td.setTripId(rs.getString("trip_id"));
                    td.setCompanyId(rs.getString("company_id"));
                    td.setVehicleId(rs.getString("vehicle_id"));
                    td.setDriverId(rs.getString("driver_id"));
                    td.setDeparturePlace(rs.getString("departure_place"));
                    td.setArrivalPlace(rs.getString("arrival_place"));
                    td.setDepartureDate(rs.getDate("departure_date"));
                    td.setDepartureTime(rs.getTime("departure_time"));
                    td.setPrice(rs.getFloat("price"));
                    td.setStatus(rs.getString("status"));
                    td.setCompanyName(rs.getString("company_name"));
                    td.setVehicleLicensePlate(rs.getString("license_plate"));
                    td.setDriverName(rs.getString("driver_name"));
                    return td;
                }
            }
        }
        return null;
    }

    public void updateTrip(Trips trip) throws SQLException {
        String sql = "UPDATE trips SET departure_place=?, arrival_place=?, " +
                     "departure_date=?, departure_time=?, price=?, status=?::trip_status " +
                     "WHERE trip_id=?::uuid";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trip.getDeparturePlace());
            ps.setString(2, trip.getArrivalPlace());
            ps.setDate(3, trip.getDepartureDate());
            ps.setTime(4, trip.getDepartureTime());
            ps.setFloat(5, trip.getPrice());
            ps.setString(6, trip.getStatus());
            ps.setString(7, trip.getTripId()); 
            ps.executeUpdate();
        }
    }

    public void deleteTrip(String tripId) throws SQLException {
        String sql = "DELETE FROM trips WHERE trip_id = ?::uuid";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tripId);
            ps.executeUpdate();
        }
    }
}
