package com.busbooking.service;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailService {
    
    // ⚙️ Cấu hình email thực tế của bạn
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "doancaothai3004@gmail.com"; // Gmail của bạn
    private static final String EMAIL_PASSWORD = "htmrkwolocfottfu"; // App password (bỏ khoảng trắng)
    private static final String FROM_EMAIL = "doancaothai3004@gmail.com"; // Người gửi
    
    public static boolean sendOTPEmail(String toEmail, String otp) {
        System.out.println("🔄 Bắt đầu gửi OTP đến: " + toEmail);
        
        try {
            // Kiểm tra input parameters
            if (toEmail == null || toEmail.trim().isEmpty()) {
                System.out.println("❌ Email không hợp lệ: " + toEmail);
                return false;
            }
            
            if (otp == null || otp.trim().isEmpty()) {
                System.out.println("❌ OTP không hợp lệ: " + otp);
                return false;
            }
            
            System.out.println("📧 Cấu hình SMTP...");
            // Cấu hình các thuộc tính SMTP
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", SMTP_HOST);
            properties.put("mail.smtp.port", SMTP_PORT);
            properties.put("mail.debug", "true"); // Bật debug mode
            
            System.out.println("🔐 Tạo session xác thực...");
            // Tạo session xác thực với tài khoản Gmail
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    System.out.println("🔑 Xác thực với email: " + EMAIL_USERNAME);
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });
            
            System.out.println("📝 Soạn nội dung email...");
            // Soạn nội dung email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "BusBooking System"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã xác thực OTP - BusBooking");

            // Nội dung HTML của email
            String emailContent = createOTPEmailContent(otp);
            message.setContent(emailContent, "text/html; charset=utf-8");

            System.out.println("📤 Đang gửi email...");
            // Gửi email
            Transport.send(message);
            System.out.println("✅ Gửi OTP thành công đến: " + toEmail);
            return true;

        } catch (jakarta.mail.AuthenticationFailedException e) {
            System.out.println("❌ Lỗi xác thực email: " + e.getMessage());
            System.out.println("💡 Kiểm tra lại email và app password!");
            e.printStackTrace();
            return false;
        } catch (jakarta.mail.MessagingException e) {
            System.out.println("❌ Lỗi gửi email: " + e.getMessage());
            System.out.println("💡 Kiểm tra kết nối mạng và cài đặt SMTP!");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("❌ Lỗi không xác định khi gửi OTP: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static String createOTPEmailContent(String otp) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>Mã xác thực OTP</title>" +
                "</head>" +
                "<body>" +
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                "<h2 style='color: #333; text-align: center;'>Xác thực tài khoản BusBooking</h2>" +
                "<div style='background-color: #f9f9f9; padding: 20px; border-radius: 10px; text-align: center;'>" +
                "<p style='font-size: 16px; margin-bottom: 20px;'>Mã xác thực OTP của bạn là:</p>" +
                "<div style='background-color: #007bff; color: white; padding: 15px; border-radius: 5px; font-size: 24px; font-weight: bold; letter-spacing: 3px;'>" +
                otp +
                "</div>" +
                "<p style='font-size: 14px; color: #666; margin-top: 20px;'>Mã này có hiệu lực trong 5 phút.</p>" +
                "<p style='font-size: 14px; color: #666;'>Vui lòng không chia sẻ mã này với bất kỳ ai.</p>" +
                "</div>" +
                "<p style='font-size: 12px; color: #999; text-align: center; margin-top: 30px;'>" +
                "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này." +
                "</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
