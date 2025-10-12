package com.busbooking.controller;

import com.busbooking.dao.*;
import com.busbooking.model.*;
import com.busbooking.model.enums.TripStatus;
import com.busbooking.util.AuthUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;

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

        if (!AuthUtils.isAdmin(req, resp))
            return;

        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        HttpSession session = req.getSession(false);
        String action = req.getParameter("action");
        if (action == null)
            action = "list";

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
                        Integer tripId;
                        try {
                            tripId = Integer.parseInt(idStr);
                        } catch (NumberFormatException e) {
                            req.setAttribute("errorMessage", "ID chuyến xe không hợp lệ.");
                            forwardToList(req, resp);
                            return;
                        }

                        TripTransport trip = tripTransportDAO.getTripById(tripId);
                        if (trip == null) {
                            req.setAttribute("errorMessage", "Không tìm thấy chuyến xe với ID: " + tripId);
                            forwardToList(req, resp);
                            return;
                        }

                        if (trip.getDepartureDate() != null)
                            req.setAttribute("departureDate", trip.getDepartureDate().toString());
                        if (trip.getDepartureTime() != null)
                            req.setAttribute("departureTime", trip.getDepartureTime().toString());
                        if (trip.getArrivalDate() != null)
                            req.setAttribute("arrivalDate", trip.getArrivalDate().toString());
                        if (trip.getArrivalTime() != null)
                            req.setAttribute("arrivalTime", trip.getArrivalTime().toString());

                        req.setAttribute("trip", trip);
                        req.setAttribute("mode", "edit");
                    }

                    req.setAttribute("contentPage", "/WEB-INF/view/pages/trip-form-content.jsp");
                    req.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(req, resp);
                    break;

                default:
                    forwardToList(req, resp);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Lỗi khi xử lý dữ liệu chuyến xe", e);
        }
    }

    private void forwardToList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int currentPage = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }

        int totalTrips = tripTransportDAO.getTotalTripCount();
        int totalPages = (int) Math.ceil((double) totalTrips / TRIPS_PER_PAGE);

        if (currentPage < 1)
            currentPage = 1;
        if (currentPage > totalPages && totalPages > 0)
            currentPage = totalPages;

        List<TripTransport> tripList = tripTransportDAO.getTripsByPage(currentPage, TRIPS_PER_PAGE);

        req.setAttribute("tripList", tripList);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("contentPage", "/WEB-INF/view/pages/trips-content.jsp");
        req.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!AuthUtils.isAdmin(req, resp))
            return;

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        AppUser user = (AppUser) session.getAttribute("user"); // Lấy từ session
        String email = (user != null) ? user.getEmail() : "system";

        String action = req.getParameter("action");

        try {
            if ("create".equals(action) || "edit".equals(action)) {
                TripTransport trip = buildTripFromRequest(req, user); // Truyền user vào

                if (!transportCompanyDAO.companyExists(trip.getTransportCompany().getCompanyId()))
                    throw new Exception("ID Công ty không tồn tại!");
                if (!vehicleTransportDAO.vehicleExists(trip.getVehicleTransport().getVehicleId()))
                    throw new Exception("ID Xe không tồn tại!");
                if (!driverTransportDAO.driverExists(trip.getDriverTransport().getDriverId()))
                    throw new Exception("ID Tài xế không tồn tại!");

                if ("create".equals(action)) {
                    tripTransportDAO.insertTrip(trip, email);
                    session.removeAttribute("tripFormData");
                } else {
                    tripTransportDAO.updateTrip(trip, email);
                }

            } else if ("delete".equals(action)) {
                try {
                    Integer tripId = Integer.parseInt(req.getParameter("tripId"));
                    tripTransportDAO.deleteTrip(tripId, email);
                } catch (Exception e) {
                    req.setAttribute("errorMessage", "Không thể xóa chuyến xe: " + e.getMessage());
                    forwardToList(req, resp);
                    return;
                }
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            try {
                req.setAttribute("companyList", transportCompanyDAO.getAllCompanies());
                req.setAttribute("vehicleList", vehicleTransportDAO.getAllVehicles());
                req.setAttribute("driverList", driverTransportDAO.getAllDrivers());
                req.setAttribute("trip", buildTripFromRequest(req, user));
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

    private TripTransport buildTripFromRequest(HttpServletRequest req, AppUser user) throws Exception {
        TripTransport trip = new TripTransport();

        String tripIdStr = req.getParameter("tripId");
        if (tripIdStr != null && !tripIdStr.isEmpty())
            trip.setTripId(Integer.parseInt(tripIdStr));

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

        trip.setDeparturePoint(req.getParameter("departurePoint"));
        trip.setDepartureCity(req.getParameter("departureCity"));
        trip.setDepartureAddress(req.getParameter("departureAddress"));
        trip.setArrivalPoint(req.getParameter("arrivalPoint"));
        trip.setArrivalCity(req.getParameter("arrivalCity"));
        trip.setArrivalAddress(req.getParameter("arrivalAddress"));

        String distanceStr = req.getParameter("distanceKm");
        if (distanceStr != null && !distanceStr.isEmpty()) {
            try {
                trip.setDistanceKm(new BigDecimal(distanceStr));
            } catch (NumberFormatException e) {
                throw new Exception("Khoảng cách (km) không hợp lệ");
            }
        } else
            trip.setDistanceKm(BigDecimal.ZERO);

        try {
            String departureDateStr = req.getParameter("departureDate");
            String departureTimeStr = req.getParameter("departureTime");
            String arrivalDateStr = req.getParameter("arrivalDate");
            String arrivalTimeStr = req.getParameter("arrivalTime");

            if (departureDateStr != null && !departureDateStr.isEmpty())
                trip.setDepartureDate(LocalDate.parse(departureDateStr));
            if (departureTimeStr != null && !departureTimeStr.isEmpty())
                trip.setDepartureTime(LocalTime.parse(departureTimeStr));
            if (arrivalDateStr != null && !arrivalDateStr.isEmpty())
                trip.setArrivalDate(LocalDate.parse(arrivalDateStr));
            if (arrivalTimeStr != null && !arrivalTimeStr.isEmpty())
                trip.setArrivalTime(LocalTime.parse(arrivalTimeStr));
        } catch (Exception e) {
            throw new Exception("Ngày hoặc giờ đi/đến không hợp lệ");
        }

        String priceStr = req.getParameter("price");
        if (priceStr != null && !priceStr.isEmpty()) {
            try {
                trip.setPrice(new BigDecimal(priceStr));
            } catch (NumberFormatException e) {
                throw new Exception("Giá chuyến không hợp lệ");
            }
        }

        String statusStr = req.getParameter("status");
        if (statusStr != null && !statusStr.isEmpty())
            trip.setStatus(TripStatus.valueOf(statusStr.toLowerCase()));

        if (user != null)
            trip.setUpdatedBy(user);

        return trip;
    }
}
