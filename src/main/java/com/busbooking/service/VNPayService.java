package com.busbooking.service;

import com.busbooking.config.VNPayConfig;
import com.busbooking.entity.Payment;
import com.busbooking.entity.PaymentTransactionLog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VNPayService {
    private final Gson gson = new Gson();
    private final PaymentService paymentService;

    public VNPayService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Create VNPay payment URL
    public String createPaymentUrl(Payment payment, HttpServletRequest request) throws Exception {
        String vnp_TxnRef = String.valueOf(payment.getPaymentId());
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        long amount = payment.getFinalAmount().multiply(new BigDecimal("100")).longValue();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + payment.getOrder().getId());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", now.format(formatter));
        vnp_Params.put("vnp_ExpireDate", now.plusMinutes(15).format(formatter));

        // Sắp xếp các tham số và xây dựng chuỗi dữ liệu để tạo chữ ký
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                // QUAN TRỌNG: Mã hóa giá trị fieldValue theo chuẩn UTF-8
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        // Log the transaction (giữ nguyên)
        PaymentTransactionLog log = new PaymentTransactionLog();
        log.setPayment(payment);
        log.setRequestPayload(new Gson().toJson(vnp_Params));
        log.setResponsePayload(paymentUrl);
        paymentService.saveTransactionLog(log);

        // Update payment with transaction reference (giữ nguyên)
        payment.setTransactionNo(vnp_TxnRef);
        paymentService.updatePayment(payment);

        return paymentUrl;
    }

    // Verify VNPay callback
    public boolean verifyCallback(Map<String, String> params) {
        String vnp_SecureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHashType");
        params.remove("vnp_SecureHash");

        String hash = VNPayConfig.hashAllFields(params);
        return hash.equals(vnp_SecureHash);
    }

    // Query transaction status from VNPay
    public JsonObject queryTransaction(String orderId, String transDate) throws Exception {
        String vnp_RequestId = VNPayConfig.getRandomNumber(8);
        String vnp_TxnRef = orderId;
        String vnp_OrderInfo = "Kiem tra ket qua GD OrderId:" + vnp_TxnRef;

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        JsonObject vnp_Params = new JsonObject();
        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.addProperty("vnp_Command", "querydr");
        vnp_Params.addProperty("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.addProperty("vnp_TransactionDate", transDate);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", "127.0.0.1");

        String hash_Data = String.join("|", vnp_RequestId, VNPayConfig.vnp_Version, "querydr",
                VNPayConfig.vnp_TmnCode, vnp_TxnRef, transDate, vnp_CreateDate, "127.0.0.1", vnp_OrderInfo);
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hash_Data);
        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        // 4. Tạo HttpClient với timeout
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))  // timeout kết nối
                .build();

        // 5. Tạo HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VNPayConfig.vnp_ApiUrl))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(20))  // timeout đọc dữ liệu
                .POST(HttpRequest.BodyPublishers.ofString(vnp_Params.toString()))
                .build();

        // 6. Gửi request và nhận response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 7. Kiểm tra status code
        if (response.statusCode() != 200) {
            throw new RuntimeException("VNPay API error: HTTP " + response.statusCode());
        }

        // 8. Parse kết quả JSON
        return gson.fromJson(response.body(), JsonObject.class);
    }

    //Process refund transaction
    public JsonObject refundTransaction(String orderId, long amount, String transDate, String user) throws Exception {
        String vnp_RequestId = VNPayConfig.getRandomNumber(8);
        String vnp_TxnRef = orderId;
        String vnp_Amount = String.valueOf(amount * 100);
        String vnp_OrderInfo = "Hoan tien GD OrderId:" + vnp_TxnRef;

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        JsonObject vnp_Params = new JsonObject();
        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.addProperty("vnp_Command", "refund");
        vnp_Params.addProperty("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.addProperty("vnp_TransactionType", "02"); // Full refund
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_Amount", vnp_Amount);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.addProperty("vnp_TransactionDate", transDate);
        vnp_Params.addProperty("vnp_CreateBy", user);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", "127.0.0.1");

        String hash_Data = String.join("|", vnp_RequestId, VNPayConfig.vnp_Version, "refund",
                VNPayConfig.vnp_TmnCode, "02", vnp_TxnRef, vnp_Amount, "", transDate,
                user, vnp_CreateDate, "127.0.0.1", vnp_OrderInfo);

        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hash_Data);
        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        // ---- Gửi request bằng HttpClient ----
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VNPayConfig.vnp_ApiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(vnp_Params.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse JSON trả về
        return gson.fromJson(response.body(), JsonObject.class);
    }

}