package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import data.ConnectionDB;
import model.Driver;

public class DriverService {
	public static List<Driver> selectAll() {
		Statement statement = null;
		ResultSet resultSet = null;
		List<Driver> driverList = new ArrayList<Driver>();
		try {
			Connection connection =ConnectionDB.openConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from driver");
			while (resultSet.next()) {
				String driver_id = resultSet.getString("driver_id");
				String company_id = resultSet.getString("company_id");
				String name = resultSet.getString("name");
				String phone = resultSet.getString("phone");
				String license_no = resultSet.getString("license_no");
				int experience_years = resultSet.getInt("experience_years");
				String status = resultSet.getString("status");
				Driver driver = new Driver(driver_id, company_id, name, phone, license_no, experience_years, status);
				driverList.add(driver);
			}
		}
		catch(Exception ex) {
			System.out.println("Loi truy van");
			ex.printStackTrace();
		}
		finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				ConnectionDB.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return driverList;
	}
	public static boolean insert(Driver driver) {
	    PreparedStatement statement = null;
	    try {
	        Connection connection = ConnectionDB.openConnection();
	        String query = "insert into driver(driver_id, company_id, name, phone, license_no, experience_years, status) values(gen_random_uuid(), ?, ?, ?, ?, ?, ?)";
	        statement = connection.prepareStatement(query);
	        
	        // Chuyển đổi company_id từ String sang UUID cho PreparedStatement
	        statement.setObject(1, UUID.fromString(driver.getCompany_id()));
	        statement.setString(2, driver.getName());
	        statement.setString(3, driver.getPhone());
	        statement.setString(4, driver.getLicense_no());
	        statement.setInt(5, driver.getExperience_years());
	        statement.setString(6, driver.getStatus());
	        
	        int rowsInserted = statement.executeUpdate();
	        return rowsInserted > 0;
	    } catch(Exception ex){
	        System.out.println("Error in insert: " + ex.getMessage());
	        ex.printStackTrace();
	        return false;
	    } finally {
	        try {
	            if (statement != null) {
	                statement.close();
	            }
	            ConnectionDB.closeConnection();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static boolean delete(String driver_id) {
	    PreparedStatement statement = null;
	    try {
	        Connection connection = ConnectionDB.openConnection();
	        String query = "delete from driver where driver_id = ?::uuid";
	        statement = connection.prepareStatement(query);
	        statement.setObject(1, UUID.fromString(driver_id)); // Chuyển sang UUID
	        int rowsDeleted = statement.executeUpdate();
	        return rowsDeleted > 0;
	    } catch(Exception e){
	        System.out.println("Error in delete: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    } finally {
	        try {
	            if (statement != null) {
	                statement.close();
	            }
	            ConnectionDB.closeConnection();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static boolean update(Driver driver) {
		PreparedStatement statement = null;
	    try {
	        Connection connection = ConnectionDB.openConnection();
	        String query = "update driver set company_id = ?, name = ?, phone = ?, license_no = ?, experience_years = ?, status = ? where driver_id = ?";
	        statement = connection.prepareStatement(query);
	        
	        // Chuyển đổi company_id từ String sang UUID cho PreparedStatement
	        statement.setObject(1, UUID.fromString(driver.getCompany_id()));
	        statement.setString(2, driver.getName());
	        statement.setString(3, driver.getPhone());
	        statement.setString(4, driver.getLicense_no());
	        statement.setInt(5, driver.getExperience_years());
	        statement.setString(6, driver.getStatus());
	        statement.setObject(7, UUID.fromString(driver.getDriver_id()));
	        
	        int rowsInserted = statement.executeUpdate();
	        return rowsInserted > 0;
	    } catch(Exception ex){
	        System.out.println("Error in insert: " + ex.getMessage());
	        ex.printStackTrace();
	        return false;
	    } finally {
	        try {
	            if (statement != null) {
	                statement.close();
	            }
	            ConnectionDB.closeConnection();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	public Driver getbyid(String driver_id) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = ConnectionDB.openConnection();
			String querry = "select * from driver where driver_id = ?::uuid";
			statement = connection.prepareStatement(querry);
	        statement.setString(1, driver_id);
	        resultSet = statement.executeQuery();
	        while (resultSet.next()) {
				String company_id = resultSet.getString("company_id");
				String name = resultSet.getString("name");
				String phone = resultSet.getString("phone");
				String license_no = resultSet.getString("license_no");
				int experience_years = resultSet.getInt("experience_years");
				String status = resultSet.getString("status");
				Driver driver = new Driver(driver_id, company_id, name, phone, license_no, experience_years, status);
				return driver;
			}
			
		}
		catch(Exception ex) {
			System.out.println("Loi truy van");
			ex.printStackTrace();
		}
		finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				ConnectionDB.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
}
