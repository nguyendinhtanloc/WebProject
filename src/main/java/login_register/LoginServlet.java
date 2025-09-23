package login_register;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String SUPABASE_URL = "https://kognqhcifxbpihjrwocg.supabase.co";
    private static final String ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtvZ25xaGNpZnhicGloanJ3b2NnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTY2MDkzNDEsImV4cCI6MjA3MjE4NTM0MX0.P2thkCIF98_bnuJdcy6Nwxp3-9uOSvbebx1rfkU2j04";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // URL tới đúng bảng "users"
        String apiUrl = SUPABASE_URL + "/rest/v1/users?email=eq." +
                java.net.URLEncoder.encode(email, "UTF-8") +
                "&password=eq." +
                java.net.URLEncoder.encode(password, "UTF-8");

        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("apikey", ANON_KEY);
        con.setRequestProperty("Authorization", "Bearer " + ANON_KEY);
        con.setRequestProperty("Accept", "application/json");

        int status = con.getResponseCode();
        if (status == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            in.close();
            con.disconnect();

            String json = content.toString().trim();
            if (json.equals("[]")) {
                // Sai tài khoản/mật khẩu
                response.sendRedirect("login.jsp?error=1");
            } else {
                // Đúng
                HttpSession session = request.getSession();
                session.setAttribute("userEmail", email);
                response.sendRedirect("index.jsp");
            }
        } else {
            response.getWriter().println("Lỗi kết nối Supabase: " + status);
        }
    }
}