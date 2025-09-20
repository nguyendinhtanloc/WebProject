package trip.controller;

import trip.DAO.SeatDAO;
import trip.model.Seat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/book")
public class ChooseSeatServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Lấy type từ URL
            String type = request.getParameter("vehicle_type");
            String ve_id = request.getParameter("vehicle_id");
            int lane = Integer.parseInt(request.getParameter("vehicle_lane"));
            SeatDAO dao = new SeatDAO();
            List<Seat> seats = dao.getSeatsByVehicle(ve_id);

            int seatCount = 0;
            boolean isSleeper = false;

            if ("seat16".equals(type)) {
                seatCount = 16;
            } else if ("seat22".equals(type)) {
                seatCount = 22;
            } else if ("sleeper32".equals(type)) {
                isSleeper = true;
            }

            request.setAttribute("seatCount", seatCount);
            request.setAttribute("isSleeper", isSleeper);
            request.setAttribute("seats", seats);
            request.setAttribute("lane", lane);

            request.getRequestDispatcher("/choose-seat.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error fetching seats", e);
        }
    }
}
