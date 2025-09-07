package ticket.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ticket.model.Ticket;
import util.SupabaseClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/ticket-lookup")
public class TicketController extends HttpServlet {
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword"); // email hoặc phone

        if (keyword == null || keyword.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Vui lòng nhập email hoặc số điện thoại để tra cứu vé.");
            req.getRequestDispatcher("/ticket-lookup.jsp").forward(req, resp);
            return;
        }

        try {
            // 1. Tìm user theo email hoặc phone
            String encodedKeyword = URLEncoder.encode(keyword.trim(), StandardCharsets.UTF_8.toString());
            String userJson = SupabaseClient.get("User", "or=(email.eq." + encodedKeyword + ",phone.eq." + encodedKeyword + ")");

            // 2. Parse JSON để lấy user_id
            JSONArray userArray = new JSONArray(userJson);
            if (userArray.isEmpty()) {
                req.setAttribute("errorMessage", "Không tìm thấy người dùng với thông tin đã nhập.");
                req.getRequestDispatcher("/ticket-lookup.jsp").forward(req, resp);
                return;
            }

            JSONObject userObj = userArray.getJSONObject(0);
            String userId = userObj.getString("user_id");

            // 3. Lấy danh sách vé của user, join Trip
            String ticketJson = SupabaseClient.get(
                    "Ticket",
                    "user_id=eq." + userId + "&select=ticket_id,seat_number,booking_time,status,trip:Trip(start_location,end_location,start_time)"
            );

            // 4. Parse JSON thành List<Ticket>
            Type listType = new TypeToken<List<Ticket>>() {}.getType();
            List<Ticket> tickets = gson.fromJson(ticketJson, listType);

            req.setAttribute("tickets", tickets);
            req.setAttribute("keyword", keyword.trim());

        } catch (Exception e) {
            System.err.println("Lỗi tra cứu vé: " + e.getMessage());
            req.setAttribute("errorMessage", "Đã xảy ra lỗi khi tra cứu vé. Vui lòng thử lại.");
        }

        req.getRequestDispatcher("/ticket-lookup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}