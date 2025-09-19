package controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DriverService;
import model.Driver;

/**
 * Servlet implementation class Driver
 */
@WebServlet("/drivers")
public class DriverServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // mặc định
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
            default:
                listDrivers(request, response);
                break;
        }
        System.out.println("Context Path: " + request.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	System.out.println("Received POST request for action: " + request.getParameter("action"));
        System.out.println("URL: " + request.getRequestURL());

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

    // --- Các hàm xử lý ---
    private void listDrivers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // code giống Manage_driver.java
    	request.setCharacterEncoding("UTF-8");
		List<Driver> driverList = DriverService.selectAll();
		request.setAttribute("driverList", driverList);
		RequestDispatcher rd = request.getRequestDispatcher("/pages/drivers.jsp");
        rd.forward(request, response);   // forward để giữ attribute
    }

    private void insertDriver(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
     // Debug: kiểm tra tất cả parameters
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            System.out.println("Parameter: " + paramName + " = " + request.getParameter(paramName));
        }
        String company_id = request.getParameter("company_id");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String license_no = request.getParameter("license_no");
        String experience_years_str = request.getParameter("experience_years");
        String status = request.getParameter("status");

        System.out.println("Received parameters: company_id=" + company_id + ", name=" + name + ", phone=" + phone +
                ", license_no=" + license_no + ", experience_years=" + experience_years_str + ", status=" + status);

        try {
            System.out.println("Attempting to parse company_id: " + company_id);
            UUID companyUUID = UUID.fromString(company_id);
            System.out.println("company_id parsed successfully");

            int experience_years = Integer.parseInt(experience_years_str);
            System.out.println("experience_years parsed successfully: " + experience_years);

            Driver driver = new Driver(null, companyUUID.toString(), name, phone, license_no, experience_years, status);
            DriverService driverService = new DriverService();
            System.out.println("Inserting driver: " + driver);
            boolean check = driverService.insert(driver);
            System.out.println("Insert operation result: " + check);

            if (check) {
                response.sendRedirect(request.getContextPath() + "/drivers?action=list");
                System.out.println("Company ID: " + company_id);
                System.out.println("Name: " + name);
                System.out.println("Phone: " + phone);
                System.out.println("License No: " + license_no);
                System.out.println("Experience Years: " + experience_years);
                System.out.println("Status: " + status);
            } else {
                request.setAttribute("error", "Lỗi khi thêm driver");
                request.getRequestDispatcher("pages/drivers.jsp").forward(request, response);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException: " + e.getMessage());
            request.setAttribute("error", "Company ID không đúng định dạng UUID: " + e.getMessage());
            request.getRequestDispatcher("pages/drivers.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("pages/drivers.jsp").forward(request, response);
        }
    }
    
    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void deleteDriver(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String driver_id = request.getParameter("driver_id");
        
        System.out.println("=== DELETE DEBUG ===");
        System.out.println("Driver ID to delete: '" + driver_id + "'");
        System.out.println("UUID format valid: " + isValidUUID(driver_id));
        
        // Debug: in tất cả parameters
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            System.out.println("Delete parameter: " + paramName + " = " + request.getParameter(paramName));
        }
        
        if (driver_id == null || driver_id.trim().isEmpty()) {
            System.out.println("Error: driver_id is null or empty");
            request.setAttribute("error", "ID tài xế không hợp lệ");
            listDrivers(request, response);
            return;
        }
        
        try {
            boolean check = DriverService.delete(driver_id);
            System.out.println("Delete operation result: " + check);
            
            if (check) {
                response.sendRedirect(request.getContextPath() + "/drivers?action=list");
            } else {
                request.setAttribute("error", "Lỗi khi xóa tài xế");
                listDrivers(request, response);
            }
        } catch (Exception e) {
            System.out.println("Exception during delete: " + e.getMessage());
            request.setAttribute("error", "Lỗi hệ thống khi xóa: " + e.getMessage());
            listDrivers(request, response);
        }
    }

    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // forward sang form update.jsp
    	request.setCharacterEncoding("UTF-8");
        String driver_id = request.getParameter("driver_id");
        DriverService driverService = new DriverService();
        Driver driver = driverService.getbyid(driver_id);
        request.setAttribute("driver", driver);
        request.getRequestDispatcher("update.jsp").forward(request, response);
    }
    
    private void updateDriver(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // code giống DriverUpdate.java
        // Xử lý khi submit form
        request.setCharacterEncoding("UTF-8");
        String driver_id = request.getParameter("driver_id");
        String company_id = request.getParameter("company_id");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String license_no = request.getParameter("license_no");
        int experience_years = Integer.parseInt(request.getParameter("experience_years"));
        String status = request.getParameter("status");
        
        try {
            // Chuyển đổi company_id từ String sang UUID
            UUID companyUUID = UUID.fromString(company_id);
            UUID driverUUID = UUID.fromString(driver_id);            
            Driver driver = new Driver(driver_id, companyUUID.toString(), name, phone, license_no, experience_years, status);
            DriverService driverService = new DriverService();
            boolean check = driverService.update(driver);
            
            if (check) {
                // Sửa thành redirect đến trang danh sách
                response.sendRedirect(request.getContextPath() + "/drivers?action=list");
            } else {
                request.setAttribute("error", "Lỗi khi cập nhật driver");
                request.getRequestDispatcher("update.jsp").forward(request, response);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Company ID không đúng định dạng UUID: " + e.getMessage());
            request.getRequestDispatcher("update.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("update.jsp").forward(request, response);
        }
    }
}

