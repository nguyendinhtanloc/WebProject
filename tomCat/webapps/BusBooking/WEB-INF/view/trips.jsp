<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trips</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h2>Danh sách chuyến xe</h2>
    <table>
        <thead>
            <tr>
                <th>Trip ID</th>
                <th>Company ID</th>
                <th>Vehicle ID</th>
                <th>Driver ID</th>
                <th>Departure Place</th>
                <th>Arrival Place</th>
                <th>Departure Date</th>
                <th>Departure Time</th>
                <th>Price</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="trip" items="${tripList}">
                <tr>
                    <td>${trip.tripId}</td>
                    <td>${trip.companyId}</td>
                    <td>${trip.vehicleId}</td>
                    <td>${trip.driverId}</td>
                    <td>${trip.departurePlace}</td>
                    <td>${trip.arrivalPlace}</td>
                    <td>${trip.departureDate}</td>
                    <td>${trip.departureTime}</td>
                    <td>${trip.price}</td>
                    <td>${trip.status}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
