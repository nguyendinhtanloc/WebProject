package com.busbooking.dao;

import com.busbooking.model.Trips;
import com.busbooking.util.DatabaseConnection; // Sử dụng lớp kết nối của bạn

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {

    /**
     * Phương thức này lấy tất cả các chuyến xe từ cơ sở dữ liệu.
     * @return một danh sách (List) các đối tượng Trips.
     */
    public List<Trips> getAllTrips() {
        // Khởi tạo một danh sách rỗng để chứa các chuyến xe
        List<Trips> tripList = new ArrayList<>();
        // Câu lệnh SQL để chọn tất cả các cột từ bảng trips
        String sql = "SELECT * FROM trips";
        
        // Sử dụng try-with-resources để đảm bảo Connection, PreparedStatement và ResultSet được đóng tự động
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            // Lặp qua từng dòng kết quả trả về từ câu lệnh SQL
            while (rs.next()) {
                // Tạo một đối tượng Trips mới cho mỗi dòng
                Trips trip = new Trips();

                // Đọc dữ liệu từ ResultSet và gán vào đối tượng trip
                trip.setTripId(rs.getString("tripId"));
                trip.setCompanyId(rs.getString("companyId"));
                trip.setVehicleId(rs.getString("vehicleId"));
                trip.setDriverId(rs.getString("driverId"));
                trip.setDeparturePlace(rs.getString("departurePlace"));
                trip.setArrivalPlace(rs.getString("arrivalPlace"));
                trip.setDepartureDate(rs.getDate("departureDate"));
                trip.setDepartureTime(rs.getTime("departureTime"));
                trip.setPrice(rs.getFloat("price"));
                trip.setStatus(rs.getString("status"));

                // Thêm đối tượng trip đã có dữ liệu vào danh sách
                tripList.add(trip);
            }
        } catch (SQLException e) {
            // In ra lỗi nếu có sự cố xảy ra khi tương tác với database
            e.printStackTrace();
        }
        
        // Trả về danh sách các chuyến xe
        return tripList;
    }
}