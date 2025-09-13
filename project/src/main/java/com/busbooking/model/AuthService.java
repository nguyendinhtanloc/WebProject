package com.busbooking.model;

import com.busbooking.util.EmailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.SecureRandom; // Dùng lớp này để tạo số ngẫu nhiên cho an toàn, chống bị đoán.

/**
 * Lớp này là trung tâm xử lý mọi logic liên quan đến xác thực người dùng.
 * Nó giao tiếp với Supabase để check login, tạo và gửi OTP, gửi email thông báo các kiểu.
 * Tách riêng ra một lớp service như này cho code sạch sẽ, servlet chỉ việc gọi thôi.
 */
public class AuthService {

    // --- PHẦN NÂNG CẤP ---
    // Định nghĩa các ký tự cho phép trong OTP, không dùng mỗi số nữa cho khó đoán.
    private static final String OTP_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int OTP_LENGTH = 8; // Tăng độ dài OTP lên 8 ký tự cho an toàn hơn.
    private static final SecureRandom random = new SecureRandom(); // Dùng SecureRandom thay vì Random thường.
    // --- KẾT THÚC NÂNG CẤP ---

    // Khai báo các biến cần thiết cho việc gọi API và gửi mail.
    private final String supabaseUrl;
    private final String anonKey;
    private final EmailService emailService;

    public AuthService() {
        // Cấu hình để đọc file .env.
        // Thêm ignoreIfMissing() để lỡ có deploy lên server mà quên file .env thì nó không chết chương trình.
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();

        this.supabaseUrl = dotenv.get("SUPABASE_URL");
        // Lưu ý: Key này là SUPABASE_SERVICE_KEY, có full quyền admin.
        // Bình thường nên dùng ANON_KEY (chỉ có quyền đọc công khai) nhưng ở đây chắc cần quyền cao hơn.
        // Phải bảo mật key này cẩn thận!
        this.anonKey = dotenv.get("SUPABASE_SERVICE_KEY"); 
        this.emailService = new EmailService(); // Khởi tạo service gửi mail.
    }

    /**
     * Hàm này gọi API của Supabase để xác thực email và mật khẩu.
     * @return Trả về một JsonNode chứa thông tin user nếu thành công, ngược lại trả về null.
     */
    public JsonNode authenticateUser(String email, String password) throws IOException {
        OkHttpClient client = new OkHttpClient(); // Dùng thư viện OkHttp để gọi API, khá phổ biến và mạnh mẽ.
        String jsonBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

        // Build một request POST tới endpoint xác thực của Supabase.
        Request request = new Request.Builder()
                .url(supabaseUrl + "/auth/v1/token?grant_type=password") // Đây là endpoint chuẩn của Supabase.
                .post(body)
                .addHeader("apikey", anonKey) // Header apikey là bắt buộc.
                .addHeader("Content-Type", "application/json")
                .build();

        // Thực thi request và xử lý response
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Nếu gọi API thành công (status 2xx)
                String responseBody = response.body().string();
                ObjectMapper mapper = new ObjectMapper(); // Dùng Jackson để parse chuỗi JSON thành object.
                return mapper.readTree(responseBody);
            }
            // Nếu sai email/pass hoặc lỗi gì đó, Supabase sẽ trả về status 4xx, isSuccessful() là false.
            return null;
        }
    }

    /**
     * Gửi email cảnh báo bảo mật khi có nhiều lần nhập OTP sai.
     * Hàm này để "hù" người dùng là chính. 😂
     */
    public void sendSecurityAlert(String userEmail) {
        String subject = "Cảnh báo bảo mật tài khoản";
        String body = "Chúng tôi phát hiện có nhiều lần nhập sai mã xác thực OTP cho tài khoản của bạn. " +
                "Nếu đây không phải là bạn, hãy kiểm tra lại hoạt động tài khoản ngay lập tức.";
        emailService.sendEmail(userEmail, subject, body);
    }
    
    // --- PHẦN NÂNG CẤP ---
    /**
     * Tạo mã OTP ngẫu nhiên và gửi đi.
     * Đây là bản nâng cấp, tạo OTP chữ và số, đồng thời gọi hàm gửi email chi tiết.
     * @param request Cần có 'request' để lấy được IP và trình duyệt của người dùng.
     * @return Chuỗi OTP đã được tạo.
     */
    public String generateAndSendOtp(String email, HttpServletRequest request) {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        // Vòng lặp để lấy ngẫu nhiên 8 ký tự từ chuỗi OTP_CHARACTERS.
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(OTP_CHARACTERS.charAt(random.nextInt(OTP_CHARACTERS.length())));
        }
        String otpString = otp.toString();

        // Tách riêng logic gửi mail ra một hàm khác cho gọn.
        sendDetailedOtpEmail(email, otpString, request);
        return otpString;
    }

    /**
     * Hàm private chỉ dùng nội bộ, chuyên để soạn và gửi email OTP với nội dung chi tiết.
     * Thêm thông tin IP và trình duyệt vào mail giúp người dùng biết ai đang cố đăng nhập. Xịn!
     */
    private void sendDetailedOtpEmail(String toEmail, String otp, HttpServletRequest request) {
        // Lấy thông tin từ request của người dùng.
        String userAgent = request.getHeader("User-Agent"); // Tên trình duyệt, hệ điều hành.
        String ipAddress = request.getHeader("X-FORWARDED-FOR"); // Check header này trước, vì app có thể chạy sau proxy.
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr(); // Nếu không có thì lấy IP gốc.
        }

        // Tạo nội dung email đẹp đẽ, chuyên nghiệp.
        String subject = "Mã xác minh đăng nhập BusBooking";
        String body = String.format(
                "Xin chào,\n\n" +
                "Mã xác minh của bạn là: %s\n\n" +
                "Mã này sẽ hết hạn sau 30 giây.\n\n" +
                "Yêu cầu này được thực hiện từ:\n" +
                " - Địa chỉ IP: %s\n" +
                " - Trình duyệt/Thiết bị: %s\n\n" +
                "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email hoặc liên hệ hỗ trợ.\n\n" +
                "Trân trọng,\nĐội ngũ BusBooking",
                otp, ipAddress, userAgent
        );

        // Gọi service để gửi đi.
        emailService.sendEmail(toEmail, subject, body);
    }
    // --- KẾT THÚC NÂNG CẤP ---

    /**
     * Gửi email thông báo cho các admin khác khi có một admin đăng nhập.
     * Đây là chức năng "nội bộ".
     */
    public void notifyAdmins(String userEmail) {
        // Lấy danh sách admin từ trong config ra để check.
        boolean isAdmin = emailService.getAllAdminEmails().contains(userEmail);
        // Nếu người vừa đăng nhập là admin...
        if (isAdmin) {
            // ...thì gửi mail cho tất cả admin khác.
            emailService.sendEmailToAllAdmins(
                    "Thông báo đăng nhập Admin",
                    "Admin có email '" + userEmail + "' vừa đăng nhập vào hệ thống.");
        }
    }
}