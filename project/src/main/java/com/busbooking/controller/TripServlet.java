package com.busbooking.controller;

import com.busbooking.dao.CompanyDAO;
import com.busbooking.dao.DriverDAO;
import com.busbooking.dao.TripDAO;
import com.busbooking.dao.VehicleDAO;
import com.busbooking.model.TripDetail;
import com.busbooking.model.Trips;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

@WebServlet("/trips")
public class TripServlet extends HttpServlet {
    private TripDAO tripDAO;
    private CompanyDAO companyDAO;
    private VehicleDAO vehicleDAO;
    private DriverDAO driverDAO;
    private static final int TRIPS_PER_PAGE = 10;

    @Override
    public void init() {
        tripDAO = new TripDAO();
        companyDAO = new CompanyDAO();
        vehicleDAO = new VehicleDAO();
        driverDAO = new DriverDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                case "edit":
                    req.setAttribute("companyList", companyDAO.getAllCompanies());
                    req.setAttribute("vehicleList", vehicleDAO.getAllVehicles());
                    req.setAttribute("driverList", driverDAO.getAllDrivers());

                    if ("new".equals(action)) {
                        session.removeAttribute("tripFormData");
                        req.setAttribute("trip", new Trips());
                        req.setAttribute("mode", "create");
                    } else {
                        String id = req.getParameter("id");
                        TripDetail trip = tripDAO.getTripById(id);
                        if (trip == null) {
                            resp.sendRedirect(req.getContextPath() + "/trips");
                            return;
                        }
                        req.setAttribute("trip", trip);
                        req.setAttribute("mode", "edit");
                    }

                    req.setAttribute("contentPage", "/WEB-INF/view/pages/trip-form-content.jsp");
                    req.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(req, resp);
                    break;

                default: // "list"
                    int currentPage = 1;
                    String pageParam = req.getParameter("page");
                    if (pageParam != null) {
                        try {
                            currentPage = Integer.parseInt(pageParam);
                        } catch (NumberFormatException e) {
                            // Bỏ qua nếu tham số trang không phải là số
                        }
                    }

                    int totalTrips = tripDAO.getTotalTripCount();
                    int totalPages = (int) Math.ceil((double) totalTrips / TRIPS_PER_PAGE);

                    if (currentPage < 1) {
                        currentPage = 1;
                    }
                    if (currentPage > totalPages && totalPages > 0) {
                        currentPage = totalPages;
                    }

                    List<TripDetail> tripList = tripDAO.getTripsByPage(currentPage, TRIPS_PER_PAGE);

                    req.setAttribute("tripList", tripList);
                    req.setAttribute("currentPage", currentPage);
                    req.setAttribute("totalPages", totalPages);

                    req.setAttribute("contentPage", "/WEB-INF/view/pages/trips-content.jsp");
                    req.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(req, resp);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in doGet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        String action = req.getParameter("action");

        String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            email = "system";
        }

        try {
            if ("create".equals(action) || "edit".equals(action)) {
                Trips t = buildTripFromRequest(req);

                if (!companyDAO.companyExists(t.getCompanyId())) {
                    throw new Exception("Lỗi: ID Công ty không tồn tại!");
                }
                if (!vehicleDAO.vehicleExists(t.getVehicleId())) {
                    throw new Exception("Lỗi: ID Xe không tồn tại!");
                }
                if (!driverDAO.driverExists(t.getDriverId())) {
                    throw new Exception("Lỗi: ID Tài xế không tồn tại!");
                }

                if ("create".equals(action)) {
                    // ================== SỬA Ở ĐÂY ==================
                    tripDAO.insertTrip(t, email);
                    session.removeAttribute("tripFormData");
                } else {
                    // ================== SỬA Ở ĐÂY ==================
                    tripDAO.updateTrip(t, email);
                }

            } else if ("delete".equals(action)) {
                String id = req.getParameter("tripId");
                // ================== SỬA Ở ĐÂY ==================
                tripDAO.deleteTrip(id, email);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            try {
                req.setAttribute("companyList", companyDAO.getAllCompanies());
                req.setAttribute("vehicleList", vehicleDAO.getAllVehicles());
                req.setAttribute("driverList", driverDAO.getAllDrivers());
                req.setAttribute("trip", buildTripFromRequest(req));
            } catch (Exception ex) {
                req.setAttribute("trip", new Trips());
            }
            req.setAttribute("mode", action);
            req.setAttribute("contentPage", "/WEB-INF/view/pages/trip-form-content.jsp");
            req.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/trips");
    }

    private Trips buildTripFromRequest(HttpServletRequest req) throws Exception {
        Trips t = new Trips();

        String tripId = req.getParameter("tripId");
        if (tripId != null && !tripId.isEmpty()) {
            t.setTripId(tripId);
        }

        t.setCompanyId(req.getParameter("companyId"));
        t.setVehicleId(req.getParameter("vehicleId"));
        t.setDriverId(req.getParameter("driverId"));
        t.setDeparturePlace(req.getParameter("departurePlace"));
        t.setArrivalPlace(req.getParameter("arrivalPlace"));

        try {
            String departureDateStr = req.getParameter("departureDate");
            if (departureDateStr != null && !departureDateStr.isEmpty()) {
                t.setDepartureDate(Date.valueOf(departureDateStr));
            }
        } catch (IllegalArgumentException e) {
            throw new Exception("Ngày đi không hợp lệ");
        }

        try {
            String departureTimeStr = req.getParameter("departureTime");
            if (departureTimeStr != null && !departureTimeStr.isEmpty()) {
                if (departureTimeStr.length() == 5) {
                    departureTimeStr += ":00";
                }
                t.setDepartureTime(Time.valueOf(departureTimeStr));
            }
        } catch (IllegalArgumentException e) {
            throw new Exception("Giờ đi không hợp lệ");
        }

        try {
            String priceStr = req.getParameter("price");
            if (priceStr != null && !priceStr.isEmpty()) {
                float price = Float.parseFloat(priceStr);
                if (price < 0) {
                    throw new Exception("Giá vé phải là một số không âm");
                }
                t.setPrice(price);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Giá vé không hợp lệ");
        }

        t.setStatus(req.getParameter("status"));
        return t;
    }
}