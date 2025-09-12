package trip.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/book")
public class ChooseSeatServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy type từ URL
        String type = request.getParameter("vehicle_type");
        int seatCount = 0;
        boolean isSleeper = false;

        if ("seat16".equals(type)) {
            seatCount = 16;
        } else if ("seat22".equals(type)) {
            seatCount = 22;
        } else if ("sleeper32".equals(type)) {
            seatCount = 32;
            isSleeper = true;
        }

        request.setAttribute("seatCount", seatCount);
        request.setAttribute("isSleeper", isSleeper);

        request.getRequestDispatcher("/choose-seat.jsp").forward(request, response);
    }
}
