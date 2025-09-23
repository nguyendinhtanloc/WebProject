package login_register;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {

    private static final String SUPABASE_URL = "https://kognqhcifxbpihjrwocg.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZ25xaGNpZnhicGloanJ3b2NnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTY2MDkzNDEsImV4cCI6MjA3MjE4NTM0MX0.P2thkCIF98_bnuJdcy6Nwxp3-9uOSvbebx1rfkU2j04";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        URL url = new URL(SUPABASE_URL + "/rest/v1/users?email=eq." + email);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("X-HTTP-Method-Override", "PATCH"); // override thành PATCH
        conn.setRequestProperty("apikey", SUPABASE_KEY);
        conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Prefer", "return=representation");
        conn.setDoOutput(true);


        InputStream inputStream = conn.getInputStream();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line.trim());
        }

        org.json.JSONArray arr = new org.json.JSONArray(sb.toString());
        if (arr.length() > 0) {
            org.json.JSONObject user = arr.getJSONObject(0);
            request.setAttribute("user", user);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String birthDate = request.getParameter("birth_date");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");

        URL url = new URL(SUPABASE_URL + "/rest/v1/users?email=eq." + email);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PATCH");
        conn.setRequestProperty("apikey", SUPABASE_KEY);
        conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Prefer", "return=representation");
        conn.setDoOutput(true);

        String jsonBody = String.format(
                "{\"name\":\"%s\",\"phone\":\"%s\",\"birth_date\":\"%s\",\"address\":\"%s\",\"gender\":\"%s\"}",
                name, phone, birthDate, address, gender);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes("utf-8"));
        }

        int statusCode = conn.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            // Cập nhật session để index.jsp hiển thị mới
            session.setAttribute("name", name);
            session.setAttribute("phone", phone);
            session.setAttribute("birth_date", birthDate);
            session.setAttribute("address", address);
            session.setAttribute("gender", gender);

            response.sendRedirect("index.jsp");
        } else {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line.trim());
            }
            response.getWriter().println("<script>alert('Cập nhật thất bại: " + sb.toString() + "');history.back();</script>");
        }
    }
}
