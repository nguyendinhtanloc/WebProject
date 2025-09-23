package trip.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import trip.model.Trip;
import trip.util.SupabaseClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String from = req.getParameter("fromPlace");
        String to = req.getParameter("toPlace");
        String date = req.getParameter("departureDate");

        // Kiểm tra xem có tham số tìm kiếm không
        if (from != null && to != null && date != null &&
                !from.trim().isEmpty() && !to.trim().isEmpty() && !date.trim().isEmpty()) {

            // Tạo query parameters cho Supabase
            // Sử dụng * thay vì % cho wildcard trong Supabase ILIKE
            String queryParams = String.format(
                    "select=*,vehicle:fk_trips_vehicle(*)&departure_place=ilike.*%s*&arrival_place=ilike.*%s*&departure_date=eq.%s",
                    urlEncode(from.trim()), urlEncode(to.trim()), urlEncode(date.trim())
            );

            try {
                // Gọi API Supabase
                String jsonResult = SupabaseClient.get("trips", queryParams);

                // Parse JSON thành List<Trip>
                Type tripListType = new TypeToken<List<Trip>>() {}.getType();
                List<Trip> trips = gson.fromJson(jsonResult, tripListType);

                // Đưa danh sách chuyến lên request attribute
                req.setAttribute("trips", trips);

                // Lưu lại giá trị tìm kiếm để hiển thị lại form
                req.setAttribute("fromPlace", from.trim());
                req.setAttribute("toPlace", to.trim());
                req.setAttribute("departureDate", date.trim());

                // Forward đến trang kết quả tìm kiếm
                req.getRequestDispatcher("/search-results.jsp").forward(req, resp);

            } catch (Exception e) {
                // Xử lý lỗi - log và redirect về trang chủ với thông báo lỗi
                System.err.println("Error searching trips: " + e.getMessage());
                e.printStackTrace();

                req.setAttribute("errorMessage", "Có lỗi xảy ra trong quá trình tìm kiếm. Vui lòng thử lại.");
                req.setAttribute("fromPlace", from.trim());
                req.setAttribute("toPlace", to.trim());
                req.setAttribute("departureDate", date.trim());

                req.getRequestDispatcher("/index.jsp").forward(req, resp);
            }
        } else {
            // Không có tham số tìm kiếm hoặc tham số rỗng - hiển thị trang chủ
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Chuyển POST request thành GET request
        doGet(req, resp);
    }

    /**
     * URL encode helper method với xử lý tốt hơn
     */
    private String urlEncode(String value) {
        try {
            // Loại bỏ khoảng trắng thừa và encode
            return java.net.URLEncoder.encode(value.trim(), "UTF-8");
        } catch (Exception e) {
            System.err.println("Error encoding URL parameter: " + value);
            return value.trim().replaceAll("[^a-zA-Z0-9\\s]", ""); // fallback: chỉ giữ ký tự an toàn
        }
    }
}