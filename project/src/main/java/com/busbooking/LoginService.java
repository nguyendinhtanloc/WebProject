// muốn test kết nối supabase, chạy hai lệnh sau trong terminal 
// mvn clean compile
// mvn exec:java -Dexec.mainClass="com.busbooking.LoginService"

// Khai báo package cho lớp, giúp tổ chức code
package com.busbooking;

// Import các thư viện cần thiết
import io.github.cdimascio.dotenv.Dotenv; // Thư viện để đọc các biến môi trường từ file .env
import okhttp3.*; // Thư viện để thực hiện các cuộc gọi HTTP (gửi request đến server)
import com.fasterxml.jackson.databind.JsonNode; // Thư viện để xử lý dữ liệu JSON
import com.fasterxml.jackson.databind.ObjectMapper; // Thư viện để chuyển đổi giữa đối tượng Java và JSON
import com.busbooking.util.EmailService; // Import lớp EmailService tự định nghĩa để xử lý việc gửi email

import java.io.IOException; // Import để xử lý các ngoại lệ về Input/Output
import java.util.Scanner; // Import để đọc dữ liệu nhập vào từ người dùng

/**
 * Lớp này xử lý logic đăng nhập của người dùng.
 * Nó nhận email và mật khẩu, xác thực với Supabase,
 * và thực hiện các hành động tiếp theo như gửi email thông báo nếu người dùng là admin.
 */
public class LoginService {

    // Phương thức main là điểm khởi đầu của chương trình
    public static void main(String[] args) {
        // Tải các biến môi trường từ file .env. Giúp bảo mật các thông tin nhạy cảm.
        Dotenv dotenv = Dotenv.load();
        String supabaseUrl = dotenv.get("SUPABASE_URL"); // Lấy URL của Supabase
        String anonKey = dotenv.get("SUPABASE_ANON_KEY"); // Lấy khóa công khai (anon key) của Supabase

        // Tạo một đối tượng Scanner để nhận dữ liệu đầu vào từ bàn phím
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập email: ");
        String email = scanner.nextLine(); // Đọc email người dùng nhập
        System.out.print("Nhập mật khẩu: ");
        String password = scanner.nextLine(); // Đọc mật khẩu người dùng nhập

        // Khởi tạo đối tượng EmailService để sử dụng các chức năng liên quan đến email
        EmailService emailService = new EmailService();
        // Kiểm tra xem email người dùng nhập có nằm trong danh sách email của quản trị viên không
        boolean isAdmin = emailService.getAllAdminEmails().contains(email);
        System.out.println(isAdmin ? email + " là admin" : email + " không phải admin");

        // Tạo một đối tượng OkHttpClient để gửi request HTTP
        OkHttpClient client = new OkHttpClient();

        // Tạo chuỗi JSON chứa email và mật khẩu để gửi đi trong body của request
        String jsonBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        // Tạo RequestBody từ chuỗi JSON
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

        // Xây dựng request POST để gửi đến API xác thực của Supabase
        Request request = new Request.Builder()
                .url(supabaseUrl + "/auth/v1/token?grant_type=password") // Đặt URL của API endpoint
                .post(body) // Chỉ định phương thức là POST và đính kèm body
                .addHeader("apikey", anonKey) // Thêm header 'apikey' cần thiết cho Supabase
                .addHeader("Content-Type", "application/json") // Thêm header để chỉ định định dạng nội dung là JSON
                .build(); // Hoàn thành việc xây dựng request

        // Thực thi request và xử lý response trong một khối try-with-resources
        // để đảm bảo response được đóng tự động
        try (Response response = client.newCall(request).execute()) {
            // Kiểm tra xem request có thành công không (mã trạng thái 2xx)
            if (response.isSuccessful()) {
                // Lấy nội dung của response dưới dạng chuỗi
                String responseBody = response.body().string();
                // Tạo một ObjectMapper để phân tích chuỗi JSON
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(responseBody); // Chuyển chuỗi JSON thành một cây cấu trúc dữ liệu
                System.out.println("Đăng nhập thành công!");

                // Nếu người dùng đăng nhập là admin
                if (isAdmin) {
                    // Gửi email thông báo đến tất cả các admin khác
                    emailService.sendEmailToAllAdmins(
                            "Thông báo đăng nhập", // Tiêu đề email
                            "User " + email + " đã đăng nhập thành công." // Nội dung email
                    );
                }

            } else {
                // Nếu đăng nhập thất bại, in ra mã lỗi và nội dung lỗi từ server
                System.out.println("Đăng nhập thất bại: " + response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            // Bắt và xử lý các lỗi có thể xảy ra trong quá trình gửi request (ví dụ: mất mạng)
            e.printStackTrace();
        }

        // Đóng đối tượng scanner để giải phóng tài nguyên hệ thống
        scanner.close();
    }
}