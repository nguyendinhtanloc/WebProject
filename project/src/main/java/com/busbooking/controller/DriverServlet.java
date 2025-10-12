package com.busbooking.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.busbooking.dao.DriverTransportDAO;
import com.busbooking.dao.TransportCompanyDAO;
import com.busbooking.model.DriverTransport;
import com.busbooking.model.TransportCompany;
import com.busbooking.model.AppUser;
import com.busbooking.model.enums.DriverStatus;

/**
 * Servlet implementation class DriverServlet
 */
@WebServlet("/drivers")
public class DriverServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			action = "list";
		}
		
		switch (action) {
		case "list":
			listDrivers(request, response);
			break;
		case "delete":
			deleteDriver(request, response);
			break;
		case "update":
			showUpdateForm(request, response);
			break;
		case "getDriver":
			getDriver(request, response);
			break;
		default:
			listDrivers(request, response);
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
			insertDriver(request, response);
			break;
		case "update":
			updateDriver(request, response);
			break;
		default:
			listDrivers(request, response);
			break;
		}
	}
	
	private void listDrivers (HttpServletRequest request, HttpServletResponse response)
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
		
		int driversPerPage = 10;
		DriverTransportDAO driverDAO = new DriverTransportDAO();
		TransportCompanyDAO companyDAO = new TransportCompanyDAO();
		
		int totalDrivers = driverDAO.getTotalDriverCount();
		int totalPages = (int) Math.ceil((double) totalDrivers / driversPerPage);
		
		if (currentPage < 1) currentPage = 1;
		if (currentPage > totalPages && totalPages > 0) currentPage = totalPages;
		
		List<DriverTransport> driverList = driverDAO.getDriversByPage(currentPage, driversPerPage);
		List<TransportCompany> companyList = companyDAO.getAllCompanies();
		
		request.setAttribute("driverList", driverList);
		request.setAttribute("companyList", companyList);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPages", totalPages);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/pages/drivers.jsp");
		rd.forward(request, response);
	}
	
	private void insertDriver (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String companyIdStr = request.getParameter("company_id");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String licenseNo = request.getParameter("license_no");
		String hireDateStr = request.getParameter("hire_date");
		String endDateStr = request.getParameter("end_date");
		String status = request.getParameter("status");
		
		System.out.println("INSERT DEBUG - companyId: " + companyIdStr + ", name: " + name + ", status: " + status);
		
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
			
			DriverTransport driver = new DriverTransport();
			driver.setTransportCompany(company);
			driver.setName(name);
			driver.setPhone(phone);
			driver.setLicenseNo(licenseNo);
			
			// Parse dates
			if (hireDateStr != null && !hireDateStr.trim().isEmpty()) {
				driver.setHireDate(LocalDate.parse(hireDateStr));
			}
			if (endDateStr != null && !endDateStr.trim().isEmpty()) {
				driver.setEndDate(LocalDate.parse(endDateStr));
			}
			
			driver.setStatus(DriverStatus.valueOf(status));
			driver.setUpdatedBy(currentUser);
			
			System.out.println("INSERT DEBUG - Driver created, inserting...");
			DriverTransportDAO driverDAO = new DriverTransportDAO();
			boolean success = driverDAO.insert(driver);
			if (success) {
				System.out.println("INSERT DEBUG - Driver inserted successfully!");
				response.sendRedirect(request.getContextPath() + "/drivers?action=list");
			} else {
				throw new Exception("Không thể thêm tài xế vào database");
			}
		} catch (Exception e) {
			System.out.println("INSERT ERROR: " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("error", "Lỗi khi thêm tài xế: " + e.getMessage());
			listDrivers(request, response);
		}
	}
	
	private void deleteDriver (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String driverId = request.getParameter("driver_id");
		
		try {
			DriverTransportDAO driverDAO = new DriverTransportDAO();
            boolean check = driverDAO.delete(driverId);
            if (check) {
                response.sendRedirect(request.getContextPath() + "/drivers?action=list");
            } else {
                request.setAttribute("error", "Lỗi khi xóa tài xế");
                listDrivers(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi xóa tài xế: " + e.getMessage());
            listDrivers(request, response);
        }
	}
	
	private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // forward sang form update.jsp
    	request.setCharacterEncoding("UTF-8");
        String driver_id = request.getParameter("driver_id");
        DriverTransportDAO driverDAO = new DriverTransportDAO();
        TransportCompanyDAO companyDAO = new TransportCompanyDAO();
        List<DriverTransport> driverList = driverDAO.selectAll();
        List<TransportCompany> companyList = companyDAO.getAllCompanies();
        request.setAttribute("driverList", driverList);
        request.setAttribute("companyList", companyList);
        DriverTransport driver = driverDAO.getById(driver_id);
        request.setAttribute("driver", driver);
        request.getRequestDispatcher("pages/drivers.jsp").forward(request, response);
    }

    // --- Lấy JSON ---
	private void getDriver(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");

        String driverId = request.getParameter("driver_id");
        try {
            DriverTransportDAO driverDAO = new DriverTransportDAO();
            DriverTransport driver = driverDAO.getById(driverId);

            if (driver == null) {
                response.getWriter().write("{\"error\":\"Driver not found\"}");
                return;
            }

            // Tạo JSON thủ công để tránh vấn đề lazy loading
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"driverId\":").append(driver.getDriverId()).append(",");
            json.append("\"name\":\"").append(escapeJson(driver.getName())).append("\",");
            json.append("\"phone\":\"").append(escapeJson(driver.getPhone())).append("\",");
            json.append("\"licenseNo\":\"").append(escapeJson(driver.getLicenseNo())).append("\",");
            json.append("\"hireDate\":\"").append(driver.getHireDate() != null ? driver.getHireDate().toString() : "").append("\",");
            json.append("\"endDate\":\"").append(driver.getEndDate() != null ? driver.getEndDate().toString() : "").append("\",");
            json.append("\"status\":\"").append(driver.getStatus() != null ? driver.getStatus().toString() : "").append("\",");
            
            // Thêm thông tin company
            if (driver.getTransportCompany() != null) {
                json.append("\"transportCompany\":{");
                json.append("\"companyId\":").append(driver.getTransportCompany().getCompanyId()).append(",");
                json.append("\"name\":\"").append(escapeJson(driver.getTransportCompany().getName())).append("\"");
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
    private void updateDriver(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String driverIdStr = request.getParameter("driver_id");
        String companyIdStr = request.getParameter("company_id");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String licenseNo = request.getParameter("license_no");
        String hireDateStr = request.getParameter("hire_date");
        String endDateStr = request.getParameter("end_date");
        String status = request.getParameter("status");

        try {
            Integer companyId = Integer.parseInt(companyIdStr);
            
            DriverTransportDAO driverDAO = new DriverTransportDAO();
            TransportCompanyDAO companyDAO = new TransportCompanyDAO();
            
            // Get existing driver
            DriverTransport driver = driverDAO.getById(driverIdStr);
            if (driver == null) {
                request.setAttribute("error", "Tài xế không tồn tại");
                listDrivers(request, response);
                return;
            }
            
            // Get company
            TransportCompany company = companyDAO.getById(companyId);
            
            // Get current user from session for updatedBy
            HttpSession session = request.getSession();
            AppUser currentUser = (AppUser) session.getAttribute("currentUser");
            
            // Update driver properties
            driver.setTransportCompany(company);
            driver.setName(name);
            driver.setPhone(phone);
            driver.setLicenseNo(licenseNo);
            
            // Parse dates
            if (hireDateStr != null && !hireDateStr.trim().isEmpty()) {
                driver.setHireDate(LocalDate.parse(hireDateStr));
            } else {
                driver.setHireDate(null);
            }
            if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                driver.setEndDate(LocalDate.parse(endDateStr));
            } else {
                driver.setEndDate(null);
            }
            
            driver.setStatus(DriverStatus.valueOf(status));
            driver.setUpdatedBy(currentUser);
            
            driverDAO.update(driver);
            response.sendRedirect(request.getContextPath() + "/drivers?action=list");
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi cập nhật tài xế: " + e.getMessage());
            listDrivers(request, response);
        }
    }
}
