// // Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
// package com.busbooking.service;

// import jakarta.mail.Message;
// import jakarta.mail.MessagingException;
// import jakarta.mail.Session;
// import jakarta.mail.Transport;
// import jakarta.mail.Message.RecipientType;
// import jakarta.mail.internet.InternetAddress;
// import jakarta.mail.internet.MimeMessage;
// import java.util.Properties;

// public class EmailService {
//     private static final String GMAIL_USERNAME = "doancaothai3004@gmail.com";
//     private static final String GMAIL_APP_PASSWORD = "htmrkwolocfottfu";
//     private static final String FROM_NAME = "BusBooking";

//     public EmailService() {
//     }

//     public static boolean sendOTPEmail(String toEmail, String otp) {
//         System.out.println("\ud83d\ude80 Attempting to send OTP email to: " + toEmail);
//         System.out.println("\ud83d\udce7 OTP: " + otp);

//         try {
//             Properties props = new Properties();
//             props.put("mail.smtp.auth", "true");
//             props.put("mail.smtp.starttls.enable", "true");
//             props.put("mail.smtp.host", "smtp.gmail.com");
//             props.put("mail.smtp.port", "587");
//             props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//             Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
//                 protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
//                     return new jakarta.mail.PasswordAuthentication(GMAIL_USERNAME, GMAIL_APP_PASSWORD);
//                 }
//             });
//             Message message = new MimeMessage(session);
//             message.setFrom(new InternetAddress("doancaothai3004@gmail.com", "BusBooking"));
//             message.setRecipients(RecipientType.TO, InternetAddress.parse(toEmail));
//             message.setSubject("Mã xác thực OTP - BusBooking");
//             String htmlContent = String.format("<html><body style='font-family: Arial, sans-serif;'><div style='max-width: 600px; margin: 0 auto; padding: 20px;'><h2 style='color: #2196F3; text-align: center;'>\ud83d\ude8c Bus Booking Service</h2><div style='background: #f5f5f5; padding: 20px; border-radius: 10px; text-align: center;'><h3>Mã xác thực OTP của bạn:</h3><div style='font-size: 32px; font-weight: bold; color: #FF5722; letter-spacing: 5px; margin: 20px 0;'>%s</div><p style='color: #666;'>Mã này có hiệu lực trong <strong>5 phút</strong></p></div></div></body></html>", otp);
//             message.setContent(htmlContent, "text/html; charset=utf-8");
//             System.out.println("\ud83d\udce4 Sending email via Gmail SMTP...");
//             Transport.send(message);
//             System.out.println("✅ Email sent successfully via Gmail!");
//             return true;
//         } catch (MessagingException var6) {
//             System.err.println("❌ MessagingException when sending email: " + var6.getMessage());
//             var6.printStackTrace();
//             return false;
//         } catch (Exception var7) {
//             System.err.println("❌ Exception when sending email: " + var7.getMessage());
//             var7.printStackTrace();
//             return false;
//         }
//     }
// }

package com.busbooking.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class EmailService {
    private static final String SENDGRID_API_KEY = System.getenv("SENDGRID_API_KEY");
    private static final String SENDER_EMAIL = System.getenv("SENDGRID_SENDER_EMAIL");
    private static final String FROM_NAME = "BusBooking";

    public EmailService() {
    }

    public static boolean sendOTPEmail(String toEmail, String otp) {
        System.out.println("🚀 Attempting to send OTP email to: " + toEmail);
        System.out.println("📧 OTP: " + otp);

        if (SENDGRID_API_KEY == null || SENDGRID_API_KEY.isEmpty()) {
            System.err.println("❌ Missing SENDGRID_API_KEY in environment variables.");
            return false;
        }

        if (SENDER_EMAIL == null || SENDER_EMAIL.isEmpty()) {
            System.err.println("❌ Missing SENDGRID_SENDER_EMAIL in environment variables.");
            return false;
        }

        try {
            URL url = new URL("https://api.sendgrid.com/v3/mail/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + SENDGRID_API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String htmlContent = String.format(
                    "<html><body style='font-family: Arial, sans-serif;'>"
                            + "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>"
                            + "<h2 style='color: #2196F3; text-align: center;'>🚌 Bus Booking Service</h2>"
                            + "<div style='background: #f5f5f5; padding: 20px; border-radius: 10px; text-align: center;'>"
                            + "<h3>Mã xác thực OTP của bạn:</h3>"
                            + "<div style='font-size: 32px; font-weight: bold; color: #FF5722; letter-spacing: 5px; margin: 20px 0;'>%s</div>"
                            + "<p style='color: #666;'>Mã này có hiệu lực trong <strong>5 phút</strong></p>"
                            + "</div></div></body></html>", otp);

            String payload = "{"
                    + "\"personalizations\": [{ \"to\": [{ \"email\": \"" + toEmail + "\" }] }],"
                    + "\"from\": { \"email\": \"" + SENDER_EMAIL + "\", \"name\": \"" + FROM_NAME + "\" },"
                    + "\"subject\": \"Mã xác thực OTP - BusBooking\","
                    + "\"content\": [{ \"type\": \"text/html\", \"value\": \"" + escapeJson(htmlContent) + "\" }]"
                    + "}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("✅ Email sent successfully via SendGrid!");
                return true;
            } else {
                System.err.println("❌ Failed to send email. Response code: " + responseCode);
                try (Scanner scanner = new Scanner(conn.getErrorStream())) {
                    StringBuilder error = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        error.append(scanner.nextLine());
                    }
                    System.err.println("📩 SendGrid error response: " + error);
                }
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Exception when sending email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
