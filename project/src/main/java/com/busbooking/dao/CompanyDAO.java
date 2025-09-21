package com.busbooking.dao;

import com.busbooking.model.Company; // Import model
import com.busbooking.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {
    public boolean companyExists(String companyId) throws SQLException {
        String sql = "SELECT 1 FROM bus_company WHERE company_id = ?::uuid";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // BỔ SUNG PHƯƠNG THỨC NÀY
    public List<Company> getAllCompanies() throws SQLException {
        List<Company> list = new ArrayList<>();
        String sql = "SELECT company_id, name FROM bus_company ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Company c = new Company();
                c.setCompanyId(rs.getString("company_id"));
                c.setName(rs.getString("name"));
                list.add(c);
            }
        }
        return list;
    }
}