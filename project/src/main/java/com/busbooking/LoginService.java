package com.busbooking;

// Import các thư viện cần thiết
import io.github.cdimascio.dotenv.Dotenv; // Thư viện để đọc các biến môi trường từ file .env
import okhttp3.*; // Thư viện để thực hiện các cuộc gọi HTTP
import com.fasterxml.jackson.databind.JsonNode; // Thư viện để xử lý dữ liệu JSON
import com.fasterxml.jackson.databind.ObjectMapper; // Thư viện để chuyển đổi giữa đối tượng Java và JSON
import com.busbooking.util.EmailService; // Lớp dịch vụ tự định nghĩa để gửi email

import java.io.IOException; // Xử lý các ngoại lệ liên quan đến I/O
import java.util.Scanner; // Lớp để đọc dữ liệu nhập từ người dùng
import java.util.Random; // Lớp để tạo số ngẫu nhiên

/**
 * Lớp LoginService thực hiện chức năng đăng nhập 2 bước với Supabase.
 * Bước 1: Xác thực email và mật khẩu thông qua API của Supabase.
 * Bước 2: Gửi và xác thực mã OTP qua email.
 */
public class LoginService {

    public static void main(String[] args) {
        // Tải các biến môi trường từ file .env
        Dotenv dotenv = Dotenv.load();
        String supabaseUrl = dotenv.get("SUPABASE_URL"); // Lấy URL của Supabase
        String serviceKey = dotenv.get("SUPABASE_SERVICE_KEY"); // Lấy Service Key để có quyền quản trị

        // Tạo đối tượng Scanner để nhận input từ console
        Scanner scanner = new Scanner(System.in);

        // Yêu cầu người dùng nhập email và mật khẩu
        System.out.print("Nhập email: ");
        String email = scanner.nextLine();
        System.out.print("Nhập mật khẩu: ");
        String password = scanner.nextLine();

        // Khởi tạo EmailService để xử lý các tác vụ liên quan đến email
        EmailService emailService = new EmailService();
        // Kiểm tra xem email người dùng nhập có nằm trong danh sách admin hay không
        boolean isAdmin = emailService.getAllAdminEmails().contains(email);
        System.out.println(isAdmin ? email + " là admin" : email + " không phải admin");

        // Khởi tạo một OkHttpClient để gửi request đến server
        OkHttpClient client = new OkHttpClient();

        // Tạo chuỗi JSON chứa email và mật khẩu để gửi đi trong body của request
        String jsonBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        // Tạo RequestBody từ chuỗi JSON
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

        // Xây dựng request POST để xác thực người dùng với Supabase
        Request request = new Request.Builder()
                .url(supabaseUrl + "/auth/v1/token?grant_type=password") // Endpoint xác thực của Supabase
                .post(body) // Gửi dữ liệu bằng phương thức POST
                .addHeader("apikey", serviceKey) // Thêm Service Key vào header để xác thực API
                .addHeader("Authorization", "Bearer " + serviceKey) // Thêm Bearer token (cũng là service key)
                .addHeader("Content-Type", "application/json") // Định dạng nội dung là JSON
                .build();

        // Thực thi request và xử lý response trong khối try-with-resources để tự động đóng response
        try (Response response = client.newCall(request).execute()) {
            // Nếu request thành công (HTTP status code 2xx)
            if (response.isSuccessful()) {
                // Đọc nội dung response
                String responseBody = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(responseBody);

                System.out.println("Đăng nhập thành công bước 1 (email + mật khẩu)");

                // ---- BẮT ĐẦU BƯỚC 2: XÁC THỰC OTP ----

                // Tạo một mã OTP ngẫu nhiên gồm 6 chữ số
                int otp = 100000 + new Random().nextInt(900000);
                // Gửi mã OTP đến email của người dùng
                emailService.sendEmail(email, "Mã xác minh OTP", "Mã xác minh của bạn là: " + otp);

                // Yêu cầu người dùng nhập mã OTP đã nhận được
                System.out.print("Nhập mã OTP đã gửi đến email: ");
                String inputOtp = scanner.nextLine();

                // So sánh mã OTP người dùng nhập với mã đã tạo
                if (String.valueOf(otp).equals(inputOtp)) {
                    System.out.println("Xác thực OTP thành công. Bạn đã login!");

                    // Nếu người dùng là admin, gửi email thông báo cho tất cả các admin khác
                    if (isAdmin) {
                        emailService.sendEmailToAllAdmins(
                                "Thông báo đăng nhập",
                                "Admin " + email + " đã đăng nhập thành công."
                        );
                    }
                } else {
                    // Nếu mã OTP sai
                    System.out.println("Sai mã OTP. Đăng nhập thất bại.");
                }

            } else {
                // Nếu request không thành công, in ra mã lỗi và thông điệp lỗi từ server
                System.out.println("Đăng nhập thất bại: " + response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            // Xử lý các lỗi có thể xảy ra trong quá trình gửi request (ví dụ: mất mạng)
            e.printStackTrace();
        }

        // Đóng đối tượng scanner để giải phóng tài nguyên
        scanner.close();
    }
}