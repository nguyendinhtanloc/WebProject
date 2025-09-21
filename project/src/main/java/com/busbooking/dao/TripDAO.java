package com.busbooking.dao;

import com.busbooking.model.TripDetail;
import com.busbooking.model.Trips;
import com.busbooking.util.DatabaseConnection;
import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {
    private TripLogDAO tripLogDAO = new TripLogDAO();
    private Gson gson = new Gson();

    public void insertTrip(Trips trip, String userEmail) throws SQLException {
        String sql = "INSERT INTO trips (company_id, vehicle_id, driver_id, " +
                     "departure_place, arrival_place, departure_date, departure_time, price, status) " +
                     "VALUES (?::uuid, ?::uuid, ?::uuid, ?, ?, ?, ?, ?, ?::trip_status)";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
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

            String newDataJson = gson.toJson(trip);
            tripLogDAO.logChange("INSERT", null, newDataJson, userEmail, conn);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    public void updateTrip(Trips trip, String userEmail) throws SQLException {
        String sql = "UPDATE trips SET departure_place=?, arrival_place=?, " +
                     "departure_date=?, departure_time=?, price=?, status=?::trip_status " +
                     "WHERE trip_id=?::uuid";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            TripDetail oldTrip = getTripById(trip.getTripId());
            String oldDataJson = gson.toJson(oldTrip);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, trip.getDeparturePlace());
                ps.setString(2, trip.getArrivalPlace());
                ps.setDate(3, trip.getDepartureDate());
                ps.setTime(4, trip.getDepartureTime());
                ps.setFloat(5, trip.getPrice());
                ps.setString(6, trip.getStatus());
                ps.setString(7, trip.getTripId());
                ps.executeUpdate();
            }
            
            String newDataJson = gson.toJson(trip);
            tripLogDAO.logChange("UPDATE", oldDataJson, newDataJson, userEmail, conn);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public void deleteTrip(String tripId, String userEmail) throws SQLException {
        String sql = "DELETE FROM trips WHERE trip_id = ?::uuid";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            TripDetail oldTrip = getTripById(tripId);
            String oldDataJson = gson.toJson(oldTrip);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tripId);
                ps.executeUpdate();
            }
            
            tripLogDAO.logChange("DELETE", oldDataJson, null, userEmail, conn);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    // CÁC PHƯƠNG THỨC BÊN DƯỚI KHÔNG THAY ĐỔI
    public List<TripDetail> getTripsByPage(int pageNumber, int pageSize) throws SQLException {
        List<TripDetail> list = new ArrayList<>();
        String sql = "SELECT t.*, c.name as company_name, v.license_plate, d.name as driver_name " +
                     "FROM trips t " +
                     "JOIN bus_company c ON t.company_id = c.company_id " +
                     "JOIN vehicle v ON t.vehicle_id = v.vehicle_id " +
                     "JOIN driver d ON t.driver_id = d.driver_id " +
                     "ORDER BY t.departure_date DESC, t.departure_time DESC " +
                     "LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, pageSize);
            ps.setInt(2, (pageNumber - 1) * pageSize);

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
}