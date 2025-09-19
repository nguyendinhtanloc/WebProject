package com.busbooking.dao;

import com.busbooking.model.Trips;
import com.busbooking.util.DatabaseConnection;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class TripDAO {

    private static Map<String, String> columnMapping;

    static {
        try {
            // Đọc file JSON từ resources
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = TripDAO.class.getClassLoader().getResourceAsStream("mapping/trips_mapping.json");
            if (is != null) {
                columnMapping = mapper.readValue(is, Map.class);
            } else {
                throw new RuntimeException("Không tìm thấy file trips_mapping.json trong resources/mapping/");
            }
        } catch (Exception e) {
            e.printStackTrace();
            columnMapping = new HashMap<>();
        }
    }

    /**
     * Phương thức này lấy tất cả các chuyến xe từ cơ sở dữ liệu.
     * @return một danh sách (List) các đối tượng Trips.
     */
    public List<Trips> getAllTrips() {
        List<Trips> tripList = new ArrayList<>();
        String sql = "SELECT * FROM trips";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Trips trip = new Trips();

                // Ánh xạ bằng JSON mapping
                trip.setTripId(rs.getString(columnMapping.get("tripId")));
                trip.setCompanyId(rs.getString(columnMapping.get("companyId")));
                trip.setVehicleId(rs.getString(columnMapping.get("vehicleId")));
                trip.setDriverId(rs.getString(columnMapping.get("driverId")));
                trip.setDeparturePlace(rs.getString(columnMapping.get("departurePlace")));
                trip.setArrivalPlace(rs.getString(columnMapping.get("arrivalPlace")));
                trip.setDepartureDate(rs.getDate(columnMapping.get("departureDate")));
                trip.setDepartureTime(rs.getTime(columnMapping.get("departureTime")));
                trip.setPrice(rs.getFloat(columnMapping.get("price")));
                trip.setStatus(rs.getString(columnMapping.get("status")));

                tripList.add(trip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tripList;
    }
}
