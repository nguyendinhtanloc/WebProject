// Khai báo package, giúp tổ chức các lớp tiện ích một cách hợp lý
package com.busbooking.util;

// Import các thư viện cần thiết
import io.github.cdimascio.dotenv.Dotenv; // Thư viện để đọc các biến môi trường từ file .env

import javax.mail.*; // Các lớp chính của JavaMail API (Session, Message, Transport...)
import javax.mail.internet.*; // Các lớp cụ thể hơn như MimeMessage, InternetAddress
import java.util.*; // Import các cấu trúc dữ liệu của Java như Map, HashMap, Set

/**
 * Lớp EmailService chịu trách nhiệm cho tất cả các hoạt động liên quan đến việc gửi email.
 * Nó đọc thông tin cấu hình admin từ file .env và cung cấp các phương thức để gửi email.
 */
public class EmailService {

    // Một Map để lưu trữ thông tin của các admin.
    // Key là địa chỉ email của admin.
    // Value là "Mật khẩu ứng dụng" (App Password) tương ứng của email đó.
    private final Map<String, String> adminMap;

  
  
    /**
     * Hàm khởi tạo (Constructor) của lớp EmailService.
     * Khi một đối tượng EmailService được tạo, nó sẽ tự động đọc file .env
     * để lấy danh sách email và mật khẩu ứng dụng của các admin.
     */
    public EmailService() {
        // Tải các biến môi trường từ file .env
        Dotenv dotenv = Dotenv.load();
        // Khởi tạo HashMap để lưu trữ email và mật khẩu
        adminMap = new HashMap<>();
        // Lấy chuỗi chứa thông tin admin từ biến môi trường 'ADMIN_GMAILS'
        // Ví dụ định dạng trong file .env: ADMIN_GMAILS="admin1@gmail.com:pass1,admin2@gmail.com:pass2"
        String admins = dotenv.get("ADMIN_GMAILS");

        // Kiểm tra xem biến môi trường có tồn tại và có nội dung không
        if (admins != null && !admins.isEmpty()) {
            // Tách chuỗi theo dấu phẩy "," để lấy ra từng cặp "email:password"
            for (String pair : admins.split(",")) {
                // Tách mỗi cặp theo dấu hai chấm ":" để lấy ra email và password
                String[] parts = pair.split(":");
                // Đảm bảo rằng việc tách ra đúng 2 phần (email và password)
                if (parts.length == 2) {
                    // Thêm email (key) và mật khẩu ứng dụng (value) vào Map.
                    // trim() để xóa các khoảng trắng thừa ở đầu và cuối.
                    // replace("\"", "") để loại bỏ các dấu ngoặc kép có thể có trong mật khẩu.
                    adminMap.put(parts[0].trim(), parts[1].trim().replace("\"", ""));
                }
            }
        }
    }

    /**
     * Gửi email tới một địa chỉ email cụ thể.
     * Phương thức này sử dụng chính email của người nhận để làm người gửi.
     * @param toEmail Email của người nhận (cũng là admin sẽ gửi email)
     * @param subject Tiêu đề của email
     * @param body    Nội dung của email
     */
    public void sendEmail(String toEmail, String subject, String body) {
        String fromEmail = toEmail; // Sử dụng chính email của admin để làm email gửi đi
        String appPassword = adminMap.get(toEmail); // Lấy mật khẩu ứng dụng tương ứng

        // Nếu không tìm thấy mật khẩu ứng dụng cho email này, in lỗi và không gửi.
        if (appPassword == null) {
            System.err.println("Không tìm thấy App Password cho " + toEmail);
            return;
        }

        // Tạo đối tượng Properties để cấu hình thông tin cho máy chủ SMTP của Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Máy chủ SMTP của Gmail
        props.put("mail.smtp.port", "587"); // Cổng TLS
        props.put("mail.smtp.auth", "true"); // Yêu cầu xác thực
        props.put("mail.smtp.starttls.enable", "true"); // Bật STARTTLS để mã hóa kết nối

        // Tạo một phiên (Session) với máy chủ email, cung cấp thông tin xác thực
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                // Trả về thông tin xác thực gồm email và mật khẩu ứng dụng
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            // Tạo một đối tượng Message để soạn email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail)); // Đặt người gửi
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // Đặt người nhận
            message.setSubject(subject); // Đặt tiêu đề
            message.setText(body); // Đặt nội dung

            // Gửi email đi
            Transport.send(message);
            System.out.println("Email đã gửi tới: " + toEmail);
        } catch (MessagingException e) {
            // Bắt và xử lý các lỗi có thể xảy ra trong quá trình gửi email
            System.err.println("Gửi email thất bại tới " + toEmail);
            e.printStackTrace();
        }
    }

    /**
     * Gửi cùng một email tới tất cả các admin có trong danh sách.
     * @param subject Tiêu đề của email
     * @param body    Nội dung của email
     */
    public void sendEmailToAllAdmins(String subject, String body) {
        // Lặp qua tất cả các email admin trong danh sách
        for (String adminEmail : adminMap.keySet()) {
            // Gọi phương thức sendEmail cho từng admin
            sendEmail(adminEmail, subject, body);
        }
    }

    /**
     * Lấy ra một tập hợp (Set) chứa tất cả các địa chỉ email của admin.
     * @return Một Set<String> chứa các email.
     */
    public Set<String> getAllAdminEmails() {
        // Trả về tập hợp các key (chính là các email) từ adminMap
        return adminMap.keySet();
    }
}