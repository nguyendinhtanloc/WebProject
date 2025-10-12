package com.busbooking.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.busbooking.dao.VehicleTransportDAO;
import com.busbooking.dao.TransportCompanyDAO;
import com.busbooking.model.VehicleTransport;
import com.busbooking.model.TransportCompany;
import com.busbooking.model.AppUser;
import com.busbooking.model.enums.VehicleStatus;

/**
 * Servlet implementation class VehicleServlet
 */
@WebServlet("/vehicles")
public class VehicleServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			action = "list";
		}
		
		switch (action) {
		case "list":
			listVehicles(request, response);
			break;
		case "delete":
			deleteVehicle(request, response);
			break;
		case "update":
			showUpdateForm(request, response);
			break;
		case "getVehicle":
			getVehicle(request, response);
			break;
		default:
			listVehicles(request, response);
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			action = "";
		}
		switch (action) {
		case "insert":
			insertVehicle(request, response);
			break;
		case "update":
			updateVehicle(request, response);
			break;
		default:
			listVehicles(request, response);
			break;
		}
	}
	
	private void listVehicles (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		request.setCharacterEncoding("UTF-8");
		
		// Xử lý phân trang
		int currentPage = 1;
		String pageParam = request.getParameter("page");
		if (pageParam != null) {
			try { 
				currentPage = Integer.parseInt(pageParam); 
			} catch (NumberFormatException ignored) {}
		}
		
		int vehiclesPerPage = 10;
		VehicleTransportDAO vehicleDAO = new VehicleTransportDAO();
		TransportCompanyDAO companyDAO = new TransportCompanyDAO();
		
		int totalVehicles = vehicleDAO.getTotalVehicleCount();
		int totalPages = (int) Math.ceil((double) totalVehicles / vehiclesPerPage);
		
		if (currentPage < 1) currentPage = 1;
		if (currentPage > totalPages && totalPages > 0) currentPage = totalPages;
		
		List<VehicleTransport> vehicleList = vehicleDAO.getVehiclesByPage(currentPage, vehiclesPerPage);
		List<TransportCompany> companyList = companyDAO.getAllCompanies();
		
		request.setAttribute("vehicleList", vehicleList);
		request.setAttribute("companyList", companyList);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPages", totalPages);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/pages/vehicles.jsp");
		rd.forward(request, response);
	}
	
	private void insertVehicle (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String companyIdStr = request.getParameter("company_id");
		String licensePlate = request.getParameter("license_plate");
		String capacity = request.getParameter("capacity");
		String type = request.getParameter("type");
		String status = request.getParameter("status");
		String seatLayout = request.getParameter("seat_layout");
		String amenities = request.getParameter("amenities");
		
		System.out.println("INSERT DEBUG - companyId: " + companyIdStr + ", licensePlate: " + licensePlate + ", status: " + status);
		
		try {
			Integer companyId = Integer.parseInt(companyIdStr);
			TransportCompanyDAO companyDAO = new TransportCompanyDAO();
			TransportCompany company = companyDAO.getById(companyId);
			
			if (company == null) {
				throw new Exception("Không tìm thấy công ty với ID: " + companyId);
			}
			
			// Get current user from session for updatedBy
			HttpSession session = request.getSession();
			AppUser currentUser = (AppUser) session.getAttribute("currentUser");
			System.out.println("INSERT DEBUG - currentUser: " + (currentUser != null ? currentUser.getEmail() : "null"));
			
			VehicleTransport vehicle = new VehicleTransport();
			vehicle.setTransportCompany(company);
			vehicle.setLicensePlate(licensePlate);
			vehicle.setCapacity(Integer.parseInt(capacity));
			vehicle.setType(type);
			vehicle.setStatus(VehicleStatus.valueOf(status));
			vehicle.setSeatLayout(seatLayout);
			vehicle.setAmenities(amenities);
			vehicle.setUpdatedBy(currentUser);
			
			System.out.println("INSERT DEBUG - Vehicle created, inserting...");
			VehicleTransportDAO vehicleDAO = new VehicleTransportDAO();
			boolean success = vehicleDAO.insert(vehicle);
			if (success) {
				System.out.println("INSERT DEBUG - Vehicle inserted successfully!");
				response.sendRedirect(request.getContextPath() + "/vehicles?action=list");
			} else {
				throw new Exception("Không thể thêm phương tiện vào database");
			}
		} catch (Exception e) {
			System.out.println("INSERT ERROR: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("error", "Lỗi khi thêm phương tiện: " + e.getMessage());
			listVehicles(request, response);
		}
	}
	private void deleteVehicle (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String vehicleId = request.getParameter("vehicle_id");
		
		try {
			VehicleTransportDAO vehicleDAO = new VehicleTransportDAO();
            boolean check = vehicleDAO.delete(vehicleId);
            if (check) {
                response.sendRedirect(request.getContextPath() + "/vehicles?action=list");
            } else {
                request.setAttribute("error", "Lỗi khi xóa phương tiện");
                listVehicles(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi xóa phương tiện: " + e.getMessage());
            listVehicles(request, response);
        }
	}
	private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // forward sang form update.jsp
    	request.setCharacterEncoding("UTF-8");
        String vehicle_id = request.getParameter("vehicle_id");
        VehicleTransportDAO vehicleDAO = new VehicleTransportDAO();
        TransportCompanyDAO companyDAO = new TransportCompanyDAO();
        List<VehicleTransport> vehicleList = vehicleDAO.selectAll();
        List<TransportCompany> companyList = companyDAO.getAllCompanies();
        request.setAttribute("vehicleList", vehicleList);
        request.setAttribute("companyList", companyList);
        VehicleTransport vehicle = vehicleDAO.getById(vehicle_id);
        request.setAttribute("vehicle", vehicle);
        request.getRequestDispatcher("pages/vehicles.jsp").forward(request, response);
    }

    // --- Lấy JSON ---
	private void getVehicle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");

        String vehicleId = request.getParameter("vehicle_id");
        try {
            VehicleTransportDAO vehicleDAO = new VehicleTransportDAO();
            VehicleTransport vehicle = vehicleDAO.getById(vehicleId);

            if (vehicle == null) {
                response.getWriter().write("{\"error\":\"Vehicle not found\"}");
                return;
            }

            // Tạo JSON thủ công để tránh vấn đề lazy loading
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"vehicleId\":").append(vehicle.getVehicleId()).append(",");
            json.append("\"licensePlate\":\"").append(escapeJson(vehicle.getLicensePlate())).append("\",");
            json.append("\"capacity\":").append(vehicle.getCapacity()).append(",");
            json.append("\"type\":\"").append(escapeJson(vehicle.getType())).append("\",");
            json.append("\"seatLayout\":\"").append(escapeJson(vehicle.getSeatLayout())).append("\",");
            json.append("\"amenities\":\"").append(escapeJson(vehicle.getAmenities())).append("\",");
            json.append("\"status\":\"").append(vehicle.getStatus() != null ? vehicle.getStatus().toString() : "").append("\",");
            
            // Thêm thông tin company
            if (vehicle.getTransportCompany() != null) {
                json.append("\"transportCompany\":{");
                json.append("\"companyId\":").append(vehicle.getTransportCompany().getCompanyId()).append(",");
                json.append("\"name\":\"").append(escapeJson(vehicle.getTransportCompany().getName())).append("\"");
                json.append("}");
            } else {
                json.append("\"transportCompany\":null");
            }
            
            json.append("}");
            
            response.getWriter().write(json.toString());
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    // Hàm helper để escape JSON string
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\r", "\\r")
                  .replace("\n", "\\n")
                  .replace("\t", "\\t");
    }

    // --- Cập nhật ---
    private void updateVehicle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String vehicleIdStr = request.getParameter("vehicle_id");
        String companyIdStr = request.getParameter("company_id");
        String licensePlate = request.getParameter("license_plate");
        String capacity = request.getParameter("capacity");
        String type = request.getParameter("type");
        String status = request.getParameter("status");
        String seatLayout = request.getParameter("seat_layout");
        String amenities = request.getParameter("amenities");

        try {
            Integer companyId = Integer.parseInt(companyIdStr);
            
            VehicleTransportDAO vehicleDAO = new VehicleTransportDAO();
            TransportCompanyDAO companyDAO = new TransportCompanyDAO();
            
            // Get existing vehicle
            VehicleTransport vehicle = vehicleDAO.getById(vehicleIdStr);
            if (vehicle == null) {
                request.setAttribute("error", "Phương tiện không tồn tại");
                listVehicles(request, response);
                return;
            }
            
            // Get company
            TransportCompany company = companyDAO.getById(companyId);
            
            // Get current user from session for updatedBy
            HttpSession session = request.getSession();
            AppUser currentUser = (AppUser) session.getAttribute("currentUser");
            
            // Update vehicle properties
            vehicle.setTransportCompany(company);
            vehicle.setLicensePlate(licensePlate);
            vehicle.setCapacity(Integer.parseInt(capacity));
            vehicle.setType(type);
            vehicle.setStatus(VehicleStatus.valueOf(status));
            vehicle.setSeatLayout(seatLayout);
            vehicle.setAmenities(amenities);
            vehicle.setUpdatedBy(currentUser);
            
            vehicleDAO.update(vehicle);
            response.sendRedirect(request.getContextPath() + "/vehicles?action=list");
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi cập nhật phương tiện: " + e.getMessage());
            listVehicles(request, response);
        }
    }
}
