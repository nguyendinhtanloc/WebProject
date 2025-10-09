package com.busbooking.controller;

import com.busbooking.dao.TransportCompanyDAO;
import com.busbooking.dao.DriverTransportDAO;
import com.busbooking.dao.TripTransportDAO;
import com.busbooking.dao.VehicleTransportDAO;
import com.busbooking.model.TransportCompany;
import com.busbooking.model.DriverTransport;
import com.busbooking.model.TripTransport;
import com.busbooking.model.VehicleTransport;
import com.busbooking.model.enums.TripStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/trips")
public class TripServlet extends HttpServlet {
    private TripTransportDAO tripTransportDAO;
    private TransportCompanyDAO transportCompanyDAO;
    private VehicleTransportDAO vehicleTransportDAO;
    private DriverTransportDAO driverTransportDAO;
    private static final int TRIPS_PER_PAGE = 10;

    @Override
    public void init() {
        tripTransportDAO = new TripTransportDAO();
        transportCompanyDAO = new TransportCompanyDAO();
        vehicleTransportDAO = new VehicleTransportDAO();
        driverTransportDAO = new DriverTransportDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                case "edit":
                    req.setAttribute("companyList", transportCompanyDAO.getAllCompanies());
                    req.setAttribute("vehicleList", vehicleTransportDAO.getAllVehicles());
                    req.setAttribute("driverList", driverTransportDAO.getAllDrivers());

                    if ("new".equals(action)) {
                        session.removeAttribute("tripFormData");
                        req.setAttribute("trip", new TripTransport());
                        req.setAttribute("mode", "create");
                    } else {
                        String idStr = req.getParameter("id");
                        Integer tripId = Integer.parseInt(idStr); // parse trực tiếp sang Integer
                        TripTransport trip = tripTransportDAO.getTripById(tripId);
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

                default:
                    int currentPage = 1;
                    String pageParam = req.getParameter("page");
                    if (pageParam != null) {
                        try { currentPage = Integer.parseInt(pageParam); } catch (NumberFormatException ignored) {}
                    }

                    int totalTrips = tripTransportDAO.getTotalTripCount();
                    int totalPages = (int) Math.ceil((double) totalTrips / TRIPS_PER_PAGE);

                    if (currentPage < 1) currentPage = 1;
                    if (currentPage > totalPages && totalPages > 0) currentPage = totalPages;

                    List<TripTransport> tripList = tripTransportDAO.getTripsByPage(currentPage, TRIPS_PER_PAGE);

                    req.setAttribute("tripList", tripList);
                    req.setAttribute("currentPage", currentPage);
                    req.setAttribute("totalPages", totalPages);
                    req.setAttribute("contentPage", "/WEB-INF/view/pages/trips-content.jsp");
                    req.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(req, resp);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Lỗi khi xử lý dữ liệu chuyến xe", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        String action = req.getParameter("action");

        String email = (String) session.getAttribute("userEmail");
        if (email == null) email = "system";

        try {
            if ("create".equals(action) || "edit".equals(action)) {
                TripTransport trip = buildTripFromRequest(req);

                if (!transportCompanyDAO.companyExists(trip.getTransportCompany().getCompanyId())) {
                    throw new Exception("Lỗi: ID Công ty không tồn tại!");
                }
                if (!vehicleTransportDAO.vehicleExists(trip.getVehicleTransport().getVehicleId())) {
                    throw new Exception("Lỗi: ID Xe không tồn tại!");
                }
                if (!driverTransportDAO.driverExists(trip.getDriverTransport().getDriverId())) {
                    throw new Exception("Lỗi: ID Tài xế không tồn tại!");
                }

                if ("create".equals(action)) {
                    tripTransportDAO.insertTrip(trip, email);
                    session.removeAttribute("tripFormData");
                } else {
                    tripTransportDAO.updateTrip(trip, email);
                }

            } else if ("delete".equals(action)) {
                String idStr = req.getParameter("tripId");
                Integer tripId = Integer.parseInt(idStr); // parse trực tiếp sang Integer
                tripTransportDAO.deleteTrip(tripId, email);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            try {
                req.setAttribute("companyList", transportCompanyDAO.getAllCompanies());
                req.setAttribute("vehicleList", vehicleTransportDAO.getAllVehicles());
                req.setAttribute("driverList", driverTransportDAO.getAllDrivers());
                req.setAttribute("trip", buildTripFromRequest(req));
            } catch (Exception ex) {
                req.setAttribute("trip", new TripTransport());
            }
            req.setAttribute("mode", action);
            req.setAttribute("contentPage", "/WEB-INF/view/pages/trip-form-content.jsp");
            req.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/trips");
    }

    private TripTransport buildTripFromRequest(HttpServletRequest req) throws Exception {
        TripTransport trip = new TripTransport();

        // Trip ID
        String tripIdStr = req.getParameter("tripId");
        if (tripIdStr != null && !tripIdStr.isEmpty()) {
            trip.setTripId(Integer.parseInt(tripIdStr));
        }

        // Company, Vehicle, Driver IDs
        Integer companyId = Integer.parseInt(req.getParameter("companyId"));
        Integer vehicleId = Integer.parseInt(req.getParameter("vehicleId"));
        Integer driverId = Integer.parseInt(req.getParameter("driverId"));

        TransportCompany transportCompany = new TransportCompany();
        transportCompany.setCompanyId(companyId);
        trip.setTransportCompany(transportCompany);

        VehicleTransport vehicle = new VehicleTransport();
        vehicle.setVehicleId(vehicleId);
        trip.setVehicleTransport(vehicle);

        DriverTransport driver = new DriverTransport();
        driver.setDriverId(driverId);
        trip.setDriverTransport(driver);

        // Departure & Arrival
        trip.setDeparturePoint(req.getParameter("departurePlace"));
        trip.setArrivalPoint(req.getParameter("arrivalPlace"));

        try {
            String departureDateStr = req.getParameter("departureDate");
            String departureTimeStr = req.getParameter("departureTime");
            if (departureDateStr != null && departureTimeStr != null) {
                LocalDate date = LocalDate.parse(departureDateStr);
                if (departureTimeStr.length() == 5) departureTimeStr += ":00";
                LocalTime time = LocalTime.parse(departureTimeStr);
                trip.setDepartureDatetime(LocalDateTime.of(date, time));
            }
        } catch (Exception e) {
            throw new Exception("Ngày hoặc giờ đi không hợp lệ");
        }

        // Trip Status
        String statusStr = req.getParameter("status");
        if (statusStr != null && !statusStr.isEmpty()) {
            trip.setStatus(TripStatus.valueOf(statusStr));
        }

        return trip;
    }
}
