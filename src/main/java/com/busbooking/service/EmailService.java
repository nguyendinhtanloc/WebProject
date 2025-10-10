package com.busbooking.service;

import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
public class EmailService {
    
    // Cấu hình email (cần thay đổi theo thông tin thực tế)
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "your-email@gmail.com"; // Thay bằng email thực tế
    private static final String EMAIL_PASSWORD = "your-app-password"; // Thay bằng app password thực tế
    private static final String FROM_EMAIL = "your-email@gmail.com"; // Thay bằng email thực tế
    
    public static boolean sendOTPEmail(String toEmail, String otp) {
        try {
            // Cấu hình properties cho SMTP
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", SMTP_HOST);
            properties.put("mail.smtp.port", SMTP_PORT);
            
            // Tạo session với authenticator
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });
            
            // Tạo message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã xác thực OTP - BusBooking");
            
            // Nội dung email
            String emailContent = createOTPEmailContent(otp);
            message.setContent(emailContent, "text/html; charset=utf-8");
            
            // Gửi email
            Transport.send(message);
            return true;
            
        } catch (MessagingException e) {
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